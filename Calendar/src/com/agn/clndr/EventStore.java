package com.agn.clndr;

import java.util.*;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.joda.time.DateTime;

public class EventStore {
    private Map allEvents;
    private MultiMap titleMap;
    private MultiMap timeStartMap; //if we want to use search by event.timeEnd we need add MultiMap timeEndMap

    public EventStore() {
        this.allEvents = new HashMap<UUID, Event>();
        this.titleMap = new MultiValueMap<String,UUID>();
        this.timeStartMap = new MultiValueMap<String, UUID> ();
    }

    //TODO need verify the parameters (class EventStoreVerifier?). Id and Event must be not null
    //addEvent()->add()
    public void addEvent(UUID id, Event event) {
        allEvents=new HashMap<UUID, Event>();
        titleMap=new MultiValueMap<String,UUID>();
        timeStartMap=new MultiValueMap<DateTime, UUID>();
    }

    //This own function must be added to EventStorage interface
    //because, perhaps, in feature, we will need to change the data store, and we will not
    //need an UUID
    public void addEvent(Event event){
        UUID uuid=UUID.randomUUID();
        this.addEvent(uuid, event);
    }

    public Event removeEvent(UUID uuid){
        Event event=(Event) allEvents.get(uuid);
        if (event==null)    //it's possible, that allEvents have a record with correct uuid and null event
            return null;
        return this.removeEvent(event);
    }

    public Event removeEvent(Event event){
        UUID uuid=event.getId();
        titleMap.removeMapping(event.getTitle(), uuid);
        timeStartMap.removeMapping(event.getTimeStart(), uuid);
        allEvents.remove(uuid);
        return event;
    }

    public int size(){
        return allEvents.size();
    }

    public Map getAllEvents() {
        return allEvents;
    }

    public Event findById(UUID id) {
        return (Event) allEvents.get(id);
    }

}
