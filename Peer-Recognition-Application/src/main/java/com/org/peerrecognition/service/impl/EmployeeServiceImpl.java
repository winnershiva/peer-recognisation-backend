package com.org.peerrecognition.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.org.peerrecognition.dto.EmployeeDto;
import com.org.peerrecognition.dto.EmployeeRecognitionDto;
import com.org.peerrecognition.dto.RecognitionDto;
import com.org.peerrecognition.exception.EmployeesNotFoundException;
import com.org.peerrecognition.exception.ResourceNotFoundException;
import com.org.peerrecognition.model.Employee;
import com.org.peerrecognition.model.Recognition;
import com.org.peerrecognition.repository.EmployeeRepository;
import com.org.peerrecognition.service.EmployeeService;
import com.org.peerrecognition.util.NumberUtils;

@Service
public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {
	
	@Autowired
	private EmployeeRepository employeeRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public EmployeeDto getEmployeeById(int employeeId) {
		Employee receivedEmployee = this.employeeRepo.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee","Id",employeeId));
		return this.employeeToDto(receivedEmployee);
	}
	
	public EmployeeDto employeeToDto(Employee employee)
	{
		return this.modelMapper.map(employee, EmployeeDto.class);
	}
	
	public Employee dtoToEmployee(EmployeeDto employeeDto) {
		return this.modelMapper.map(employeeDto, Employee.class);
	}

	@Override
	public List<EmployeeDto> searchEmployee(String employeeName) {
		if(NumberUtils.isInteger(employeeName))
		{	
			List<Employee> employeesByIds = this.employeeRepo.findByEmployeeIdContaining(employeeName);
			List<EmployeeDto> searchedEmployeesByIds = employeesByIds.stream()
					.map((employee) -> this.employeeToDto(employee))
					.collect(Collectors.toList());
			if(searchedEmployeesByIds.isEmpty()) throw new EmployeesNotFoundException(employeeName);
			return searchedEmployeesByIds;
		}
		else
		{
			List<Employee> employeesByName = this.employeeRepo.findByEmployeeNameContaining(employeeName);
			List<EmployeeDto> searchedEmployeesByNames = employeesByName.stream()
					.map((employee) -> this.employeeToDto(employee))
					.collect(Collectors.toList());
			if(searchedEmployeesByNames.isEmpty()) throw new EmployeesNotFoundException(employeeName);
			return searchedEmployeesByNames;
		}
		
	}

	@Override
	public EmployeeRecognitionDto getRecognitions(int employeeId) {
		Employee receivedEmployee = this.employeeRepo.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee","Id",employeeId));
		EmployeeRecognitionDto recognitionDto = new EmployeeRecognitionDto();
		recognitionDto.setEmployeeId(receivedEmployee.getEmployeeId());
		recognitionDto.setEmployeeName(receivedEmployee.getEmployeeName());
		recognitionDto.setEmail(receivedEmployee.getEmail());
		recognitionDto.setPoints(receivedEmployee.getPoints());
		recognitionDto.setEarned(receivedEmployee.getEarned());
		
		Set<RecognitionDto> recognitions = receivedEmployee.getBadgesReceived()
				.stream()
				.map((recognition) -> this.modelMapper.map(recognition, RecognitionDto.class)).collect(Collectors.toSet());
		
		recognitionDto.setBadgesReceived(recognitions);
		return recognitionDto;
	}

	@Override
	public int getEmployeeIdByEmail(String email) {
		Optional<Employee> employee = this.employeeRepo.findByEmail(email);

		return employee.get().getEmployeeId();
	}

	public RecognitionDto recognitionToDto(Recognition recognition)
	{
		return this.modelMapper.map(recognition, RecognitionDto.class);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// Write logic to fetch customer from DB
		Employee employee = employeeRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Employee not found with email: " + email));

		return new User(employee.getEmail(), employee.getPassword(), Collections.emptyList());
	}

}
