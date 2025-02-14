package nl.codeclan.timesheet.entities

import ValidLocation
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@ValidLocation
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
    var lon: Double = 0.0,

    @Column
    var distance: Int? = null,
) {
    companion object {
        const val HOME = "Thuis"
    }
}
