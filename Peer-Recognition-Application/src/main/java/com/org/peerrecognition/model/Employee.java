package com.org.peerrecognition.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeId;
	private String employeeName;
	private String email;
	private String password;
	private String designation;
	private int points;
	private int earned;
	
	@OneToMany(cascade = CascadeType.ALL, 
			mappedBy = "receiver", fetch = FetchType.LAZY)
	private Set<Recognition> badgesReceived = new HashSet<>();
	
	
	@OneToMany(cascade = CascadeType.ALL, 
			mappedBy = "giver", fetch = FetchType.LAZY)
	private Set<Recognition> badgesGiven = new HashSet<>();

}
