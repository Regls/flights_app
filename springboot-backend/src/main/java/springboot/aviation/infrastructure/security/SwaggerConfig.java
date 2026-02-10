package springboot.aviation.infrastructure.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Flight Management API")
                .version("1.0")
                .description("""
                    API for managing flights, airlines, airports, clients and bookings.
                    
                    **Authorization Roles:**
                    - **USER**: Read-only access (GET endpoints)
                    - **ADMIN**: Full access (all endpoints)
                    """)
            )
            .components(new Components()
                .addSecuritySchemes("keycloak",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                        .description("Keycloak OAuth2 Authentication")
                        .flows(new OAuthFlows()
                            .password(new OAuthFlow()
                                .tokenUrl("http://localhost:8081/realms/flights_app/protocol/openid-connect/token")
                            )
                        )
                )
            )
            .security(List.of(new SecurityRequirement().addList("keycloak")));
    }
}
