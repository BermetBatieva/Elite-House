package com.example.HRMS.services;

import com.example.HRMS.DTO.AnalyticsDto;
import com.example.HRMS.DTO.PositionAndTest;
import com.example.HRMS.DTO.UserAndAVG;
import com.example.HRMS.entity.Result;
import com.example.HRMS.entity.Test;
import com.example.HRMS.enums.Role;
import com.example.HRMS.enums.Status;
import com.example.HRMS.enums.StatusTest;
import com.example.HRMS.repository.ResultRepository;
import com.example.HRMS.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AnalyticsService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private TestRepository testRepository;

    @Transactional
    public List<AnalyticsDto> diagram(PositionAndTest positionAndTest) {
        List<AnalyticsDto> result = new ArrayList<>();

        List<Result> resultList = resultRepository.Test(testRepository.findById(positionAndTest
                .getTestId()).get().getId(), Role.EMPLOYEE);
        Set<Result> results = new HashSet<>();

        for (Result r : resultList)
            results.add(r);

        List<Result> resultsList = new ArrayList<>();

        for (Result r : results) {
            resultsList.add(r);
        }

        Collections.sort(resultsList, new Comparator<Result>() {
            @Override
            public int compare(Result u1, Result u2) {
                return u1.getMaxScore().compareTo(u2.getMaxScore());
            }
        });

        int index = 0;

        for (Result r : resultsList) {
            AnalyticsDto analyticsDto = new AnalyticsDto();
            analyticsDto.setEmployeeId(r.getUser().getId());
            analyticsDto.setFirsName(r.getUser().getFirstName());
            analyticsDto.setLastName(r.getUser().getLastName());
            analyticsDto.setPatronymic(r.getUser().getPatronymic());
            analyticsDto.setPhoto(r.getUser().getPhotoUrl());
            analyticsDto.setGeneralScore(r.getGeneralScore());
            result.add(analyticsDto);
            index++;
            if (index == 3)
                break;
        }
        return result;
    }


}