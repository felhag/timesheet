package nl.codeclan.timesheet.repository

import nl.codeclan.timesheet.entities.Employee
import nl.codeclan.timesheet.entities.Timesheet
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.YearMonth

@Repository
interface TimesheetRepository : CrudRepository<Timesheet, Long> {
    fun findByEmployeeAndYearMonth(employee: Employee, yearMonth: YearMonth): Timesheet?
}
