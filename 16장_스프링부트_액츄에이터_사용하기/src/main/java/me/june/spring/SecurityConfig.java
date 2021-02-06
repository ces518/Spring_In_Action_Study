package me.june.spring;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .requestMatcher(
                    EndpointRequest.toAnyEndpoint()
                    .excluding("health", "info") // 특정 경로 제외
            ) // /actuator 가 기본 엔드포인트 지만, 만약 base-path 속성이 변경될 경우.. EndpointRequest 클래스를 통해 하드코딩 하지않아도 됨
            .authorizeRequests()
                .anyRequest().hasRole("ADMIN")
            .and()
            .httpBasic();
    }
}
