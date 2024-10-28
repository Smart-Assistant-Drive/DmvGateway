package org.example.com.smartassistantdrive.dmvproxy.interfaceAdaptersLayer.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig {

	@Bean
	@Throws(Exception::class)
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.authorizeHttpRequests(
				Customizer { authorize->
					authorize
						.anyRequest().authenticated()
				}
			)
			.httpBasic(Customizer.withDefaults())
			.formLogin(Customizer.withDefaults())
			.csrf { it.disable() }

		return http.build()
	}

	@Bean
	fun userDetailsService(): UserDetailsService {
		val userDetails: UserDetails = User.builder()
			.username("user")
			.password("{bcrypt}\$2a\$12\$gYpJWJFTxbTebG22Lyl/6eUSpM2aBpROJm20AT.qWhshP3oRGZj8W")
			.roles("ADMIN")
			.build()

		return InMemoryUserDetailsManager(userDetails)
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder()
	}
}
