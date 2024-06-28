package nl.codeclan.timesheet

import nl.codeclan.timesheet.model.DayType
import nl.codeclan.timesheet.model.Timesheet
import org.junit.jupiter.api.Test
import java.time.Month
import java.time.YearMonth

class ExcelWriterTest {

    @Test
    fun write() {
        val timesheet = Timesheet(YearMonth.of(2024, Month.JUNE), types())
        ExcelWriter().write(timesheet)
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
