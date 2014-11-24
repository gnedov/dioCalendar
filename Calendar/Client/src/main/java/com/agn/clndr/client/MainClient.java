package com.agn.clndr.client;

import com.agn.clndrclient.CalendarClient;
import com.agn.cmdparser.ConsoleInput;

public class MainClient {
    public static void main(String[] args) {

        CalendarClient calendarClient = new CalendarClientImpl();
        ConsoleInput consoleInput = new ConsoleInput(calendarClient);

        consoleInput.startConsole(args);

    }

}

