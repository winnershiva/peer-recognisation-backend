package com.org.peerrecognition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.org.peerrecognition.model.Recognition;

public interface RecognitionRepository extends JpaRepository<Recognition, Integer>{

}
