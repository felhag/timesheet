package nl.codeclan.timesheet.model

enum class Location(val title: String, val icon: String, km: Int) {
    HOME("Thuis", "🏠", 0),
    ARNHEM("Arnhem", "🏢", 22),
    DUIVEN("Duiven", "🐦", 28),
    DENBOSCH("Den Bosch", "🐉",48)
}
