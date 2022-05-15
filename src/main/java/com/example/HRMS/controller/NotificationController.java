package com.example.HRMS.controller;

import com.example.HRMS.DTO.NotificationDTO;
import com.example.HRMS.services.NotificationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @ApiOperation(value = "Получение всех уведомлений пользователя")
    @GetMapping("/get-all")
    public List<NotificationDTO> findAllByUser() {
        return notificationService.findAllNotificationsUser();
    }

    @ApiOperation(value = "Удаление одного уведомления пользователя")
    @DeleteMapping("/delete/{notificationId}")
    public void deleteOne(@PathVariable Long notificationId) {
        notificationService.delete(notificationId);
    }

    @ApiOperation(value = "Удаление всех уведомлений пользователя")
    @DeleteMapping("/delete-all")
    public void deleteAll(){
        notificationService.deleteAllNotificationsUser();
    }
}
