package com.org.peerrecognition.service.impl;

import com.org.peerrecognition.dto.BadgeDto;
import com.org.peerrecognition.dto.EmployeeDto;
import com.org.peerrecognition.dto.EmployeeRecognitionDto;
import com.org.peerrecognition.dto.RecognitionDto;
import com.org.peerrecognition.exception.EmployeesNotFoundException;
import com.org.peerrecognition.exception.ResourceNotFoundException;
import com.org.peerrecognition.model.Employee;
import com.org.peerrecognition.model.Recognition;
import com.org.peerrecognition.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository mockEmployeeRepo;
    @Mock
    private ModelMapper mockModelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImplUnderTest;

    @Test
    void testGetEmployeeById() {
        // Setup
        // Configure EmployeeRepository.findById(...).
        final Employee employee1 = new Employee();
        employee1.setEmployeeId(100);
        employee1.setEmployeeName("Aravind");
        employee1.setEmail("aravind@dbs.com");
        employee1.setPassword("aravind");
        employee1.setPoints(800);
        employee1.setEarned(290);
        final Recognition recognition = new Recognition();
        employee1.setBadgesReceived(Set.of(recognition));
        final Optional<Employee> employee = Optional.of(employee1);
        when(mockEmployeeRepo.findById(100)).thenReturn(employee);

        // Configure ModelMapper.map(...).
        final EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(employee1.getEmployeeId());
        employeeDto.setEmployeeName(employee1.getEmployeeName());
        employeeDto.setDesignation(employee1.getDesignation());
        employeeDto.setEmail(employee1.getEmail());
        employeeDto.setEarned(employee1.getEarned());
        when(mockModelMapper.map(any(Object.class), eq(EmployeeDto.class))).thenReturn(employeeDto);

        // Run the test
        final EmployeeDto result = employeeServiceImplUnderTest.getEmployeeById(100);

        // Verify the results
        assertEquals(employeeDto.getEmployeeId(), result.getEmployeeId());
        assertEquals(employeeDto.getEmployeeName(), result.getEmployeeName());

    }

    @Test
    void testGetEmployeeById_EmployeeRepositoryReturnsAbsent() {
        // Setup
        when(mockEmployeeRepo.findById(0)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> employeeServiceImplUnderTest.getEmployeeById(0))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testEmployeeToDto() {
        // Setup
        final Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setEmployeeName("abc");
        employee.setEmail("abc@gmail.com");
        employee.setPassword("abc");
        employee.setPoints(500);
        employee.setEarned(800);
        final Recognition recognition = new Recognition();
        employee.setBadgesReceived(Set.of(recognition));

        // Configure ModelMapper.map(...).
        final EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(employeeDto.getEmployeeId());
        employeeDto.setEmployeeName(employeeDto.getEmployeeName());
        employeeDto.setDesignation(employeeDto.getDesignation());
        employeeDto.setEmail(employeeDto.getEmail());
        employeeDto.setEarned(employeeDto.getEarned());
        when(mockModelMapper.map(any(Object.class), eq(EmployeeDto.class))).thenReturn(employeeDto);

        // Run the test
        final EmployeeDto result = employeeServiceImplUnderTest.employeeToDto(employee);

        // Verify the results
        assertEquals(employeeDto.getEmployeeId(), result.getEmployeeId());
        assertEquals(employeeDto.getEmployeeName(), result.getEmployeeName());
    }

    @Test
    void testDtoToEmployee() {
        // Setup
        final EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(0);
        employeeDto.setEmployeeName("employeeName");
        employeeDto.setDesignation("designation");
        employeeDto.setEmail("email");
        employeeDto.setEarned(0);

        // Configure ModelMapper.map(...).
        final Employee employee = new Employee();
        employee.setEmployeeId(0);
        employee.setEmployeeName("employeeName");
        employee.setEmail("email");
        employee.setPassword("password");
        employee.setPoints(0);
        employee.setEarned(0);
        final Recognition recognition = new Recognition();
        employee.setBadgesReceived(Set.of(recognition));
        when(mockModelMapper.map(any(Object.class), eq(Employee.class))).thenReturn(employee);

        // Run the test
        final Employee result = employeeServiceImplUnderTest.dtoToEmployee(employeeDto);

        // Verify the results
    }

    @Test
    void testSearchEmployee() {
        // Setup
        // Configure EmployeeRepository.findByEmployeeIdContaining(...).
        final Employee employee1 = new Employee();
        employee1.setEmployeeId(56080);
        employee1.setEmployeeName("Anusha");
        employee1.setEmail("Anusha@gmail.com");
        employee1.setPassword("anusha");
        employee1.setPoints(700);
        employee1.setEarned(300);
        final Recognition recognition1 = new Recognition();
        employee1.setBadgesReceived(Set.of(recognition1));

        final Employee employee2 = new Employee();
        employee2.setEmployeeId(56081);
        employee2.setEmployeeName("Anand");
        employee2.setEmail("Anand@gmail.com");
        employee2.setPassword("anand");
        employee2.setPoints(900);
        employee2.setEarned(800);
        final Recognition recognition2 = new Recognition();
        employee2.setBadgesReceived(Set.of(recognition2));

        final List<Employee> employeesById = List.of(employee1, employee2);

        // Configure ModelMapper.map(...).
        final EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setEmployeeId(employee1.getEmployeeId());
        employeeDto1.setEmployeeName(employee1.getEmployeeName());
        employeeDto1.setEmail(employee1.getEmail());
        employeeDto1.setEarned(employee1.getEarned());

        final EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setEmployeeId(employee2.getEmployeeId());
        employeeDto2.setEmployeeName(employee2.getEmployeeName());
        employeeDto2.setEmail(employee2.getEmail());
        employeeDto2.setEarned(employee2.getEarned());

        when(mockModelMapper.map(employee1, EmployeeDto.class)).thenReturn(employeeDto1);
        when(mockModelMapper.map(employee2, EmployeeDto.class)).thenReturn(employeeDto2);

        when(mockEmployeeRepo.findByEmployeeIdContaining("560")).thenReturn(employeesById);

        // Run the test
        final List<EmployeeDto> result = employeeServiceImplUnderTest.searchEmployee("560");

        // Verify the results
        assertEquals(2, result.size()); // Verify that the result list has two elements

        // Verify mappings for each employee
        EmployeeDto resultEmployeeDto1 = result.get(0); // order may vary
        assertEquals(employee1.getEmployeeId(), resultEmployeeDto1.getEmployeeId());
        assertEquals(employee1.getEmployeeName(), resultEmployeeDto1.getEmployeeName());

        EmployeeDto resultEmployeeDto2 = result.get(1); // order may vary
        assertEquals(employee2.getEmployeeId(), resultEmployeeDto2.getEmployeeId());
        assertEquals(employee2.getEmployeeName(), resultEmployeeDto2.getEmployeeName());
    }

    @Test
    void testSearchEmployee_EmployeeRepositoryFindByEmployeeIdContainingReturnsNoItems() {
        // Setup
        when(mockEmployeeRepo.findByEmployeeIdContaining("999")).thenReturn(Collections.emptyList());

        // Run the test
        assertThatThrownBy(() -> employeeServiceImplUnderTest.searchEmployee("999"))
                .isInstanceOf(EmployeesNotFoundException.class)
                .hasMessageContaining("999"); // Check the exception message if it includes the searched term
    }

    @Test
    void testSearchEmployee_EmployeeRepositoryFindByEmployeeNameContainingReturnsNoItems() {
        // Setup
        when(mockEmployeeRepo.findByEmployeeNameContaining("employeeName")).thenReturn(Collections.emptyList());

        // Run the test
        assertThatThrownBy(() -> employeeServiceImplUnderTest.searchEmployee("employeeName"))
                .isInstanceOf(EmployeesNotFoundException.class);
    }

    @Test
    void testGetRecognitions() {
        // Setup
        // Configure EmployeeRepository.findById(...).
        final Employee employee1 = new Employee();
        employee1.setEmployeeId(0);
        employee1.setEmployeeName("employeeName");
        employee1.setEmail("email");
        employee1.setPassword("password");
        employee1.setPoints(0);
        employee1.setEarned(0);
        final Recognition recognition = new Recognition();
        employee1.setBadgesReceived(Set.of(recognition));
        final Optional<Employee> employee = Optional.of(employee1);
        when(mockEmployeeRepo.findById(0)).thenReturn(employee);

        // Configure ModelMapper.map(...).
        final RecognitionDto recognitionDto = new RecognitionDto();
        recognitionDto.setGiverName("giverName");
        recognitionDto.setReceiverName("receiverName");
        recognitionDto.setBadgeName("badgeName");
        recognitionDto.setComment("comment");
        final BadgeDto badges = new BadgeDto();
        recognitionDto.setBadges(badges);
        when(mockModelMapper.map(any(Object.class), eq(RecognitionDto.class))).thenReturn(recognitionDto);

        // Run the test
        final EmployeeRecognitionDto result = employeeServiceImplUnderTest.getRecognitions(0);

        // Verify the results
    }

    @Test
    void testGetRecognitions_EmployeeRepositoryReturnsAbsent() {
        // Setup
        when(mockEmployeeRepo.findById(0)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> employeeServiceImplUnderTest.getRecognitions(0))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetEmployeeIdByEmail() {
        // Setup
        // Configure EmployeeRepository.findByEmail(...).
        final Employee employee1 = new Employee();
        employee1.setEmployeeId(0);
        employee1.setEmployeeName("employeeName");
        employee1.setEmail("email");
        employee1.setPassword("password");
        employee1.setPoints(0);
        employee1.setEarned(0);
        final Recognition recognition = new Recognition();
        employee1.setBadgesReceived(Set.of(recognition));
        final Optional<Employee> employee = Optional.of(employee1);
        when(mockEmployeeRepo.findByEmail("email")).thenReturn(employee);

        // Run the test
        final int result = employeeServiceImplUnderTest.getEmployeeIdByEmail("email");

        // Verify the results
        assertThat(result).isEqualTo(0);
    }

    @Test
    void testGetEmployeeIdByEmail_EmployeeRepositoryReturnsAbsent() {
        // Setup
        when(mockEmployeeRepo.findByEmail("email")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> employeeServiceImplUnderTest.getEmployeeIdByEmail("email"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testRecognitionToDto() {
        // Setup
        final Recognition recognition = new Recognition();
        recognition.setRecognitionId(0);
        final Employee giver = new Employee();
        giver.setEmployeeId(0);
        giver.setEmployeeName("employeeName");
        giver.setEmail("email");
        recognition.setGiver(giver);

        // Configure ModelMapper.map(...).
        final RecognitionDto recognitionDto = new RecognitionDto();
        recognitionDto.setGiverName("giverName");
        recognitionDto.setReceiverName("receiverName");
        recognitionDto.setBadgeName("badgeName");
        recognitionDto.setComment("comment");
        final BadgeDto badges = new BadgeDto();
        recognitionDto.setBadges(badges);
        when(mockModelMapper.map(any(Object.class), eq(RecognitionDto.class))).thenReturn(recognitionDto);

        // Run the test
        final RecognitionDto result = employeeServiceImplUnderTest.recognitionToDto(recognition);

        // Verify the results
    }

    @Test
    void testLoadUserByUsername() {
        // Setup
        // Configure EmployeeRepository.findByEmail(...).
        final Employee employee1 = new Employee();
        employee1.setEmployeeId(0);
        employee1.setEmployeeName("employeeName");
        employee1.setEmail("email");
        employee1.setPassword("password");
        employee1.setPoints(0);
        employee1.setEarned(0);
        final Recognition recognition = new Recognition();
        employee1.setBadgesReceived(Set.of(recognition));
        final Optional<Employee> employee = Optional.of(employee1);
        when(mockEmployeeRepo.findByEmail("email")).thenReturn(employee);

        // Run the test
        final UserDetails result = employeeServiceImplUnderTest.loadUserByUsername("email");

        // Verify the results
    }

    @Test
    void testLoadUserByUsername_EmployeeRepositoryReturnsAbsent() {
        // Setup
        when(mockEmployeeRepo.findByEmail("email")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> employeeServiceImplUnderTest.loadUserByUsername("email"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
    @Test
    void testGetAllEmployees() {
        // Setup
        // Create and configure an Employee entity
        final Employee employee = new Employee();
        employee.setEmployeeId(0);
        employee.setEmployeeName("employeeName");
        employee.setEmail("email");
        employee.setPassword("password");
        employee.setPoints(0);
        employee.setEarned(0);
        final Recognition recognition = new Recognition();
        employee.setBadgesReceived(Set.of(recognition));
        final List<Employee> employees = List.of(employee);

        // Mock EmployeeRepository to return the employee list
        when(mockEmployeeRepo.findAll()).thenReturn(employees);

        // Create and configure the corresponding EmployeeDto
        final EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(0);
        employeeDto.setEmployeeName("employeeName");
        employeeDto.setDesignation("designation"); // Ensure this matches your DTO definition
        employeeDto.setEmail("email");
        employeeDto.setEarned(0);

        // Mock ModelMapper to map Employee to EmployeeDto
        when(mockModelMapper.map(any(Employee.class), eq(EmployeeDto.class))).thenReturn(employeeDto);

        // Run the test
        final List<EmployeeDto> result = employeeServiceImplUnderTest.getAllEmployees();

        // Verify the results
        assertEquals(1, result.size()); // Verify that the result list has one element

        // Verify mapping for the employee
        EmployeeDto resultEmployeeDto = result.get(0);
        assertEquals(employee.getEmployeeId(), resultEmployeeDto.getEmployeeId());
        assertEquals(employee.getEmployeeName(), resultEmployeeDto.getEmployeeName());
        assertEquals(employee.getEmail(), resultEmployeeDto.getEmail());
        assertEquals(employee.getPoints(), resultEmployeeDto.getPoints()); // Ensure this matches your DTO definition
        assertEquals(employee.getEarned(), resultEmployeeDto.getEarned());
    }
}

