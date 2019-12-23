package com.vt.admin;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author vate
 * @date 2019/12/18 23:03
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class AdminServerApp {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AdminServerApp.class);
        springApplication.run(args);
    }

    @Profile("dev")
    @Configuration()
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {

        private final String adminContextPath;

        public SecurityPermitAllConfig(AdminServerProperties adminServerProperties) {
            this.adminContextPath = adminServerProperties.getContextPath();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll().and().csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).ignoringRequestMatchers(
                    new AntPathRequestMatcher(this.adminContextPath + "/instances", HttpMethod.POST.toString()),
                    new AntPathRequestMatcher(this.adminContextPath + "/instances/*",
                            HttpMethod.DELETE.toString()),
                    new AntPathRequestMatcher(this.adminContextPath + "/actuator/**"));
        }
    }

    @Profile("pro")
    @Configuration()
    public static class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

        private final String adminContextPath;

        public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
            this.adminContextPath = adminServerProperties.getContextPath();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
            successHandler.setTargetUrlParameter("redirectTo");
            successHandler.setDefaultTargetUrl(this.adminContextPath + "/");

            http.authorizeRequests()
                    .antMatchers(this.adminContextPath + "/assets/**").permitAll()
                    .antMatchers(this.adminContextPath + "/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin().loginPage(this.adminContextPath + "/login").successHandler(successHandler).and()
                    .logout().logoutUrl(this.adminContextPath + "/logout").and()
                    .httpBasic().and()
                    .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringRequestMatchers(
                            new AntPathRequestMatcher(this.adminContextPath + "/instances", HttpMethod.POST.toString()),
                            new AntPathRequestMatcher(this.adminContextPath + "/instances/*", HttpMethod.DELETE.toString()),
                            new AntPathRequestMatcher(this.adminContextPath + "/actuator/**")
                    );
            // @formatter:on
        }

    }

}
