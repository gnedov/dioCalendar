package com.agn.clndr;

import java.util.*;

public class EventStore {
    private Map allEvents;
    private Map titleMap;

    public EventStore() {
        this.allEvents = new HashMap<UUID, Event>();
        this.titleMap = new HashMap<String, ArrayList<UUID>>();
    }

    public void addEvent(UUID id, Event event) {
        List<UUID> listId = new ArrayList<UUID>();

        allEvents.put(id, event);
        String evTitle = event.getTitle();
        if (!titleMap.containsKey(evTitle)) {
            listId.add(event.getId());
            titleMap.put(evTitle, listId);
        } else {
            listId = (List<UUID>) titleMap.get(evTitle);
            listId.add(event.getId());
        }
    }

    public Map getAllEvents() {
        return allEvents;
    }

    public Event findById(UUID id) {
        return (Event) allEvents.get(id);
    }

}
