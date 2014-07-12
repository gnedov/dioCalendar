package com.agn.clndr;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public class CalendarService implements ICalendarService {
    private EventStore evStore;

    public CalendarService(EventStore evStore) {
        this.evStore = evStore;
    }

    @Override
    public void createEvent(UUID id, String title, String description, List<String> attenders,
                            GregorianCalendar timeStart, GregorianCalendar timeEnd) {

        id = id != null ? id : UUID.randomUUID();

        Event newEvent = new Event.EvntBuilder()
                .id(id)
                .title(title)
                .description(description)
                .attenders(attenders)
                .timeStart(timeStart)
                .timeEnd(timeEnd)
                .build();
        evStore.addEvent(newEvent.getId(), newEvent);
    }

    @Override
    public void deleteEvent(UUID eventId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateEvent(UUID eventId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Event getEventById(UUID eventId) {
        Event ev;
        ev = evStore.findById(eventId);
        return ev;
    }

    @Override
    public Event getEventByTitle(String eventTitle) {
        return null;
    }

    public void printEvent(Event ev) {
        System.out.print(ev.toString());
        //[Andr] ToDo: optimize GregorianCalendar data for output
    }
}
