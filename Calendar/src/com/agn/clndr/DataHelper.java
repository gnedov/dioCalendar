package com.agn.clndr;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

import static java.nio.file.FileVisitResult.*;

public class DataHelper {
    private static final String APP_DATA_DIRECTORY = "./xmldata";
    private static final String FILE_PATTERN = "*.xml";

    public static class Finder
            extends SimpleFileVisitor<Path> {

        private final PathMatcher matcher;
        private int numMatches = 0;
        private List<Path> pathList = new ArrayList<>();

        Finder(String pattern) {
            matcher = FileSystems.getDefault()
                    .getPathMatcher("glob:" + pattern);
        }

        // Compares the glob pattern against
        // the file or directory name.
        void find(Path file) {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
                numMatches++;
                pathList.add(file);
                System.out.println(file);
            }
        }

        // Prints the total number of
        // matches to standard out.
        List<Path> done() {
            System.out.println("Matched: "
                    + numMatches);
            return pathList;
        }

        // Invoke the pattern matching
        // method on each file.
        @Override
        public FileVisitResult visitFile(Path file,
                                         BasicFileAttributes attrs) {
            find(file);
            return CONTINUE;
        }

        // Invoke the pattern matching
        // method on each directory.
        @Override
        public FileVisitResult preVisitDirectory(Path dir,
                                                 BasicFileAttributes attrs) {
            find(dir);
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file,
                                               IOException exc) {
            System.err.println(exc);
            return CONTINUE;
        }
    }

    private Event getEventFromFile(Path path) {
        EventAdapter eventAdapter = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(EventAdapter.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            eventAdapter = (EventAdapter) jaxbUnmarshaller.unmarshal(path.toFile());
            System.out.println(eventAdapter);
        } catch (JAXBException e) {
            System.out.print("Something wrong with unmarshalling: ");
            e.printStackTrace();
        }
        if (eventAdapter != null) {
            return new Event.EventBuilder(eventAdapter).build();
        }
        return null;
    }

    public List<Event> getEventsByPath(Path path) {
        List<Event> eventList = new ArrayList<>();
        List<Path> pathList;
        String pattern = FILE_PATTERN;
        Path startingDir;

        startingDir = path;
        if (path == null) {
            startingDir = Paths.get(APP_DATA_DIRECTORY);
        }
        Finder finder = new Finder(pattern);
        try{
        Files.walkFileTree(startingDir, finder);
        } catch (IOException e ){
            e.printStackTrace();
        }
        pathList = finder.done();

        for (Path p : pathList) {
            eventList.add(getEventFromFile(p));
        }
        return eventList;
    }
}
