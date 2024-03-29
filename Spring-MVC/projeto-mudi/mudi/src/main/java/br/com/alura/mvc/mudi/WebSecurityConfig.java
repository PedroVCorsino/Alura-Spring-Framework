package br.com.alura.mvc.mudi;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/home/**")
					.permitAll()
			    .anyRequest().authenticated()
			.and()
                .formLogin(form -> form
					.loginPage("/login")
					.defaultSuccessUrl("/usuario/pedido", true)
					.permitAll()
				)
				.logout(logout -> {
					logout.logoutUrl("/logout")
						.logoutSuccessUrl("/home");
				})
				.csrf().disable(); // Pesquisar sobre essa porcaria
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		// UserDetails user =
		// 	 User.builder()
		// 		.username("judith")
		// 		.password(encoder.encode("judith"))
		// 		.roles("ADM")
		// 		.build();

		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.passwordEncoder(encoder)
			// .withUser(user)
		;
	}

	// Configuração de autenticação do Spring Security
	// @Bean
	// @Override
	// public UserDetailsService userDetailsService() {
	// 	UserDetails user =
	// 		 User.withDefaultPasswordEncoder()
	// 			.username("pedro")
	// 			.password("pedro")
	// 			.roles("ADM")
	// 			.build();

	// 	return new InMemoryUserDetailsManager(user);
	// }
}