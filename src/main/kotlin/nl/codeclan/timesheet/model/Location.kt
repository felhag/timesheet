package nl.codeclan.timesheet.model

enum class Location(val title: String, val icon: String, val km: Int) {
    HOME("Thuis", "🏠", 0),
    ARNHEM("Arnhem", "🏢", 44),
    DUIVEN("Duiven", "🐦", 56),
    DENBOSCH("Den Bosch", "🐉",96)
}
