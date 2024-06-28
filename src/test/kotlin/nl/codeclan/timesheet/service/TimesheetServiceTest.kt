package nl.codeclan.timesheet.service

import org.junit.jupiter.api.Test
import java.time.Month
import java.time.YearMonth

class TimesheetServiceTest {
    private val service = TimesheetService(GoogleCalendarService())

    @Test
    fun generate() {
        val result = service.generate(YearMonth.of(2024, Month.MAY))
        println(result)
    }
}
