package ua.edu.sumdu.j2se.kiptenko.tasks.model;

import org.apache.log4j.Logger;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public class LinkedTaskList extends AbstractTaskList{
    private static final Logger logger = Logger.getLogger(LinkedTaskList.class);
    private Node first;
    private Node last;
    private int size;

    public LinkedTaskList() {
        first = null;
        last = null;
        size = 0;
    }

    public void add(Task task) throws IllegalArgumentException{
        if (task == null) {
            logger.error("Task is empty");
            throw new IllegalArgumentException("Task can`t` be empty!");
        }

        Node newLast = new Node(task);
        if (first == null) {
            first = newLast;
            last = newLast;
        } else {
            last.setNext(newLast);
            last = newLast;
        }
        size++;
    }

    public boolean remove(Task task) {
        if (task == null || first == null) {
            return false;
        } else {
            Node previous = first;
            for(Node i = first; i != null; i = i.getNext())
            {
                if (task.equals(i.getValue())) {
                    if(i.equals(first)) {
                        first = first.getNext();
                        if (size == 1) {
                            last = first;
                        }
                    } else if (i.equals(last)) {
                        previous.setNext(null);
                        last = previous;
                    } else {
                        previous.setNext(i.getNext());
                    }
                    size--;
                    return true;
                }
                previous = i;
            }
            return false;
        }
    }

    public int size() {
        return size;
    }

    public Task getTask(int index) {
        if (index < 0 || index >= size) {
            logger.error("Entered wrong index");
            throw new IndexOutOfBoundsException("Incorrect index of element");
        } else {
            Node element = first;
            for (int i = 0; i != index; i++) {
                element = element.getNext();
            }
            return element.getValue();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        LinkedTaskList list = (LinkedTaskList) o;
        if(list.size() == this.size()) {
            for (int i = 0; i < this.size(); i++) {
                if (!this.getTask(i).equals(list.getTask(i))) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, last, size);
    }

    @Override
    public String toString() {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            Task tempTask = getTask(i);
            line.append(tempTask.toString());
        }
        return line.toString();
    }

    @Override
    protected LinkedTaskList clone() throws CloneNotSupportedException {
        LinkedTaskList list = (LinkedTaskList) super.clone();
        for (int i = 0; i < this.size(); i++) {
            list.add(this.getTask(i));
        }
        return list;
    }

    @Override
    public Iterator<Task> iterator() {
        Iterator<Task> iterator = new Iterator<Task>() {
            private int lastReturned = -1;
            private int current = 0;


            @Override
            public boolean hasNext() {
                return current < size() && getTask(current) != null;
            }

            @Override
            public Task next() {
                lastReturned = current;
                return getTask(current++);
            }

            @Override
            public void remove() throws IllegalStateException {
                if (current > 0) {
                    LinkedTaskList.this.remove(getTask(lastReturned));
                    current--;
                }else {
                    throw new IllegalStateException();
                }
            }
        };
        return iterator;
    }

    @Override
    public Stream<Task> getStream() {
        Task[] result = new Task[size];
        int i = 0;
        for (Node x = last; x != null; x = x.next)
            result[i++] = x.value;
        return Stream.of(result);
    }
//    public LinkedTaskList incoming(int from, int to) {
//
//        if (from < 0 || to < 0) {
//            throw new IllegalArgumentException("Time can`t` be negative!");
//        }
//
//        if (to - from < 0) {
//            throw new IllegalArgumentException("'From' cannot be less then 'to'!");
//        }
//
//        LinkedTaskList list = new LinkedTaskList();
//
//        for (Node i = first; i != null; i = i.getNext()) {
//            if (i.getValue().nextTimeAfter(from) <= to
//                    && i.getValue().nextTimeAfter(from) != -1) {
//                list.add(i.getValue());
//            }
//        }
//        return list;
//    }

    private static class Node {
        private Task value;
        private Node next;

        public Node(Task value) {
            this.value = value;
            next = null;
        }

        public Task getValue() {
            return value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }
}
