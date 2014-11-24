package com.agn.clndr.client;

import com.agn.clndr.service.CalendarService;
import com.agn.clndrclient.CalendarClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CalendarClientImpl implements CalendarClient {
    private List<String> strList = new ArrayList<>();

    public CalendarClientImpl() {
        ApplicationContext context = new ClassPathXmlApplicationContext("ClientApplicationContext.xml");
        CalendarService service = (CalendarService) context.getBean("calendarService");
    }

    @Override
    public Collection getLastSearchResult() {
        return strList;
    }

    @Override
    public void searchAll() {
        strList.clear();
        strList.add("1aaa");
        strList.add("2bbb");
        strList.add("3cccc");
    }

    @Override
    public void searchByTitle(String s) {
        strList.clear();
        strList.add(s);
    }
}
