package nl.codeclan.timesheet.repository

import nl.codeclan.timesheet.entities.TimesheetDay
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface DayRepository: CrudRepository<TimesheetDay, LocalDate>
