package com.org.peerrecognition.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecognitionDto {
	
	private String giverName;
	private String receiverName;
	private String badgeName;
	private String comment;
	private BadgeDto badges;

}
