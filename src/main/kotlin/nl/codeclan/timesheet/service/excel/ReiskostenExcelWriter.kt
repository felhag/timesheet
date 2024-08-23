package nl.codeclan.timesheet.service.excel

import nl.codeclan.timesheet.model.Day
import nl.codeclan.timesheet.model.Location
import nl.codeclan.timesheet.model.Timesheet
import org.apache.poi.ss.usermodel.*
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class ReiskostenExcelWriter : AbstractExcelWriter() {

    override fun name(): String {
        return "Reiskosten"
    }

    override fun write(workbook: Workbook, sheet: Sheet, timesheet: Timesheet) {
        IntRange(0, 3).forEach{ i -> sheet.setColumnWidth(i, 4000) }

        sheet.createRow(0).createCell(0).setCellValue(timesheet.getMonthDisplay())
        createHeaders(workbook, sheet, 2)
        var rowIdx = 2
        timesheet.days
            .forEachIndexed { i, day ->
                if (day.location != null && day.location != Location.HOME) {
                    rowIdx++
                    val row = sheet.createRow(rowIdx)
                    val date = LocalDate.of(timesheet.month.year, timesheet.month.month, i + 1)
                    createRow(row, rowIdx + 1, date, day, workbook)
                }
            }

        val headerStyle = workbook.createCellStyle()
        headerStyle.borderTop = BorderStyle.THIN

        val total = sheet.createRow(rowIdx++)
        IntRange(0, 3).forEach { i -> createTotalCell(total, i, workbook) }
        total.getCell(0).setCellValue("Totaal")
        total.getCell(2).cellFormula = sumFormula(2, 4, 2, rowIdx - 1)
        total.getCell(2).cellStyle.dataFormat = workbook.creationHelper.createDataFormat().getFormat("# k\\m")
        total.getCell(3).cellFormula = sumFormula(3, 4, 3, rowIdx - 1)
        total.getCell(3).cellStyle.dataFormat = workbook.creationHelper.createDataFormat().getFormat("\\€ #,##0.00")
    }

    private fun createTotalCell(row: Row, idx: Int, workbook: Workbook): Cell? {
        val kmCell = row.createCell(idx)
        kmCell.cellStyle = boldStyle(workbook)
        kmCell.cellStyle.borderTop = BorderStyle.THIN
        kmCell.cellStyle.verticalAlignment = VerticalAlignment.CENTER
        return kmCell
    }

    private fun createHeaders(workbook: Workbook, sheet: Sheet, index: Int) {
        val row = sheet.createRow(index)
        listOf("Datum", "Locatie", "Kilometers", "Kosten").forEachIndexed { i, it -> boldCell(row, i, workbook, it) }
    }

    private fun boldCell(row: Row, i: Int, workbook: Workbook, value: String) {
        val cell = row.createCell(i)
        cell.cellStyle = boldStyle(workbook)
        cell.setCellValue(value)
    }

    private fun createRow(row: Row, rowIdx: Int, date: LocalDate, day: Day, workbook: Workbook) {
        val dateCell = row.createCell(0)
        dateCell.cellStyle = formatStyle(workbook, "dd-mm-yyyy")
        dateCell.cellStyle.alignment = HorizontalAlignment.LEFT
        dateCell.setCellValue(date)

        val locCell = row.createCell(1)
        locCell.setCellValue(day.location!!.title)

        val kmCell = row.createCell(2, CellType.NUMERIC)
        kmCell.cellStyle = formatStyle(workbook, "# k\\m")
        kmCell.setCellValue(day.location.km.toDouble())

        val costsCell = row.createCell(3)
        costsCell.cellFormula = "C${rowIdx}*0.23"
        costsCell.cellStyle = formatStyle(workbook, "\\€ #,##0.00")
    }

    private fun formatStyle(workbook: Workbook, format: String): CellStyle? {
        val cellStyle3 = workbook.createCellStyle()
        cellStyle3.dataFormat = workbook.creationHelper.createDataFormat().getFormat(format)
        return cellStyle3
    }

//
//    private fun dateRow(workbook: Workbook,  sheet: Sheet, timesheet: Timesheet) {
//        val dates = sheet.createRow(2)
//        timesheet.days.forEachIndexed { i, day ->
//            val type = day.type
//            val cell = dates.createCell(i + 1)
//            cell.setCellValue("${i + 1}")
//            cell.cellStyle = style(workbook, if (type == DayType.WEEKEND || type == DayType.HOLIDAY) IndexedColors.GREY_25_PERCENT else IndexedColors.WHITE)
//        }
//    }
//
//    private fun createRow(workbook: Workbook, sheet: Sheet, timesheet: Timesheet, index: Int, label: String, type: DayType) {
//        val row = sheet.createRow(index)
//        row.createCell(0).setCellValue(label)
//        addIfType(workbook, row, timesheet.days, type, index)
//    }
//
//    private fun addIfType(workbook: Workbook, row: Row, types: List<Day>, target: DayType, index: Int) {
//        types.forEachIndexed{ i, day ->
//            run {
//                val cell = row.createCell(i + 1)
//                val style = workbook.createCellStyle()
//                cell.cellStyle = style
//                if (day.type == target) {
//                    cell.setCellValue(8.0)
//                    style.alignment = HorizontalAlignment.CENTER
//                }
//                if (day.type == DayType.WEEKEND || day.type == DayType.HOLIDAY) {
//                    style.fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
//                }
//            }
//        }
//
//        val total = row.createCell(types.size + 1)
//        total.cellFormula = sumFormula(1, index + 1, types.size, index + 1)
//        total.cellStyle = style(workbook, IndexedColors.WHITE)
//    }
//
//    private fun totalRow(workbook: Workbook, sheet: Sheet, timesheet: Timesheet) {
//        val total: Row = sheet.createRow(8)
//        val totalLabel = total.createCell(0)
//        totalLabel.setCellValue("Totaal aantal uren")
//        totalLabel.cellStyle = boldStyle(workbook)
//
//        timesheet.days.forEachIndexed { i, day ->
//            val column = i + 1
//            val cell = total.createCell(column)
//            val holiday = day.type == DayType.WEEKEND || day.type == DayType.HOLIDAY
//            if (!holiday) {
//                cell.cellFormula = sumFormula(column, 4, column, 8)
//            }
//            cell.cellStyle = style(workbook, if (holiday) IndexedColors.GREY_25_PERCENT else IndexedColors.WHITE)
//        }
//
//        val colIndex = timesheet.days.size + 1
//        val sum = total.createCell(colIndex)
//        sum.cellFormula = sumFormula(colIndex, 4, colIndex, 8)
//        sum.cellStyle = style(workbook, IndexedColors.LIME)
//
//    }
//
//    private fun style(workbook: Workbook, color: IndexedColors): CellStyle {
//        val headerStyle = boldStyle(workbook)
//        headerStyle.fillForegroundColor = color.index
//        headerStyle.borderBottom = BorderStyle.THIN
//        headerStyle.borderTop = BorderStyle.THIN
//        headerStyle.borderRight = BorderStyle.THIN
//        headerStyle.borderLeft = BorderStyle.THIN
//        headerStyle.alignment = HorizontalAlignment.CENTER
//        return headerStyle
//    }
//

//
//    private fun sumFormula(colStart: Int, rowStart: Int, colEnd: Int, rowEnd: Int): String {
//        return "SUM(${convertNumToColString(colStart)}${rowStart}:${convertNumToColString(colEnd)}${rowEnd})"
//    }
}
