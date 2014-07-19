package com.agn.clndr;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {

    public static void main(String[] arg) {
        //local code review (vtegza): clean up code @ 20.07.14
        UUID id = null;
        String title = "cccccc";
        String description = "descr bnbnb kkfkf otoot";
        //local code review (vtegza): use diamond operator @ 20.07.14
        List<String> attenders = new ArrayList<String>();
        attenders.add("eeeee@mail.ff");
        attenders.add("vvvvv@mail.ff");
        DateTime timeStart = new DateTime(2014, 7, 2, 16, 22, 34);
        DateTime timeEnd = new DateTime(2014, 7, 2, 23, 11, 11);

        /* // This commented code works fine for creating event
        EventStoreImpl evStore = new EventStoreImpl();
        CalendarService clndrService = new CalendarService(evStore);

        clndrService.createEvent(id, title, description, attenders, timeStart, timeEnd);
        clndrService.printEvent(); // TO DO...
        */

        // extend the application with Spring framework
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        CalendarService service = (CalendarService) context.getBean("calendarService");
  id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00c");  // for test purposes only!
        String[] reservedCalendarNames = {"eventTitle_1", "eventTitle_2", "eventTitle_1"};
        for (String name : reservedCalendarNames)
            service.createEvent(id, name, name, attenders, timeStart, timeEnd);

        // [Andr] test find by Id functionality.
        // this block is for test purposes only!
        id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00c");
        service.createEvent(id, "eventTitle_1", description, attenders, timeStart, timeEnd);
        Event myEvent;
        service.printEvent( service.getEventById(id));

    }
}
