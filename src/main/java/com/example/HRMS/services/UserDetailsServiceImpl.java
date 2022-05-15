package com.example.HRMS.services;

import com.example.HRMS.DTO.*;
import com.example.HRMS.config.WebSecurityConfig;
import com.example.HRMS.entity.*;
import com.example.HRMS.enums.*;
import com.example.HRMS.exception.ResourceNotFoundException;
import com.example.HRMS.jwt.JwtUtils;
import com.example.HRMS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder encode;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PsychologicalResultsRepository psychologicalResultsRepository;

    @Autowired
    private GeneralResultRepository generalResultRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден!"); //TODO тут проект крашится, надо исправить
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.getAuthorities());
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public AuthenticationResponse token(AuthDTO auth) {
        if (userServiceImpl.existsByEmail(auth.getEmail())) {
            Role role = userRepository.findByEmail(auth.getEmail()).getRole();
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(auth.getEmail(),
                                auth.getPassword())
                );
            } catch (BadCredentialsException e) {
                return new AuthenticationResponse(null, "Не правильный логин или пароль!", role);
            }
            final UserDetails userDetails = userServiceImpl.loadUserByUsername(auth.getEmail());
            return new AuthenticationResponse(jwtUtils.generateToken(userDetails), "successfully", role);
        } else
            return new AuthenticationResponse(null, "Пользователь не найден!", null);
    }

    @Transactional
    public ResponseMessage addNewEmployee(EmployeeRegistrDto newEmployee) {
        if (!userRepository.existsByEmail(newEmployee.getEmail())) {
            User user = userRepository.getById(newEmployee.getUserId());
            String alterEmail = user.getEmail();
            user.setEmail(newEmployee.getEmail());
            user.setUserPassword(encode.encode(newEmployee.getPassword()));
            user.setRole(Role.EMPLOYEE);
            userRepository.save(user);
            String message = "Здравствуйте, " + user.getFirstName() + "!" + "\n"
                    + "Для входа в систему используйте, пожалуйста," + " вашу почту : " + user.getEmail() + "\n"+
                    " и пароль: " + newEmployee.getPassword();
            mailService.send(alterEmail, "Приглашение", message);
            return new ResponseMessage("успешно зарегистрирован!", 200);
        }
        return new ResponseMessage("email уже существует!", 400);
    }


    public ResponseMessage checkMail(String mail) {
        User user = userRepository.findByEmail(mail);
        if (user != null)
            if (user.getApprovness() == Approvness.APPROVED)
                return new ResponseMessage("Пользователь с такой почтой уже существует!", 200);
            else
                return updateNewUser(user);
        else
            return createNewUser(mail);
    }

    private ResponseMessage createNewUser(String mail) {
        String code = CodeGenerate.generate();
        User newUser = new User();
        newUser.setEmail(mail);
        newUser.setRole(Role.CANDIDATE);
        newUser.setStatus(Status.ACTIVE);
        newUser.setApprovness(Approvness.NOT_APPROVED);
        newUser.setUserPassword(encode.encode(code));
        userRepository.save(newUser);

        String message = "Здравствуйте!  \n\nКод подтверждение почты: " + code;
        return mailService.send(mail, "Подтверждение почты EliteHouse", message);

    }

    private ResponseMessage updateNewUser(User newUser) {
        String code = CodeGenerate.generate();
        newUser.setUserPassword(encode.encode(code));
        userRepository.save(newUser);
        String message = "Здравствуйте!  \n\nКод подтверждение почты: " + code;
        return mailService.send(newUser.getEmail(), "Подтверждение почты EliteHouse", message);
    }

    public ResponseMessage updatePersonalData(CandidateRegistrationDto candidateRegistrationDto) {
        try {
            User user = getCurrentUser();

            user.setPosition(positionRepository.getById(candidateRegistrationDto.getPositionId()));
            user.setFirstName(candidateRegistrationDto.getFirstName());
            user.setLastName(candidateRegistrationDto.getLastName());
            user.setPatronymic(candidateRegistrationDto.getPatronymic());
            user.setUserPassword(encode.encode(candidateRegistrationDto.getPassword()));
            user.setGender(candidateRegistrationDto.getGender());
            user.setApprovness(Approvness.APPROVED);
            user.setPhoneNumber(candidateRegistrationDto.getPhoneNumber());
            user.setApplicationStatus(ApplicationStatus.NOT_SENT);
            //Здесь открывается доступ к тестам
            accessToTests(userRepository.save(user), candidateRegistrationDto.getPositionId());
            return new ResponseMessage("Данные успешно сохранены!", 200);
        } catch (Exception e) {
            return new ResponseMessage("Данные не сохранены! " + e.getMessage(), 400);
        }
    }

    private void accessToTests(User user, Long positionId) {

        StringBuilder testsDate = new StringBuilder("Вам нужно пройти следующие тесты:");
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd ' время' hh:mm:ss");
        int index = 1;
        for (Test test : testRepository.findAllByPosition_IdAndStatus(positionId, Status.ACTIVE)) {
            Result result = new Result();
            result.setUser(userRepository.getById(user.getId()));
            result.setStatusTest(StatusTest.AVAILABLE);
            result.setAttemptDate(Date
                    .from(LocalDateTime.now().plusDays(test.getTimeLimit()).atZone(ZoneId.systemDefault())
                            .toInstant()));
            testsDate.append("\n ").append(index).append(". ")
                    .append(test.getName()).append("   до ")
                    .append(formatForDateNow.format(result.getAttemptDate()));
            result.setTest(test);
            setRandomQuestions(resultRepository.save(result), test.getId());
            index++;
        }
        PsychologicalResults psychologicalResults = new PsychologicalResults();
        psychologicalResults.setStatusTest(StatusTest.AVAILABLE);
        psychologicalResults.setUser(user);
        psychologicalResults.setAttemptDate(Date
                .from(LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault())
                        .toInstant()));
        testsDate.append("\n ").append(index).append(". ")
                .append("Психологический тест").append("   до ")
                .append(formatForDateNow.format(psychologicalResults.getAttemptDate()));
        psychologicalResultsRepository.save(psychologicalResults);
        String message = mailService.send(user.getEmail(), "Открыт доступ к тестам!",
                "Вам доступны тесты от EliteHouse для позиции " + user.getPosition().getName() + ". \n" +
                        testsDate + "\n" +
                        "Вы должны пройти все тесты в течении 3 дней. \n\nЖелаем вам удачи!!! ").getMassage();
        System.out.println(message);
    }

    @Transactional
    void setRandomQuestions(Result result, Long testId) {
        List<Question> questionList = questionRepository.findAllByTest_IdAndStatus(testId, Status.ACTIVE);
        Random rand = new Random();

        int maxScore = 0;

        for (int i = 0; i < result.getTest().getNumberOfDisplayedQuestions(); i++) {
            int randomIndex = rand.nextInt(questionList.size());
            Review review = new Review();
            review.setQuestions(questionList.get(randomIndex));
            review.setResult(result);
            review.setChecking(Checking.UNCHECKED);
            reviewRepository.save(review);
            maxScore += questionList.get(randomIndex).getScore();
            questionList.remove(randomIndex);
            if (questionList.isEmpty())
                break;
        }
        result.setMaxScore(maxScore);
        resultRepository.save(result);
    }

    public ResponseMessage updatePersonalDataNoPosition(CandidateRegistrationDto candidateRegistrationDto) {
        try {
            User user = getCurrentUser();

            user.setFirstName(candidateRegistrationDto.getFirstName());
            user.setLastName(candidateRegistrationDto.getLastName());
            user.setPatronymic(candidateRegistrationDto.getPatronymic());
            user.setUserPassword(encode.encode(candidateRegistrationDto.getPassword()));
            user.setGender(candidateRegistrationDto.getGender());
            user.setApprovness(Approvness.APPROVED);
            user.setPhoneNumber(candidateRegistrationDto.getPhoneNumber());
            userRepository.save(user);
            return new ResponseMessage("Данные успешно сохранены!", 200);
        } catch (Exception e) {
            return new ResponseMessage("Данные не сохранены! \n\n" + e.getMessage(), 400);
        }
    }

    public ResponseEntity<InfoMessage> getUserInfo() {
        if (getCurrentUser() == null)
            return ResponseEntity.ok().body(new InfoMessage(null, "User is not found!"));
        User user = getCurrentUser();
        UserInfoDto dto = new UserInfoDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPatronymic(user.getPatronymic());
        dto.setEmail(user.getEmail());
        dto.setPhotoUrl(user.getPhotoUrl());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setGender(user.getGender());
        dto.setDepartment(user.getPosition().getDepartment());
        dto.setPosition(user.getPosition());
        dto.setResume(documentRepository.findDocumentByDocumentTypeAndUser(DocumentType.RESUME, user));
        dto.setRecommendations(documentRepository.findDocumentByDocumentTypeAndUser(DocumentType.RECOMMENDATION_LETTER, user));
        dto.setCertificates(documentRepository.findDocumentByDocumentTypeAndUser(DocumentType.ADDITIONAL_FILES, user));
        return ResponseEntity.ok().body(new InfoMessage(dto, "Success!"));
    }


    public AdminPersonalData getDataForAdmin() {
        User user = getCurrentUser();
        AdminPersonalData admin = new AdminPersonalData();
        admin.setId(user.getId());
        admin.setEmail(user.getEmail());
        admin.setLastname(user.getLastName());
        admin.setFirstname(user.getFirstName());
        admin.setRole(user.getRole());
        return admin;
    }


    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName());
    }

    public MessageResponse changePassword(ChangePasswordDto dto) {
        User user = getCurrentUser();
        if (!webSecurityConfig.passwordEncoder().matches(dto.getCurrPassword(), user.getPassword()))
            return new MessageResponse("Incorrect password!");

        user.setUserPassword(webSecurityConfig.passwordEncoder().encode(dto.getNewPassword()));
        userRepository.save(user);
        return new MessageResponse("Success!");

    }

    public ResponseMessage changeEmailRequest(EmailDto emailDto) {
        boolean inDb = userRepository.existsByEmail(emailDto.getEmail());
        if (inDb && userRepository.findByEmail(emailDto.getEmail()).getApprovness() == Approvness.APPROVED)
            return new ResponseMessage("This email address is already in use!", 200);

        User user = getCurrentUser();
        String key = UUID.randomUUID().toString();
        ResetPassword item = new ResetPassword(user, key, emailDto.getEmail());
        item.setUser(user);
        resetPasswordRepository.save(item);
        String message = "http://localhost:8080/edit/set-new-email/key/" + key;
        mailService.send(emailDto.getEmail(), "Сброс пароля E-mail", message);
        return new ResponseMessage("Confirmation message is sent to email.", 200);
    }


    public ResponseEntity<AuthenticationResponse> checkNewEmailConfirmationAndAuth(String key, PasswordDto dto) {
        User curr = getCurrentUser();
        if (!resetPasswordRepository.existsByKey(key))
            return ResponseEntity.ok().body(new AuthenticationResponse(null,
                    "Email is not confirmed. Invalid key!", null));

        ResetPassword res = resetPasswordRepository.findByKey(key);

        boolean inDb = userRepository.existsByEmail(res.getNewEmail());
        //if the email is in users table and reg is not completed; and if key(email) is confirmed to set it to an another than del it from users and set it to the curr user
        if (inDb && userRepository.findByEmail(res.getNewEmail()).getApprovness() == Approvness.NOT_APPROVED) {
            if (key.equals(res.getKey()))
                userRepository.delete(userRepository.findByEmail(res.getNewEmail())); //TODO test it if u r free
        }

        if (!curr.getId().equals(res.getUser().getId()))
            return ResponseEntity.ok().body(new AuthenticationResponse(null,
                    "Someone another is trying to hack!", null));

        if (!key.equals(res.getKey()))
            return ResponseEntity.ok().body(new AuthenticationResponse(null,
                    "Invalid key!", null));

        curr.setEmail(res.getNewEmail());
        userRepository.save(curr);
        resetPasswordRepository.delete(res);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(curr.getEmail(),
                        dto.getPassword()));

        final UserDetails userDetails = userServiceImpl.loadUserByUsername(curr.getEmail());

        return ResponseEntity.ok().body(new AuthenticationResponse(jwtUtils.generateToken(userDetails),
                "success", curr.getRole()));
    }

    @Transactional
    public UserDetailsDto getDetailsAboutUser(Long userId){

        User user = userRepository.getById(userId);
        UserDetailsDto details = new UserDetailsDto();

        details.setDepartment(user.getPosition().getDepartment().getName());
        details.setFirstname(user.getFirstName());
        details.setLastname(user.getLastName());
        details.setEmail(user.getEmail());
        details.setPosition(user.getPosition().getName());
        details.setPhoneNumber(user.getPhoneNumber());
        details.setUserId(user.getId());
        details.setUrlPhoto(user.getPhotoUrl());
        PsychologicalResults psychologicalResults = psychologicalResultsRepository.findByUser_IdAndStatusTest(userId,StatusTest.CHECKED);

        List<GeneralResult> list = generalResultRepository.findAllByPsychologicalResults_Id(psychologicalResults.getId());

        List<PsychologicalListDto> listDto = new ArrayList<>();

        for ( GeneralResult g : list){
            PsychologicalListDto psychological = new PsychologicalListDto();
            psychological.setType(g.getPsychologicalType().getName());
            psychological.setResult(g.getGeneralScore());
            listDto.add(psychological);
        }

        details.setListPsychTests(listDto);
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
        details.setListTechTests(testResults);



        Document resume = documentRepository.findDocumentByDocumentTypeAndUser(DocumentType.RESUME, userRepository.getById(userId)).get(0);

        details.setResume(resume.getId());
        List<Document> recommendations = documentRepository.findDocumentByDocumentTypeAndUser(DocumentType.RECOMMENDATION_LETTER, userRepository.getById(userId));
        List<Document> additionalFiles = documentRepository.findDocumentByDocumentTypeAndUser(DocumentType.ADDITIONAL_FILES, userRepository.getById(userId));

        List<Long> recommendation = new ArrayList<>();

        for (Document d : recommendations) {
            recommendation.add(d.getId());
        }
        details.setRecommendations(recommendation);

        List<Long> additionalFile = new ArrayList<>();
        for (Document d : additionalFiles) {
            additionalFile.add(d.getId());
        }
        details.setAdditionalFiles(additionalFile);

        return details;
    }
}
