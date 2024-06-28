package nl.codeclan.timesheet.model

import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

class Timesheet(private val month: YearMonth, val types: List<DayType>) {

    override fun toString(): String {
        var result = getMonthDisplay()
        types.forEachIndexed { i, dayType -> result += "\n${(i+1).toString().padStart(2, '0')}: $dayType" }
        return result
    }

    fun getMonthDisplay() = "${month.year} ${month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())}"
    fun forEachColumn(fnc: (int: Int) -> Unit) = types.forEachIndexed { i, _ -> fnc(i + 1)}
}
