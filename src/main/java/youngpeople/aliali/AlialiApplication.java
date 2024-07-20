package youngpeople.aliali;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AlialiApplication {
	public static void main(String[] args) {
		SpringApplication.run(AlialiApplication.class, args);
	}
}