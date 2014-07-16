package com.agn.clndr;

import org.joda.time.DateTime;

import java.util.Collection;

interface EventStore{
    void addEvent(Event event);
    Event removeEvent(Event event);
    int size();
    Collection<Event> findAllByTitle(String title);
    Collection <Event> findAllByTimePeriod(DateTime start, DateTime end);
    Event findNextByDate(DateTime time);
}