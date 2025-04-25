package nl.codeclan.timesheet.service

import de.focus_shift.jollyday.core.HolidayManager
import de.focus_shift.jollyday.core.HolidayType
import de.focus_shift.jollyday.core.ManagerParameters
import nl.codeclan.timesheet.model.Day
import nl.codeclan.timesheet.model.DayType
import nl.codeclan.timesheet.model.TimesheetDto
import nl.codeclan.timesheet.repository.LocationRepository
import nl.codeclan.timesheet.repository.TimesheetRepository
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import java.util.*

private val CLANDAY = LocalDate.of(2024, Month.JANUARY, 5)
private val HOLIDAY_MANAGER = HolidayManager.getInstance(ManagerParameters.create(Locale.of("nl")))

@Service
class TimesheetService(
    val timesheetRepository: TimesheetRepository,
    val locRepository: LocationRepository,
    val employeeService: EmployeeService,
) {
    fun generate(month: YearMonth): TimesheetDto {
        val types = determineTypes(month)
        return TimesheetDto(month, types)
    }

    private fun determineTypes(month: YearMonth): ArrayList<Day> {
        val employee = employeeService.getEmployee()
        val persisted = timesheetRepository.findByEmployeeAndYearMonth(employee, month)
        val byDate = persisted?.days?.associateBy { it.date!! } ?: mapOf()
        val locations = locRepository.findAllByEmployeeEmail(employee.email).flatMap { d -> d.defaultDays.map { it to d } }.toMap()
        var from = month.atDay(1)
        val until = month.atEndOfMonth()
        val days = ArrayList<Day>()
        val vacation = emptySet<LocalDate>()
        while (from <= until) {
            days.add(byDate[from]
                ?.let { Day(it.type!!, it.location?.id) }
                ?: Day(determineType(from, vacation), locations[from.dayOfWeek]?.id))
            from = from.plusDays(1)
        }
        return days
    }

    private fun determineType(day: LocalDate, vacation: Set<LocalDate>): DayType {
        return if (day.dayOfWeek == DayOfWeek.SATURDAY || day.dayOfWeek == DayOfWeek.SUNDAY) {
            DayType.WEEKEND
        } else if (HOLIDAY_MANAGER.isHoliday(day, HolidayType.PUBLIC_HOLIDAY)) {
            DayType.HOLIDAY
        } else if (vacation.contains(day)) {
            DayType.LEAVE
        } else if (isClanday(day)) {
            DayType.CLANDAY
        } else {
            DayType.WORK
        }
    }

    private fun isClanday(day: LocalDate): Boolean {
        return day.dayOfWeek == DayOfWeek.FRIDAY && ChronoUnit.WEEKS.between(CLANDAY, day) % 2 == 0L
    }
}
