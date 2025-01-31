package nl.codeclan.timesheet.entities

import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

@Entity
class Location(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:NotBlank
    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var lat: Double = 0.0,

    @Column(nullable = false)
    var long: Double = 0.0,

    @field:Min(1)
    @Column(nullable = false)
    var distance: Int = 0,
) {
    companion object {
        const val HOME = "Thuis"
    }
}
