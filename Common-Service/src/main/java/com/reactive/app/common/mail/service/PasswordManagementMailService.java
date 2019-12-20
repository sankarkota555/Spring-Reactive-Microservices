package com.reactive.app.common.mail.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.reactive.app.common.constants.ContactType;
import com.reactive.app.common.constants.MailConstants;
import com.reactive.app.common.constants.MailTemplateConstants;
import com.reactive.app.common.model.ContactDetails;
import com.reactive.app.common.model.LoginEntity;
import com.reactive.app.common.model.UserEntity;

import freemarker.template.TemplateException;

@Service
public class PasswordManagementMailService {

	private static final Logger log = LoggerFactory.getLogger(PasswordManagementMailService.class);

	@Autowired
	private MailService mailService;

	@Async
	public void sendPasswordManagementMail(LoginEntity loginEntity, String mailBody) {

		UserEntity userEntity = loginEntity.getUserEntity();
		Map<String, Object> model = new HashMap<>();
		model.put("name", userEntity.getFirstName());
		model.put("signature", "Reactive Team");
		model.put("mailBody", mailBody);

		ContactDetails emailContact = userEntity.getContactDetails().stream()
				.filter(contact -> contact.getType().equals(ContactType.EMAILID)).findAny().orElseThrow();
		try {
			mailService.sendMail(emailContact.getValue(), MailConstants.PASSWORD_MANAGEMENT_MAIL_SUBJECT, null,
					MailTemplateConstants.PASSWORD_MANAGEMENT_MAIL_TEMPLATE, model);
		} catch (MessagingException | IOException | TemplateException e) {
			log.error("Error while sending registration mail:", e);
		}

	}

}
