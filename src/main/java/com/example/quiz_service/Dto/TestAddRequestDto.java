package com.example.quiz_service.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestAddRequestDto {
    private int testOwnerId;
    private String testName;
    private int testId;
    private int requesterId;
    private int questionId;
}
