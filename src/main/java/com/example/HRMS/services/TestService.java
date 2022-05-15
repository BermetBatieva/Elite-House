package com.example.HRMS.services;

import com.example.HRMS.DTO.*;
import com.example.HRMS.entity.*;
import com.example.HRMS.enums.*;
import com.example.HRMS.enums.Checking;
import com.example.HRMS.enums.Role;
import com.example.HRMS.enums.Status;
import com.example.HRMS.exception.ResourceNotFoundException;
import com.example.HRMS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ImagesService imagesService;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PsychologicalResultsRepository psychologicalResultsRepository;

    @Autowired
    private GeneralResultRepository generalResultRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<TestList> findAll() {

        List<Test> tests = testRepository.findAll();

        List<TestList> testLists = new ArrayList<>();
        for (Test t : tests) {
            TestList testList = new TestList();
            testList.setTestId(t.getId());
            testList.setNameTest(t.getName());
            testList.setNumberOfDisplayedQuestions(t.getNumberOfDisplayedQuestions());
            testList.setQuantityQuestions(questionRepository.findAllByTest_Id(t.getId()).size());
            testList.setStatusTest(t.getStatus());

            if (resultRepository.findAllByTest_IdAndStatusTest(t.getId(), StatusTest.FINISHED).isEmpty())
                testList.setStatus(true);
            else
                testList.setStatus(false);
            testLists.add(testList);
        }

        return testLists;
    }

    @Transactional
    public List<InformationAboutCandidates> getByDepAndPosCandidate(DepAndPosDto dto) {
        List<InformationAboutCandidates> candidatesList = new ArrayList<>();

        if (dto.getDepId() != null) {
            List<User> users = userRepository.findAllByStatusAndRoleAndPosition_Department_Id(Status.ACTIVE, Role.CANDIDATE, dto.getDepId());
            for (User user : users) {
                InformationAboutCandidates model = new InformationAboutCandidates();
                model.setPosition(user.getPosition().getName());
                model.setEmail(user.getEmail());
                model.setFirstname(user.getFirstName());
                model.setLastname(user.getLastName());
                model.setId(user.getId());
                model.setApplicationStatus(user.getApplicationStatus());

                List<TestResultForTable> testResults = new ArrayList<>();

                List<Result> testList = resultRepository.findAllByUser_Id(user.getId());
                for (Result r : testList) {
                        TestResultForTable resultForTable = new TestResultForTable();
                        resultForTable.setName(r.getTest().getName());
                        List<Review> reviewList = reviewRepository.findAllByResult_Id(r.getId());
                        double trueAnswers = 0.0;
                        for (Review review : reviewList) {
                            if (review.getChecking() == Checking.TRUE)
                                trueAnswers++;
                        }
                        resultForTable.setTestResult(trueAnswers / reviewList.size() * 100);
                        testResults.add(resultForTable);
                    }
                    model.setTestResultForTableList(testResults);
                    candidatesList.add(model);
                }
        }else if(dto.getPosId() != null && dto.getDepId() != null ){

            List<User> users = userRepository.findAllByStatusAndRoleAndPosition_IdAndPosition_Department_Id(Status.ACTIVE,Role.CANDIDATE,dto.getPosId(),dto.getDepId());
            for (User user : users) {

                InformationAboutCandidates model = new InformationAboutCandidates();
                model.setId(user.getId());
                model.setLastname(user.getLastName());
                model.setFirstname(user.getFirstName());
                model.setDepartment(user.getPosition().getDepartment().getName());
                model.setPosition(user.getPosition().getName());
                model.setEmail(user.getEmail());
                model.setApplicationStatus(user.getApplicationStatus());

                List<TestResultForTable> testResults = new ArrayList<>();

                List<Result> testList = resultRepository.findAllByUser_Id(user.getId());

                for (Result r : testList) {
                    TestResultForTable resultForTable = new TestResultForTable();
                    resultForTable.setName(r.getTest().getName());
                    List<Review> reviewList = reviewRepository.findAllByResult_Id(r.getId());
                    double trueAnswers = 0.0;
                    for (Review review : reviewList) {
                        if (review.getChecking() == Checking.TRUE)
                            trueAnswers++;
                    }
                    resultForTable.setTestResult(trueAnswers / reviewList.size() * 100);
                    testResults.add(resultForTable);
                }
                model.setTestResultForTableList(testResults);
                candidatesList.add(model);
            }
        }
        else
            return getInfoAboutCandidates();
        return candidatesList;
    }




    public List<InformationAboutCandidates> getInfoAboutCandidates() {
        List<User> list = userRepository.findByStatusAndRole(Status.ACTIVE, Role.CANDIDATE);

        List<InformationAboutCandidates> candidatesList = new ArrayList<>();
        for (User user : list) {
            InformationAboutCandidates model = new InformationAboutCandidates();
            model.setId(user.getId());
            model.setLastname(user.getLastName());
            model.setFirstname(user.getFirstName());
            model.setDepartment(user.getPosition().getDepartment().getName());
            model.setPosition(user.getPosition().getName());
            model.setEmail(user.getEmail());
            model.setApplicationStatus(user.getApplicationStatus());

            List<TestResultForTable> testResults = new ArrayList<>();

            List<Result> testList = resultRepository.findAllByUser_Id(user.getId());

            for (Result r : testList) {
                TestResultForTable resultForTable = new TestResultForTable();
                resultForTable.setName(r.getTest().getName());
                List<Review> reviewList = reviewRepository.findAllByResult_Id(r.getId());
                double trueAnswers = 0.0;
                for (Review review : reviewList) {
                    if (review.getChecking() == Checking.TRUE)
                        trueAnswers++;
                }
                resultForTable.setTestResult(trueAnswers / reviewList.size() * 100);
                testResults.add(resultForTable);
            }
            model.setTestResultForTableList(testResults);
            candidatesList.add(model);
        }

        return candidatesList;
    }




    public List<InfoAboutEmployees> getInfoAboutEmployeesByDepAndPos(DepAndPosDto dto) {
        List<InfoAboutEmployees> result = new ArrayList<>();
        if(dto.getDepId() != null) {
            List<User> users = userRepository.findAllByStatusAndRoleAndPosition_Department_Id(Status.ACTIVE, Role.EMPLOYEE,dto.getDepId());
            for (User user : users) {
                InfoAboutEmployees model = new InfoAboutEmployees();
                model.setEmail(user.getEmail());
                model.setId(user.getId());
                model.setFirstname(user.getFirstName());
                model.setLastname(user.getLastName());
                model.setDepartment(user.getPosition().getDepartment().getName());
                model.setPosition(user.getPosition().getName());
                List<TestResultForTable> testResults = new ArrayList<>();

                List<Result> testList = resultRepository.findAllByUser_Id(user.getId());

                for (Result r : testList) {
                    TestResultForTable resultForTable = new TestResultForTable();
                    resultForTable.setName(r.getTest().getName());
                    resultForTable.setId(r.getId());
                    List<Review> reviewList = reviewRepository.findAllByResult_Id(r.getId());
                    double trueAnswers = 0.0;
                    for (Review review : reviewList) {
                        if (review.getChecking() == Checking.TRUE)
                            trueAnswers++;
                    }
                    resultForTable.setTestResult(trueAnswers / reviewList.size() * 100);
                    testResults.add(resultForTable);
                }
                model.setTestResultForTableList(testResults);
                result.add(model);
            }
        }else if(dto.getPosId() != null && dto.getDepId() != null ){
            List<User> users = userRepository.findAllByStatusAndRoleAndPosition_IdAndPosition_Department_Id(Status.ACTIVE,Role.EMPLOYEE,dto.getPosId(),dto.getDepId());
            for (User user : users) {
                InfoAboutEmployees model = new InfoAboutEmployees();
                model.setEmail(user.getEmail());
                model.setId(user.getId());
                model.setFirstname(user.getFirstName());
                model.setLastname(user.getLastName());
                model.setDepartment(user.getPosition().getDepartment().getName());
                model.setPosition(user.getPosition().getName());
                List<TestResultForTable> testResults = new ArrayList<>();

                List<Result> testList = resultRepository.findAllByUser_Id(user.getId());

                for (Result r : testList) {
                    TestResultForTable resultForTable = new TestResultForTable();
                    resultForTable.setName(r.getTest().getName());
                    List<Review> reviewList = reviewRepository.findAllByResult_Id(r.getId());
                    double trueAnswers = 0.0;
                    for (Review review : reviewList) {
                        if (review.getChecking() == Checking.TRUE)
                            trueAnswers++;
                    }
                    resultForTable.setTestResult(trueAnswers / reviewList.size() * 100);
                    testResults.add(resultForTable);
                }
                model.setTestResultForTableList(testResults);
                result.add(model);
            }
        } else
            return getInfoAboutEmployees();
        return result;
    }






        public List<InfoAboutEmployees> getInfoAboutEmployees() {

        List<User> list = userRepository.findByStatusAndRole(Status.ACTIVE, Role.EMPLOYEE);

        List<InfoAboutEmployees> result = new ArrayList<>();
        for (User user : list) {
            InfoAboutEmployees model = new InfoAboutEmployees();
            model.setEmail(user.getEmail());
            model.setId(user.getId());
            model.setFirstname(user.getFirstName());
            model.setLastname(user.getLastName());
            model.setDepartment(user.getPosition().getDepartment().getName());
            model.setPosition(user.getPosition().getName());
            List<TestResultForTable> testResults = new ArrayList<>();

            List<Result> testList = resultRepository.findAllByUser_Id(user.getId());

            for (Result r : testList) {
                TestResultForTable resultForTable = new TestResultForTable();
                resultForTable.setName(r.getTest().getName());
                List<Review> reviewList = reviewRepository.findAllByResult_Id(r.getId());
                double trueAnswers = 0.0;
                for (Review review : reviewList) {
                    if (review.getChecking() == Checking.TRUE)
                        trueAnswers++;
                }
                resultForTable.setTestResult(trueAnswers / reviewList.size() * 100);
                testResults.add(resultForTable);
            }
            model.setTestResultForTableList(testResults);
            result.add(model);        }
        return result;
    }






    public List<PsychologicalListDto> getPsychologicalResult(Long userId){

        PsychologicalResults psychologicalResults = psychologicalResultsRepository.findByUser_IdAndStatusTest(userId,StatusTest.CHECKED);

        List<GeneralResult> list = generalResultRepository.findAllByPsychologicalResults_Id(psychologicalResults.getId());

        List<PsychologicalListDto> listDto = new ArrayList<>();

        for ( GeneralResult g : list){
            PsychologicalListDto psychological = new PsychologicalListDto();
            psychological.setType(g.getPsychologicalType().getName());
            psychological.setResult(g.getGeneralScore());
            listDto.add(psychological);
        }
        return listDto;
    }





    public List<RecruiterDto> getAllActiveRecruiters(){

        List<User> recruiters = userRepository.findByStatusAndRole(Status.ACTIVE,Role.RECRUITER);

        List<RecruiterDto> recruiterDtos = new ArrayList<>();
        for (User u : recruiters){
            RecruiterDto recruiterDto = new RecruiterDto();
            recruiterDto.setId(u.getId());
            recruiterDto.setFirstname(u.getFirstName());
            recruiterDto.setLastname(u.getLastName());
            recruiterDto.setDepartment(u.getPosition().getDepartment().getName());
            recruiterDto.setPosition(u.getPosition().getName());
            recruiterDto.setEmail(u.getEmail());
            recruiterDto.setPhoneNumber(u.getPhoneNumber());
            recruiterDtos.add(recruiterDto);
        }
        return recruiterDtos;
    }





    public Test createTest(TestDTO testDTO) {

        if (testRepository.existsByNameAndPosition_Id(testDTO.getName(), testDTO.getPositionId()))
            throw new ResourceNotFoundException("Такой тест для этой позиции уже существует!");
        Test test = new Test();
        test.setStatus(Status.ACTIVE);
        test.setName(testDTO.getName());
        test.setNumberOfDisplayedQuestions(testDTO.getNumberOfDisplayedQuestions());
        test.setTimeLimit(testDTO.getTimeLimit());
        test.setPosition(positionRepository.findById(testDTO.getPositionId()).orElseThrow(
                () -> new ResourceNotFoundException("Позиция не найден!")
        ));
        testRepository.save(test);
        return testRepository.save(test);

    }

    public IdDto createTestQuestion(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setQuestion(questionDTO.getQuestion());
        question.setStatus(Status.ACTIVE);
        question.setScore(questionDTO.getScore());
        question.setTest(testRepository.findById(questionDTO.getTestId()).orElseThrow(
                () -> new ResourceNotFoundException("Тест не найден!")
        ));
        question.setTypeQuestion(questionDTO.getTypeQuestion());
        long idTest = setAnswersToQuestion(questionDTO.getAnswers(), questionRepository.save(question).getId());
        return new IdDto(idTest);
    }

    @Transactional
    long setAnswersToQuestion(List<AnswerDTO> answerDTOList, Long testId) {
        Question question = questionRepository.findById(testId).orElse(null);

        if (question == null)
            throw new ResourceNotFoundException("При добавлении ответов тест не найден! id = " + testId);

        for (AnswerDTO answerDTO : answerDTOList) {
            Answer answer = new Answer();
            answer.setName(answerDTO.getName());
            answer.setCorrectAnswer(answerDTO.isCorrectAnswer());
            answer.setQuestion(question);
            answer.setStatus(Status.ACTIVE);
            answerRepository.save(answer);
        }

        return testId;
    }

    public void addImageToQuestion(Long questionId, MultipartFile photo) {

        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new ResourceNotFoundException("Вопрос не найден! questionId = " + questionId));

        question.setPhoto(imagesService.set(photo, question.getTest().getName()
                .replaceAll("\\s+", "_") + "_" + question.getId()));
        questionRepository.save(question);
    }

    public List<TestsForUsers> findByTestsCurrentUser() {
        User user = userDetailsService.getCurrentUser();
        List<TestsForUsers> testList = new ArrayList<>();

        for (Result result : resultRepository.findAllByUser_Id(user.getId())) {
            TestsForUsers testsForUsers = new TestsForUsers();
            testsForUsers.setId(result.getId());
            testsForUsers.setStatusTest(result.getStatusTest());
            testsForUsers.setTestName(result.getTest().getName());

            Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

            int day = result.getAttemptDate().getDate() - date.getDate();
            int hour = result.getAttemptDate().getHours() - date.getHours();
            int min = result.getAttemptDate().getMinutes() - date.getMinutes();
            if (day < 0)
                day = 0;
            if (hour < 0)
                hour = 0;
            if (min < 0)
                min = 0;

            testsForUsers.setRemainedTime(day + " д : " + hour + " ч : " + min + " м");
            testList.add(testsForUsers);
        }

        return testList;
    }

    @Transactional
    public ResponseMessage startTest(TestsForUsers test) {
        Result result = resultRepository.getById(test.getId());
        result.setStatusTest(StatusTest.IP);
        resultRepository.save(result);
        return new ResponseMessage("Тест открыт!", 200);
    }

    public List<TestQuestion> findAllTestQuestion(Long testId) {
        List<TestQuestion> questionList = new ArrayList<>();
        for (Review r : reviewRepository.findAllByResult_Id(testId)) {
            TestQuestion testQuestion = new TestQuestion();
            testQuestion.setReviewId(r.getId());
            testQuestion.setTypeQuestion(r.getQuestions().getTypeQuestion());
            testQuestion.setQuestionId(r.getQuestions().getId());
            testQuestion.setQuestion(r.getQuestions().getQuestion());
            testQuestion.setImage(r.getQuestions().getPhoto());
            List<AnswerDAO> answerDAOList = new ArrayList<>();
            for (Answer a : answerRepository.findAllByQuestion_Id(r.getQuestions().getId())) {
                AnswerDAO answer = new AnswerDAO();
                answer.setAnswer(a.getName());
                answer.setId(a.getId());
                answer.setSelect(null);
                answerDAOList.add(answer);
            }
            testQuestion.setAnswers(answerDAOList);
            questionList.add(testQuestion);
        }
        return questionList;
    }

    @Transactional
    public ResponseMessage setTestResults(List<TestQuestion> testResult) {


        List<TestQuestion> openAnswerList = new ArrayList<>();

        boolean flag = false;



        for (TestQuestion t : testResult) {
            if (t.getTypeQuestion() == TypeQuestion.OPEN_ANSWER) {
                openAnswerList.add(t);
                testResult.remove(t);
            }
        }

        //обрабатывает результаты Открытых Вопросов.(OpenAnswer)
        calculationResultsOpenAnswer(openAnswerList);

        if(openAnswerList.isEmpty()){
            flag = true;
        }
        //обрабатывает результаты обычных вопросов.(Вопрос варианты ответов)
        calculationResultsChosen(testResult,flag);



        return new ResponseMessage("Тест сохранен!", 200);
    }

    private void calculationResultsChosen(List<TestQuestion> testResult, boolean flag2) {

        for (TestQuestion t : testResult) {
            Review review = reviewRepository.getById(t.getReviewId());

            if (t.getAnswers().isEmpty()) {
                review.setChecking(Checking.FALSE);
                reviewRepository.save(review);
                continue;
            }

            List<Answer> correctAnswerList = answerRepository
                    .findAllByQuestion_IdAndCorrectAnswer(review.getQuestions().getId(), true);
            List<AnswerDAO> selectAnswerList = getSelectAnswers(t.getAnswers());

            if (correctAnswerList.size() != selectAnswerList.size()) {
                review.setChecking(Checking.FALSE);
                reviewRepository.save(review);
            } else {
                boolean flag = true;
                for (AnswerDAO selectAnswer : selectAnswerList) {
                    if (!answerRepository.getById(selectAnswer.getId()).isCorrectAnswer()) {
                        flag = false;
                        review.setChecking(Checking.FALSE);
                        reviewRepository.save(review);
                        break;
                    }
                }
                if (flag) {
                    review.setChecking(Checking.TRUE);
                    reviewRepository.save(review);
                }
            }
        }

        Result result = resultRepository.getById(reviewRepository.findById(testResult.get(1).getReviewId()).get().getResult().getId());
        result.setStatusTest(StatusTest.CHECKED);
        resultRepository.save(result);
    }

    private void calculationResultsOpenAnswer(List<TestQuestion> openAnswerList) {
        for (TestQuestion t : openAnswerList) {
            Review review = reviewRepository.getById(t.getReviewId());
            if (t.getOpenAnswer() == null)
                review.setChecking(Checking.FALSE);
            else
                review.setOpenAnswer(t.getOpenAnswer());
            reviewRepository.save(review);
        }

    }

    private List<AnswerDAO> getSelectAnswers(List<AnswerDAO> answerDAOList) {
        answerDAOList.removeIf(a -> a.getSelect() == null);
        return answerDAOList;
    }

    public List<OpenAnswerCheck> findAllOpenAnswerResult(Long resultId) {

        List<OpenAnswerCheck> answerCheckList = new ArrayList<>();
        List<Review> reviewList = reviewRepository
                .findAllByResult_IdAndQuestions_TypeQuestion(resultId, TypeQuestion.OPEN_ANSWER);

        for (Review r : reviewList) {
            OpenAnswerCheck openAnswerCheck = new OpenAnswerCheck();
            openAnswerCheck.setReviewId(r.getId());
            openAnswerCheck.setAnswer(r.getQuestions().getQuestion());
            openAnswerCheck.setChecked(null);
            openAnswerCheck.setAnswer(r.getOpenAnswer());
        }

        return answerCheckList;
    }

    public ResponseMessage setResultCheckedOpenAnswer(List<OpenAnswerCheck> openAnswerCheckList, Long testId) {

        for (OpenAnswerCheck o : openAnswerCheckList) {
            Review review = reviewRepository.getById(o.getReviewId());
            if (o.getChecked())
                review.setChecking(Checking.TRUE);
            else
                review.setChecking(Checking.FALSE);
            reviewRepository.save(review);
        }
        setGeneralScore(testId);


        return new ResponseMessage("Результаты теста готовы!", 200);
    }

    private void setGeneralScore(Long resultId) {

        Result result = resultRepository.getById(resultId);
        int generalScore = 0;
        List<Review> reviewList = reviewRepository.findAllByResult_Id(resultId);

        for (Review r : reviewList) {
            if (r.getChecking() == Checking.TRUE)
                generalScore += r.getQuestions().getScore();
        }
        result.setGeneralScore(generalScore);
        resultRepository.save(result);
    }

}