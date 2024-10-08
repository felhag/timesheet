package nl.codeclan.timesheet.service

import nl.codeclan.timesheet.model.DayType
import nl.codeclan.timesheet.service.excel.TimesheetExcelWriter
import org.junit.jupiter.api.Test
import java.time.Month
import java.time.YearMonth

class TimesheetExcelWriterTest {
    private val service = TimesheetService(GoogleCalendarService())

    @Test
    fun write() {
//        val timesheet = Timesheet(YearMonth.of(2024, Month.JUNE), types())
        TimesheetExcelWriter().write(service.generate(YearMonth.of(2024, Month.MAY)))
    }

    private fun types(): List<DayType> {
        return listOf(
            DayType.WEEKEND,
            DayType.WEEKEND,
            DayType.WORK,
            DayType.WORK,
            DayType.WORK,
            DayType.WORK,
            DayType.LEAVE,
            DayType.WEEKEND,
            DayType.WEEKEND,
            DayType.WORK,
            DayType.WORK,
            DayType.WORK,
            DayType.WORK,
            DayType.CLANDAY,
            DayType.WEEKEND,
            DayType.WEEKEND,
            DayType.WORK,
            DayType.WORK,
            DayType.LEAVE,
            DayType.LEAVE,
            DayType.LEAVE,
            DayType.WEEKEND,
            DayType.WEEKEND,
            DayType.LEAVE,
            DayType.WORK,
            DayType.WORK,
            DayType.WORK,
            DayType.CLANDAY,
            DayType.WEEKEND,
            DayType.WEEKEND
        )
    }
}
