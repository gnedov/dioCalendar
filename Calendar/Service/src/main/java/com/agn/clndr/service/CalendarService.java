package com.agn.clndr.service;

import org.joda.time.DateTime;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface CalendarService extends Remote {

    void createEvent(UUID id, String title, String description, List<String> attenders,
                     DateTime timeStart, DateTime timeEnd) throws RemoteException;

    boolean deleteEvent(UUID eventId) throws RemoteException;

    void updateEvent(UUID eventId) throws RemoteException;

    Event getEventById(UUID eventId) throws RemoteException;

    Event getEventByTitle(String eventTitle) throws RemoteException;

    List<Event> getEventsOnWholeDay(DateTime timeDay) throws RemoteException;

    boolean isPersonBusyOnTime(String attender, DateTime concreteTime) throws RemoteException;

    List<Event> getEventsPersonInvolvedByTime(String attender, DateTime timeStart, DateTime timeEnd) throws RemoteException;

}
