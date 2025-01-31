package nl.codeclan.timesheet.controller

import jakarta.validation.Valid
import nl.codeclan.timesheet.entities.Location
import nl.codeclan.timesheet.repository.LocationRepository
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/location")
class LocationController(private val repository: LocationRepository) {

    @GetMapping
    fun locations(): Iterable<Location> = repository.findAll()

    @PostMapping
    fun save(@Valid @RequestBody location: Location): Location = repository.save(location)
}
