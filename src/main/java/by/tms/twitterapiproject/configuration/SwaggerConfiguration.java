package by.tms.twitterapiproject.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    /**
     * Создает и настраивает бин {@link OpenAPI} со спецификацией API, метаданными и схемой безопасности JWT.
     *
     * @return настроенный экземпляр OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("TWITTER-API-PROJECT")
                        .description("REST API социальной платформы на Spring Boot")
                        .version("1.0.0")
                        .contact(new Contact().name("Почта для связи").email("support@c41-onl.by")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Введите JWT Access токен (без префикса Bearer) для доступа к защищенным эндпоинтам")));
    }
}
