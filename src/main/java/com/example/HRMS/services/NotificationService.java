package com.example.HRMS.services;

import com.example.HRMS.DTO.NotificationDTO;
import com.example.HRMS.entity.Notification;
import com.example.HRMS.entity.User;
import com.example.HRMS.enums.Status;
import com.example.HRMS.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;



    public void newNotification(User user, String title, String text) {
        Notification notification = new Notification();

        notification.setText(text);
        notification.setTitle(title);
        notification.setUser(user);
        notification.setDate(new Date());
        notificationRepository.save(notification);
    }

    public List<NotificationDTO> findAllNotificationsUser() {

        List<Notification> notificationList = notificationRepository.findAllByUser_Id(
                userDetailsService.getCurrentUser().getId());

        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification n : notificationList) {
            NotificationDTO notificationDTO = new NotificationDTO();

            notificationDTO.setId(n.getId());
            notificationDTO.setDate(notificationDTO.getDate().toString());
            notificationDTO.setTitle(n.getTitle());
            notificationDTO.setText(n.getText());

            notificationDTOList.add(notificationDTO);
        }

        return notificationDTOList;
    }

    public void delete(Long notificationId){
        notificationRepository.deleteById(notificationId);
    }

    public void deleteAllNotificationsUser(){
        notificationRepository.deleteAllByUser_Id(userDetailsService.getCurrentUser().getId());
    }
}

