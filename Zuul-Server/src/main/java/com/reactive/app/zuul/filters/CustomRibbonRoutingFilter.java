package com.reactive.app.zuul.filters;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.reactive.app.authentication.user.TapasSpringSecurityUser;
import com.reactive.app.common.constants.HeaderConstants;

@Component
public class CustomRibbonRoutingFilter extends ZuulFilter {

	private static final Logger log = LoggerFactory.getLogger(CustomRibbonRoutingFilter.class);

	private static final String ROUTE_TYPE = "route";

	private static final String SERVICE_ID_KEY = "serviceId";

	@Autowired
	private RibbonCommandFactory<?> ribbonCommandFactory;

	private RibbonRoutingFilter ribbonRoutingFilter;

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		log.debug("ctx.getRouteHost():{}, ctx.get(SERVICE_ID_KEY):{}, ctx.sendZuulResponse():{}", ctx.getRouteHost(),
				ctx.get(SERVICE_ID_KEY), ctx.sendZuulResponse());
		return (ctx.getRouteHost() == null && ctx.get(SERVICE_ID_KEY) != null && ctx.sendZuulResponse());
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext currentRequestContext = RequestContext.getCurrentContext();

		// Allow cors policy
		HttpServletResponse response = currentRequestContext.getResponse();
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");

		HttpServletRequest request = currentRequestContext.getRequest();
		// Don't process 'OPTIONS' method, it is a pre-flight request comes from browser
		if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
			return null;
		}
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof TapasSpringSecurityUser) {
			TapasSpringSecurityUser tapasSpringSecurityUser = (TapasSpringSecurityUser) principal;
			currentRequestContext.addZuulRequestHeader(HeaderConstants.LOGIN_USER_ID, tapasSpringSecurityUser.getId());
			// TODO: if logged in user role is not needed, then remove below code
			currentRequestContext.addZuulRequestHeader(HeaderConstants.LOGIN_USER_ROLE,
					tapasSpringSecurityUser.getAuthorities().toArray()[0].toString());
		}
		log.info("Processing request:{}", request.getRequestURI());
		return ribbonRoutingFilter.run();
	}

	@Override
	public String filterType() {
		return ROUTE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@PostConstruct
	public void initRibbonFilter() {
		ribbonRoutingFilter = new RibbonRoutingFilter(new ProxyRequestHelper(new ZuulProperties()),
				ribbonCommandFactory, new ArrayList<>());
	}

}
