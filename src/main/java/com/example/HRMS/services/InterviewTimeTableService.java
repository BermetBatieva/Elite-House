package com.example.HRMS.services;

import com.example.HRMS.DTO.*;
import com.example.HRMS.entity.InterviewTimeTable;
import com.example.HRMS.entity.User;
import com.example.HRMS.enums.ReservationStatus;
import com.example.HRMS.enums.Status;
import com.example.HRMS.exception.ResourceNotFoundException;
import com.example.HRMS.repository.InterviewTimeTableRepository;
import com.example.HRMS.repository.PositionRepository;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class InterviewTimeTableService {

    @Autowired
    private InterviewTimeTableRepository interviewTimeTableRepository;

    @Autowired
    private UserDetailsServiceImpl userServiceImpl;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private MailService mailService;

    public List<SlotResponseMessage> createSlots(List<SlotDto> slotDtos){
        List<SlotResponseMessage> ans = new ArrayList<>();
        for (SlotDto slotDto : slotDtos) {
            ans.add(createSlot(slotDto));
        }
        return ans;
    }

    @Transactional
    public SlotResponseMessage createSlot(SlotDto slotDto){
        if(interviewTimeTableRepository.existsByInterviewer_IdAndStartDateTime(userServiceImpl.getCurrentUser().getId(), slotDto.getStartDateAndTime()))
            return new SlotResponseMessage(null, "This time is already used for another slot by you.");
        InterviewTimeTable slot = new InterviewTimeTable();

        slot.setStartDateTime(slotDto.getStartDateAndTime());
        slot.setDuration(slotDto.getDuration());
        slot.setMeetingLink(slotDto.getMeetingLink());
        slot.setDescription(slotDto.getDescription());
        slot.setInterviewer(userServiceImpl.getCurrentUser());
        slot.setPosition(positionRepository.findById(slotDto.getPositionId()).orElse(null));
        slot.setReservationStatus(ReservationStatus.FREE);
        slot.setMeetingLink(slotDto.getMeetingLink());
        slot.setStatus(Status.ACTIVE);
        interviewTimeTableRepository.save(slot);
        return new SlotResponseMessage(null, "SUCCESS!");
    }

    public Set<DateAndTimes> getAllSlotsByMonth(String yyyy_mm) throws ParseException {
        inactivateOldSlots();
        //это все слоты за месяц
        List<InterviewTimeTable> list = interviewTimeTableRepository.findByPosition_IdAndStartDateTimeStartsWithAndReservationStatus(userServiceImpl.getCurrentUser().getPosition().getId(), yyyy_mm, ReservationStatus.FREE);
        //это все дни за этот месяц
        Set<DateAndTimes> dateAndTimes = new HashSet<>(); //yyyy_mm_dd; List<TimeAndHrs>

        //перебираем все слоты за 1 месяц
        for (InterviewTimeTable i : list) {
            //текущий день (у него есть времена внутри)
            DateAndTimes dateAndTimesCurr = new DateAndTimes();
            String currDay = i.getStartDateTime().substring(0, 10);
            dateAndTimesCurr.setYyyy_mm_dd(currDay); //сетим день
            //теперь надо сетить времена
            //это времена за текущий день
            List<TimeAndHrs> timeAndHrs = new ArrayList<>();
            //все слоты за текущий день
            List<InterviewTimeTable> ss = interviewTimeTableRepository.findByStartDateTimeStartsWithAndPosition_IdAndReservationStatus(currDay, userServiceImpl.getCurrentUser().getPosition().getId(), ReservationStatus.FREE);
            //проходимся по слотам
            for(InterviewTimeTable y : ss){ //y это один слот
                TimeAndHrs time = new TimeAndHrs();
                time.setSlotId(y.getId());
                time.setHh_mm(y.getStartDateTime().substring(11).trim());
                time.setDuration(y.getDuration());
                time.setMeetingLink(y.getMeetingLink());
                List<Long> hrIds = new ArrayList<>();
                List<InterviewTimeTable> detailedTimeSlots = interviewTimeTableRepository.findByStartDateTimeAndPosition_IdAndReservationStatus(y.getStartDateTime(), userServiceImpl.getCurrentUser().getPosition().getId(), ReservationStatus.FREE);
                for(InterviewTimeTable d : detailedTimeSlots){ //slot by yyyy-mm-dd hh-mm
                    hrIds.add(d.getInterviewer().getId());
                }
                time.setInterviewerIds(hrIds);
                timeAndHrs.add(time);
            }

            dateAndTimesCurr.setTimeAndHrs(new HashSet<>(timeAndHrs));
            dateAndTimes.add(dateAndTimesCurr);
        }
        return dateAndTimes;
    }

    public Set<DayInterviewer> getAllSlotsForInterviewer(String yyyy_mm) throws ParseException {
        inactivateOldSlots();
        Long currUserId = userServiceImpl.getCurrentUser().getId();
        List<InterviewTimeTable> slots = interviewTimeTableRepository.findByInterviewer_IdAndStartDateTimeStartsWithAndStatus(currUserId, yyyy_mm, Status.ACTIVE);
        Set<DayInterviewer> interviewDays = new HashSet<>();

        for (InterviewTimeTable i : slots) {
            //текущий день (у него есть времена внутри)
            DayInterviewer dayDto = new DayInterviewer();
            String currDay = i.getStartDateTime().substring(0, 10);
            dayDto.setYyyy_mm_dd(currDay); //сетим день
            //теперь надо сетить времена
            //это времена за текущий день
            List<TimeInterviewer> timesDto = new ArrayList<>();
            //все слоты за текущий день
            List<InterviewTimeTable> ss = interviewTimeTableRepository.findByInterviewer_IdAndStartDateTimeStartsWithAndStatus(currUserId, currDay, Status.ACTIVE);
            //проходимся по слотам
            for(InterviewTimeTable y : ss){ //y это один слот
                TimeInterviewer time = new TimeInterviewer();
                time.setHh_mm(y.getStartDateTime().substring(11).trim());
                time.setDuration(y.getDuration());
                time.setReservationStatus(y.getReservationStatus());
                if(time.getReservationStatus() == ReservationStatus.RESERVED){
                    User candidate = y.getCandidate();
                    time.setCandidateId(candidate.getId());
                    time.setCandidateFullName(candidate.getFirstName() + candidate.getLastName());
                }
                time.setMeetingLink(y.getMeetingLink());
                timesDto.add(time);
            }
            dayDto.setTimes(new HashSet<>(timesDto));
            interviewDays.add(dayDto);
        }
        return interviewDays;
    }

    public void inactivateOldSlots() throws ParseException {
        List<InterviewTimeTable> activeSlots = interviewTimeTableRepository.findAllByStatus(Status.ACTIVE);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for(InterviewTimeTable slot : activeSlots){
            String strDate = slot.getStartDateTime();
            Date date = formatter.parse(strDate);

            LocalDateTime now = LocalDateTime.now();
            Date noww = java.util.Date
                    .from(now.atZone(ZoneId.systemDefault())
                            .toInstant());
            if(noww.after(date)) {
                slot.setStatus(Status.OLD);
                interviewTimeTableRepository.save(slot);
            }
        }
    }

    public Set<MyUpcomingInterviews> getAllUpcomingInterviewSlotsForCandidate(){
        Long candidateId = userServiceImpl.getCurrentUser().getId();
        List<InterviewTimeTable> slots = interviewTimeTableRepository.findByCandidate_IdAndReservationStatusAndStatus(candidateId, ReservationStatus.RESERVED, Status.ACTIVE);
        Set<MyUpcomingInterviews> ans = new HashSet<>();
        for (InterviewTimeTable i : slots){
            MyUpcomingInterviews dto = new MyUpcomingInterviews();
            dto.setId(i.getId());
            dto.setInterviewerId(i.getInterviewer().getId());
            dto.setInterviewerFullName(i.getInterviewer().getFirstName() + " " + i.getInterviewer().getLastName());
            dto.setDate(i.getStartDateTime().substring(0, 10).trim());
            dto.setTime(i.getStartDateTime().substring(11));
            dto.setMeetingLink(i.getMeetingLink());
            ans.add(dto);
        }
        return ans;
    }


    public void reserveSlot(IdDto idDto){
        InterviewTimeTable slot = interviewTimeTableRepository.findById(idDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Слот не найден! slotId = " + idDto.getId())
        );
        if(slot.getReservationStatus() == ReservationStatus.RESERVED)
            //return that it's reserved
        slot.setReservationStatus(ReservationStatus.RESERVED);
        slot.setCandidate(userServiceImpl.getCurrentUser());
        interviewTimeTableRepository.save(slot);
        String message = "Когда: " + slot.getStartDateTime() +"\n" +
                "Где: " + slot.getMeetingLink() + "\n" +
                "Кто: " + slot.getInterviewer().getEmail() + "\n" +
                slot.getCandidate().getEmail();
        mailService.send(userServiceImpl.getCurrentUser().getEmail(), "Приглашение: "
                + slot.getCandidate().getFirstName() + " and " + slot.getInterviewer().getFirstName(), message);
    }

    public void cancelSlot(CancelDto cancelDto){
        InterviewTimeTable slot = interviewTimeTableRepository.findById(cancelDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Слот не найден! slot_id = " + cancelDto.getId())
        );
        slot.setReservationStatus(ReservationStatus.FREE);
        User candidate = slot.getCandidate();
        slot.setCandidate(null);
        interviewTimeTableRepository.save(slot);
        //TODO: if reason == "", do not add it to message
        String message = "Встреча " + slot.getStartDateTime() + " отменена кандидатом " + candidate.getFirstName() +
                " " + candidate.getLastName() + " " + candidate.getPatronymic() + ". Причина: " + cancelDto.getReason();
        mailService.send(slot.getInterviewer().getEmail(), "Отмена встречи", message);
    }
}
