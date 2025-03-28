package nl.codeclan.timesheet.entities

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:NotBlank
    @Column(nullable = false)
    var name: String = "",

    @field:NotBlank
    @Column(nullable = false)
    var email: String = "",

    @ManyToOne(optional = true)
    var home: Location? = null
)
