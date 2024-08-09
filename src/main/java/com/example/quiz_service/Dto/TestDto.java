package com.example.quiz_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {
    private int id;
    private int ownerId;
    private String name;
    private String tag;
}
