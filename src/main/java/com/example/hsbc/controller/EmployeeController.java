package com.example.hsbc.controller;


import com.example.hsbc.dto.EmployeeDto;
import com.example.hsbc.exception.CustomException;
import com.example.hsbc.exception.CustomResponse;
import com.example.hsbc.mapper.EmployeeMapper;
import com.example.hsbc.service.EmployeeService;
import com.example.hsbc.util.RequestSearchEmployeeCriteria;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private static final String EMPLOYEE_WITH_ID = "Employee with ID: '";

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @Operation(summary = "Get all employees")

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "In case of a valid query, the status code 200 and a list of all employees are returned. "),
            @ApiResponse(code = 404, message = "In the absence of data  " +
                    "404 Not Found is returned"),
            @ApiResponse(code = 400, message = "In case of incorrectly formulated queries, the service returns" +
                    " 400 Bad Request message")
    })

    @GetMapping()
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeMapper.toEmployeeListDto(employeeService.getAllEmployees()));

    }

    @Operation(summary = "Get employee by id")

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "In case of a valid query, the status code 200 and an employee with the given id is returned. "),
            @ApiResponse(code = 404, message = "In the absence of data  " +
                    "404 Not Found is returned"),
            @ApiResponse(code = 400, message = "In case of incorrectly formulated queries, the service returns" +
                    " 400 Bad Request message")
    })

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(
            @ApiParam(name = "id",
                    value = "Employee id",
                    required = true,
                    type = "Long")
            @PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(employeeMapper.toDtoWithOptional(employeeService.findById(id)));
    }

    @Operation(summary = "Save employee in database")

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "In case of a valid query, the status code 200 and an employee is saved in database "),
            @ApiResponse(code = 400, message = "In case of incorrectly formulated queries, the service returns" +
                    " 400 Bad Request message")
    })

    @PostMapping("/save")
    public ResponseEntity<EmployeeDto> saveEmployee(@RequestBody @Validated EmployeeDto employee) {
        return ResponseEntity.ok(employeeMapper.toDto(employeeService.save(employeeMapper.toEntity(employee))));
    }

    @Operation(summary = "Partial update of employee")

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "In case of a valid query, the status code 200 and partial update of field is save "),
            @ApiResponse(code = 404, message = "In the absence of data  " +
                    "404 Not Found is returned"),
            @ApiResponse(code = 400, message = "In case of incorrectly formulated queries, the service returns" +
                    " 400 Bad Request message")
    })

    @PatchMapping("/partialUpdate/{id}")
    public ResponseEntity<EmployeeDto> partialUpdateEmployee(@PathVariable final Long id, @RequestBody @Validated Map<String, Object> fieldsToUpdate) throws CustomException {
        EmployeeDto employee = employeeMapper.toDtoWithOptional(employeeService.findById(id));
        if (employee != null) {
            fieldsToUpdate.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(EmployeeDto.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, employee, value);
            });
            EmployeeDto updateEmployee = employeeMapper.toDto(employeeService.save(employeeMapper.toEntity(employee)));
            return ResponseEntity.ok(updateEmployee);
        }
        return null;
    }

    @Operation(summary = "Update employee in database ")

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "In case of a valid query, the status code 200 and an employee data id changed in database "),
            @ApiResponse(code = 404, message = "In the absence of data  " +
                    "404 Not Found is returned"),
            @ApiResponse(code = 400, message = "In case of incorrectly formulated queries, the service returns" +
                    " 400 Bad Request message")
    })

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody @Validated EmployeeDto employeeDto, @PathVariable Long id) throws CustomException {
        if (employeeService.findById(employeeDto.getId()).isPresent()) {
            return new ResponseEntity<>(employeeMapper.toDto(employeeService.update(employeeMapper.toEntity(employeeDto), id)), HttpStatus.OK);
        } else {
            throw new CustomException(EMPLOYEE_WITH_ID + employeeDto.getId() + "' not found.");
        }
    }

    @Operation(summary = "Delete employee ")

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "In case of a valid query, the status code 200 and an employee is deleted from database "),
            @ApiResponse(code = 404, message = "In the absence of data  " +
                    "404 Not Found is returned"),
            @ApiResponse(code = 400, message = "In case of incorrectly formulated queries, the service returns" +
                    " 400 Bad Request message")
    })

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse> deleteEmployee(@PathVariable Long id) throws CustomException {
        if (employeeService.findById(id).isPresent()) {
            this.employeeService.deleteById(id);
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK.value(),
                            EMPLOYEE_WITH_ID + id + "' deleted."), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.NOT_FOUND.value(),
                            EMPLOYEE_WITH_ID + id + "' not found."), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/getByCriteria")
    public ResponseEntity<Page<EmployeeDto>> getEmployeeByCriteria(@PageableDefault(size = 15) Pageable pageable,
                                     RequestSearchEmployeeCriteria requestEmployeeCriteria){
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeesBySearchCriteria(pageable, requestEmployeeCriteria));
    }
}
