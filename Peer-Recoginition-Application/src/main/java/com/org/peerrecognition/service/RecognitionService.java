package com.org.peerrecognition.service;

import com.org.peerrecognition.dto.RecognitionDto;
import com.org.peerrecognition.dto.RecognizeDto;

public interface RecognitionService {
	
	RecognitionDto recognize(int giver, int receiver, RecognizeDto recognizeDto);

}
