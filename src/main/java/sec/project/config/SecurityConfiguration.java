package sec.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /*
    VULNERABILITY: SECURITY MISCONFIGURATION
    The application has practically no security whatsoever, which
    allows for CSRF attacks, brute-forcing and, even exposes event attendee
    personal information as their addresses are not encoded.
    */
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().permitAll();
        
        /* Maybe "the developer" thought that disabling csrf() in fact protects
        the application from CSRF attacks? To be fair, the Spring method
        naming system here is not super intuitive. */
        http.csrf().disable();
    }
}
