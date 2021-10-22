package controller;

import com.example.hsbc.HsbcApplication;
import com.example.hsbc.entity.Employee;
import com.example.hsbc.service.EmployeeService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.hsbc.enums.Grade.BOSS;
import static com.example.hsbc.enums.Grade.CHICKEN;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HsbcApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;
    @MockBean
    private EmployeeService employeeService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    Employee employee1 =Employee.builder()
            .id(1L)
            .name("Baba")
            .surname("Yaga")
            .grade(BOSS)
            .salary(13000.0)
            .build();

    Employee employee2 =Employee.builder()
            .id(2L)
            .name("Matka")
            .surname("Boszka")
            .grade(CHICKEN)
            .salary(13000.0)
            .build();

    Employee employee3 =Employee.builder()
            .id(3L)
            .name("Matka")
            .surname("Bardziej Boszka")
            .grade(BOSS)
            .salary(13000.0)
            .build();

    @Test
    public void shouldFetchAllEmployees() throws Exception {
        List<Employee> employees = new ArrayList<>(Arrays.asList(employee1,employee2,employee3));
        Mockito.when(employeeService.getAllEmployees()).thenReturn(employees);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/employees")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(3)))
                .andReturn();
    }

    @Test
    public void shouldFindEmployeeById() throws Exception {
        Employee employee = employee2;
        Mockito.when(employeeService.findById(employee.getId())).thenReturn(java.util.Optional.of(employee));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/" + "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value("Matka"))
                .andExpect(jsonPath("$.surname").value("Boszka"))
                .andExpect(jsonPath("$.grade").value(CHICKEN.name()))
                .andExpect(jsonPath("$.salary").value(13000.0))
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andReturn();
    }


    @Test
    public void shouldVerifyInvalidEmployeeArgument() throws Exception {
        Employee employee = employee2;
        Mockito.when(employeeService.findById(employee.getId())).thenReturn(java.util.Optional.of(employee));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/" + "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Your request has issued a malformed or illegal request."))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidSaveEmployee() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"surname\": \"Bohun\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Your request has issued a malformed or illegal request."))
                .andReturn();

    }


    @Test
    public void shouldVerifyInvalidPropertyNameWhenUpdateEmployee() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 2, \"nnaammee\": \"C. S. Lewis\", \"surname\": \"cslewis@books.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Your request has issued a malformed or illegal request."))
                .andReturn();
    }

    @Test
    public void shouldRemoveEmployee() throws Exception {
        Employee employee = employee1;
        Mockito.when(employeeService.findById(employee.getId())).thenReturn(java.util.Optional.of(employee));
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/delete/" + "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Employee with ID: '1' deleted."))
                .andReturn();
    }

    @Test
    public void shouldVerifyInvalidEmployeeRemove() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/delete/" + "999")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Employee with ID: '999' not found."))
                .andReturn();
    }
}
