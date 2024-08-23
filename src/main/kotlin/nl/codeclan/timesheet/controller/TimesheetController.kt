package nl.codeclan.timesheet.controller

import nl.codeclan.timesheet.model.Location
import nl.codeclan.timesheet.model.LocationDto
import nl.codeclan.timesheet.model.Timesheet
import nl.codeclan.timesheet.service.TimesheetService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TimesheetController(private val timesheetService: TimesheetService) {

    @GetMapping("/locations")
    fun locations(): Map<String, LocationDto> {
        return Location.entries.associate { loc -> Pair(loc.name, LocationDto(loc.title, loc.icon)) }
    }

    @GetMapping("/timesheet")
    fun generate(): Timesheet {
//        return Timesheet(YearMonth.of(2024, Month.JULY), listOf(
//            DayType.WEEKEND,
//            DayType.WEEKEND,
//            DayType.WORK,
//            DayType.WORK,
//            DayType.WORK,
//            DayType.WORK,
//            DayType.LEAVE,
//            DayType.WEEKEND,
//            DayType.WEEKEND,
//            DayType.WORK,
//            DayType.WORK,
//            DayType.WORK,
//            DayType.WORK,
//            DayType.CLANDAY,
//            DayType.WEEKEND,
//            DayType.WEEKEND,
//            DayType.WORK,
//            DayType.WORK,
//            DayType.LEAVE,
//            DayType.LEAVE,
//            DayType.LEAVE,
//            DayType.WEEKEND,
//            DayType.WEEKEND,
//            DayType.LEAVE,
//            DayType.WORK,
//            DayType.WORK,
//            DayType.WORK,
//            DayType.CLANDAY,
//            DayType.WEEKEND,
//            DayType.WEEKEND
//        ));
        return timesheetService.generate()
    }

    @PostMapping
    fun submit(@RequestBody timesheet: Timesheet) {

    }
}
