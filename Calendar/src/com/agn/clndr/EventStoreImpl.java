package com.agn.clndr;

import java.io.File;
import java.util.*;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.joda.time.DateTime;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class EventStoreImpl implements EventStore {
    private Map<UUID, Event> allEvents;
    private MultiMap<String, UUID> titleMap;
    private MultiValueMap<DateTime, UUID> timeStartMap;
    private MultiValueMap<DateTime, UUID> timeEndMap;
    private MultiValueMap<String, UUID> attenderMap;

    public EventStoreImpl() {
        this.allEvents = new HashMap<>();
        this.titleMap = new MultiValueMap<>();
        this.timeStartMap = new MultiValueMap<>();
        this.timeEndMap=new MultiValueMap<>();
        this.attenderMap = new MultiValueMap<>();
    }

    //TODO need verify the parameters (class EventStoreVerifier?). Id and Event must be not null
    //addEvent()->add()
    public void addEvent(UUID id, Event event) {
        allEvents.put(id, event);
        titleMap.put(event.getTitle(), id);
        timeStartMap.put(event.getTimeStart(), id);
        timeEndMap.put(event.getTimeEnd(), id);
        List<String> attenders = event.getAttenders();
        for (String attender : attenders) {
            attenderMap.put(attender, id);
        }
    }

    //This own function must be added to EventStorage interface
    //because, perhaps, in feature, we will need to change the data store, and we will not
    //need an UUID
    public void addEvent(Event event) {
        UUID uuid = event.getId();
        if (uuid == null)
            uuid = UUID.randomUUID();
        this.addEvent(uuid, event);
    }

    public boolean removeEvent(UUID uuid) {
        Event event = allEvents.get(uuid);
        return this.removeEvent(event);
    }

    public boolean removeEvent(Event event) {
        UUID uuid = event.getId();
        titleMap.removeMapping(event.getTitle(), uuid);
        timeStartMap.removeMapping(event.getTimeStart().toString(), uuid);
        allEvents.remove(uuid);
        return true;
    }

    public int size() {
        return allEvents.size();
    }

    //Todo Change getAllEvents: it must return Collection<Event>
    public Map getAllEvents() {
        return allEvents;
    }

    public Event findById(UUID id) {
        return allEvents.get(id);
    }

    public List<Event> findEventsByIds(List<UUID> ids) {
        List<Event> eventList = new ArrayList<>();
        for (UUID id : ids) {
            eventList.add(allEvents.get(id));
        }
        return eventList;
    }

    public List<UUID> findEventsIdsStartedBefore(DateTime beforeDate) {
        List<UUID> idsList = new ArrayList<>();
        for (DateTime evDate : timeStartMap.keySet()) {
            if (evDate.isBefore(beforeDate)) {
                idsList.addAll((List<UUID>) timeStartMap.get(evDate));
            }
        }
        return idsList;
    }

    public List<UUID> findEventsIdsEndedAfter(DateTime afterDate) {
        List<UUID> idsList = new ArrayList<>();
        for (DateTime evDate : timeEndMap.keySet()) {
            if (evDate.isAfter(afterDate)) {
                idsList.addAll((List<UUID>) timeEndMap.get(evDate));
            }
        }
        return idsList;
    }

    public List<UUID> findEventsIdsStartedOnTimeRange(DateTime timeStart, DateTime timeEnd) {
        List<UUID> idsList = new ArrayList<>();
        for (DateTime startedTime : timeStartMap.keySet()) {
            if (startedTime.isAfter(timeStart) && startedTime.isBefore(timeEnd)) {
                idsList.addAll((List<UUID>) timeStartMap.get(startedTime));
            }
        }
        return idsList;
    }

    public Collection<Event> findAllByTitle(String title) {
        if (!titleMap.containsKey(title))
            return new ArrayList<>(0);
        //TODO This code is bad?
        //local code review (vtegza): yes, there are lots of casts @ 20.07.14
        Collection<UUID> uuids = ((MultiValueMap) titleMap).getCollection(title);
        Collection<Event> events = new ArrayList<>(uuids.size());
        for (UUID id : uuids) {
            events.add(allEvents.get(id));
        }
        return events;
    }

    //TODO Think about getting all Events by time period (findAllByTimePeriod() put into CalendarService)
    public Collection<Event> findAllByDate(DateTime date) {
        Iterator<Map.Entry<DateTime, UUID>> iterator = ((MultiValueMap) timeStartMap).iterator();
        Collection<Event> events = new ArrayList<Event>();
        //local code review (vtegza): lot of complexity, use keySet() @ 20.07.14
        while (iterator.hasNext()) {
            Map.Entry<DateTime, UUID> entry = iterator.next();
            if ((entry.getKey()).getYear() == date.getYear() &&
                    (entry.getKey()).getDayOfYear() == date.getDayOfYear()) {
                UUID id = entry.getValue();
                events.add(allEvents.get(id));
            }
        }
        return events;
    }

    public Collection<Event> findAllByTimePeriod(DateTime start, DateTime end) {
        Iterator<Map.Entry<DateTime, UUID>> iterator = ((MultiValueMap) timeStartMap).iterator();
        Collection<Event> events = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<DateTime, UUID> entry = iterator.next();
            if (entry.getKey().isAfter(start) && entry.getKey().isBefore(end)) {
                UUID id = entry.getValue();
                events.add(allEvents.get(id));
            }
        }
        return events;
    }

    public Event findNextByDate(DateTime time) {
        //local code review (vtegza): shine of iterators, stick to keySet if you need @ 20.07.14
        Iterator<Map.Entry<DateTime, UUID>> iterator = ((MultiValueMap) timeStartMap).iterator();
        Map.Entry<DateTime, UUID> good_entry = null;
        while (iterator.hasNext()) {
            Map.Entry<DateTime, UUID> entry = iterator.next();
            if (time.isBefore(entry.getKey()))
                if (good_entry == null || time.isBefore(good_entry.getKey()))
                    good_entry = entry;
        }
        if (good_entry == null) {
            return null;
        }
        return allEvents.get(good_entry.getValue());
    }

    public List<UUID> findEventsIdsByAttender(String attender) {
        Iterator<Map.Entry<String, UUID>> iterator = attenderMap.iterator();
        List<UUID> eventsIds = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, UUID> entry = iterator.next();
            if (entry.getKey().equalsIgnoreCase(attender)) {
                eventsIds.add(entry.getValue());
            }
        }
        return eventsIds;
    }
    
    public void saveEventToXml(Event expectedEvent){
        JAXBContext context = null;

        EventAdapter eventAdapter = new EventAdapter(expectedEvent);
        try {
            context = JAXBContext.newInstance(EventAdapter.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(eventAdapter, new File("./"+expectedEvent.getTitle() +". xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
