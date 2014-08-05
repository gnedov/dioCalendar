package com.agn.clndr;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] arg) {
        UUID id;
        String title = "eventTitle_1";
        String description = "descr bnbnb kkfkf otoot";
        List<String> attenders = new ArrayList<>();
        attenders.add("eeeee@mail.ff");
        attenders.add("vvvvv@mail.ff");
        DateTime timeStart = new DateTime(2014, 7, 2, 16, 22, 34);
        DateTime timeEnd = new DateTime(2014, 7, 2, 23, 11, 11);

        // extend the application with Spring framework
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        CalendarServiceImpl service = (CalendarServiceImpl) context.getBean("calendarService");

        id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        service.createEvent(id, title, description, attenders, timeStart, timeEnd);
        service.printEvent(service.getEventById(id));

        DateTime timeTEst = new DateTime(2014, 7, 23, 16, 22, 34);

        service.getEventsOnWholeDay(timeTEst);

        timeTEst = new DateTime(2014, 7, 2, 20, 59, 59);
        if (service.isPersonBusyOnTime("eeeee@mail.ff", timeTEst))
            System.out.println("Person <eeeee@mail.ff> is busy on " + timeTEst);


        for (Event ev : service.getEventsPersonInvolvedByTime("eeeee@mail.ff", timeTEst.minusHours(10), timeTEst.plusHours(10))) {
            service.printEvent(ev);
            service.updateEvent(ev);
        }
    }
}
