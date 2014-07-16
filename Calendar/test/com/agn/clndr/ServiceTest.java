package com.agn.clndr;

import org.hamcrest.Matcher;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ServiceTest {
    private Event excpectedEvent;
    private UUID id;
    private String inputName;
    private String description;
    private List<String> attenders;
    private DateTime timeStart;
    private DateTime timeEnd;

    @Before
    public void setUpEvent() {
        this.id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        this.inputName = "dddd";
        this.description = "descr bnbnb kkfkf otoot";
        this.attenders = new ArrayList<String>();
        this.attenders.add("eeeee@mail.ff");
        this.attenders.add("vvvvv@mail.ff");
        this.timeStart = new DateTime(2014, 7, 2, 16, 22, 34);
        this.timeEnd = new DateTime(2014, 7, 2, 23, 11, 11);

        excpectedEvent = new Event.EvntBuilder()
                .id(this.id)
                .title(this.inputName)
                .description(this.description)
                .attenders(this.attenders)
                .timeStart(this.timeStart)
                .timeEnd(this.timeEnd)
                .build();
    }

    @After
    public void tearDown() {
        excpectedEvent = null;
    }
    //

    @Test //[Andr] ? (expected = Exception.class)
    public void testAddEvent() throws Exception {
        EventStoreImpl evStore = mock(EventStoreImpl.class);
        CalendarService service = new CalendarService(evStore);
        //[Andr] doThrow(new Exception("stubbed void function")).when(evStore).addEvent(argThat(isUUID()), argThat(isEvent()));
        //[Andr] doNothing().when(evStore).addEvent(argThat(isUUID()), argThat(isEvent()));
        service.createEvent(id, inputName, description, attenders, timeStart, timeEnd);

        InOrder inOrder = inOrder(evStore);

        inOrder.verify(evStore).findById(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"));
        inOrder.verify(evStore).addEvent(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"), excpectedEvent);
        verifyNoMoreInteractions(evStore);
    }

    private Matcher<UUID> isUUID() {
        return new ArgumentMatcher<UUID>() {
            @Override
            public boolean matches(Object argument) {
                return argument instanceof UUID;
            }
        };
    }

    private Matcher<Event> isEvent() {
        return new ArgumentMatcher<Event>() {
            @Override
            public boolean matches(Object argument) {
                return argument instanceof Event;
            }
        };
    }

    @Test
    public void testCheckIdIsExists() throws Exception {
        EventStoreImpl evStore = mock(EventStoreImpl.class);
        CalendarService service = new CalendarService(evStore);
        //[Andr]: changed checkIdIsExists() method scope from <private> to <default_package> for testing only
        service.checkIdIsExists(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"));

        verify(evStore).findById(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"));
    }

    @Test
    public void testAddEvent_CheckCapturedParameters() throws Exception {
        EventStoreImpl evStore = mock(EventStoreImpl.class);
        CalendarService service = new CalendarService(evStore);

        service.createEvent(id, inputName, description, attenders, timeStart, timeEnd);

        ArgumentCaptor<UUID> argUUID = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Event> argEvent = ArgumentCaptor.forClass(Event.class);

        verify(evStore).addEvent(argUUID.capture(), argEvent.capture());

        assertEquals("38400000-8cf0-11bd-b23e-10b96e4ef00d", argUUID.getValue().toString());
        assertEquals(excpectedEvent, argEvent.getValue());

    }

}
