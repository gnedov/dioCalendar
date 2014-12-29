package com.agn.clndr.client;

import com.agn.clndrclient.CalendarClient;
import com.agn.cmdparser.ConsoleInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainClient {
    private static final Logger LOG = LoggerFactory.getLogger(MainClient.class);
    public static void main(String[] args) {
        LOG.info("Application started");
        CalendarClient calendarClient = new CalendarClientImpl();
        ConsoleInput consoleInput = new ConsoleInput(calendarClient);

        consoleInput.startConsole(args);

    }

}

