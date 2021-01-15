package me.june.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .antMatchers("/design", "/orders").hasRole("USER")
                .antMatchers("/", "/**").permitAll()
            .and()
                .formLogin()
                .loginPage("/login")
    //            .loginProcessingUrl("/authenticate") // 미설정시 /login
                .defaultSuccessUrl("/design", true) // true 옵션을 줄 경우 로그인 성공시 /design 페이지로 무조건 이동한다.
    //            .usernameParameter("user") // 미설정시 username
    //            .passwordParameter("pwd") // 미설정시 password
            .and()
                .logout()
                .logoutSuccessUrl("/")
            .and()
                .csrf()
        ;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//        .withUser("user1")
//            .password("{noop}password1")
//            .authorities("ROLE_USER")
//        .and()
//        .withUser("user2")
//            .password("{noop}password2")
//            .authorities("ROLE_USER");

        /*
           JDBC 기반 사용자 스토어
           기본 사용자 조회 쿼리 = select username,password,enabled from users where username = ?
           기본 권한 조회 쿼리 = select username,authority from authorities where username = ?
           기본 권한 그룹 조회 쿼리 = select g.id, g.group_name, a.authority from authorities g, group_members gm, group_authorities ga where gm.username = ? and g.id = ga.group_id and g.id = gm.group_id
           각 조회쿼리를 변경할 수 있다.
           auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("select username,password,enabled from users where username = ?")
            .authoritiesByUsernameQuery("select username,authority from authorities where username = ?")
             .passwordEncoder(new NoEncodingPasswordEncoder());
         */

        /*
            LDAP 기반 사용자 스토어
            LDAP 의 기본 쿼리는 모두 비어 있어서 루트부터 검색을 시작한다.

        // 아래 설정을 할 경우 루트부터 검색하지 않는다.
        auth.ldapAuthentication()
                .userSearchBase("ou=people") // 사용자를 찾기위한 기준점 쿼리 제공
                .userSearchFilter("(uid={0}}")
                .groupSearchBase("ou=groups") //그룹을 찾기 위한 기준점 쿼리 지정
                .groupSearchFilter("member={0}")
                .passwordCompare() // 패스워드 비교를 LDAP 에서 처리한다.
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPasscode") // 패스워드 속성명을 지정한다. 지정하지 않을 경우 userPassword
                .and()
//                .contextSource().url("ldap://tacocloud.com:389/dc=tacocloud,dc=com") // LDAP 인증 설정을 별도로 하지 않은 경우 localhost:33389 로 접속을 시도한다.
                .contextSource().root("dc=tacocloud,dc=com") // 내장 LDAP 서버를 사용할 경우 root() 를 사용해 지정할 수 있다.
                .ldif("classpath:users.ldif") // LDIF 파일을 찾을수 있도록 경로 지정
        ;
         */
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(encoder())
        ;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
