package com.agn.clndr.service;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.LinkedList;
import java.util.UUID;

public class EventStorageImplTest extends Assert {
    private EventStorageImpl storage;
    private Event event;
    private Event event2;

    @Before
    public void setUpStorage() {
        storage = new EventStorageImpl();
        UUID uuid = UUID.randomUUID();
        LinkedList<String> attenders = new LinkedList();
        attenders.push("attOne@gmail.com");
        attenders.push("attTwo@gmail.com");
        event = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Event one")
                .description("This is simple event")
                .timeStart(new DateTime(2014, 9, 20, 12, 11, 00))
                .timeEnd(new DateTime(2014, 9, 20, 13, 11, 10))
                .attenders(attenders)
                .build();
        LinkedList<String> attenders2 = new LinkedList();
        attenders2.push("attThree@gmail.com");
        attenders2.push("attTwo@gmail.com");
        event2 = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Event two")
                .description("This is another simple event")
                .timeStart(new DateTime(2014, 8, 16, 00, 00, 00))
                .timeEnd(new DateTime(2014, 8, 16, 23, 05, 10))
                .attenders(attenders2)
                .build();
        storage.addEvent(event);
        storage.addEvent(event2);
    }

    @After
    public void cleanUp() {
        storage.removeEvent(event);
        storage.removeEvent(event2);
    }

    @Test
    public void addEventGoodTest() {
        LinkedList<String> attenders = new LinkedList();
        attenders.push("attThree@gmail.com");
        attenders.push("attOne@gmail.com");
        Event event = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Event temp")
                .description("This is some temporary simple event")
                .timeStart(new DateTime(2014, 8, 16, 00, 00, 00))
                .timeEnd(new DateTime(2014, 8, 16, 23, 05, 10))
                .attenders(attenders)
                .build();
        int sizeBefore = storage.size();
        storage.addEvent(event);
        int sizeAfter = storage.size();
        assertEquals(sizeBefore + 1, sizeAfter);
        storage.removeEvent(event);
    }


    @Test(expected = IllegalArgumentException.class)
    public void addEventIsNullTest() {
        Event event = null;
        storage.addEvent(event);
    }

    @Test
    public void removeEventGoodTest() {
        LinkedList<String> attenders = new LinkedList();
        attenders.push("attThree@gmail.com");
        attenders.push("attOne@gmail.com");
        Event event = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Event temp")
                .description("This is some temporary simple event")
                .timeStart(new DateTime(2014, 8, 16, 00, 00, 00))
                .timeEnd(new DateTime(2014, 8, 16, 23, 05, 10))
                .attenders(attenders)
                .build();
        storage.addEvent(event);
        int sizeBefore = storage.size();
        assertEquals(true, storage.removeEvent(event));
        int sizeAfter = storage.size();
        assertEquals(sizeBefore - 1, sizeAfter);
        storage.removeEvent(event);
    }

    @Test
    public void removeEventIsAbsentTest() {
        EventStorageImpl otherStorage = new EventStorageImpl();
        LinkedList<String> attenders = new LinkedList();
        attenders.push("attThree@gmail.com");
        attenders.push("attOne@gmail.com");
        Event event = new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Event temp")
                .description("This is some temporary simple event")
                .timeStart(new DateTime(2014, 8, 16, 00, 00, 00))
                .timeEnd(new DateTime(2014, 8, 16, 23, 05, 10))
                .attenders(attenders)
                .build();
        assertEquals(false, otherStorage.removeEvent(event));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeEventIsNullTest() {
        Event event = null;
        storage.removeEvent(event);
    }

    @Test
    public void findByIdTestGood() {
        LinkedList<String> attenders = new LinkedList();
        attenders.push("attThree@gmail.com");
        attenders.push("attOne@gmail.com");
        UUID uuid = UUID.randomUUID();
        Event event = new Event.EventBuilder()
                .id(uuid)
                .title("Event temp")
                .description("This is some temporary simple event")
                .timeStart(new DateTime(2014, 8, 16, 00, 00, 00))
                .timeEnd(new DateTime(2014, 8, 16, 23, 05, 10))
                .attenders(attenders)
                .build();
        storage.addEvent(event);
        assertEquals(event, storage.findById(uuid));
        storage.removeEvent(event);
    }

    @Test
    public void findByIdIfEventIsAbsentTest() {
        UUID uuid = UUID.randomUUID();
        assertEquals(null, storage.findById(uuid));
    }


}
