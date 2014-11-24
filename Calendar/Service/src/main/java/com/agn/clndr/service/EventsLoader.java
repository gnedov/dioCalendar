package com.agn.clndr.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class EventsLoader {
    private final EventStorage eventStorage;

    public EventsLoader(EventStorage eventStorage) {
        this.eventStorage = eventStorage;
    }

    public void loadEvents() {

        DataHelper dataHelper = new DataHelper();
        Map<Path, Event> eventPathMap;
        Path path = null;
        LoadEventThread loadEventThread;

        eventPathMap = dataHelper.getEventsByPath(path);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Future> futureList = new ArrayList<>();

        for (Map.Entry<Path, Event> entry : eventPathMap.entrySet()) {
            loadEventThread = new LoadEventThread(entry.getKey(), entry.getValue());
            futureList.add(executorService.submit(loadEventThread));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        checkLoadingResult(futureList);
    }

    private void checkLoadingResult(List<Future> futureList) {
        int eventCount = 0;
        for (Future f : futureList) {
            try {
                if ((Boolean) f.get())
                    eventCount++;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Log: <" + eventCount + "> events were loaded.");
    }

    private class LoadEventThread implements Callable<Boolean> {
        private final Event event;
        private final Path path;

        private LoadEventThread(Path path, Event event) {
            this.event = event;
            this.path = path;
        }

        @Override
        public Boolean call() {
            if (!eventStorage.isEventExist(event.getId())) {
                eventStorage.addEventToStorage(event, path);
            } else
                return Boolean.FALSE;

            return Boolean.TRUE;
        }

    }
}
