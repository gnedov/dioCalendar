package com.agn.clndr;

import org.joda.time.DateTime;

import java.util.Collection;

interface EventStorage {
    void addEvent(Event event);
    boolean removeEvent(Event event);
    int size();
    Collection <Event> findAllByTitle(String title);
    Collection <Event> findAllStartedByTimePeriod(DateTime start, DateTime end );
    Collection <Event> findAllEndedByTimePeriod(DateTime start, DateTime end);
    Collection <Event> findNextByDate(DateTime time);

}