package com.example.HRMS.services;

import com.example.HRMS.DTO.ResponseMessage;
import com.example.HRMS.DTO.VacancyDto;
import com.example.HRMS.entity.Vacancy;
import com.example.HRMS.enums.Status;
import com.example.HRMS.repository.PositionRepository;
import com.example.HRMS.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacancyService {

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private PositionRepository positionRepository;

    public ResponseMessage create(VacancyDto newVacancy) {
        Vacancy vacancy = new Vacancy();
        vacancy.setStatus(Status.ACTIVE);
        vacancy.setTitle(newVacancy.getTitle());
        vacancy.setText(newVacancy.getText());
        vacancy.setWage(newVacancy.getWage());
        vacancy.setPosition(positionRepository.getById(newVacancy.getPositionId()));
        vacancyRepository.save(vacancy);
        return new ResponseMessage("Новая вакансия создана!", 201);
    }

    public ResponseMessage update(Long id, VacancyDto newVacancy) {
        Vacancy vacancy = vacancyRepository.findById(id).orElse(null);
        if (vacancy == null)
            return new ResponseMessage("Вакансия не найдена! id = " + id, 404);

        vacancy.setTitle(newVacancy.getTitle());
        vacancy.setText(newVacancy.getText());
        vacancy.setWage(newVacancy.getWage());
        vacancy.setPosition(positionRepository.getById(newVacancy.getPositionId()));
        vacancyRepository.save(vacancy);
        return new ResponseMessage("Данные вакансии обновлены!", 200);
    }

    public ResponseMessage delete (Long id){
        Vacancy vacancy = vacancyRepository.findById(id).orElse(null);
        if (vacancy == null)
            return new ResponseMessage("Вакансия не найдена! id = " + id, 404);
        vacancy.setStatus(Status.REMOVED);
        vacancyRepository.save(vacancy);
        return new ResponseMessage("Вакансия удалена!", 200);
    }

    public List<Vacancy> getAll(){
        return vacancyRepository.findAllByStatus(Status.ACTIVE);
    }




}
