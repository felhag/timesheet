package nl.codeclan.timesheet.controller

import nl.codeclan.timesheet.model.Location
import nl.codeclan.timesheet.model.LocationDto
import nl.codeclan.timesheet.model.Timesheet
import nl.codeclan.timesheet.service.TimesheetService
import nl.codeclan.timesheet.service.excel.AbstractExcelWriter
import nl.codeclan.timesheet.service.excel.ReiskostenExcelWriter
import nl.codeclan.timesheet.service.excel.TimesheetExcelWriter
import org.springframework.core.io.InputStreamResource
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.FileInputStream
import java.time.LocalDate
import java.time.YearMonth
import java.util.*


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

    @GetMapping("/timesheet", "timesheet/{monthYear}")
    fun generate(@PathVariable(required = false) @DateTimeFormat(pattern = "M-yyyy") monthYear: Optional<YearMonth>): Timesheet {
        return monthYear.orElseGet(this::determineMonth).let(timesheetService::generate)
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

    private fun determineMonth(): YearMonth {
        var now = LocalDate.now()
        if (now.dayOfMonth < 15) {
            now = now.minusMonths(1)
        }
        return YearMonth.from(now)
    }
}
