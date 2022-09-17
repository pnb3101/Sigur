package sigur.services;

import sigur.entities.Employee;
import sigur.entities.Guest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sigur.repository.EmployeeRepository;
import sigur.repository.GuestRepository;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PassService {
    private final EmployeeRepository employeeRepository;
    private final GuestRepository guestRepository;

    public byte[] getRandomNotExistingCard() {
        byte[] card = new byte[16];
        do {
            new Random().nextBytes(card);
        } while (employeeRepository.existsByCard(card) || guestRepository.existsByCard(card));
        return card;
    }

    public byte[] getRandomExistingCard() {
        List<byte[]> employeeCards = employeeRepository.findAll().stream().map(Employee::getCard).collect(Collectors.toList());
        List<byte[]> guestCards = guestRepository.findAll().stream().map(Guest::getCard).collect(Collectors.toList());

        List<byte[]> allCards = new ArrayList<>();
        allCards.addAll(employeeCards);
        allCards.addAll(guestCards);

        return allCards.get(new Random().nextInt(allCards.size()));
    }

    public void validateCard(OffsetDateTime now, byte[] card) {
        Optional<Employee> employeeOpt = employeeRepository.findByCard(card);

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();

            if (employee.getFiredTime() != null && now.isAfter(employee.getFiredTime())) {
                logBlockEmployee(now, employee);
            } else {
                logPassEmployee(now, employee);
            }

            return;
        }

        Optional<Guest> guestOpt = guestRepository.findByCard(card);

        if (guestOpt.isPresent()) {
            Guest guest = guestOpt.get();

            if (canGuestVisit(now, guest)) {
                logPassGuest(now, guest);
            } else {
                logBlockGuest(now, guest);
            }
        }

        logUnknownCard(card);
    }

    private boolean canGuestVisit(OffsetDateTime time, Guest guest) {
        if (guest.getEmployee().getFiredTime() != null && guest.getEmployee().getFiredTime().isBefore(time)) {
            return false;
        }
        if (Duration.between(guest.getVisitTime(), time).abs().toDays() >= 1) {
            return false;
        }

        return true;
    }

    private void logBlockEmployee(OffsetDateTime time, Employee employee) {
        String BLOCK_EMPLOYEE_LOG_TEMPLATE = "{}. Доступ запрещён {}. Отдел: {}. Карта: {}";
        log.info(BLOCK_EMPLOYEE_LOG_TEMPLATE, formatTime(time), employee.getId(), employee.getDepartment().getName(), toHex(employee.getCard()));
    }

    private void logPassEmployee(OffsetDateTime time, Employee employee) {
        String PASS_EMPLOYEE_LOG_TEMPLATE = "{}. Предоставлен доступ сотруднику {}. Отдел: {}. Карта: {}";
        log.info(PASS_EMPLOYEE_LOG_TEMPLATE, formatTime(time), employee.getId(), employee.getDepartment().getName(), toHex(employee.getCard()));
    }

    private void logBlockGuest(OffsetDateTime time, Guest guest) {
        String BLOCK_GUEST_LOG_TEMPLATE = "{}. Доступ запрещён гостю {}. Карта: {}";
        log.info(BLOCK_GUEST_LOG_TEMPLATE, formatTime(time), guest.getId(), toHex(guest.getCard()));
    }

    private void logPassGuest(OffsetDateTime time, Guest guest) {
        String PASS_GUEST_LOG_TEMPLATE = "{}. Предоставлен доступ гостю {}. Пришёл к {} из отдела: {}. Карта: {}";
        log.info(PASS_GUEST_LOG_TEMPLATE, formatTime(time), guest.getId(), guest.getEmployee().getId(), guest.getEmployee().getDepartment().getName(), toHex(guest.getCard()));
    }

    private void logUnknownCard(byte[] card) {
        String UNKNOWN_CARD_LOG_TEMPLATE = "Поднесена неизвестная карта {}";
        log.info(UNKNOWN_CARD_LOG_TEMPLATE, toHex(card));
    }

    private String formatTime(OffsetDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String toHex(byte[] card) {
        StringBuilder hex = new StringBuilder();

        for (byte i : card) {
            hex.append(String.format("%02X", i));
        }

        return hex.toString();
    }
}
