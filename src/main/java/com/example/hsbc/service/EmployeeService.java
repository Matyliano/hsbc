package com.example.hsbc.service;


import com.example.hsbc.entity.Employee;
import com.example.hsbc.exception.CustomException;
import com.example.hsbc.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;


    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;

    }

    public Employee save(Employee employee) {
        log.info("Saving new employee {} to database", employee.getSurname());
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        log.info("Getting all employees from database ");
        return employeeRepository.findAll();
    }


    public Optional<Employee> findById(Long id) throws CustomException {
        log.info("Getting employee from database with ID " + id);
        return Optional.ofNullable(employeeRepository.findById(id).orElseThrow(() -> new CustomException("Employee with ID: '" + id + "' not found.")));


    }

    public Employee update(Employee employee, Long id) {
        log.info("Updating an employee wit ID " + id);
        Employee updateEmployee = employeeRepository.getById(id);
        if (employee.getName() != null) {
            updateEmployee.setName(employee.getName());
        }
        if (employee.getSurname() != null) {
            updateEmployee.setSurname(employee.getSurname());
        }
        if (employee.getGrade() != null) {
            updateEmployee.setGrade(employee.getGrade());
        }
        if (employee.getSalary() != null) {
            updateEmployee.setSalary(employee.getSalary());
        }
        return employeeRepository.save(updateEmployee);
    }


    public void deleteById(Long id) {
        log.info("Delete employee with ID " + id);
        this.employeeRepository
                .deleteById(id);
    }
}
