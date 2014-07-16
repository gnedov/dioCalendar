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

    //Todo Change getAllEvents: it must return Collection<Event>
    public Map getAllEvents() {
        return allEvents;
    }

    public Event findById(UUID id) {
        return (Event) allEvents.get(id);
    }

    public Collection<Event> findAllByTitle(String title){
        if (!titleMap.containsKey(title))
            return new ArrayList<Event>(0);
        //TODO This code is bad?
        Collection<UUID> uuids=((MultiValueMap)titleMap).getCollection(title);
        Collection<Event> events=new ArrayList<Event>(uuids.size());
        for (UUID id:uuids){
            events.add((Event)allEvents.get(id));
        }
        return events;
    }

    //TODO Think about getting all Events by time period (findAllByTimePeriod() put into CalendarService)
    public Collection<Event> findAllByDate(DateTime date){
        Iterator<Map.Entry<DateTime,UUID>> iterator=((MultiValueMap)timeStartMap).iterator();
        Collection<Event> events=new ArrayList<Event>();
        while (iterator.hasNext()){
            Map.Entry<DateTime,UUID> entry=iterator.next();
            if ((entry.getKey()).getYear()==date.getYear() &&
                (entry.getKey()).getDayOfYear()==date.getDayOfYear()){
                UUID id=entry.getValue();
                events.add((Event)allEvents.get(id));
            }
        }
        return events;
    }

    public Collection <Event> findAllByTimePeriod(DateTime start, DateTime end){
        Iterator<Map.Entry<DateTime,UUID>> iterator=((MultiValueMap)timeStartMap).iterator();
        Collection<Event> events=new ArrayList<Event>();
        while (iterator.hasNext()){
            Map.Entry<DateTime, UUID> entry=iterator.next();
            if (entry.getKey().isAfter(start) && entry.getKey().isBefore(end)){
                UUID id=entry.getValue();
                events.add((Event)allEvents.get(id));
            }
        }
        return events;
    }

    public Event findNextByDate(DateTime time){
        Iterator<Map.Entry<DateTime,UUID>> iterator=((MultiValueMap)timeStartMap).iterator();
        Map.Entry<DateTime,UUID> good_entry=null;
        while (iterator.hasNext()){
            Map.Entry<DateTime,UUID> entry=iterator.next();
            if (time.isBefore(entry.getKey()))
                if (good_entry==null || time.isBefore(good_entry.getKey()))
                    good_entry=entry;
        }
        if (good_entry==null){
            return null;
        }
        return (Event) allEvents.get(good_entry.getValue());
    }
}
