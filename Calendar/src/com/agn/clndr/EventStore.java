package com.agn.clndr;

import java.util.HashMap;
import java.util.UUID;

public class EventStore{
    private HashMap allEvents;

    public EventStore() {
        this.allEvents = new HashMap(10);
    }

    public void addEvent(UUID id, Event event){
        allEvents.put(id, event);
    }
    public HashMap getAllEvents() {
        return allEvents;
    }
    public Event find(UUID id){
        return (Event) allEvents.get(id);
    }
}
