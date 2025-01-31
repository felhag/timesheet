package nl.codeclan.timesheet.repository

import nl.codeclan.timesheet.entities.Location
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LocationRepository: CrudRepository<Location, Long>
