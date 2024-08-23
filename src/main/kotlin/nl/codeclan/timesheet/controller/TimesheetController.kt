package nl.codeclan.timesheet.controller

import nl.codeclan.timesheet.model.Location
import nl.codeclan.timesheet.model.LocationDto
import nl.codeclan.timesheet.model.Timesheet
import nl.codeclan.timesheet.service.TimesheetService
import nl.codeclan.timesheet.service.excel.AbstractExcelWriter
import nl.codeclan.timesheet.service.excel.ReiskostenExcelWriter
import nl.codeclan.timesheet.service.excel.TimesheetExcelWriter
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.FileInputStream


@RestController
class TimesheetController(
    private val timesheetService: TimesheetService,
    private val reiskostenWriter: ReiskostenExcelWriter,
    private val timesheetWriter: TimesheetExcelWriter,
) {

    @GetMapping("/locations")
    fun locations(): Map<String, LocationDto> {
        return Location.entries.associate { loc -> Pair(loc.name, LocationDto(loc.title, loc.icon, loc.km)) }
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

    @PostMapping("/reiskosten")
    fun reiskosten(@RequestBody timesheet: Timesheet): ResponseEntity<InputStreamResource> {
        return writeExcel(reiskostenWriter, timesheet)
    }

    @PostMapping("/timesheet")
    fun timesheet(@RequestBody timesheet: Timesheet): ResponseEntity<InputStreamResource> {
        return writeExcel(timesheetWriter, timesheet)
    }

    private fun writeExcel(writer: AbstractExcelWriter, timesheet: Timesheet): ResponseEntity<InputStreamResource> {
        val file = writer.write(timesheet)
        val resource = InputStreamResource(FileInputStream(file))

        return ResponseEntity.ok()
            .contentLength(file.length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }
}
