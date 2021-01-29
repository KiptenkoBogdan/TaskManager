package ua.edu.sumdu.j2se.kiptenko.tasks.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Tasks implements Serializable {
    public static Iterable<Task> incoming(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end) {
        AbstractTaskList result = TaskListFactory.createTaskList(ListTypes.types.ARRAY);

        for (Task task : tasks) {
            if (task.nextTimeAfter(start) != null && task.nextTimeAfter(start).compareTo(end) <= 0) {
                Objects.requireNonNull(result).add(task);
            }
        }
        return result;
    }

    public static SortedMap<LocalDateTime, Set<Task>> calendar(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end) {
        SortedMap<LocalDateTime, Set<Task>> sortedMap = new TreeMap<>();
        Iterable<Task> taskIterable = incoming(tasks, start, end);

        for(Task task : taskIterable) {
            LocalDateTime nextTime = task.nextTimeAfter(start);
            while (nextTime != null && (nextTime.isBefore(end) || nextTime.isEqual(end))) {
                if (!sortedMap.containsKey(nextTime)) {
                    Set<Task> taskSet = new HashSet<>();
                    taskSet.add(task);
                    sortedMap.put(nextTime, taskSet);
                } else {
                    sortedMap.get(nextTime).add(task);
                }
                nextTime = task.nextTimeAfter(nextTime);
            }
        }
        return sortedMap;
    }
}
