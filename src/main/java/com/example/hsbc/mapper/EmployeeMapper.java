package com.example.hsbc.mapper;

import com.example.hsbc.dto.EmployeeDto;
import com.example.hsbc.entity.Employee;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public class EmployeeMapper {

    public EmployeeDto toDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .grade(employee.getGrade())
                .salary(employee.getSalary())
                .build();
    }
    public EmployeeDto toDtoWithOptional(Optional<Employee> employee) {
        return EmployeeDto.builder()
                .id(employee.get().getId())
                .name(employee.get().getName())
                .surname(employee.get().getSurname())
                .grade(employee.get().getGrade())
                .salary(employee.get().getSalary())
                .build();
    }

    public Employee toEntity(EmployeeDto employeeDto) {
        return Employee.builder()
                .id(employeeDto.getId())
                .name(employeeDto.getName())
                .surname(employeeDto.getSurname())
                .grade(employeeDto.getGrade())
                .salary(employeeDto.getSalary())
                .build();
    }

    public List<EmployeeDto> toEmployeeListDto(List<Employee> employees) {
        return employees.stream().map(this::toDto)
                .collect(Collectors.toList());
    }

}
