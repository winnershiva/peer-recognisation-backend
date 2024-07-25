package com.org.peerrecognition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.org.peerrecognition.model.Badges;

public interface BadgeRepository extends JpaRepository<Badges, Integer>{
	
	Badges findByBadgeName(String badgeName);

}
