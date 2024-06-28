package nl.codeclan.timesheet.service

import de.focus_shift.jollyday.core.HolidayManager
import de.focus_shift.jollyday.core.ManagerParameters
import nl.codeclan.timesheet.model.DayType
import nl.codeclan.timesheet.model.Timesheet
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import java.util.*

private val CLANDAY = LocalDate.of(2024, Month.JANUARY, 12)
private val HOLIDAY_MANAGER = HolidayManager.getInstance(ManagerParameters.create(Locale.of("nl")))

@Service
class TimesheetService(val calendarService: GoogleCalendarService) {

    fun generate(): Timesheet {
        return generate(determineMonth())
    }

    fun generate(month: YearMonth): Timesheet {
        val types = determineTypes(month)
        return Timesheet(month, types)
    }

    private fun determineTypes(month: YearMonth): ArrayList<DayType> {
        var from = month.atDay(1)
        val until = month.atEndOfMonth()
        val types = ArrayList<DayType>()
        val vacation = calendarService.getEvents(month)
        while (from <= until) {
            types.add(determineType(from, vacation))
            from = from.plusDays(1)
        }
        return types
    }

    private fun determineType(day: LocalDate, vacation: Set<LocalDate>): DayType {
        return if (day.dayOfWeek == DayOfWeek.SATURDAY || day.dayOfWeek == DayOfWeek.SUNDAY) {
            DayType.WEEKEND
        } else if (HOLIDAY_MANAGER.isHoliday(day)) {
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

    private fun determineMonth(): YearMonth {
        var now = LocalDate.now()
        if (now.dayOfMonth < 15) {
            now = now.minusMonths(1)
        }
        return YearMonth.from(now)
    }
}
