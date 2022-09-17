package sigur.services;

import sigur.entities.Department;
import sigur.entities.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sigur.repository.EmployeeRepository;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public Employee getRandomEmployee() {
        Employee employee = new Employee();
        employee.setId(getUniqueId());
        employee.setCard(getUniqueCard());
        return employee;
    }

    private Integer getUniqueId() {
        int id;
        do {
            id = new Random().nextInt();
        } while (employeeRepository.existsById(id));
        return id;
    }

    private byte[] getUniqueCard() {
        byte[] card = new byte[16];
        do {
            new Random().nextBytes(card);
        } while (employeeRepository.existsByCard(card));
        return card;
    }

    public Employee hire(OffsetDateTime now, OffsetDateTime hireTime, Employee employee, Department department) {
        employee.setHireTime(hireTime);
        employee.setDepartment(department);
        employee = employeeRepository.save(employee);
        logHiring(now, employee, department);
        return employee;
    }

    public void fire(OffsetDateTime time, Integer numberOfEmployees) {
        for (int i = 0; i < numberOfEmployees; i++) {
            List<Employee> workingEmployees = employeeRepository.findAllByFiredTime(null);

            if (workingEmployees.size() > 0) {
                Employee employee = workingEmployees.get(new Random().nextInt(workingEmployees.size()));
                if (time.isAfter(employee.getHireTime())) {
                    employee.setFiredTime(time);
                    employeeRepository.save(employee);
                    logFiring(time, employee, employee.getDepartment(), Duration.between(employee.getHireTime(), employee.getFiredTime()).abs().toDays());
                }
            }
        }
    }

    private void logHiring(OffsetDateTime time, Employee employee, Department department) {
        String HIRING_LOG_TEMPLATE = "{}. Сотрудник {} нанят {}. Отдел: {}";
        log.info(HIRING_LOG_TEMPLATE, formatTime(time), employee.getId(), formatTime(employee.getHireTime()), department.getName());
    }

    private void logFiring(OffsetDateTime time, Employee employee, Department department, Long daysWorked) {
        String FIRING_LOG_TEMPLATE = "{}. Сотрудник {} уволен {}. Отдел: {}. Проработал: {}";
        log.info(FIRING_LOG_TEMPLATE, formatTime(time), employee.getId(), formatTime(employee.getFiredTime()), department.getName(), daysWorked);
    }

    private String formatTime(OffsetDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
