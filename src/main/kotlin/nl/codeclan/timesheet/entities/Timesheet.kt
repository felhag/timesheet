package nl.codeclan.timesheet.entities

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDate
import java.time.YearMonth

@Entity
class Timesheet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var yearMonth: YearMonth? = null,

    @ManyToOne
    var employee: Employee? = null,

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "DATE")
    var generated: LocalDate = LocalDate.now(),

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "timesheet_id")
    var days: List<TimesheetDay> = listOf()
)
