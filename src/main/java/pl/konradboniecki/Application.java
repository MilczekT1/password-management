package pl.konradboniecki;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import pl.konradboniecki.chassis.ChassisApplication;

@EnableJpaRepositories("pl.konradboniecki.budget.passwordmanagement.service")
@ChassisApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
