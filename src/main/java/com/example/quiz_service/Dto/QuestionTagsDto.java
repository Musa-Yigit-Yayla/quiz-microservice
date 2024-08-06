package com.example.quiz_service.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTagsDto {
    private int questionId;
    private List<String> tags;
}
