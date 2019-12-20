package com.reactive.app.device.control.schedule.task;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledTask implements Runnable, Serializable {

	private static final long serialVersionUID = -2870105827766008185L;

	private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

	private String requestUrl;

	public ScheduledTask(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	@Override
	public void run() {

		HttpRequest httpRequest = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.noBody())
				.uri(URI.create(requestUrl)).build();
		try {
			HttpResponse<String> reponse = HttpClient.newHttpClient().send(httpRequest,
					HttpResponse.BodyHandlers.ofString());
			log.info("Response body: {}", reponse.body());
		} catch (IOException e) {
			log.error("Scheduler http call exception:", e);

		} catch (InterruptedException e) {
			log.error("Scheduler http call exception:", e);
			// Restore interrupted state...
			Thread.currentThread().interrupt();
		}

	}

}