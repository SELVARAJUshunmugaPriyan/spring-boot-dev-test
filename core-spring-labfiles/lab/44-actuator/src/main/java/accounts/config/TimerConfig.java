package accounts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class TimerConfig {

	@Bean
	TimedAspect timedAspect(MeterRegistry registery) {
		return new TimedAspect(registery);
	}
	
}
