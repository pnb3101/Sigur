package components;

import entities.Employee;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import services.GuestService;
import util.TimeConstants;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class GuestMgr implements ApplicationListener<TimeEvent> {
    private final GuestService guestService;

    @Override
    public void onApplicationEvent(@NonNull TimeEvent event) {
        // 10 раз в день проверяет встречи
        guestService.checkVisitsFromThisTime(event.getTime());
    }

    public void saveGuestFor(OffsetDateTime now, Employee employee) {
        if (new Random().nextInt(2) == 1) {
            guestService.saveGuestForEmployee(
                    now,
                    guestService.getRandomGuest(),
                    employee,
                    getRandomVisitTime(employee));
        }
    }

    private OffsetDateTime getRandomVisitTime(Employee employee) {
        Random random = new Random();
        long differenceInSeconds1 = employee.getHireTime().until(employee.getHireTime().plusMonths(6), ChronoUnit.SECONDS);
        long differenceInSeconds2 = employee.getHireTime().until(TimeConstants.END_TIME, ChronoUnit.SECONDS);
        long minDifferenceFromTwo = Math.min(differenceInSeconds1, differenceInSeconds2);
        long seconds = (long) (random.nextDouble() * minDifferenceFromTwo);
        return employee.getHireTime().plusSeconds(seconds);
    }
}
