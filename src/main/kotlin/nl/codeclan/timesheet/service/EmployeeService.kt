package nl.codeclan.timesheet.service

import nl.codeclan.timesheet.entities.Employee
import nl.codeclan.timesheet.entities.Location
import nl.codeclan.timesheet.repository.EmployeeRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class EmployeeService(private val repo: EmployeeRepository) {
    fun getEmail(): String {
        val auth = SecurityContextHolder.getContext().authentication.principal as OAuth2User
        return auth.attributes["email"] as String
    }

    fun getEmployee(): Employee = repo.findByEmail(getEmail()) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Employee ${getEmail()} not found")

    fun findOrCreate(name: String, email: String): Employee = repo.findByEmail(email) ?: repo.save(Employee(null, name, email))

    fun setHome(employee: Employee, location: Location): Employee {
        employee.home = location
        return repo.save(employee)
    }
}
