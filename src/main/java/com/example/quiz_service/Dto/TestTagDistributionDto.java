package com.example.quiz_service.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestTagDistributionDto {
    private int testCount;
    private String tag;
}
