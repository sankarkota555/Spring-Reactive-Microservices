package com.reactive.app.device.control.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeviceControlDto implements Serializable {

	private static final long serialVersionUID = -6006263510464077206L;

	private String deviceId;

	private Integer pktType;

	private Integer seqNumber;

	private Integer action;

	private Integer pktStatus;

	private Integer mode;

	private Integer duration;

	private Integer lightIntensity;

	private Integer lightColour;

	private Integer lightMode;

	private String topic;

	@JsonProperty("device_data")
	private String deviceData;

}
