package com.agn.clndr;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.Map;

import static java.nio.file.FileVisitResult.*;

public class DataHelper {
    public static final String APP_DATA_DIRECTORY = "./xmldata";
    private static final String APP_TEMPLATES_DIRECTORY = "./xsd_templates";
    private static final String EVENT_ADAPTER_XSD_TEMPLATE = "eventAdapterXSD.xsd";
    private static final String FILE_PATTERN = "*.xml";
    private Validator validator;

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

    private void setupXMLValidator() {

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = null;
        try {
            schema = sf.newSchema(new File(APP_TEMPLATES_DIRECTORY, EVENT_ADAPTER_XSD_TEMPLATE));
        } catch (SAXException e) {
            System.out.println("SAXException: ");
            System.out.println("schema file <" + EVENT_ADAPTER_XSD_TEMPLATE + "> is not valid.");
            // e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (schema != null) {
            validator = schema.newValidator();
        }
    }

    private boolean validateXMLFile(Path p) {
        boolean validateOK = false;
        try {
            validator.validate(new StreamSource(p.toFile()));
            validateOK = true;
        } catch (SAXException e) {
            System.out.println("SAXException: ");
            System.out.println("file <" + p.getFileName() + "> is not valid.");
            // e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return validateOK;
    }

    private Event getEventFromFile(Path path) {
        EventAdapter eventAdapter = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(EventAdapter.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            eventAdapter = (EventAdapter) jaxbUnmarshaller.unmarshal(path.toFile());
            System.out.println(eventAdapter);
        } catch (JAXBException e) {
            System.out.println("Something wrong with unmarshalling: ");
            e.printStackTrace();
        }
        if (eventAdapter != null) {
            return new Event.EventBuilder(eventAdapter).build();
        }
        return null;
    }

    public Map<Path, Event> getEventsByPath(Path path) {
        Map<Path, Event> eventPathMap = new HashMap<>();
        List<Path> pathList;
        String pattern = FILE_PATTERN;
        Path startingDir;

        startingDir = path;
        if (path == null) {
            startingDir = Paths.get(APP_DATA_DIRECTORY);
        }
        Finder finder = new Finder(pattern);
        try {
            Files.walkFileTree(startingDir, finder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pathList = finder.done();
        if (pathList.size() > 0) {
            setupXMLValidator();
            for (Path p : pathList) {
                Event ev = null;
                if (validateXMLFile(p)) {
                    ev = getEventFromFile(p);
                }
                if (ev != null)
                    eventPathMap.put(p, ev);
            }
        }
        return eventPathMap;
    }
}
