package com.agn.clndr.service;

import org.joda.time.DateTime;

import java.nio.file.Path;
import java.util.Collection;
import java.util.UUID;

interface EventStorage {
    void addEvent(Event event);

    boolean removeEvent(Event event);

    int size();

    Collection<Event> findAllByTitle(String title);

    Collection<Event> findAllStartedByTimePeriod(DateTime start, DateTime end);

    Collection<Event> findAllEndedByTimePeriod(DateTime start, DateTime end);

    Collection<Event> findNextByDate(DateTime time);

    boolean isEventExist(UUID id);

    void addEventToStorage(Event event, Path xmlPath);
}