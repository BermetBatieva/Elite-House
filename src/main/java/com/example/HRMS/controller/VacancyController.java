package com.example.HRMS.controller;

import com.example.HRMS.entity.Vacancy;
import com.example.HRMS.services.VacancyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/vacancy")
public class VacancyController {

    @Autowired
    private VacancyService vacancyService;

    @ApiOperation(value = "Получение активных вакансий")
    @GetMapping("/all")
    public List<Vacancy> getAll(){
        return vacancyService.getAll();
    }

}
