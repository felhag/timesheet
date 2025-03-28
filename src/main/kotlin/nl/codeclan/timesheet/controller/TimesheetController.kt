package nl.codeclan.timesheet.controller

import nl.codeclan.timesheet.entities.Employee
import nl.codeclan.timesheet.entities.TimesheetDay
import nl.codeclan.timesheet.model.DayType
import nl.codeclan.timesheet.model.Timesheet
import nl.codeclan.timesheet.repository.DayRepository
import nl.codeclan.timesheet.repository.LocationRepository
import nl.codeclan.timesheet.repository.findByMonth
import nl.codeclan.timesheet.service.EmployeeService
import nl.codeclan.timesheet.service.TimesheetService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

@RestController
@RequestMapping("/timesheet")
class TimesheetController(
    private val timesheetService: TimesheetService,
    private val employeeService: EmployeeService,
    private val dayRepository: DayRepository,
    private val locationRepository: LocationRepository,
) {
    @GetMapping("", "/{monthYear}")
    fun generate(@PathVariable(required = false) @DateTimeFormat(pattern = "M-yyyy") monthYear: Optional<YearMonth>): Timesheet {
        val month = monthYear.orElseGet(this::determineMonth)
        return month.let(timesheetService::generate)
    }

    @PostMapping
    fun timesheet(@RequestBody timesheet: Timesheet) {
        val employee = employeeService.getEmployee()
        val existing = dayRepository.findByMonth(employee, timesheet.month)
        dayRepository.saveAll(timesheet.days.mapIndexed { idx, day ->
            day(existing, timesheet.getDay(idx), day.type, day.location, employee)
        })
    }

    private fun day(existing: Map<LocalDate, TimesheetDay>, date: LocalDate, type: DayType, location: Long?, employee: Employee): TimesheetDay {
        val day = existing[date] ?: TimesheetDay()
        day.type = type
        day.date = date
        day.location = location?.let { locationRepository.findById(it).get() }
        day.employee = employee
        return day
    }

    private fun determineMonth(): YearMonth {
        var now = LocalDate.now()
        if (now.dayOfMonth < 15) {
            now = now.minusMonths(1)
        }
        return YearMonth.from(now)
    }
}
