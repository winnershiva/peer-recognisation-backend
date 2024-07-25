package com.org.peerrecognition.exception;

@SuppressWarnings("serial")
public class EmployeesNotFoundException extends RuntimeException{
	
	String employeeName;
	
	int employeeId;

	public EmployeesNotFoundException(int employeeId) {
		super(String.format("Employee records not found with : %s", employeeId));
		this.employeeId = employeeId;
	}

	public EmployeesNotFoundException(String employeeName) {
		super(String.format("Employee records not found with : %s", employeeName));
		this.employeeName = employeeName;
	}
	

}
