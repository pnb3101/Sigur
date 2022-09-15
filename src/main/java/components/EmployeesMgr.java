package components;

import entities.Employee;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import services.DepartmentService;
import services.EmployeeService;
import util.TimeConstants;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class EmployeesMgr implements ApplicationListener<TimeEvent> {
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    private final GuestMgr guestMgr;

    private int hiringNumber = 0;

    @Override
    public void onApplicationEvent(@NonNull TimeEvent event) {
        if (event.isStartOfTheNewDay()) {
            if (isFiringTime()) {
                employeeService.fire(
                        event.getTime(),
                        getRandomNumberOfEmployeesToFire());
            } else {
                Employee employee = employeeService.hire(
                        event.getTime(),
                        getRandomHireTime(event.getTime()),
                        employeeService.getRandomEmployee(),
                        departmentService.getRandomDepartment());
                guestMgr.saveGuestFor(event.getTime(), employee);
            }
        }
    }

    private boolean isFiringTime() {
        return ++hiringNumber % 5 == 0;
    }

    private int getRandomNumberOfEmployeesToFire() {
        return new Random().nextInt(4 - 1) + 1;
    }

    private OffsetDateTime getRandomHireTime(OffsetDateTime now) {
        Random random = new Random();
        long differenceInSeconds = now.until(TimeConstants.END_TIME, ChronoUnit.SECONDS);
        long seconds = (long) (random.nextDouble() * differenceInSeconds);
        return now.plusSeconds(seconds);
    }
}
