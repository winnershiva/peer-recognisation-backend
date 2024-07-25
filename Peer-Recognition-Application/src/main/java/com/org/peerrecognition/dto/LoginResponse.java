package com.org.peerrecognition.dto;


import com.org.peerrecognition.model.Employee;

public record LoginResponse(String jwt, int employeeId) {
}
