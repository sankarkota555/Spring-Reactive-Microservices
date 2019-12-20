package com.reactive.app.user.management.registration.mail.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.reactive.app.common.constants.CommonRouteConstants;
import com.reactive.app.common.constants.ContactType;
import com.reactive.app.common.constants.MailConstants;
import com.reactive.app.common.constants.MailTemplateConstants;
import com.reactive.app.common.mail.service.MailService;
import com.reactive.app.common.model.ContactDetails;
import com.reactive.app.common.model.LoginEntity;
import com.reactive.app.common.model.UserEntity;
import com.reactive.app.common.repository.EnvironmentDetailsReactiveRepository;

import freemarker.template.TemplateException;

@Service
public class RegistrationMailService {

	private static final Logger log = LoggerFactory.getLogger(RegistrationMailService.class);

	@Value("${spring.profiles.active}")
	private String activeEnvironment;

	@Autowired
	private MailService mailService;

	@Autowired
	private EnvironmentDetailsReactiveRepository environmentDetailsReactiveRepository;

	@Async
	public void sendRegistrationSuccessMail(LoginEntity loginEntity) {
		environmentDetailsReactiveRepository.findByEnvironmentName(activeEnvironment).subscribe(environmentDetails -> {

			UserEntity userEntity = loginEntity.getUserEntity();
			Map<String, Object> model = new HashMap<>();
			model.put("name", userEntity.getFirstName());
			model.put("signature", "TAPAS Team");
			String url = environmentDetails.getApplicationUrl() + CommonRouteConstants.USER_MANAGEMENT_SERVICE_PATTERN
					+ CommonRouteConstants.USER_VERIFICATION_ROUTE + CommonRouteConstants.VERIFY_EMAIL_ROUTE + "/"
					+ loginEntity.getId() + "/" + loginEntity.getVerificationKey();
			model.put("verificationUrl", url);
			ContactDetails emailContact = userEntity.getContactDetails().stream()
					.filter(contact -> contact.getType().equals(ContactType.EMAILID)).findAny().orElseThrow();
			try {
				mailService.sendMail(emailContact.getValue(), MailConstants.REGISTRATION_MAIL_SUBJECT, null,
						MailTemplateConstants.REGISTRATION_MAIL_TEMPLATE, model);
			} catch (MessagingException | IOException | TemplateException e) {
				log.error("Error while sending registration mail:", e);
			}

		});

	}

}
