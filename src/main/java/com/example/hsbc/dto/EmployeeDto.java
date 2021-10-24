package com.example.hsbc.dto;

import com.example.hsbc.enums.Grade;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDto {

    private Long id;
    private String name;
    private String surname;
    private Grade grade;
    private Double salary;

    public static EmployeeDto buildOverallEntry(Object[] result) {
        return EmployeeDto.builder()
                .name((String) result[0])
                .surname(((String) result[1]))
                .grade((Grade)result[2])
                .salary((Double) result[3])
                .build();
    }
}
