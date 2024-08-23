package nl.codeclan.timesheet.model

import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

class Timesheet(private val month: YearMonth, val days: List<Day>) {

    override fun toString(): String {
        var result = getMonthDisplay()
        days.forEachIndexed { i, dayType -> result += "\n${(i+1).toString().padStart(2, '0')}: $dayType" }
        return result
    }

    fun getMonthDisplay() = "${month.year} ${month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())}"
    fun forEachColumn(fnc: (int: Int) -> Unit) = days.forEachIndexed { i, _ -> fnc(i + 1)}
}
