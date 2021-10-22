package service;

import com.example.hsbc.entity.Employee;
import com.example.hsbc.enums.Grade;
import com.example.hsbc.exception.CustomException;
import com.example.hsbc.repository.EmployeeRepository;
import com.example.hsbc.service.EmployeeService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;


    @InjectMocks
    private EmployeeService employeeService;

    private Employee e1 = Employee.builder().name("Matka").surname("Boszka").grade(Grade.CHICKEN).salary(13000.0).build();
    private Employee e2 = Employee.builder().name("Matka").surname("Bardzo Boszka").grade(Grade.BOSS).salary(14000.0).build();
    private Employee e3 = Employee.builder().name("Matka").surname("Boszka").grade(Grade.CHICKEN).salary(11000.0).build();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        e1.setId(1L);
        e2.setId(2L);
        e3.setId(3L);
    }

    @Test
    public void findAll() {

        given(employeeRepository.findAll()).willReturn(Arrays.asList(e1, e2, e3));

        List<Employee> all = employeeService.getAllEmployees();

        assertThat(all).hasSize(3);

        assertEmployeeFields(all.get(0));

        verify(employeeRepository, times(1)).findAll();
    }


    @Test
    public void findById() throws CustomException {

        given(employeeRepository.findById(1L)).willReturn(Optional.of(e1));

        Optional<Employee> employee = employeeService.findById(1L);

        assertThat(employee.isPresent()).isTrue();

        assertEmployeeFields(employee.orElseGet(null));

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void save(){

        given(employeeRepository.save(e1)).willReturn(e1);

        Employee result = employeeService.save(e1);

        assertEmployeeFields(result);

        verify(employeeRepository, times(1)).save(e1);

    }

    @Test
    public void delete(){

        employeeService.deleteById(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    private void assertEmployeeFields(Employee member) {
        assertThat(member.getId()).isInstanceOf(Long.class);
        assertThat(member.getId()).isEqualTo(1);
        assertThat(member.getName()).isEqualTo("Matka");
        assertThat(member.getSurname()).isEqualTo("Boszka");
        assertThat(member.getGrade()).isEqualTo(Grade.CHICKEN);
        assertThat(member.getSalary()).isEqualTo(13000.0);
    }
}
