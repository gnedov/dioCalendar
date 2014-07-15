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
        if (checkIdIsExists(id))
            return;  //Do nothing! the same event is already in store!
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

    //[Andr]: changed checkIdIsExists() method scope from <private> to <default_package> only for testing
    boolean checkIdIsExists(UUID id) {
        if (evStore.findById(id) != null) {
            System.out.println("The event with UUID:" + id.toString() + " already exists! " +
                    "You can not add this event again!");
            return true;
        }
        return false;
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
