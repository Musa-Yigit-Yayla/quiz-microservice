package com.example.quiz_service.Dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto{
    private String questionBody;
    List<String> options;
    String correctAnswer;
    String difficulty;
}