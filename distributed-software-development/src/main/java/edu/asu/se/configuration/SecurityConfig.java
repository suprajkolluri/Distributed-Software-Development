package edu.asu.se.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import edu.asu.se.service.LoginService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LoginService loginService;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(loginService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
				.antMatchers("/profile/**").access("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
				.antMatchers("/statistics/**").access("hasAnyRole('ROLE_ADMIN','ROLE_USER')").and().formLogin()
				.loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/profile").usernameParameter("username")
				.passwordParameter("password").and().logout().logoutSuccessUrl("/login?logout").and()
				.exceptionHandling().accessDeniedPage("/403").and().csrf();

		/*
		 * Extra Options .formLogin() .loginPage( "/login" )
		 * .loginProcessingUrl( "/login.do" ) .defaultSuccessUrl( "/" )
		 * .failureUrl( "/login?err=1" ) .usernameParameter( "username" )
		 * .passwordParameter( "password" ) .and()
		 * 
		 * // This is where the logout page and process is configured. The
		 * logout-url is the URL to send // the user to in order to logout, the
		 * logout-success-url is where they are taken if the logout // is
		 * successful, and the delete-cookies and invalidate-session make sure
		 * that we clean up after logout .logout() .logoutRequestMatcher( new
		 * AntPathRequestMatcher( "/logout" ) ) .logoutSuccessUrl(
		 * "/login?out=1" ) .deleteCookies( "JSESSIONID" )
		 * .invalidateHttpSession( true ) .and()
		 * 
		 * // The session management is used to ensure the user only has one
		 * session. This isn't // compulsory but can add some extra security to
		 * your application. .sessionManagement() .invalidSessionUrl(
		 * "/login?time=1" ) .maximumSessions( 1 );
		 */ }

}
