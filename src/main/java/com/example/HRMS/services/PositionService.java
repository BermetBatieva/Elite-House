package com.example.HRMS.services;

import com.example.HRMS.DTO.PositionDto;
import com.example.HRMS.DTO.ResponseMessage;
import com.example.HRMS.entity.Department;
import com.example.HRMS.entity.Position;
import com.example.HRMS.entity.Vacancy;
import com.example.HRMS.enums.Status;
import com.example.HRMS.exception.ResourceNotFoundException;
import com.example.HRMS.repository.DepartmentRepository;
import com.example.HRMS.repository.PositionRepository;
import com.example.HRMS.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private VacancyRepository vacancyRepository;

    public List<Position> findByDepartment(Long idDepartment) {
        return positionRepository.findAllByDepartment_IdAndStatus(idDepartment, Status.ACTIVE);
    }

    public ResponseMessage create(PositionDto positionDto) {
        if (positionRepository.existsByNameAndStatusAndDepartment_Id(positionDto.getName(),
                Status.ACTIVE, positionDto.getDepartmentId()))
            return new ResponseMessage("Такая позиция уже существует!", 409);

        Position position = new Position();
        Department department = departmentRepository.findByIdAndStatus(positionDto.getDepartmentId(), Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Департамент не найден!"));
        position.setStatus(Status.ACTIVE);
        position.setDepartment(department);
        position.setName(positionDto.getName());
        positionRepository.save(position);
        return new ResponseMessage("Позиция успешно добавлена!", 201);
    }

    public ResponseMessage update(Long id, PositionDto positionDto) {
        Position position = positionRepository.findByIdAndStatus(id, Status.ACTIVE).orElseThrow(
                () -> new ResourceNotFoundException("Позиция не найдена!"));
        Department department = departmentRepository.
                findByIdAndStatus(positionDto.getDepartmentId(), Status.ACTIVE).orElseThrow(
                        () -> new ResourceNotFoundException("Департамент не найден!"));
        position.setDepartment(department);
        position.setName(positionDto.getName());
        position.setStatus(Status.ACTIVE);
        positionRepository.save(position);

        return new ResponseMessage("Успешно обновлено!", 200);
    }

    public ResponseMessage delete(Long id) {
        Position position = positionRepository.findByIdAndStatus(id, Status.ACTIVE).orElseThrow(
                () -> new ResourceNotFoundException("Позиция не найдена!"));
        position.setStatus(Status.REMOVED);
        positionRepository.save(position);
        for (Vacancy v : vacancyRepository.findAllByPosition_Id(id)) {
            v.setStatus(Status.REMOVED);
            vacancyRepository.save(v);
        }
        return new ResponseMessage("Успешно удалена!", 200);
    }

    public List<Position> findByVacancy() {
        return vacancyRepository.findAllGroupByPosition(Status.ACTIVE);
    }
}
