package ua.edu.sumdu.j2se.kiptenko.tasks;

public class Task {

    private String title;
    private int time;
    private int start;
    private int end;
    private int interval;
    private boolean active;
    private boolean repeated;

    public Task(String title, int time) {
        this.title = title;
        this.time = time;
        this.start = time;
        this.end = time;
        this.interval = 0;
    }

    public Task(String title, int start, int end, int interval) {
        this.title = title;
        this.time = start;
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.repeated = true;
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

    public int getTime() {
        if (isRepeated())
            return this.start;
        else
            return this.time;
    }

    public void setTime(int time) {
        this.time = time;
        this.start = time;
        this.end = time;
        this.repeated = false;
    }

    public void setTime(int start, int end, int interval) {
        this.start = start;
        this.end = end;
        this.interval = interval;
        repeated = true;
    }

    public int getStartTime() {
        if (isRepeated())
            return this.start;
        else
            return this.time;
    }

    public int getEndTime() {
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

    public int nextTimeAfter(int current) {
        if (current < 0) {
            System.out.println("Wrong argument value");
            return -1;
        } else if (active) {
            if (isRepeated()) {
                if (current < start)
                    return start;
                else if (current >= start && current + interval < end){
                    int nextRepeat = start;
                    while (current >= nextRepeat)
                        nextRepeat += interval;
                    return nextRepeat;
                } else
                    return -1;
            } else if (current < time)
                return time;
            else
                return -1;
        } else
            return -1;
    }
}