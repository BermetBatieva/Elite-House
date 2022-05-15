package com.example.HRMS.controller;

import com.example.HRMS.DTO.*;
import com.example.HRMS.entity.Department;
import com.example.HRMS.entity.News;
import com.example.HRMS.entity.Test;
import com.example.HRMS.services.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserDetailsServiceImpl userServiceImpl;

    @Autowired
    private TestService testService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsImageService newsImageService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private MainPageService mainPageService;

    @Autowired
    private AnalyticsService analyticsService;

    @ApiOperation(value = "авторизация админа,jwt token")
    @PostMapping(value = "/auth")
    public AuthenticationResponse auth(@RequestBody AuthDTO auth) {
        return userServiceImpl.token(auth);
    }

    //TODO исправить API, и еще раз протестить
    @ApiOperation(value = "добавление сотрудников")
    @PostMapping("/employee-create")
    public ResponseEntity<ResponseMessage> create(@RequestBody EmployeeRegistrDto newEmployee) throws Exception {
        return new ResponseEntity<>(userServiceImpl.addNewEmployee(newEmployee), HttpStatus.OK);
    }

/*
    @ApiOperation(value = "подтверждение почты")
    @GetMapping("/invitation/{key}")
    public ResponseEntity<KeyCheckResponse> confirmation(@PathVariable String key){
        return new ResponseEntity<>(userServiceImpl.confirmationCheck(key), HttpStatus.OK);
    }

    @ApiOperation(value = "ввод пароля кандидата после подтверждения почты")
    @PostMapping("/password/{key}")
    public MessageResponse setNewPassword(@PathVariable String key, @RequestBody PasswordDto dto){
        return userServiceImpl.setPassword(key, dto);
    }
*/

    @ApiOperation(value = "получение персональных данных админа")
    @GetMapping("/get-personal-data")
    public ResponseEntity<AdminPersonalData> getPersonalData() {
        return new ResponseEntity<>(userServiceImpl.getDataForAdmin(), HttpStatus.OK);
    }

    @ApiOperation(value = "таблица 'Информация о сотрудниках'")
    @GetMapping("/employees-all")
    public List<InfoAboutEmployees> getInfoAboutEmployees() {
        return testService.getInfoAboutEmployees();
    }

    @ApiOperation("фильтрация по департаменту, по позиции для сотрудников")
    @PostMapping("/employees-by-dep-and-pos")
    public List<InfoAboutEmployees> getFilterTableEmployees(@RequestBody DepAndPosDto dto) {
        return testService.getInfoAboutEmployeesByDepAndPos(dto);
    }

    @ApiOperation(value = "таблица 'Информация о кандидатах'")
    @GetMapping("/candidates-all")
    public List<InformationAboutCandidates> getInfoAboutCandidates() {
        return testService.getInfoAboutCandidates();
    }

    @ApiOperation("фильтрация по департаменту, по позиции для кандидатов")
    @PostMapping("/candidates-by-dep-and-pos")
    public List<InformationAboutCandidates> getFilterTableCandidates(@RequestBody DepAndPosDto dto) {
        return testService.getByDepAndPosCandidate(dto);
    }

    @ApiOperation(value = "фотки для новостей")
    @PostMapping("/news-create")
    public News addNewNews(@RequestBody NewsDto news) {
        return newsService.addNews(news);
    }

    @ApiOperation(value = "фотки для новостей")
    @PutMapping("/news-add-image/{newsId}")
    public ResponseMessage addPhotoForNews(@PathVariable Long newsId,
                                           @RequestParam("image") MultipartFile image) {
        return newsImageService.createNewsImages(newsId, image);
    }

    @ApiOperation(value = "Редактирование новостей")
    @PutMapping("/news-edit/{newsId}")
    public ResponseMessage editNewsTitleAndText(@PathVariable Long newsId,
                                                @RequestBody NewsDto news) {
        return newsService.updateNewsById(newsId, news);
    }

    @DeleteMapping("/news-delete/{newsId}")
    public ResponseMessage deleteNews(@PathVariable Long newsId) {
        return newsService.deleteNewsById(newsId);
    }

    @ApiOperation(value = "удаление документов по id")
    @DeleteMapping("/document-delete/{documentId}")
    public ResponseMessage deleteDocuments(@PathVariable Long documentId) {
        return documentService.deleteById(documentId);
    }

    @ApiOperation(value = "Создание тех. тестов")
    @PostMapping("/test-create")
    public Test createTest(@RequestBody TestDTO test) {
        return testService.createTest(test);
    }

    @ApiOperation(value = "Таблица тесты")
    @GetMapping("/test-find-all")
    public List<TestList> findAllTest() {
        return testService.findAll();
    }

    @ApiOperation(value = "Вопросы теста")
    @GetMapping("/test/question-all/{testId}")
    public List<QuestionList> findAllQuestionsByTest(@PathVariable Long testId) {
        return questionService.findAllByTest(testId);
    }

    @ApiOperation(value = "Создание вопросов на тех. тест")
    @PostMapping("/test/question-create")
    public IdDto createQuestion(@RequestBody QuestionDTO question) {
        return testService.createTestQuestion(question);
    }

    @ApiOperation(value = "Добавление картинок к вопросам тех. тест")
    @PostMapping("/test/question-add-image/{questionId}")
    public void addImageToQuestion(@PathVariable Long questionId, @RequestParam("image") MultipartFile image) {
        testService.addImageToQuestion(questionId, image);
    }

    //TODO ??????????????
    /*@GetMapping("/document/{documentId}")
    public ResponseEntity<Object> getFile(@PathVariable Long documentId) throws FileNotFoundException {
        return documentService.getById(documentId);
    }*/

    @ApiOperation("Все департаменты")
    @GetMapping("/department-all")
    public List<Department> findAll() {
        return departmentService.findAll();
    }

    @ApiOperation("создание департамента")
    @PostMapping("/department-create")
    public ResponseMessage createDepartment(@RequestBody DepartmentDto department) {
        return departmentService.create(department);
    }

    @ApiOperation(value = "редактирование департамента")
    @PutMapping("/department-edit/{departmentId}")
    public ResponseMessage editDepartment(@PathVariable Long departmentId,
                                          @RequestBody DepartmentDto department) {
        return departmentService.update(departmentId, department);
    }

    @ApiOperation(value = "удаление департамента")
    @DeleteMapping("/department-delete/{departmentId}")
    public ResponseMessage deleteDepartment(@PathVariable Long departmentId) {
        return departmentService.delete(departmentId);
    }

    @ApiOperation("создание позиции")
    @PostMapping("/position-create")
    public ResponseMessage createPosition(@RequestBody PositionDto position) {
        return positionService.create(position);
    }

    @ApiOperation(value = "редактирование должности(позиции)")
    @PutMapping("/position-edit/{positionId}")
    public ResponseMessage editPosition(@PathVariable Long positionId,
                                        @RequestBody PositionDto position) {
        return positionService.update(positionId, position);
    }

    @ApiOperation(value = "удаление должности(позиции)")
    @DeleteMapping("/position-delete/{positionId}")
    public ResponseMessage deletePosition(@PathVariable Long positionId) {
        return positionService.delete(positionId);
    }

    @ApiOperation(value = "Создание вакансий")
    @PostMapping("/vacancy-create")
    public ResponseMessage createVacancy(@RequestBody VacancyDto vacancy) {
        return vacancyService.create(vacancy);
    }

    @ApiOperation(value = "Редактирование  вакансий")
    @PutMapping("/vacancy-edit/{idVacancy}")
    public ResponseMessage updateVacancy(@RequestBody VacancyDto vacancy, @PathVariable Long idVacancy) {
        return vacancyService.update(idVacancy, vacancy);
    }

    @ApiOperation(value = "Удаление  вакансий")
    @DeleteMapping("/vacancy-delete/{idVacancy}")
    public ResponseMessage dropVacancy(@PathVariable Long idVacancy) {
        return vacancyService.delete(idVacancy);
    }


    @ApiOperation(value = "получение всех удаленных департаментов")
    @GetMapping("/department/all-removed")
    public List<Department> getAllRemovedDepartments(){
        return departmentService.getAllRemoved();
    }

    @ApiOperation(value = "восстановление департамента")
    @PutMapping("/department-recovery/{id}")
    public ResponseMessage recoveryDepartment(@PathVariable Long id){
        return departmentService.setActive(id);
    }

    @ApiOperation(value = "таблица кандидатов")
    @GetMapping("/candidate-table")
    public List<InformationAboutCandidates> findAllCandidateTable(){
        return testService.getInfoAboutCandidates();
    }

    @ApiOperation(value = "психологические результаты по типу для каждого юзера по его Id")
    @GetMapping("/psychological-result-by-type/{userId}")
    public List<PsychologicalListDto> getPsychResults(@PathVariable Long userId){
        return testService.getPsychologicalResult(userId);
    }

    @ApiOperation(value = "таблица 'Информация о рекрутерах' ")
    @GetMapping("/recruiter-all-info")
    public List<RecruiterDto> getAllActiveRecruiters() {
        return testService.getAllActiveRecruiters();
    }

    @ApiOperation(value = "Главное окно. Топ кандидаты текущей позиции")
    @GetMapping("/main-page/{positionId}")
    public List<TopCandidates> getTopCandidates(@PathVariable Long positionId) {
        return mainPageService.topCandidates(positionId);
    }

    @ApiOperation(value = "Главное окно. Диаграмма и окно справа диаграммы")
    @GetMapping("/main-page/diagram/{positionId}")
    public DiagramAndResult mainPageDiagrams(@PathVariable Long positionId){
        return mainPageService.diagram(positionId);
    }

    @ApiOperation(value = "Главное окно. Тесты")
    @GetMapping("/main-page/tests/{positionId}")
    private List<TestMainPage> mainPageTests(@PathVariable Long positionId){
        return mainPageService.tests(positionId);
    }

//    @ApiOperation(value = "Аналитика по Сотрудникам. Диаграмма")
//    @GetMapping("/analytics/diagrams-employee")
//    private List<AnalyticsDto> analytics(@RequestBody PositionAndTest positionAndTest){
//        return analyticsService.diagram(positionAndTest);
//    }

    @GetMapping("/getFile/{idDocument}")
    private ResponseEntity<Object> getFile(@PathVariable Long idDocument) throws FileNotFoundException {
        return documentService.getById(idDocument);
    }

    @ApiOperation(value = "подробная информация о юзере")
    @GetMapping("/info-by-user-id/{userId}")
    public UserDetailsDto getInfoByUserId(@PathVariable Long userId){
        return userServiceImpl.getDetailsAboutUser(userId);
    }

}
