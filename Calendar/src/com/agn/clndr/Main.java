package com.agn.clndr;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {

    public static void main(String[] arg) {
        UUID id = null;
        String title = "cccccc";
        String description = "descr bnbnb kkfkf otoot";
        List<String> attenders = new ArrayList<String>();
        attenders.add("eeeee@mail.ff");
        attenders.add("vvvvv@mail.ff");
        GregorianCalendar timeStart = new GregorianCalendar(2014, 7, 2, 16, 22, 34);
        GregorianCalendar timeEnd = new GregorianCalendar(2014, 7, 2, 23, 11, 11);

        /* // This commented code works fine for creating event
        EventStore evStore = new EventStore();
        CalendarService clndrService = new CalendarService(evStore);

        clndrService.createEvent(id, title, description, attenders, timeStart, timeEnd);
        clndrService.printEvent(); // TO DO...
        */

        // extend the application with Spring framework
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        CalendarService service = (CalendarService) context.getBean("calendarService");

        String[] reservedCalendarNames = {"eventTitle_1", "eventTitle_2"};

        for (String name : reservedCalendarNames)
            service.createEvent(id, name, name, attenders, timeStart, timeEnd);

        // [Andr] test find by Id functionality
        id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        service.createEvent(id, title, description, attenders, timeStart, timeEnd);
        Event myEvent;
        service.printEvent( service.getEventById(id));

    }
}
