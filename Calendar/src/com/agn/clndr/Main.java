package com.agn.clndr;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.rmi.RemoteException;

public class Main {

    public static void main(String[] arg) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        CalendarService service = (CalendarService) context.getBean("calendarService");

        DateTime timeTEst = new DateTime(2014, 7, 23, 16, 22, 34);

        try {
            service.getEventsOnWholeDay(timeTEst);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        timeTEst = new DateTime(2014, 7, 2, 20, 59, 59);
        try {
            if (service.isPersonBusyOnTime("eeeee@mail.ff", timeTEst))
                System.out.println("Person <eeeee@mail.ff> is busy on " + timeTEst);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            for (Event ev : service.getEventsPersonInvolvedByTime("eeeee@mail.ff", timeTEst.minusHours(10), timeTEst.plusHours(10))) {
                printEvent(ev);
                service.updateEvent(ev.getId());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
   private static void printEvent(Event ev) {
        if (ev != null)
            System.out.println(ev.toString());
    }
}
