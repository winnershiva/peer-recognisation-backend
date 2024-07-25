package com.org.peerrecognition.repository;

import java.util.List;
import java.util.Optional;

import com.org.peerrecognition.dto.EmployeeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.org.peerrecognition.model.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	List<Employee> findByEmployeeNameContaining(String employeeName);

	@Query("SELECT e FROM Employee e WHERE str(e.employeeId) LIKE %:employeeId%")
	List<Employee> findByEmployeeIdContaining(@Param("employeeId") String employeeId);

	Employee findByEmployeeName(String employeeName);

	boolean existsByEmail(String email);

	Optional<Employee> findByEmail(String email);

}