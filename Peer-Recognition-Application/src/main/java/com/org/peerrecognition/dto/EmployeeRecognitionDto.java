package com.org.peerrecognition.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRecognitionDto {
	
	private int employeeId;
	private String employeeName;
	private String email;
	private int points;
	private int earned;
	private Set<RecognitionDto> badgesReceived = new HashSet<>();
	

}
