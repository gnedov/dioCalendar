package com.agn.clndr;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.LinkedList;
import java.util.UUID;

public class EventStorageImplTest extends Assert{
    EventStorageImpl storage;

    @Before
    public void setUpStorage(){
        storage=new EventStorageImpl();
        UUID uuid= UUID.randomUUID();
        LinkedList<String> attenders=new LinkedList<String>();
        attenders.push("attOne@gmail.com");
        attenders.push("attTwo@gmail.com");
        Event event=new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Event one")
                .description("This is simple event")
                .timeStart(new DateTime(2014,9,20,12,11,00))
                .timeEnd(new DateTime(2014,9,20,13,11,10))
                .attenders(attenders)
                .build();
        LinkedList<String> attenders2=new LinkedList<String>();
        attenders2.push("attThree@gmail.com");
        attenders2.push("attTwo@gmail.com");
        Event event2=new Event.EventBuilder()
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

    @Test
    public void addEventGoodTest(){
        LinkedList<String> attenders=new LinkedList<String>();
        attenders.push("attThree@gmail.com");
        attenders.push("attOne@gmail.com");
        Event event=new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Event temp")
                .description("This is some temporary simple event")
                .timeStart(new DateTime(2014,8,16,00,00,00))
                .timeEnd(new DateTime(2014,8,16,23,05,10))
                .attenders(attenders)
                .build();
        int sizeBeforeAdd=storage.size();
        storage.addEvent(event);
        int sizeAfterAdd=storage.size();
        assertEquals(sizeBeforeAdd+1,sizeAfterAdd);
    }


    @Test
    public void addEventIsNullTest(){
        LinkedList<String> attenders=new LinkedList<String>();
        attenders.push("attThree@gmail.com");
        attenders.push("attOne@gmail.com");
        Event event=new Event.EventBuilder()
                .id(UUID.randomUUID())
                .title("Event temp")
                .description("This is some temporary simple event")
                .timeStart(new DateTime(2014,8,16,00,00,00))
                .timeEnd(new DateTime(2014,8,16,23,05,10))
                .attenders(attenders)
                .build();
        int sizeBeforeAdd=storage.size();
        storage.addEvent(event);
        int sizeAfterAdd=storage.size();
        assertEquals(sizeBeforeAdd+1,sizeAfterAdd);
    }

    @Test
    public void removeEventByUUIDGoodTest(){

    }

}
