package com.agn.clndr;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventTest {
    private Event emptyEvent;
    private Event completeEvent;

    @Before
    public void setUp() throws Exception {
        List<String> attenders = new ArrayList<String>();
        attenders.add("dfg@ddd.fg");
        attenders.add("sdfr@dfg.ty");
        completeEvent = new Event.EventBuilder()
                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .title("eventTitle")
                .description("eventDescription")
                .attenders(attenders)
                .timeStart(new DateTime(2014, 4, 3, 12, 33, 44))
                .timeEnd(new DateTime(2014, 4, 23, 22, 34))
                .build();

        emptyEvent = new Event.EventBuilder()
                .id(null)
                .title(null)
                .description(null)
                .attenders(null)
                .timeStart(null)
                .timeEnd(null)
                .build();
    }

    @Test
    public void testGetId_IsNull() throws Exception {
        assertNull(emptyEvent.getId());
    }

    @Test
    public void testGetTitle_IsNull() throws Exception {
        assertNull(emptyEvent.getTitle());
    }

    @Test
    public void testGetTitle_IsNotEmpty() throws Exception {
        assertEquals(completeEvent.getTitle(), "eventTitle");
    }

    @Test
    public void testGetDescription() throws Exception {

    }

    @Test
    public void testGetAttenders() throws Exception {

    }

    @Test
    public void testGetTimeStart() throws Exception {

    }

    @Test
    public void testGetTimeEnd() throws Exception {

    }
}
