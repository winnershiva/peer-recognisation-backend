package com.org.peerrecognition.service.impl;

import com.org.peerrecognition.dto.BadgeDto;
import com.org.peerrecognition.dto.RecognitionDto;
import com.org.peerrecognition.dto.RecognizeDto;
import com.org.peerrecognition.exception.ResourceNotFoundException;
import com.org.peerrecognition.model.Badges;
import com.org.peerrecognition.model.Employee;
import com.org.peerrecognition.model.Recognition;
import com.org.peerrecognition.repository.BadgeRepository;
import com.org.peerrecognition.repository.EmployeeRepository;
import com.org.peerrecognition.repository.RecognitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecognitionServiceImplTest {

    @Mock
    private ModelMapper mockModelMapper;
    @Mock
    private BadgeRepository mockBadgeRepo;
    @Mock
    private EmployeeRepository mockEmployeeRepo;
    @Mock
    private RecognitionRepository mockRecognizeRepo;

    @InjectMocks
    private RecognitionServiceImpl recognitionServiceImplUnderTest;

    @Test
    void testRecognize() {
        // Setup
        final RecognizeDto recognizeDto = new RecognizeDto("badgeName", "comment");
        final Badges badge = new Badges(0, "badgeName");
        final Employee giver = new Employee();
        giver.setEmployeeId(0);
        giver.setEmployeeName("giverName");
        giver.setPoints(100);
        giver.setEarned(0);
        final Employee receiver = new Employee();
        receiver.setEmployeeId(1);
        receiver.setEmployeeName("receiverName");
        receiver.setPoints(0);
        receiver.setEarned(100);

        // Create a Recognition object to be returned from the repository save
        final Recognition recognition = new Recognition();
        recognition.setBadges(badge);
        recognition.setGiver(giver);
        recognition.setReceiver(receiver);
        recognition.setComment("comment");

        // Configure mocks
        when(mockBadgeRepo.findByBadgeName("badgeName")).thenReturn(badge);
        when(mockModelMapper.map(any(RecognizeDto.class), eq(Recognition.class))).thenReturn(recognition);
        when(mockEmployeeRepo.findById(0)).thenReturn(Optional.of(giver));
        when(mockEmployeeRepo.findById(1)).thenReturn(Optional.of(receiver));
        when(mockRecognizeRepo.save(any(Recognition.class))).thenReturn(recognition);

        // Run the test
        final RecognitionDto result = recognitionServiceImplUnderTest.recognize(0, 1, recognizeDto);

        // Verify the results
        assertNotNull(result);
        assertEquals("badgeName", result.getBadgeName());
        assertEquals("giverName", result.getGiverName());
        assertEquals("receiverName", result.getReceiverName());
        assertEquals("comment", result.getComment());

        // Verify interactions
        verify(mockEmployeeRepo).save(giver);
        verify(mockEmployeeRepo).save(receiver);
    }

    @Test
    void testRecognize_EmployeeRepositoryFindByIdReturnsAbsent() {
        // Setup
        final RecognizeDto recognizeDto = new RecognizeDto("badgeName", "comment");
        when(mockBadgeRepo.findByBadgeName("badgeName")).thenReturn(new Badges(0, "badgeName"));
        when(mockModelMapper.map(any(RecognizeDto.class), eq(Recognition.class))).thenReturn(new Recognition());
        when(mockEmployeeRepo.findById(0)).thenReturn(Optional.empty());

        // Run the test and verify exception
        assertThrows(ResourceNotFoundException.class, () -> recognitionServiceImplUnderTest.recognize(0, 1, recognizeDto));
    }

    @Test
    void testDtoToRecognition() {
        // Setup
        final RecognizeDto recognizeDto = new RecognizeDto("badgeName", "comment");
        final Recognition recognition = new Recognition();
        when(mockModelMapper.map(recognizeDto, Recognition.class)).thenReturn(recognition);

        // Run the test
        final Recognition result = recognitionServiceImplUnderTest.dtoToRecognition(recognizeDto);

        // Verify the results
        assertNotNull(result);
        verify(mockModelMapper).map(recognizeDto, Recognition.class);
    }

    @Test
    void testRecognitionToDto() {
        // Setup
        final Recognition recognition = new Recognition();
        recognition.setGiver(new Employee());
        recognition.setReceiver(new Employee());
        recognition.setBadges(new Badges());
        recognition.setComment("comment");

        final RecognitionDto recognitionDto = new RecognitionDto();
        recognitionDto.setGiverName("giverName");
        recognitionDto.setReceiverName("receiverName");
        recognitionDto.setBadgeName("badgeName");
        recognitionDto.setComment("comment");

        when(mockModelMapper.map(recognition, RecognitionDto.class)).thenReturn(recognitionDto);

        // Run the test
        final RecognitionDto result = recognitionServiceImplUnderTest.recognitionToDto(recognition);

        // Verify the results
        assertNotNull(result);
        assertEquals("giverName", result.getGiverName());
        assertEquals("receiverName", result.getReceiverName());
        assertEquals("badgeName", result.getBadgeName());
        assertEquals("comment", result.getComment());

        verify(mockModelMapper).map(recognition, RecognitionDto.class);
    }
}
