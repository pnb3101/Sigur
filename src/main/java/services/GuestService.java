package services;

import entities.Employee;
import entities.Guest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import repository.GuestRepository;
import util.TimeConstants;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;

    public Guest getRandomGuest() {
        Guest guest = new Guest();
        guest.setId(getUniqueId());
        guest.setCard(getUniqueCard());
        return guest;
    }

    private Integer getUniqueId() {
        int id;
        do {
            id = new Random().nextInt();
        } while (guestRepository.existsById(id));
        return id;
    }

    private byte[] getUniqueCard() {
        byte[] card = new byte[16];
        do {
            new Random().nextBytes(card);
        } while (guestRepository.existsByCard(card));
        return card;
    }

    public void saveGuestForEmployee(OffsetDateTime now, Guest guest, Employee employee, OffsetDateTime visitTime) {
        guest.setEmployee(employee);
        guest.setVisitTime(visitTime);
        guestRepository.save(guest);
        logVisit(guest, employee, visitTime, Duration.between(now, visitTime).abs().toDays());
    }

    private void logVisit(Guest guest, Employee employee, OffsetDateTime visitTime, Long daysUntilVisit) {
        String VISIT_LOG_TEMPLATE = "Гостю {} назначена встреча сотруднику {}. Отдел: {}. Дата: {}. До встречи осталось: {}";
        log.info(VISIT_LOG_TEMPLATE, guest.getId(), employee.getId(), employee.getDepartment().getName(), formatTime(visitTime), daysUntilVisit);
    }

    public void checkVisitsFromThisTime(OffsetDateTime time) {
        for (Guest guest : guestRepository.findAllByVisitTimeBetween(time, TimeConstants.END_TIME)) {
            Employee employee = guest.getEmployee();

            if (employee.getFiredTime() != null && employee.getFiredTime().isBefore(guest.getVisitTime())) {
                logCancelledVisit(guest, employee, guest.getVisitTime());
            }
        }
    }

    private void logCancelledVisit(Guest guest, Employee employee, OffsetDateTime visitTime) {
        String VISIT_CANCELLED_LOG_TEMPLATE = "Встреча гостя {} с сотрудником {} отменена. Отдел: {}. Дата встречи: {}, дата увольнения сотрудника: {}";
        log.info(VISIT_CANCELLED_LOG_TEMPLATE, guest.getId(), employee.getId(), employee.getDepartment().getName(), formatTime(visitTime), formatTime(employee.getFiredTime()));
    }

    private String formatTime(OffsetDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
