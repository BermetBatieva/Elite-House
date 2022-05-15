package com.example.HRMS.services;

import com.example.HRMS.DTO.*;
import com.example.HRMS.entity.Result;
import com.example.HRMS.entity.Test;
import com.example.HRMS.entity.User;
import com.example.HRMS.enums.Approvness;
import com.example.HRMS.enums.Role;
import com.example.HRMS.enums.Status;
import com.example.HRMS.enums.StatusTest;
import com.example.HRMS.repository.PositionRepository;
import com.example.HRMS.repository.ResultRepository;
import com.example.HRMS.repository.TestRepository;
import com.example.HRMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MainPageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private PositionRepository positionRepository;

    public List<TopCandidates> topCandidates(Long id) {

        List<User> candidatesList = userRepository.findByStatusAndRoleAndPosition_IdAndApprovness(Status.ACTIVE,
                Role.CANDIDATE, id, Approvness.APPROVED);

        List<TopCandidates> resultList = new ArrayList<>();

        List<UserAndAVG> candidateTestResults = new ArrayList<>();

        for (User user : candidatesList) {
            System.out.println(user.getEmail());
            List<Result> results = resultRepository.findAllByUser_IdAndStatusTest(user.getId(), StatusTest.FINISHED);
            UserAndAVG userAndAVG = new UserAndAVG();
            userAndAVG.setUser(user);
            double avg = 0.0;
            for (Result result : results) {
                avg += result.getGeneralScore() * 100 / result.getMaxScore();
            }
            userAndAVG.setAvg(avg);
            candidateTestResults.add(userAndAVG);
        }

        Collections.sort(candidateTestResults, new Comparator<UserAndAVG>() {
            @Override
            public int compare(UserAndAVG u1, UserAndAVG u2) {
                return u1.getAvg().compareTo(u2.getAvg());
            }
        });

        int index = 0;
        for (UserAndAVG u : candidateTestResults) {
            TopCandidates topCandidates = new TopCandidates();
            topCandidates.setCandidateId(u.getUser().getId());
            topCandidates.setFirstName(u.getUser().getFirstName());
            topCandidates.setLastName(u.getUser().getLastName());
            topCandidates.setPosition(u.getUser().getPosition().getName());

            topCandidates.setTests(resultRepository.findAllByUser_Id(u.getUser().getId()).size());
            topCandidates.setCompleted(resultRepository.findAllByUser_IdAndStatusTest(u.getUser().getId(), StatusTest.FINISHED).size());
            resultList.add(topCandidates);
            index++;
            if (index == 3)
                break;
        }

        return resultList;
    }

    public DiagramAndResult diagram(Long positionId) {
        DiagramAndResult diagramAndResult = new DiagramAndResult();

        List<MainPageDiagram> mainPageDiagrams = new ArrayList<>();

        List<Test> testList = testRepository.findAllByPosition_IdAndStatus(positionId, Status.ACTIVE);

        for (Test t : testList) {
            MainPageDiagram mainPageDiagram = new MainPageDiagram();
            mainPageDiagram.setCandidateAvg(resultRepository.avg(Role.CANDIDATE, t));
            mainPageDiagram.setEmployeeAvg(resultRepository.avg(Role.EMPLOYEE, t));
            mainPageDiagram.setTestName(t.getName());
            mainPageDiagrams.add(mainPageDiagram);
        }
        diagramAndResult.setDiagramList(mainPageDiagrams);

        CompletedTest completedTest = new CompletedTest();
        completedTest.setCompleted(resultRepository.countCompleted(positionId, Role.CANDIDATE));
        completedTest.setUncompleted(resultRepository.countUncompleted(positionId, Role.CANDIDATE));
        diagramAndResult.setCompletedTest(completedTest);
        return diagramAndResult;
    }

    public List<TestMainPage> tests(Long positionId) {
        List<Test> testList = testRepository.findAllByPosition_IdAndStatus(positionId, Status.ACTIVE);
        List<TestMainPage> listResult = new ArrayList<>();
        for (Test t : testList) {
            TestMainPage testMainPage = new TestMainPage();
            testMainPage.setId(t.getId());
            testMainPage.setTestName(t.getName());
            testMainPage.setCompleted(resultRepository.countCompletedTest(t.getId(), Role.CANDIDATE));
            testMainPage.setUncompleted(resultRepository.countUncompletedTest(t.getId(), Role.CANDIDATE));
            listResult.add(testMainPage);
        }

        return listResult;
    }
}
