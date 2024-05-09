package edu.iastate.cssm.springtwotokens.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RequestMatcherDelegatingAuthenticationManagerResolver;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private JwtDecoder jwtDecoder;
    private OpaqueTokenIntrospector opaqueTokenIntrospector;

    public SecurityConfig(JwtDecoder jwtDecoder, OpaqueTokenIntrospector opaqueTokenIntrospector) {
        this.jwtDecoder = jwtDecoder;
        this.opaqueTokenIntrospector = opaqueTokenIntrospector;
    }

    @Bean
    SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                //.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                //.oauth2ResourceServer(OAuth2ResourceServerConfigurer::opaqueToken)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationManagerResolver(this.tokenAuthenticationManagerResolver()))
                .build();
    }

    AuthenticationManagerResolver<HttpServletRequest> tokenAuthenticationManagerResolver() {
        List<String> readMethod = Arrays.asList("HEAD", "GET", "OPTIONS");
        RequestMatcher requestMatcher = request -> readMethod.contains(request.getMethod());

        RequestMatcherDelegatingAuthenticationManagerResolver resolver = RequestMatcherDelegatingAuthenticationManagerResolver
                .builder()
                .add(requestMatcher, jwt())
                .build();

        resolver.setDefaultAuthenticationManager(opaque());
        return resolver;
    }

    AuthenticationManager jwt() {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoder);
        provider.setJwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter());
        return provider::authenticate;
    }

    AuthenticationManager opaque() {
        return new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector)::authenticate;
    }

}
