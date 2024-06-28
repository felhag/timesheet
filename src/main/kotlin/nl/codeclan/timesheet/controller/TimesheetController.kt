package nl.codeclan.timesheet.controller

import nl.codeclan.timesheet.model.Timesheet
import nl.codeclan.timesheet.service.TimesheetService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/timesheet")
class TimesheetController(private val timesheetService: TimesheetService) {

    @GetMapping
    fun generate(): Timesheet {
        return timesheetService.generate()
    }
}
