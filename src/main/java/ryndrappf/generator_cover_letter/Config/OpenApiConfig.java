package ryndrappf.generator_cover_letter.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI  openAPI(){
        Info info = new Info().description("Backend For Orbit Application").title("Orbit Application").version("V1");
        return new OpenAPI().info(info);
    }
}
