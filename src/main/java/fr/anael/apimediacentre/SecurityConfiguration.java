package fr.anael.apimediacentre;

import lombok.extern.slf4j.Slf4j;
import org.apereo.portal.soffit.security.SoffitApiAuthenticationManager;
import org.apereo.portal.soffit.security.SoffitApiPreAuthenticatedProcessingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.Filter;
import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${soffit.jwt.signatureKey:Changeme}")
    private String signatureKey;

    @Override
    public void configure(WebSecurity web) {
        final RequestMatcher pathMatcher = new AntPathRequestMatcher("/api/**");
        final RequestMatcher inverseMatcher = new NegatedRequestMatcher(pathMatcher);
        web.ignoring().requestMatchers(inverseMatcher);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.debug("configure signatureKey = {}", this.signatureKey);
        final AbstractPreAuthenticatedProcessingFilter filter =
                new SoffitApiPreAuthenticatedProcessingFilter(this.signatureKey);

        filter.setAuthenticationManager(authenticationManager());

        http
                .addFilter(filter)
                .authorizeRequests()
                    .antMatchers("/health-check").anonymous()
                    .antMatchers("/api/**").authenticated()
                    .anyRequest().permitAll()
                .and() // pour la dev en localhost autorisation du cross domaine
                    .cors()
                    .configurationSource(
                            corsConfigurationSource()
                    )
                .and()
                    .sessionManagement()
                        .sessionFixation()
                            .none();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new SoffitApiAuthenticationManager();
    }

    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }

    @Bean
    public FilterRegistrationBean<Filter> disableSpringBootErrorFilter() {
        /*
         * The ErrorPageFilter (Spring) makes extra calls to HttpServletResponse.flushBuffer(),
         * and this behavior produces many warnings in the portal logs during portlet requests.
         */
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(errorPageFilter());
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        if(log.isWarnEnabled()) log.warn("CORS ABILITATI! CORS est autorisé");

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.asList(
                        "http://localhost:8080",
                        "http://192.168.45.196:8080",
                        "http://192.168.45.156:8080",
                        "https://test-lycee.giprecia.net"
                )
        );
        configuration.setAllowedMethods(
                Arrays.asList(
                        RequestMethod.GET.name(),
                        RequestMethod.POST.name(),
                        RequestMethod.OPTIONS.name(),
                        RequestMethod.DELETE.name(),
                        RequestMethod.PUT.name()
                )
        );

        configuration.setExposedHeaders(
                Arrays.asList(
                        "x-auth-token",
                        "x-requested-with",
                        "x-xsrf-token"
                )
        );
        configuration.setAllowedHeaders(
                Arrays.asList(
                        "content-type",
                        "authorization",
                        "x-com-persist",
                        "X-Auth-Token",
                        "x-auth-token",
                        "x-requested-with",
                        "x-xsrf-token"
                )
        );
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
