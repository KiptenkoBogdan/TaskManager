package ua.edu.sumdu.j2se.kiptenko.tasks.model;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public abstract class AbstractTaskList implements Iterable<Task>, Cloneable{
    public abstract void add(Task task);
    public abstract boolean remove(Task task);
    public abstract int size();
    public abstract Task getTask(int index);

    public abstract Stream<Task> getStream();

//    public AbstractTaskList incoming(int from, int to){
//
//        if (from < 0 || to < 0) {
//            throw new IllegalArgumentException("Time can`t` be negative!");
//        }
//
//        if (to - from < 0) {
//            throw new IllegalArgumentException("'From' cannot be less then 'to'!");
//        }
//
//        if(this instanceof ArrayTaskList){
//            AbstractTaskList list = TaskListFactory.createTaskList(ListTypes.types.ARRAY);
//            for (int i = 0; i < size(); i++) {
//                if (getTask(i).nextTimeAfter(from) <= to && getTask(i).nextTimeAfter(from) != -1) {
//                    list.add(getTask(i));
//                }
//            }
//            return list;
//        } else if(this instanceof LinkedTaskList){
//            AbstractTaskList list = TaskListFactory.createTaskList(ListTypes.types.LINKED);
//            for (int i = 0; i < size(); i++) {
//                if (getTask(i).nextTimeAfter(from) <= to && getTask(i).nextTimeAfter(from) != -1) {
//                    list.add(getTask(i));
//                }
//            }
//            return list;
//        }
//        return null;
//    }

    public final AbstractTaskList incoming(LocalDateTime from, LocalDateTime to) {
        AbstractTaskList incomingTasks;
        String clazz = this.getClass().getSimpleName();
        if (clazz.equals("ArrayTaskList")) {
            incomingTasks = TaskListFactory.createTaskList(
                    ListTypes.types.ARRAY);
        } else if (clazz.equals("LinkedTaskList")) {
            incomingTasks = TaskListFactory.createTaskList(
                    ListTypes.types.LINKED);
        } else {
            return null;
        }
        if (incomingTasks != null) {
            this.getStream()
                    .filter(t -> t != null && t.isActive()
                            && t.nextTimeAfter(from).isAfter(from)
                            && t.nextTimeAfter(to).isBefore(to))
                    .forEach(incomingTasks::add);
        }
        return incomingTasks;
    }
}
