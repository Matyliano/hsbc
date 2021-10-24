package com.example.hsbc.util;

import com.example.hsbc.enums.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class RequestSearchEmployeeCriteria {

    private List<String> name;
    private List<String> surname;
    private List<Grade> grade;
    private List<Double> salary;

}
