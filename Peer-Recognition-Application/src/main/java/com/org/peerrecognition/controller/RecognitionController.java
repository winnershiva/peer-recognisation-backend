package com.org.peerrecognition.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.peerrecognition.dto.RecognitionDto;
import com.org.peerrecognition.dto.RecognizeDto;
import com.org.peerrecognition.service.RecognitionService;

@RestController
@RequestMapping("/api/")
public class RecognitionController {
	
	@Autowired
	private RecognitionService recognizeService;
	
	@PostMapping("recognize/{giverId}/{receiverId}")
	public ResponseEntity<RecognitionDto> recognize(@PathVariable int giverId, @PathVariable int receiverId, 
			@RequestBody RecognizeDto recognizeDto)
	{
		RecognitionDto recognizedDto = this.recognizeService.recognize(giverId, receiverId, recognizeDto);
		return new ResponseEntity<>(recognizedDto,HttpStatus.CREATED);
	}
}
