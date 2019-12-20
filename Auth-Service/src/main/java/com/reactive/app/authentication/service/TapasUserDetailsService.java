package com.reactive.app.authentication.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.reactive.app.authentication.constnats.OAuth2LoginConstnats;
import com.reactive.app.authentication.user.TapasSpringSecurityUser;
import com.reactive.app.common.constants.QueryParameters;
import com.reactive.app.common.model.LoginEntity;
import com.reactive.app.common.model.RoleEntity;
import com.reactive.app.common.model.UserEntity;
import com.reactive.app.common.repository.LoginRepository;

@Service
public class TapasUserDetailsService implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(TapasUserDetailsService.class);

	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private HttpServletRequest request;

	private boolean enabled = true;

	private boolean accountNonExpired = true;

	private boolean credentialsNonExpired = true;

	private boolean accountNonLocked = true;

	@Override
	public UserDetails loadUserByUsername(String username) {
		log.info("Load user:{}", username);

		LoginEntity loginObj = loginRepository.findByUsername(username);

		if (loginObj != null) {

			List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			UserEntity userEntity = loginObj.getUserEntity();
			for (RoleEntity roleEntity : userEntity.getRoles()) {
				GrantedAuthority authority = new SimpleGrantedAuthority(roleEntity.getName());
				grantedAuthorities.add(authority);
			}

			String userPassword = null;
			String loginProvider = request.getParameter(QueryParameters.LOGIN_PROVIDER);

			if (loginProvider != null) {

				switch (loginProvider) {
				case OAuth2LoginConstnats.FACEBOOK_OAUTH2_PROVIDER:
					log.info("Authenticating using facebook token");
					userPassword = loginObj.getFacebookToken();
					break;

				case OAuth2LoginConstnats.GOOGLE_OAUTH2_PROVIDER:
					log.info("Authenticating using google token");
					userPassword = loginObj.getGoogleToken();
					break;
				default:
					log.error("loginProvider:{} in switch", loginProvider);
					userPassword = loginObj.getPassword();
					break;
				}
			} else {
				userPassword = loginObj.getPassword();
			}
			return new TapasSpringSecurityUser(loginObj.getUsername(), "{noop}" + userPassword, enabled,
					accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities, userEntity.getId());
		} else {
			log.info("User with username:{} not found in DB", username);
			throw new UsernameNotFoundException(String.format("User '%s' not found", username));
		}

	}

}
