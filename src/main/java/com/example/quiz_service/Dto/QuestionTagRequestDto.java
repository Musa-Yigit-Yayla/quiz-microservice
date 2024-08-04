package com.example.quiz_service.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTagRequestDto {
    private int questionOwnerId;
    private int questionId;
    private String tag;
    private int requesterId;
}
