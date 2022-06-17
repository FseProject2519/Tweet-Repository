package com.tweetapp.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.tweetapp.authorization.service.DetailsService;
import com.tweetapp.authorization.service.RegisterService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	DetailsService detailsService;

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests().antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
				.permitAll().anyRequest().authenticated().and().exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.logout(logout -> logout.logoutUrl("/api/v1.0/authorization/tweets/logout")
						.logoutSuccessUrl("/api/v1.0/authorization/tweets/userlogin").invalidateHttpSession(true));

	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
				
		web.ignoring().antMatchers("/api/v1.0/authorization/tweets/uservalidate");
		web.ignoring().antMatchers("/api/v1.0/authorization/tweets/userlogin");
		web.ignoring().antMatchers("/api/v1.0/authorization/tweets/register");
		web.ignoring().antMatchers("/api/v1.0/authorization/tweets/logout");
		web.ignoring().antMatchers("/api/v1.0/authorization/tweets/**");
		web.ignoring().antMatchers("/v3/api-docs/**");
		web.ignoring().antMatchers("/swagger-resouces/**");
		web.ignoring().antMatchers("/swagger-ui/**");
		web.ignoring().antMatchers("/configuration/ui");
		web.ignoring().antMatchers("/configuration/security");
		web.ignoring().antMatchers("/webjars/**");
	}

	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		super.configure(auth);
		auth.userDetailsService(detailsService);

	}

}