package com.org.peerrecognition.controller;

import com.org.peerrecognition.dto.LoginRequest;
import com.org.peerrecognition.dto.LoginResponse;
import com.org.peerrecognition.model.Employee;
import com.org.peerrecognition.service.impl.EmployeeServiceImpl;
import com.org.peerrecognition.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final EmployeeServiceImpl employeeService;

    private final JwtUtil jwtUtil;


    @Autowired
    public LoginController(AuthenticationManager authenticationManager, EmployeeServiceImpl employeeService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.employeeService = employeeService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws IOException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect email or password.");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee is not activated");
            return null;
        }
        final UserDetails userDetails = employeeService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        String loggedInEmployeeEmail = userDetails.getUsername();
        int employeeId = this.employeeService.getEmployeeIdByEmail(loggedInEmployeeEmail);
        return new LoginResponse(jwt, employeeId);
    }

}
