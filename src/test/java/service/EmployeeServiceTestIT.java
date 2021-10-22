package service;

import com.example.hsbc.HsbcApplication;
import com.example.hsbc.entity.Employee;
import com.example.hsbc.enums.Grade;
import com.example.hsbc.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = {HsbcApplication.class})
public class EmployeeServiceTestIT {

    @Autowired
    private EmployeeService employeeService;

    @BeforeEach
    void cleanService() {

    }

    @Test
    void ShouldNotBeEmpty() {
        Employee employee = Employee.builder().id(1L).name("Matka").surname("Bardzo Boszka")
                .grade(Grade.BOSS).salary(14000.0).build();
        employeeService.save(employee);
        List<Employee> all = employeeService.getAllEmployees();
        assertThat(all).isNotEmpty();

    }

    @Test
    void shouldAddEmployeeToDB() {
        Employee player = Employee.builder().id(1L).name("Matka").surname("Bardzo Boszka")
                .grade(Grade.BOSS).salary(14000.0).build();
        Employee save = employeeService.save(player);
        assertThat(save.getId()).isPositive();
    }

}
