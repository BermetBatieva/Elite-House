package com.example.HRMS.controller;

import com.example.HRMS.DTO.MessageResponse;
import com.example.HRMS.DTO.PasswordDto;
import com.example.HRMS.entity.Department;
import com.example.HRMS.services.DepartmentService;
import com.example.HRMS.services.NewsImageService;
import com.example.HRMS.services.UserDetailsServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/user")
public class AllUsersController {

    @Autowired
    private UserDetailsServiceImpl userServiceImpl;

    @Autowired
    private DepartmentService departmentService;



    @ApiOperation(value = "получение всех активных департаментов")
    @GetMapping("/all-active-departments")
    public List<Department> getAllActiveDepartments(){
        return departmentService.getAllActive();
    }


}
