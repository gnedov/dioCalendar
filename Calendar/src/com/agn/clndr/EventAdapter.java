package com.agn.clndr;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement
@XmlType(name = "event")
public class EventAdapter implements Serializable {

    private String id;
    private String title;
    private String description;
    private List<String> attenders;
    private String timeStart;
    private String timeEnd;

    public EventAdapter() {
    }

    public EventAdapter(Event event) {
        this.id = event.getId().toString();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.attenders = new ArrayList<>();
        if (event.getAttenders() != null) {
            for (String att : event.getAttenders())
                this.attenders.add(att);
        }
        this.timeStart = event.getTimeStart().toString();
        this.timeEnd = event.getTimeEnd().toString();
    }

    @XmlElement(name = "eventId")
    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    @XmlElement(name = "event")
    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "startDate")
    public String getStartTime() {
        return timeStart;
    }

    public void setStartTime(String startTime) {
        this.timeStart = startTime;
    }

    @XmlElement(name = "endDate")
    public String getEndTime() {
        return timeEnd;
    }

    public void setEndTime(String endTime) {
        this.timeEnd = endTime;
    }

    @XmlElement(name = "attenders")
    public List<String> getAttenders() {
        return attenders;
    }

    public void setAttenders(List<String> attenders) {
        this.attenders = attenders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventAdapter that = (EventAdapter) o;

        if (attenders != null ? !attenders.equals(that.attenders) : that.attenders != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (attenders != null ? attenders.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventAdapter{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", attenders=" + attenders +
                '}';
    }
}
