package nl.codeclan.timesheet.controller

import jakarta.validation.Valid
import nl.codeclan.timesheet.entities.Location
import nl.codeclan.timesheet.repository.LocationRepository
import nl.codeclan.timesheet.service.EmployeeService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/location")
class LocationController(private val repository: LocationRepository,
                         private val employeeService: EmployeeService) {

    @GetMapping
    fun locations(): Iterable<Location> = repository.findAllByEmployeeEmail(employeeService.getEmail())

    @PostMapping("/home/{lat}/{lon}")
    fun home(@PathVariable lat: Double, @PathVariable lon: Double): Location {
        val home = save(Location(null, Location.HOME, employeeService.getEmployee(), lat, lon))
        employeeService.setHome(home.employee!!, home)
        return home
    }

    @PostMapping
    fun save(@Valid @RequestBody location: Location): Location {
        val days = location.defaultDays
        if (days.isNotEmpty()) {
            // remove default day from other locations
            repository.findAll()
                .filter { it -> it.defaultDays.removeAll { it in days } }
                .onEach { repository.save(it) }
        }
        location.employee = employeeService.getEmployee()
        return repository.save(location)
    }
}
