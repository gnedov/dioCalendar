package com.agn.clndr.service;

import org.joda.time.DateTime;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
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

    Event findById(UUID eventId);

    Collection<UUID> findEventsIdsStartedOnTimeRange(DateTime timeStart, DateTime timeEnd);

    Collection<UUID> findEventsIdsByAttender(String attender);

    Collection<Event> findEventsByIds(List<UUID> idsAttendersEvents);

    Collection<UUID> findEventsIdsEndedAfter(DateTime concreteTime);

    Collection<UUID> findEventsIdsStartedBefore(DateTime concreteTime);
}