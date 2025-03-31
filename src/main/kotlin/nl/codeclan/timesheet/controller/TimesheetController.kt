package nl.codeclan.timesheet.controller

import nl.codeclan.timesheet.entities.Timesheet
import nl.codeclan.timesheet.entities.TimesheetDay
import nl.codeclan.timesheet.model.TimesheetDto
import nl.codeclan.timesheet.repository.LocationRepository
import nl.codeclan.timesheet.repository.TimesheetRepository
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
    private val timesheetRepository: TimesheetRepository,
    private val locationRepository: LocationRepository,
) {
    @GetMapping("", "/{monthYear}")
    fun generate(@PathVariable(required = false) @DateTimeFormat(pattern = "M-yyyy") monthYear: Optional<YearMonth>): TimesheetDto {
        val month = monthYear.orElseGet(this::determineMonth)
        return month.let(timesheetService::generate)
    }

    @PostMapping
    fun timesheet(@RequestBody dto: TimesheetDto) {
        val employee = employeeService.getEmployee()
        val timesheet = timesheetRepository.findByEmployeeAndYearMonth(employee, dto.month)
            ?: Timesheet(
                yearMonth = dto.month,
                employee = employee,
                days = List(dto.month.lengthOfMonth()) { TimesheetDay(date = dto.month.atDay(it + 1)) }
            )
        timesheet.days.forEachIndexed { idx, day ->
            val d = dto.days[idx]
            day.type = d.type
            day.location = d.location?.let { locationRepository.findById(it).get() }
        }
        timesheetRepository.save(timesheet)
    }

    private fun determineMonth(): YearMonth {
        var now = LocalDate.now()
        if (now.dayOfMonth < 15) {
            now = now.minusMonths(1)
        }
        return YearMonth.from(now)
    }
}
