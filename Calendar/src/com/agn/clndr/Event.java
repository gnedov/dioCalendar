package com.agn.clndr;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;


public class Event {
    private final UUID id;
    private final String title;
    private final String description;
    private final List<String> attenders;
    private final GregorianCalendar timeStart;
    private final GregorianCalendar timeEnd;


    private Event(EvntBuilder eb) {
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
        return attenders;
    }

    public GregorianCalendar getTimeStart() {
        return timeStart;
    }

    public GregorianCalendar getTimeEnd() {
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

    public static class EvntBuilder {
        private UUID id;
        private String title;
        private String description;
        private List<String> attenders;
        private GregorianCalendar timeStart;
        private GregorianCalendar timeEnd;

        public EvntBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public EvntBuilder title(String title) {
            this.title = title;
            return this;
        }

        public EvntBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EvntBuilder attenders(List<String> attenders) {
            this.attenders = attenders;
            return this;
        }

        public EvntBuilder timeStart(GregorianCalendar timeStart) {
            this.timeStart = timeStart;
            return this;
        }

        public EvntBuilder timeEnd(GregorianCalendar timeEnd) {
            this.timeEnd = timeEnd;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}
