package com.org.peerrecognition.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "badges")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Badges {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int badgeId;
	private String badgeName;
	
}
