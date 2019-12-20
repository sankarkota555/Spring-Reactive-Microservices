package com.reactive.app.common.mail.service;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import reactor.core.publisher.Mono;

@Service
public class MailService {

	private static final Logger log = LoggerFactory.getLogger(MailService.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private Configuration freemarkerConfig;

	public Mono<Void> sendMail(String toAddress, String subject, String mailBody, String templateName,
			Map<String, ? extends Object> templateParamValues)
			throws MessagingException, IOException, TemplateException {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false);
		messageHelper.setTo(toAddress);
		messageHelper.setSubject(subject);
		if (templateName != null && !templateName.isBlank()) {
			Template template = freemarkerConfig.getTemplate(templateName);
			messageHelper.setText(FreeMarkerTemplateUtils.processTemplateIntoString(template, templateParamValues),
					true);
		} else {
			messageHelper.setText(mailBody);
		}
		javaMailSender.send(mimeMessage);
		return Mono.empty();
	}

}
