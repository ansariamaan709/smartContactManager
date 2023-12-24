package com.smartContactManager.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
public class SecurityConfiguration {
	

	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuth = new DaoAuthenticationProvider();
		daoAuth.setUserDetailsService(this.getUserDetailsService());
		daoAuth.setPasswordEncoder(passwordEncoder());
		
		return daoAuth;
	}
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider()); // Register your custom AuthenticationProvider
       
    }
	
	
    @Bean
    protected DefaultSecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests((authz) -> {
			try {
				authz
						.requestMatchers("/admin/**").hasAnyRole("ADMIN").requestMatchers("/user/**").hasRole("USER")
						.requestMatchers("/trackingFields/**").hasRole("USER")
						.requestMatchers("/**").permitAll()
						.anyRequest().authenticated()
						.and().formLogin()
						.loginPage("/signin").defaultSuccessUrl("/user/index")
						.and().csrf().disable();
						

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    
        )
        .httpBasic();
    return http.build();
}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
   
}
