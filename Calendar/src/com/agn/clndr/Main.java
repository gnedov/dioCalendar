package com.agn.clndr;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public class Main {

    public static void  main(String[] arg){
        UUID id = null;
        String title = "cccccc";
        String description = "descr bnbnb kkfkf otoot";
        List<String> attenders = new ArrayList<String>();
            attenders.add("eeeee@mail.ff");
            attenders.add("vvvvv@mail.ff");
        GregorianCalendar timeStart = new GregorianCalendar(2014,07,2,16,22,34);
        GregorianCalendar timeEnd = new GregorianCalendar(2014,07,2,23,11,11);

        EventStore evStore = new EventStore();
        CalendarService clndrService = new CalendarService(evStore);

        clndrService.createEvent(id, title, description, attenders, timeStart, timeEnd);
        clndrService.printEvent(); // TO DO...
    }
}
