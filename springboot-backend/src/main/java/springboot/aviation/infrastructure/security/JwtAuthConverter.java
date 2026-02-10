package springboot.aviation.infrastructure.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthConverter extends JwtAuthenticationConverter {
    
    public JwtAuthConverter() {
        this.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
    }

    private static class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            
            if (realmAccess == null || !realmAccess.containsKey("roles")) {
                return List.of();
            }
            
            Object rolesObj = realmAccess.get("roles");
            if (!(rolesObj instanceof Collection<?>)) {
                return List.of();
            }
            
            return ((Collection<?>) rolesObj).stream()
                .filter(role -> role instanceof String)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        }
    }
}
