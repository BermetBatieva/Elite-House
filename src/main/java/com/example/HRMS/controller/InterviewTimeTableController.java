package com.example.HRMS.controller;

import com.example.HRMS.DTO.*;
import com.example.HRMS.entity.InterviewTimeTable;
import com.example.HRMS.repository.InterviewTimeTableRepository;
import com.example.HRMS.services.InterviewTimeTableService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/interview-slots")
public class InterviewTimeTableController {

    @Autowired
    private InterviewTimeTableService interviewTimeTableService;

    @Autowired
    private InterviewTimeTableRepository repository;

    @ApiOperation(value = "Добавление новых слотов")
    @PostMapping("/add")
    public SlotResponseMessage addSlot(@RequestBody SlotDto slotDto){
        return interviewTimeTableService.createSlot(slotDto);
    }

    @PostMapping("/add-multiple-slots")
    public List<SlotResponseMessage> addSlots(@RequestBody List<SlotDto> slots){
        return interviewTimeTableService.createSlots(slots);
    }

    @ApiOperation(value = "Бронирование слотов кандидатом")
    @PostMapping("/reserve")
    public void reserveSlot(@RequestBody IdDto idDto){
        interviewTimeTableService.reserveSlot(idDto);
    }


    @ApiOperation(value = "Выборка дня, времени и интервьювера для кандидата")
    @GetMapping("/get-slots-by-month/{yyyy_mm}")
    public Set<DateAndTimes> getSlotsByMonth(@PathVariable String yyyy_mm) throws ParseException {
        return interviewTimeTableService.getAllSlotsByMonth(yyyy_mm);
    }

    @ApiOperation(value = "Просмотр всех слотов (резервированных и свободных) для интервьювера")
    @GetMapping("/get-all-interviewers-slots/{yyyy_mm}")
    public Set<DayInterviewer> getAllSlotsOfInterviewerByMonth(@PathVariable String yyyy_mm) throws ParseException {
        return interviewTimeTableService.getAllSlotsForInterviewer(yyyy_mm);
    }

    @ApiOperation(value = "Лист всех предстоящих слотов (интервью)")
    @GetMapping("/get-all-upcoming-interviews-of-candidate")
    public Set<MyUpcomingInterviews> getUpcomingInterviewSlotsForCandidate(){
        return interviewTimeTableService.getAllUpcomingInterviewSlotsForCandidate();
    }
}
