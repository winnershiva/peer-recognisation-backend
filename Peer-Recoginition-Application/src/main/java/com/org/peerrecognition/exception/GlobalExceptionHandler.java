package com.org.peerrecognition.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.org.peerrecognition.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(EmployeesNotFoundException.class)
	public ResponseEntity<ApiResponse> employeesNotFoundExceptionnHandler(EmployeesNotFoundException ex)
	{
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage(ex.getMessage());
		apiResponse.setSuccess(false);
		return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex)
	{
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage(ex.getMessage());
		apiResponse.setSuccess(false);
		return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
	}
}
