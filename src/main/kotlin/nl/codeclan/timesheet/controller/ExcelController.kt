package nl.codeclan.timesheet.controller

import nl.codeclan.timesheet.model.TimesheetDto
import nl.codeclan.timesheet.service.excel.AbstractExcelWriter
import nl.codeclan.timesheet.service.excel.ReiskostenExcelWriter
import nl.codeclan.timesheet.service.excel.TimesheetExcelWriter
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.FileInputStream


@RestController
@RequestMapping("/excel")
class ExcelController(
    private val reiskostenWriter: ReiskostenExcelWriter,
    private val timesheetWriter: TimesheetExcelWriter,
) {
    @PostMapping("/reiskosten")
    fun reiskosten(@RequestBody timesheet: TimesheetDto): ResponseEntity<InputStreamResource> {
        return writeExcel(reiskostenWriter, timesheet)
    }

    @PostMapping("/timesheet")
    fun timesheet(@RequestBody timesheet: TimesheetDto): ResponseEntity<InputStreamResource> {
        return writeExcel(timesheetWriter, timesheet)
    }

    private fun writeExcel(writer: AbstractExcelWriter, timesheet: TimesheetDto): ResponseEntity<InputStreamResource> {
        val file = writer.write(timesheet)
        val resource = InputStreamResource(FileInputStream(file))

        return ResponseEntity.ok()
            .contentLength(file.length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }
}
