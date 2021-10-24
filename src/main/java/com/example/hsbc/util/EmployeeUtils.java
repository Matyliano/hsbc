package com.example.hsbc.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeUtils {

    public <T> Page<T> createPageFromListOfEmployeesEntry(List<T> collect, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), collect.size());

        if (start > end) {
            return new PageImpl<>(collect.subList(0, end), PageRequest.of(0, pageable.getPageSize()), collect.size());
        }
        return new PageImpl<>(collect.subList(start, end), pageable, collect.size());
    }
}
