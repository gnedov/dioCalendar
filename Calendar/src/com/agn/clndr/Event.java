package com.agn.clndr;

import org.joda.time.DateTime;          //library to work with DateTime class

import java.util.List;
import java.util.UUID;


public class Event {
    private final UUID id;
    private final String title;
    private final String description;
    private final List<String> attenders;
    private final DateTime timeStart;
    private final DateTime timeEnd;


    private Event(EventBuilder eb) {
        this.id = eb.id;
        this.title = eb.title;
        this.description = eb.description;
        this.attenders = eb.attenders;
        this.timeStart = eb.timeStart;
        this.timeEnd = eb.timeEnd;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAttenders() {
        //[Oleg] maybe we need to use this:
        //[Oleg] return new ArrayList(attenders);
        return attenders;
    }

    public DateTime getTimeStart() {
        return timeStart;
    }

    public DateTime getTimeEnd() {
        return timeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (!id.equals(event.id)) return false;
        if (title != null ? !title.equals(event.title) : event.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", attenders=" + attenders +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                '}';
    }

    //local code review (vtegza): be explicit in names @ 20.07.14
    public static class EventBuilder {
        private UUID id;
        private String title;
        private String description;
        private List<String> attenders;
        private DateTime timeStart;
        private DateTime timeEnd;

        public EventBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public EventBuilder title(String title) {
            this.title = title;
            return this;
        }

        public EventBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventBuilder attenders(List<String> attenders) {
            this.attenders = attenders;
            return this;
        }

        public EventBuilder timeStart(DateTime timeStart) {
            this.timeStart = timeStart;
            return this;
        }

        public EventBuilder timeEnd(DateTime timeEnd) {
            this.timeEnd = timeEnd;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}
