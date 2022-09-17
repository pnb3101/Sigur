package components;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.OffsetDateTime;

@Getter
class TimeEvent extends ApplicationEvent {
    private final OffsetDateTime time;

    public TimeEvent(Object source, OffsetDateTime timePayload) {
        super(source);
        this.time = timePayload;
    }

    public boolean isStartOfTheNewDay() {
        return time.getHour() == 0 && time.getMinute() == 0;
    }
}
