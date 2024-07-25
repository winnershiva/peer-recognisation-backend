package com.org.peerrecognition.service;

import java.util.List;

import com.org.peerrecognition.dto.EmployeeDto;
import com.org.peerrecognition.dto.EmployeeRecognitionDto;

public interface EmployeeService {

	EmployeeDto getEmployeeById(int employeeId);

	List<EmployeeDto> searchEmployee(String employeeName);
	
	EmployeeRecognitionDto getRecognitions(int employeeId);
	int getEmployeeIdByEmail(String email);

	List<EmployeeDto> getAllEmployees();
}

