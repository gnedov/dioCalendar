package com.agn.clndr;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public interface CalendarService {

    void createEvent(UUID id, String title, String description, List<String> attenders,
                     DateTime timeStart, DateTime timeEnd);

    boolean deleteEvent(UUID eventId);

    void updateEvent(UUID eventId);

    Event getEventById(UUID eventId);

    Event getEventByTitle(String eventTitle);

    List<Event> getEventsOnWholeDay(DateTime timeDay);

    boolean isPersonBusyOnTime(String attender, DateTime concreteTime);

    List<Event> getEventsPersonInvolvedByTime(String attender, DateTime timeStart, DateTime timeEnd);

}
