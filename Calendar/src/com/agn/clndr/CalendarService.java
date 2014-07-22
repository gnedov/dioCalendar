package com.agn.clndr;

import org.joda.time.DateTime;

import static org.joda.time.DateTimeConstants.*;

import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CalendarService implements CalendarServiceImpl {
    private EventStoreImpl evStore;

    public CalendarService(EventStoreImpl evStore) {
        this.evStore = evStore;
    }

    @Override
    //local code review (vtegza): use CRUD, separated update from create method  @ 20.07.14
    public void createEvent(UUID id, String title, String description, List<String> attenders,
                            DateTime timeStart, DateTime timeEnd) {

        id = id != null ? id : UUID.randomUUID();
        if (checkIdIsExists(id))
            return;  //Do nothing! the same event is already in store!
        Event newEvent = new Event.EventBuilder()
                .id(id)
                .title(title)
                .description(description)
                .attenders(attenders)
                .timeStart(timeStart)
                .timeEnd(timeEnd)
                .build();
        evStore.addEvent(newEvent.getId(), newEvent);
    }

    //[Andr]: changed checkIdIsExists() method scope from <private> to <default_package> for testing only
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
        evStore.removeEvent(eventId);
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

    @Override
    public List<Event> getEventsOnWholeDay(DateTime timeDay) {
        List<Event> startedEvents = new ArrayList<>();
        List<Event> endedEvents = new ArrayList<>();
        DateTime dayStart = (timeDay.withTimeAtStartOfDay()).withZoneRetainFields(DateTimeZone.UTC);
        DateTime dayEnd = dayStart.plusSeconds(SECONDS_PER_DAY - 1);
        /*  TODO: uncomment after findAllStartedByTimePeriod() and  findAllEndedByTimePeriod() appeared in eventStorage ;)
                         startedEvents = evStore.findAllStartedByTimePeriod(dayStart, dayEnd );
                         endedEvents = evStore.findAllEndedByTimePeriod(dayStart, dayEnd);
        */
        startedEvents.retainAll(endedEvents);
        return startedEvents;
    }

    @Override
    public boolean isPersonBusyOnTime(String attender, DateTime concreteTime) {
        return (getIdsListByPersonByTime(attender, concreteTime).size() > 0);
    }

    private List<UUID> getIdsListByPersonByTime(String attender, DateTime concreteTime) {
        List<UUID> idsEventsList;
        List<UUID> idsStartedBefore;
        List<UUID> idsEndedAfter;

        idsEventsList = evStore.findEventsIdsByAttender(attender);
        idsStartedBefore = evStore.findEventsIdsStartedBefore(concreteTime);
        idsEndedAfter = evStore.findEventsIdsEndedAfter(concreteTime);

        idsEventsList.retainAll(idsStartedBefore); // the first left join
        idsEventsList.retainAll(idsEndedAfter);    // the second left join

        return idsEventsList;
    }

    public void printEvent(Event ev) {
        if (ev != null)
            System.out.print(ev.toString());
    }

}
