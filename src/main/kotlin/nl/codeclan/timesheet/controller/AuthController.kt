package nl.codeclan.timesheet.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController {
    @GetMapping("/auth")
    fun auth(@AuthenticationPrincipal oAuth2User: OAuth2User, @RequestParam(value = "continue", required = false) cont: String?): ResponseEntity<String> {
        if (cont != null) {
            val headers = LinkedMultiValueMap<String, String>()
            headers.add("Location", "http://localhost:3000")
            return ResponseEntity(headers, HttpStatus.FOUND)
        } else {
            return ResponseEntity.noContent().build()
        }
    }
}
