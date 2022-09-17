package components;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import util.TimeConstants;

import java.time.OffsetDateTime;

@Component
public class TimeMgr implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;
    private OffsetDateTime time = TimeConstants.START_TIME;

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(fixedRate = 100)
    public void publishTimeChange() {
        if (time.isAfter(TimeConstants.END_TIME)) {
            applicationEventPublisher.publishEvent(null);
            return;
        }
        applicationEventPublisher.publishEvent(new TimeEvent(this, time));
        time = time.plusSeconds(24 * 60 * 60 / 10);
    }
}


