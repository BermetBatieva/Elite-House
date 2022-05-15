package com.example.HRMS.services;

import com.example.HRMS.DTO.DepAndPosDto;
import com.example.HRMS.DTO.DepartmentDto;
import com.example.HRMS.DTO.InformationAboutCandidates;
import com.example.HRMS.DTO.ResponseMessage;
import com.example.HRMS.entity.Department;
import com.example.HRMS.entity.Position;
import com.example.HRMS.entity.User;
import com.example.HRMS.enums.Role;
import com.example.HRMS.enums.Status;
import com.example.HRMS.exception.ResourceNotFoundException;
import com.example.HRMS.repository.DepartmentRepository;
import com.example.HRMS.repository.PositionRepository;
import com.example.HRMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Department> findAll() {
        List<Department> list = departmentRepository.findAll();

        for (Department d : list) {

            List<Position> positionList = positionRepository.findAllByDepartment_IdAndStatus(d.getId(), Status.ACTIVE);
            d.setQuantityPosition(positionList.size());
            d.setQuantityEmployee(userRepository.findAllByStatusAndRoleAndPosition_Department_Id(Status.ACTIVE,
                    Role.EMPLOYEE, d.getId()).size());
        }
        return list;
    }


    public ResponseMessage create(DepartmentDto departmentDto) {
        if (departmentRepository.existsByNameAndStatus(departmentDto.getName(), Status.ACTIVE))
            return new ResponseMessage("Департамент уже существует!", 400);
        Department department = new Department();
        department.setStatus(Status.ACTIVE);
        department.setName(departmentDto.getName());
        departmentRepository.save(department);
        return new ResponseMessage("Успешно добавлена!", 200);
    }

    public ResponseMessage update(Long id, DepartmentDto departmentDto) {
        Department department = departmentRepository.findByIdAndStatus(id, Status.ACTIVE).orElseThrow(
                () -> new ResourceNotFoundException("Департамент не найден!")
        );
        department.setName(departmentDto.getName());
        departmentRepository.save(department);
        return new ResponseMessage("Успешно обновлена!", 200);
    }

    public ResponseMessage delete(Long id) {
        Department department = departmentRepository.findByIdAndStatus(id, Status.ACTIVE).orElseThrow(
                () -> new ResourceNotFoundException("Департамент не найден!"));
        department.setStatus(Status.REMOVED);
        departmentRepository.save(department);
        for (Position position : positionRepository.findAllByDepartment_IdAndStatus(id, Status.ACTIVE)) {
            position.setStatus(Status.REMOVED);
            positionRepository.save(position);
        }

        return new ResponseMessage("Успешно удалена!", 200);
    }

    public List<Department> getAllActive() {
        List<Department> list = departmentRepository.findByStatus(Status.ACTIVE);
        return list;
    }

    public List<Department> getAllRemoved() {
        List<Department> list = departmentRepository.findByStatus(Status.REMOVED);
        return list;
    }

    public ResponseMessage setActive(Long id) {
        Optional<Department> department = departmentRepository.findByIdAndStatus(id, Status.REMOVED);
        if (department.isEmpty()) {
            return new ResponseMessage("department id is null!", 400);
        }
        department.get().setStatus(Status.ACTIVE);
        departmentRepository.save(department.get());
        return new ResponseMessage("successfully activated!", 200);
    }
}
