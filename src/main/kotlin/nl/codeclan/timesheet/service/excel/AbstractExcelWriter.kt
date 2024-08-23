package nl.codeclan.timesheet.service.excel

import nl.codeclan.timesheet.model.Timesheet
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellReference.convertNumToColString
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

abstract class AbstractExcelWriter {
    abstract fun write(workbook: Workbook, sheet: Sheet, obj: Timesheet)
    abstract fun name(): String

    fun write(timesheet: Timesheet): File {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet(name())

        write(workbook, sheet, timesheet)

        val file = File("C:\\tmp\\temp.xlsx")
        workbook.write(FileOutputStream(file))
        workbook.close()
        return file
    }

    protected fun boldStyle(workbook: Workbook): CellStyle {
        val font = workbook.createFont()
        font.bold = true

        val headerStyle = workbook.createCellStyle()
        headerStyle.setFont(font)
        return headerStyle
    }

    protected fun sumFormula(colStart: Int, rowStart: Int, colEnd: Int, rowEnd: Int): String {
        return "SUM(${convertNumToColString(colStart)}${rowStart}:${convertNumToColString(colEnd)}${rowEnd})"
    }
}
