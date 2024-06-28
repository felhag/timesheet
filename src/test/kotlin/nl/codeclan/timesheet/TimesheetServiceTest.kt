package nl.codeclan.timesheet

import org.junit.jupiter.api.Test

class TimesheetServiceTest {
    private val service = TimesheetService(GoogleCalendarService())

    @Test
    fun generate() {
        val result = service.generate()
        println(result)
    }
}
