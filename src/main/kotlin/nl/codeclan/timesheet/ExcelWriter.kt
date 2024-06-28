package nl.codeclan.timesheet

import nl.codeclan.timesheet.model.DayType
import nl.codeclan.timesheet.model.Timesheet
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellReference.convertNumToColString
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.FileOutputStream


@Service
class ExcelWriter {

    fun write(timesheet: Timesheet) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Timesheet")
        sheet.setColumnWidth(0, 6000)
        timesheet.forEachColumn { i -> sheet.setColumnWidth(i, 1200) }

        sheet.createRow(0).createCell(0).setCellValue(timesheet.getMonthDisplay())
        dateRow(workbook, sheet, timesheet)

        createRow(workbook, sheet, timesheet, 4, "Ziekte", DayType.SICK)
        createRow(workbook, sheet, timesheet, 3, "Klant uren", DayType.WORK)
        createRow(workbook, sheet, timesheet, 5, "Verlof", DayType.LEAVE)
        createRow(workbook, sheet, timesheet, 6, "Feestdagen", DayType.HOLIDAY)
        createRow(workbook, sheet, timesheet, 7, "Clandays", DayType.CLANDAY)

        val total: Row = sheet.createRow(9)
        val totalLabel = total.createCell(0)
        totalLabel.setCellValue("Totaal aantal uren")
        totalLabel.cellStyle = boldStyle(workbook)

        timesheet.types.forEachIndexed { i, type ->
            val column = i + 1
            val cell = total.createCell(column)
            cell.cellFormula = sumFormula(column, 4, column, 8)
            cell.cellStyle = style(workbook, if (type == DayType.WEEKEND || type == DayType.HOLIDAY) IndexedColors.SKY_BLUE else IndexedColors.WHITE)
        }

        workbook.write(FileOutputStream("C:\\tmp\\temp.xlsx"))
        workbook.close()
    }

    private fun dateRow(workbook: Workbook,  sheet: Sheet, timesheet: Timesheet) {
        val dates = sheet.createRow(2)
        timesheet.types.forEachIndexed { i, type ->
            val cell = dates.createCell(i + 1)
            cell.setCellValue("${i + 1}")
            cell.cellStyle = style(workbook, if (type == DayType.WEEKEND || type == DayType.HOLIDAY) IndexedColors.SKY_BLUE else IndexedColors.WHITE)
        }
    }

    private fun createRow(workbook: Workbook, sheet: Sheet, timesheet: Timesheet, index: Int, label: String, type: DayType) {
        val row = sheet.createRow(index)
        row.createCell(0).setCellValue(label)
        addIfType(workbook, row, timesheet.types, type, index)
    }

    private fun addIfType(workbook: Workbook, row: Row, types: List<DayType>, target: DayType, index: Int) {
        types.forEachIndexed{ i, type ->
            run {
                if (type == target) {
                    row.createCell(i + 1).setCellValue(8.0)
                }
            }
        }

        val total = row.createCell(types.size + 1)
        total.cellFormula = sumFormula(1, index + 1, types.size, index + 1)
        total.cellStyle = style(workbook, IndexedColors.WHITE)
    }

    private fun style(workbook: Workbook, color: IndexedColors): CellStyle {
        val headerStyle = boldStyle(workbook)
        headerStyle.fillForegroundColor = color.getIndex()
        headerStyle.borderBottom = BorderStyle.THIN
        headerStyle.borderTop = BorderStyle.THIN
        headerStyle.borderRight = BorderStyle.THIN
        headerStyle.borderLeft = BorderStyle.THIN
        return headerStyle
    }

    private fun boldStyle(workbook: Workbook): CellStyle {
        val font = workbook.createFont()
        font.bold = true

        val headerStyle: CellStyle = workbook.createCellStyle()
        headerStyle.setFont(font)
        return headerStyle
    }

    private fun sumFormula(colStart: Int, rowStart: Int, colEnd: Int, rowEnd: Int): String {
        return "SUM(${convertNumToColString(colStart)}${rowStart}:${convertNumToColString(colEnd)}${rowEnd})"
    }
}