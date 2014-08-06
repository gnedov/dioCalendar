package com.agn.clndr;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] arg) {
        // extend the application with Spring framework
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        CalendarServiceImpl service = (CalendarServiceImpl) context.getBean("calendarService");

        DateTime timeTEst = new DateTime(2014, 7, 23, 16, 22, 34);

        service.getEventsOnWholeDay(timeTEst);

        timeTEst = new DateTime(2014, 7, 2, 20, 59, 59);
        if (service.isPersonBusyOnTime("eeeee@mail.ff", timeTEst))
            System.out.println("Person <eeeee@mail.ff> is busy on " + timeTEst);


        for (Event ev : service.getEventsPersonInvolvedByTime("eeeee@mail.ff", timeTEst.minusHours(10), timeTEst.plusHours(10))) {
            service.printEvent(ev);
            service.updateEvent(ev.getId());
        }
    }
}
