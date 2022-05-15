package com.example.HRMS.controller;

import com.example.HRMS.entity.Position;
import com.example.HRMS.services.PositionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private PositionService positionService;


    @ApiOperation(value = "Получение всех позиции по id департамента")
    @GetMapping("/find-all/{departmentId}")
    public List<Position> findByDepartment(@PathVariable Long departmentId){
        return positionService.findByDepartment(departmentId);
    }

    @ApiOperation(value = "Получение всех открытых позиции")
    @GetMapping("/find-all-open-positions")
    public List<Position> findByVacancy(){
        return positionService.findByVacancy();
    }
}
