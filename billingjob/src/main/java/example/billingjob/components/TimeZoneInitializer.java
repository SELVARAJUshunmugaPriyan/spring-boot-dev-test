package example.billingjob.components;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TimeZoneInitializer {
    
    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void setTimeZone() {
        log.info("{}.setTimeZone() :: Setting TimeZone - {}", this.getClass().getName(), timeZone);
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }
}
