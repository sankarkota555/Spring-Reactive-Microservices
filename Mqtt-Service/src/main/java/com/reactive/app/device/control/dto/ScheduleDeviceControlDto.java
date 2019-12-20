package com.reactive.app.device.control.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScheduleDeviceControlDto implements Serializable {

	private static final long serialVersionUID = 3586471793649001655L;

	private String id;

	private DeviceControlDto deviceControlDto;

	private String taskId;

	private String deviceId;

	private String scheduledAt;

	private String scheduledDays;

	private String timeZoneOffset;

	private boolean enabled;

}
