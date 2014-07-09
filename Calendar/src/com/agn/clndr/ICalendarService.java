package com.agn.clndr;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public interface ICalendarService {

    void createEvent(UUID id, String title, String description, List<String> attenders,
                     GregorianCalendar timeStart, GregorianCalendar timeEnd);

    void deleteEvent(UUID eventId);

    void updateEvent(UUID eventId);

    Event getEventById(UUID eventId);

    Event getEventByTitle(String eventTitle);

}
