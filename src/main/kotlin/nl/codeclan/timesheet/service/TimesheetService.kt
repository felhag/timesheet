package nl.codeclan.timesheet.service

import de.focus_shift.jollyday.core.HolidayManager
import de.focus_shift.jollyday.core.ManagerParameters
import nl.codeclan.timesheet.model.Day
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
    fun generate(month: YearMonth): Timesheet {
        val types = determineTypes(month)
        return Timesheet(month, types)
    }

    private fun determineTypes(month: YearMonth): ArrayList<Day> {
        var from = month.atDay(1)
        val until = month.atEndOfMonth()
        val days = ArrayList<Day>()
        val vacation = emptySet<LocalDate>()
//        val vacation = calendarService.getEvents(month)
        while (from <= until) {
            val type = determineType(from, vacation)
            val location = null //determineLocation(from, type)
            days.add(Day(type, location))
            from = from.plusDays(1)
        }
        return days
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

// TODO: Default day for location
//    private fun determineLocation(day: LocalDate, type: DayType): Location? {
//        return when(type) {
//            DayType.WORK, DayType.CLANDAY -> {
//                return when (day.dayOfWeek) {
//                    DayOfWeek.TUESDAY -> Location.DUIVEN
//                    DayOfWeek.WEDNESDAY -> Location.ARNHEM
//                    DayOfWeek.FRIDAY -> Location.DENBOSCH
//                    else -> Location.HOME
//                }
//            }
//            else -> null
//        }
//    }
}
