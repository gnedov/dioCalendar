package com.agn.clndr;

import java.nio.file.Path;
import java.util.*;

import org.apache.commons.collections4.map.MultiValueMap;
import org.joda.time.DateTime;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    public void addEvent(Event event) {
        Path xmlPath;
        DataHelper dh = new DataHelper();
        xmlPath = dh.saveEventToXml(event);
        addEventToStorage(event, xmlPath);
    }

    private void addEventToStorage(Event event, Path xmlPath) {
        if (event == null)
            throw new IllegalArgumentException("Event cannot be null");
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

    public boolean removeEvent(Event event) {
        DataHelper dh = new DataHelper();
        if (dh.moveFileTo(pathMap.get(event.getId()), null)) {
            return removeEventFromStorage(event);
        }
        return false;
    }

    private boolean removeEventFromStorage(Event event) {
        if (event == null)
            throw new IllegalArgumentException("Event cannot be null");
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

    public boolean isEventExist(UUID id) {
        return allEvents.containsKey(id);
    }

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

    public Collection<Event> findAllStartedByTimePeriod(DateTime start, DateTime end) {
        return findAllByTimePeriod(start, end, timeStartMap);
    }

    public Collection<Event> findAllEndedByTimePeriod(DateTime start, DateTime end) {
        return findAllByTimePeriod(start, end, timeEndMap);
    }

    //[Oleg] I return a collection, because a several events can start at same time
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
            return new ArrayList<Event>(0);
        Collection<UUID> uuidCollection = timeStartMap.getCollection(good_key);
        Collection<Event> eventCollection = new ArrayList<Event>(uuidCollection.size());
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
        DataHelper dataHelper = new DataHelper();
        Map<Path, Event> eventPathMap;
        Path path = null;
        LoadEventThread loadEventThread;

        eventPathMap = dataHelper.getEventsByPath(path);
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for (Map.Entry<Path, Event> entry : eventPathMap.entrySet()) {
            loadEventThread = new LoadEventThread(entry.getKey(), entry.getValue());
            executorService.submit(loadEventThread);
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
            System.out.println("Log: <" + eventPathMap.size() + "> events were loaded.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class LoadEventThread implements Runnable {
        private final Event event;
        private final Path path;

        private LoadEventThread(Path path, Event event) {
            this.event = event;
            this.path = path;
        }

        @Override
        public void run() {
            addEventToStorage(event, path);
        }

    }
}
