package com.example.quiz_service.Dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto{
    private int id;
    private int ownerId;
    private String questionBody;
    private List<String> options;
    private int answerIndex;
    private String difficulty;
}