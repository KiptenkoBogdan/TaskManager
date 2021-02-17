package ua.edu.sumdu.j2se.kiptenko.tasks.model;

import org.apache.log4j.Logger;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private static final Logger logger = Logger.getLogger(Task.class);
    private String title;
    private LocalDateTime time;
    private LocalDateTime start;
    private LocalDateTime end;
    private int interval;
    private boolean active;
    private boolean repeated;

    public Task (String title, LocalDateTime time) {
        if(title == null){
            logger.error("Title is empty.");
            throw new IllegalArgumentException("Title can not be empty");
        }else{
            this.title = title;
        }
        if(time == null){
            logger.error("Time is empty.");
            throw new IllegalArgumentException("Time can not be empty or negative");
        }
        else{
            this.time = time;
            this.start = time;
            this.end = time;
        }
        this.interval = 0;
    }

    public Task(String title, LocalDateTime start, LocalDateTime end, int interval) {
        this.repeated = true;

        if(title == null){
            logger.error("Title is empty.");
            throw new IllegalArgumentException("Title can not be empty");
        }else{
            this.title = title;
        }
        if(start == null || end == null){
            logger.error("Start and End time is empty or negative.");
            throw new IllegalArgumentException("Time can not be empty or negative");
        }
        else{
            this.time = start;
            this.start = start;
            this.end = end;
        }
        if(interval <= 0){
            logger.error("Interval is empty or negative.");
            throw new IllegalArgumentException("Interval can not be negative");
        }
        else{
            this.interval = interval;
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getTime() {
        if (isRepeated())
            return this.start;
        else
            return this.time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
        this.start = time;
        this.end = time;
        this.repeated = false;
    }

    public void setTime(LocalDateTime start, LocalDateTime end, int interval) {
        this.start = start;
        this.end = end;
        this.interval = interval;
        repeated = true;
    }

    public LocalDateTime getStartTime() {
        if (isRepeated())
            return this.start;
        else
            return this.time;
    }

    public LocalDateTime getEndTime() {
        if (isRepeated())
            return this.end;
        else
            return this.time;
    }

    public int getRepeatInterval() {
        if (isRepeated())
            return this.interval;
        else
            return 0;
    }

    public boolean isRepeated() {
        return this.repeated;
    }

    public LocalDateTime nextTimeAfter(LocalDateTime current) {
        if (current == null) {
            System.out.println("Wrong argument value");
            return null;
        } else if (active) {
            if (isRepeated()) {
                if (current.isBefore(start))
                    return start;
                else if (current.isAfter(start) || current.isEqual(start)){
                    if(current.plusSeconds(interval).isBefore(end)) {
                        LocalDateTime nextRepeat = start;
                        while (current.isAfter(nextRepeat) || current.isEqual(nextRepeat))
                            nextRepeat = nextRepeat.plusSeconds(interval);
                        return nextRepeat;
                    }
                } else
                    return null;
            } else if (current.isBefore(time))
                return time;
            else
                return null;
        } else
            return null;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if(this.hashCode() == o.hashCode()){
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Task task = (Task) o;
            return time == task.time &&
                    start == task.start &&
                    end == task.end &&
                    interval == task.interval &&
                    active == task.active &&
                    Objects.equals(title, task.title);
        } else{
            return false;
        }

    }

    @Override
    public String toString() {
        String line;
        line = "Task{" +
                "title='" + title + " | " +
                ", time=" + time +
                ", start=" + start +
                ", end=" + end +
                ", interval=" + interval +
                ", active=" + active +
                '}';
        return line;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, time, start, end, interval, active);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}