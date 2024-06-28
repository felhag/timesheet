package nl.codeclan.timesheet.service

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.security.GeneralSecurityException
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

@Service
class GoogleCalendarService {

    @Throws(IOException::class)
    private fun getCredentials(transport: NetHttpTransport): Credential {
        // Load client secrets.
        val input = GoogleCalendarService::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
            ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(input))

        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow.Builder(transport, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build()
        val receiver = LocalServerReceiver.Builder().setPort(8888).build()
        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

    @Throws(IOException::class, GeneralSecurityException::class)
    fun getEvents(month: YearMonth): Set<LocalDate> {
        // Build a new authorized API client service.
        val transport = GoogleNetHttpTransport.newTrustedTransport()
        val service = Calendar.Builder(transport, JSON_FACTORY, getCredentials(transport))
            .setApplicationName(APPLICATION_NAME)
            .build()

        // List the next 10 events from the primary calendar.
        val events = service.events()
            .list("primary")
            .setTimeMin(asDateTime(month.atDay(1).atStartOfDay()))
            .setTimeMax(asDateTime(month.atEndOfMonth().atTime(LocalTime.MAX)))
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute()

        return events.items
            .stream()
            .filter(this::filter)
            .flatMap(this::datesBetween)
            .collect(Collectors.toSet())
    }

    private fun datesBetween(event: Event): Stream<LocalDate> {
        val start = DateTimeFormatter.ISO_DATE.parse(event.start.date.toString())
        val end = DateTimeFormatter.ISO_DATE.parse(event.end.date.toString())
        return LocalDate.from(start).datesUntil(LocalDate.from(end))
    }

    private fun filter(event: Event): Boolean {
        return event.start.date != null && "Clanday" != event.summary
    }

    companion object {
        private const val APPLICATION_NAME = "Codeclan Timesheet"
        private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()
        private val SCOPES: List<String> = java.util.List.of(CalendarScopes.CALENDAR_READONLY)
        private const val TOKENS_DIRECTORY_PATH = "tokens"
        private const val CREDENTIALS_FILE_PATH = "/credentials.json"

        private fun asDateTime(dateTime: LocalDateTime): DateTime {
            return DateTime(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()))
        }
    }
}
