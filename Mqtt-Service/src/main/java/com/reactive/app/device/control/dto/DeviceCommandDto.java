package com.reactive.app.device.control.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeviceCommandDto implements Serializable {

	private static final long serialVersionUID = -202219010677115863L;

	@JsonProperty("uuid")
	private String deviceId;

	@JsonProperty("pktTyp")
	private Integer pktType;

	@JsonProperty("seqNum")
	private Integer seqNumber;

	private Integer action;

	private Integer pktStatus;

	private Integer mode;

	private Integer duration;

}
