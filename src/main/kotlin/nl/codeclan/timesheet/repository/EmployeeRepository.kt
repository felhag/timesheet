package nl.codeclan.timesheet.repository

import nl.codeclan.timesheet.entities.Employee
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EmployeeRepository: CrudRepository<Employee, Long> {
    fun findByEmail(email: String): Employee?
}
