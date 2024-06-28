package nl.codeclan.timesheet.model

import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

class Timesheet(private val month: YearMonth, private val types: ArrayList<DayType>) {

    override fun toString(): String {
        var result = month.month.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + month.year
        types.forEachIndexed { i, dayType -> result += "\n${(i+1).toString().padStart(2, '0')}: $dayType" }
        return result
    }
}
