package com.agn.clndr;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ServiceTest {
    
    @Before
    
    //
    
    @Test                   
    public void testaddEvent()  throws Exception {
        UUID id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        String inputName = "dddd";
        String description = "descr bnbnb kkfkf otoot";
        List<String> attenders = new ArrayList<String>();
        attenders.add("eeeee@mail.ff");
        attenders.add("vvvvv@mail.ff");
        GregorianCalendar timeStart = new GregorianCalendar(2014,7,2,16,22,34);
        GregorianCalendar timeEnd = new GregorianCalendar(2014,7,2,23,11,11);
        
        Event excpectedEvent = new Event.EvntBuilder()
                .id(id)
                .title(inputName)
                .description(description)
                .attenders(attenders)
                .timeStart(timeStart)
                .timeEnd(timeEnd)
                .build();
        
        EventStore evStore = mock(EventStore.class);
        CalendarService service = new CalendarService(evStore);

        service.createEvent(id, inputName , description, attenders,timeStart,timeEnd  );

        verify(evStore).addEvent(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"), excpectedEvent);
        
    }
        
    
}
