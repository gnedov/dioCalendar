package com.agn.clndr;

import org.apache.commons.collections4.map.MultiValueMap;
import org.joda.time.DateTime;

import java.nio.file.Path;
import java.util.*;


public class EventStorageImpl implements EventStorage {
    private HashMap<UUID, Event> allEvents;
    private MultiValueMap<String, UUID> titleMap;
    private MultiValueMap<DateTime, UUID> timeStartMap;
    private MultiValueMap<DateTime, UUID> timeEndMap;
    private MultiValueMap<String, UUID> attenderMap;
    private Map<UUID, Path> pathMap;

    public EventStorageImpl() {
        this.allEvents = new HashMap<>();
        this.titleMap = new MultiValueMap<>();
        this.timeStartMap = new MultiValueMap<>();
        this.timeEndMap = new MultiValueMap<>();
        this.attenderMap = new MultiValueMap<>();
        this.pathMap = new HashMap<>();
        loadEvents();
    }

    @Override
    public void addEvent(Event event) {
        if (event == null)
            throw new IllegalArgumentException("Event cannot be null");
        Path xmlPath;
        DataHelper dh = new DataHelper();
        xmlPath = dh.saveEventToXml(event);
        addEventToStorage(event, xmlPath);
    }

    @Override
    public void addEventToStorage(Event event, Path xmlPath) {
        UUID uuid = event.getId();
        if (uuid == null)
            uuid = UUID.randomUUID();
        allEvents.put(uuid, event);

        titleMap.put(event.getTitle(), uuid);
        timeStartMap.put(event.getTimeStart(), uuid);
        timeEndMap.put(event.getTimeEnd(), uuid);
        List<String> attenders = event.getAttenders();
        for (String attender : attenders) {
            attenderMap.put(attender, uuid);
        }
        pathMap.put(uuid, xmlPath);
    }

    @Override
    public boolean removeEvent(Event event) {
        if (event == null)
            throw new IllegalArgumentException("Event cannot be null");
        DataHelper dh = new DataHelper();
        if (dh.moveFileTo(pathMap.get(event.getId()), null)) {
            return removeEventFromStorage(event);
        }
        return false;
    }

    private boolean removeEventFromStorage(Event event) {

        UUID uuid = event.getId();
        if (allEvents.remove(uuid) == null)
            return false;
        titleMap.removeMapping(event.getTitle(), uuid);
        timeStartMap.removeMapping(event.getTimeStart(), uuid);
        timeEndMap.removeMapping(event.getTimeEnd(), uuid);
        List<String> attenders = event.getAttenders();
        for (String attender : attenders) {
            attenderMap.removeMapping(attender, uuid);
        }
        pathMap.remove(uuid);
        return true;
    }

    @Override
    public int size() {
        return allEvents.size();
    }

    //[Oleg] TODO Change getAllEvents: it must return Collection<Event>
    //[Oleg] TODO Maybe need return new HashMap(allEvents)
    public Map<UUID, Event> getAllEvents() {
        return allEvents;
    }

    public Event findById(UUID id) {
        return allEvents.get(id);
    }

    @Override
    public boolean isEventExist(UUID id) {
        return allEvents.containsKey(id);
    }

    @Override
    public Collection<Event> findAllByTitle(String title) {
        if (!titleMap.containsKey(title))
            return new ArrayList<>(0);
        Collection<UUID> uuids = titleMap.getCollection(title);
        Collection<Event> events = new ArrayList<>(uuids.size());
        for (UUID id : uuids) {
            events.add(allEvents.get(id));
        }
        return events;
    }

    //[Oleg] TODO\ Need check behavior, when allEvents collection contains a several elements,
    //[Oleg] TODO/ started at same time.
    private Collection<Event> findAllByTimePeriod(DateTime start, DateTime end, MultiValueMap<DateTime, UUID> map) {
        Iterator<Map.Entry<DateTime, UUID>> iterator = map.iterator();
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

    @Override
    public Collection<Event> findAllStartedByTimePeriod(DateTime start, DateTime end) {
        return findAllByTimePeriod(start, end, timeStartMap);
    }

    @Override
    public Collection<Event> findAllEndedByTimePeriod(DateTime start, DateTime end) {
        return findAllByTimePeriod(start, end, timeEndMap);
    }

    //[Oleg] I return a collection, because a several events can start at same time
    @Override
    public Collection<Event> findNextByDate(DateTime time) {
        Set<DateTime> timeSet = timeStartMap.keySet();
        DateTime good_key = null;
        for (DateTime key : timeSet) {
            if (time.isBefore(key)) {
                if (good_key == null || time.isBefore(good_key)) {
                    good_key = key;
                }
            }
        }
        if (good_key == null)
            return new ArrayList<>(0);
        Collection<UUID> uuidCollection = timeStartMap.getCollection(good_key);
        Collection<Event> eventCollection = new ArrayList<>(uuidCollection.size());
        for (UUID uuid : uuidCollection) {
            eventCollection.add(allEvents.get(uuid));
        }
        return eventCollection;
    }

    public Collection<Event> findAllByAttender(String attender) {
        Iterator<Map.Entry<String, UUID>> iterator = attenderMap.iterator();
        Collection<Event> events = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, UUID> entry = iterator.next();
            if (entry.getKey().equalsIgnoreCase(attender)) {
                UUID id = entry.getValue();
                events.add(allEvents.get(id));
            }
        }
        return events;
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

    private void loadEvents() {
        EventsLoader evLoader = new EventsLoader(this);

        evLoader.loadEvents();
    }

}
