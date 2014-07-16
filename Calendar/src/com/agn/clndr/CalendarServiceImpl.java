package com.agn.clndr;

import org.joda.time.DateTime;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public interface CalendarServiceImpl {

    void createEvent(UUID id, String title, String description, List<String> attenders,
                     DateTime timeStart, DateTime timeEnd);

    void deleteEvent(UUID eventId);

    void updateEvent(UUID eventId);

    Event getEventById(UUID eventId);

    Event getEventByTitle(String eventTitle);

}
