package youngpeople.aliali.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
@Slf4j
public class CorsWebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://ariari.vercel.app/", "http://localhost:8080/",
                                "http://localhost:3000/", "http://localhost:5173/",
                                "https://ariaricloud-yeoniii20s-projects.vercel.app/")
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}