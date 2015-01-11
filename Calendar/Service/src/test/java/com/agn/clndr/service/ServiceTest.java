package com.agn.clndr.service;

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

import static junit.framework.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ServiceTest {
    private Event expectedEvent;
    private UUID id;
    private String inputName;
    private String description;
    private List<String> attenders;
    private DateTime timeStart;
    private DateTime timeEnd;
    private final String strUUID = "38400000-8cf0-11bd-b23e-10b96e4ef011";

    @Before
    public void setUpEvent() {
        this.id = UUID.fromString(strUUID);
        this.inputName = "dddd";
        this.description = "descr bnbnb kkfkf otoot";
        this.attenders = new ArrayList<>();
        this.attenders.add("eeeee@mail.ff");
        this.attenders.add("vvvvv@mail.ff");
        this.timeStart = new DateTime(2014, 7, 2, 16, 22, 34);
        this.timeEnd = new DateTime(2014, 7, 2, 23, 11, 11);

        expectedEvent = new Event.EventBuilder()
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
        expectedEvent = null;
    }

    @Test
    public void testAddEvent_InOrder() throws Exception {
        EventStorageImpl evStore = mock(EventStorageImpl.class);
        CalendarServiceImpl service = new CalendarServiceImpl(evStore);
        doNothing().when(evStore).addEvent(argThat(isEvent()));
        service.createEvent(id, inputName, description, attenders, timeStart, timeEnd);

        InOrder inOrder = inOrder(evStore);

        inOrder.verify(evStore).isEventExist(UUID.fromString(strUUID));
        inOrder.verify(evStore).addEvent(expectedEvent);
        verifyNoMoreInteractions(evStore);
    }

    @Test
    public void testAddEvent_InterceptedCreation() throws Exception {
        EventStorageImpl evStore = mock(EventStorageImpl.class);
        CalendarServiceImpl service = new CalendarServiceImpl(evStore);
        doNothing().when(evStore).addEvent( argThat(isEvent()));
        service.createEvent(id, inputName, description, attenders, timeStart, timeEnd);
        assertTrue(!service.checkIdIsExists(UUID.fromString(strUUID)));
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
        EventStorageImpl evStore = mock(EventStorageImpl.class);
        CalendarServiceImpl service = new CalendarServiceImpl(evStore);
        //[Andr]: changed checkIdIsExists() method scope from <private> to <default_package> for testing only
        service.checkIdIsExists(UUID.fromString(strUUID));
        verify(evStore).isEventExist(UUID.fromString(strUUID));
    }

    @Test
    public void testAddEvent_CheckCapturedParameters() throws Exception {
        EventStorageImpl evStore = mock(EventStorageImpl.class);
        CalendarServiceImpl service = new CalendarServiceImpl(evStore);

        service.createEvent(id, inputName, description, attenders, timeStart, timeEnd);
        ArgumentCaptor<Event> argEvent = ArgumentCaptor.forClass(Event.class);

        verify(evStore).addEvent( argEvent.capture());
        assertEquals(expectedEvent, argEvent.getValue());
        service.deleteEvent(id);
    }
    
    @Test
    public void testAddEvent_CallDeleteEvent() throws Exception{
        EventStorageImpl evStore = new EventStorageImpl();
        CalendarServiceImpl service = new CalendarServiceImpl(evStore);
        CalendarServiceImpl spyService =spy(service);
        spyService.createEvent(id, inputName, description, attenders, timeStart, timeEnd);
        Event oldEvent = spyService.getEventById(id);
        Event newEvent = new Event.EventBuilder(oldEvent)
                .title("new title1")
                .build();

        spyService.updateEvent(newEvent);
        verify(spyService).deleteEvent(id);
        service.deleteEvent(id);
    }
    
    @Test
    public void testGetEventById() throws Exception{
        EventStorageImpl evStore = mock(EventStorageImpl.class);
        CalendarServiceImpl service = new CalendarServiceImpl(evStore);
        service.getEventById(id);
        verify(evStore).findById(id);
    }

}
