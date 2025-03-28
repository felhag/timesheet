package nl.codeclan.timesheet.controller

import nl.codeclan.timesheet.model.UserDto
import nl.codeclan.timesheet.service.EmployeeService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class AuthController(
    private val employeeService: EmployeeService,
    @Value("\${codeclan.base-url}") private val url: String) {

    @GetMapping("/user")
    fun user(@AuthenticationPrincipal oAuth2User: OAuth2User?): ResponseEntity<UserDto> {
        if (oAuth2User == null) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        } else {
            val attrs = oAuth2User.attributes
            val name = attrs["name"].toString()
            val email = attrs["email"].toString()
            val create = employeeService.findOrCreate(name, email)
            val user = UserDto(name, email, create.home != null)
            return ResponseEntity.ok(user)
        }
    }

    @GetMapping("/login")
    fun auth(@AuthenticationPrincipal oAuth2User: OAuth2User, @RequestParam(value = "continue", required = false) cont: String?): ResponseEntity<UserDto> {
        if (cont != null) {
            val headers = LinkedMultiValueMap<String, String>()
            headers.add("Location", url)
            return ResponseEntity(headers, HttpStatus.FOUND)
        } else {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }
}
