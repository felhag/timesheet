package nl.codeclan.timesheet.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(private val clientRegistrationRepository: ClientRegistrationRepository,
                     @Value("\${codeclan.base-url}") private val url: String) {

    @Bean
    fun oauth2SecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .authorizeHttpRequests { it.requestMatchers("/actuator/**", "/user").permitAll().anyRequest().authenticated() }
            .oauth2Login {
                it.loginPage("$url/api/oauth2/authorization/google").authorizationEndpoint { endpoint ->
                    endpoint.authorizationRequestResolver(
                        DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, DEFAULT_AUTHORIZATION_REQUEST_BASE_URI)
                    )
                }
            }
            .logout { it.logoutSuccessUrl(url) }
            .oauth2Client(withDefaults())

        return http.build()
    }
}
