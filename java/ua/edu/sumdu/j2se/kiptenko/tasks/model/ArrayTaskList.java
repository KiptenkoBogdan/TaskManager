package ua.edu.sumdu.j2se.kiptenko.tasks.model;

import org.apache.log4j.Logger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public class ArrayTaskList extends AbstractTaskList{
    private static final Logger logger = Logger.getLogger(ArrayTaskList.class);
    private Task[] array;
    private int size;

    public ArrayTaskList(){
        array = new Task[10];
        size = 0;
    }

    public void add(Task task) throws IllegalArgumentException{
        if (task == null) {
            logger.error("Task is empty");
            throw new IllegalArgumentException("Task can`t` be empty!");
        }
        if (size < array.length) {
            array[size++] = task;
        } else {
            Task[] tempArray = new Task[array.length * 2];
            System.arraycopy(array, 0, tempArray, 0, array.length);
            array = tempArray;
            array[size++] = task;
        }
    }

    public boolean remove(Task task){
        if(task == null){
            return false;
        } else {
            for(int i = 0; i < size; i++){
                if(task.equals(array[i])){
                    for(int j = i; j < size; j++){
                        array[j] = array[j+1];
                    }
                    Task[] tempArray = new Task[array.length -1];
                    System.arraycopy(array, 0, tempArray, 0, tempArray.length);
                    array = tempArray;
                    size--;
                    return true;
                }
            }
            return false;
        }
    }

    public int size(){
        return size;
    }

    public Task getTask (int index) throws IndexOutOfBoundsException{
        if(index < 0 || index >= array.length){
            logger.error("Entered wrong index");
            throw new IndexOutOfBoundsException("Index out of bounds");
        } else{
            return array[index];
        }
    }

    @Override
    public boolean equals(Object o){
        if(this.hashCode() == o.hashCode()) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            ArrayTaskList list = (ArrayTaskList) o;
            if (list.size() == this.size()) {
                for (int i = 0; i < this.size(); i++) {
                    if (!this.array[i].equals(list.array[i])) {
                        return false;
                    }
                }
            } else {
                return false;
            }
            return true;
        } else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    @Override
    public String toString(){
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            Task tempTask = array[i];
            line.append(tempTask.toString());
        }
        return line.toString();
    }

    @Override
    public ArrayTaskList clone() throws CloneNotSupportedException {
        ArrayTaskList list = (ArrayTaskList) super.clone();
        list.array = Arrays.copyOf(array, size);
        return list;
    }
//    @Override
//    protected ArrayTaskList clone(){
//        ArrayTaskList list = new ArrayTaskList();
//        for (int i = 0; i < this.size(); i++) {
//            list.add(this.getTask(i));
//        }
//        return list;
//    }

    @Override
    public Iterator<Task> iterator(){
        Iterator<Task> iterator = new Iterator<Task>() {
            private int lastRet = -1; // index of last element returned; -1 if no such element found
            private int currentIndex = 0;

            @Override
            public boolean hasNext(){
                return currentIndex < size() && array[currentIndex] != null;
            }

            @Override
            public Task next(){
                lastRet = currentIndex;
                return array[currentIndex++];
            }

            @Override
            public void remove(){
                if (currentIndex > 0) {
                    ArrayTaskList.this.remove(array[lastRet]);
                    currentIndex--;
                }else {
                    throw new IllegalStateException();
                }
            }
        };
        return iterator;
    }

    @Override
    public Stream<Task> getStream() {
        return Stream.of(Arrays.copyOf(array, size));
    }
    //    public  ArrayTaskList incoming(int from, int to) {
//        if (from < 0 || to < 0) {
//            throw new IllegalArgumentException("Time can`t` be negative!");
//        }
//
//        if (to - from < 0) {
//            throw new IllegalArgumentException("'From' cannot be less then 'to'!");
//        }
//        ArrayTaskList list = new ArrayTaskList();
//        for (int i = 0; i < size; i++) {
//            if (array[i].nextTimeAfter(from) <= to && array[i].nextTimeAfter(from) != -1) {
//                list.add(array[i]);
//            }
//        }
//        return list;
//    }
}

