package com.reactive.app.device.control.scheduler.handler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

import org.redisson.api.CronSchedule;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RScheduledFuture;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.app.common.constants.CommonRouteConstants;
import com.reactive.app.common.handler.CommonHandler;
import com.reactive.app.device.control.dto.ScheduleDeviceControlDto;
import com.reactive.app.device.control.schedule.task.ScheduledTask;

import reactor.core.publisher.Mono;

@Service
public class ScheduledDeviceControlHandler extends CommonHandler {

	private static final Logger log = LoggerFactory.getLogger(ScheduledDeviceControlHandler.class);

	@Value("${azure.iot.hub.connectionString}")
	private String iotHubConnectionString;

	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	@Qualifier("applicationHost")
	private String applicationHost;

	@Autowired
	private CronExpressionGenerator cronExpressionGenerator;

	private DateTimeFormatter timeFomatter = DateTimeFormatter.ofPattern("HH.mm");

	public Mono<ServerResponse> scheduleDeviceControl(ServerRequest serverRequest) {
		Mono<ScheduleDeviceControlDto> body = serverRequest.bodyToMono(ScheduleDeviceControlDto.class);

		return ok().body(body.flatMap(scheduleDeviceControlDto -> {
			return scheduleJobInRedis(scheduleDeviceControlDto);
		}), ScheduleDeviceControlDto.class);
	}

	private Mono<? extends ScheduleDeviceControlDto> scheduleJobInRedis(
			ScheduleDeviceControlDto scheduledDeviceControlDto) {

		// schedule the job
		RScheduledExecutorService scheduledExecutorService = redissonClient.getExecutorService("executorServiceOne");

		TemporalAccessor parsedTime = timeFomatter.parse(scheduledDeviceControlDto.getScheduledAt());
		log.info("parsedTime:{}", parsedTime);

		ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDate.now(), LocalTime.from(parsedTime),
				ZoneOffset.of(scheduledDeviceControlDto.getTimeZoneOffset()));

		ZonedDateTime utcTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);

		log.info("scheduled time in UTC: {}:{}", utcTime.get(ChronoField.HOUR_OF_DAY),
				utcTime.get(ChronoField.MINUTE_OF_HOUR));

		CronSchedule cronSchedule = null;
		String scheduledDays = scheduledDeviceControlDto.getScheduledDays();
		if (scheduledDays == null || scheduledDays.isBlank()) {
			cronSchedule = CronSchedule.dailyAtHourAndMinute(utcTime.get(ChronoField.HOUR_OF_DAY),
					utcTime.get(ChronoField.MINUTE_OF_HOUR));
		} else {
			cronSchedule = CronSchedule.of(cronExpressionGenerator.expressionForRepeatedDays(
					utcTime.get(ChronoField.MINUTE_OF_HOUR), utcTime.get(ChronoField.HOUR_OF_DAY), scheduledDays));
		}

		RScheduledFuture<?> taskFuture = scheduledExecutorService.schedule(
				new ScheduledTask(applicationHost + CommonRouteConstants.DEVICE_CONTROL_SERVICE_PATTERN
						+ CommonRouteConstants.CONTROL_DEVICE_ROUTE + "/" + scheduledDeviceControlDto.getId()),
				cronSchedule);

		// save task id
		scheduledDeviceControlDto.setTaskId(taskFuture.getTaskId());
		return Mono.just(scheduledDeviceControlDto);
	}

}
