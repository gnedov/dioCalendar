package com.agn.clndr.client;

import com.agn.clndr.service.CalendarService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.rmi.RemoteException;

public class MainClient {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("ClientApplicationContext.xml");
        CalendarService service = (CalendarService) context.getBean("calendarService");

        System.out.println("client started!");

    }

}

