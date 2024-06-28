package nl.codeclan.timesheet

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

import org.junit.jupiter.api.Assertions.*

class TimesheetServiceTest {
    private val service = TimesheetService()

    @Test
    fun generate() {
        val result = service.generate()
        println(result)
    }
}
