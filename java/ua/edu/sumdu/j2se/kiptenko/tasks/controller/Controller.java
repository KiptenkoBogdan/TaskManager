package ua.edu.sumdu.j2se.kiptenko.tasks.controller;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.kiptenko.tasks.Main;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.Task;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.TaskIO;
import ua.edu.sumdu.j2se.kiptenko.tasks.view.View;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Controller{
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final Logger logger = Logger.getLogger(Controller.class);
    private final static long TIMER = 300000; // 5 MIN
    private AbstractTaskList model;
    private View view;
    Map<Integer, Runnable> menuAction = new HashMap<>();
    Map<Integer, Consumer<Task>> editAction  = new HashMap<>();

    public Controller(AbstractTaskList list, View view) {
        model = list;
        this.view = view;
        actionMenuInit();
    }

    public AbstractTaskList getModel(){
        return this.model;
    }

    public View getView(){
        return this.view;
    }

    public void getList() {
        view.showTaskList(model);
    }

    public void execute() {
        updateTaskStatus(model);
        while (true) {
            view.showMenu();
            String line = view.getOption();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (Exception e) {
                continue;
            }
            if (choice == 0) {
                view.print("Do you want to exit? (Yes/No): ");
                if (view.checkUserAnswer()) {
                    view.println("Closing program!");
                    break;
                }
            } else {
                Runnable action = menuAction.get(choice);
                if (action != null) {
                    action.run();
                } else {
                    view.println("Choose existing option (0-5)");
                }
            }
        }
    }

    public void addTask() {
        Task task;
        String title = view.getTitle();
        int repeated = view.getIsTaskRepeated();
        if (repeated == 2) {
            while (true) {
                view.print("\nStart date of the period: ");
                LocalDateTime start = view.parseDateTime();
                view.print("\nEnd date of the period: ");
                LocalDateTime end = view.parseDateTime();
                int interval = view.getInterval();
                if (!((start.isAfter(end) || start.isEqual(end)) &&
                        start.plusSeconds(interval).isAfter(end))) {
                    task = new Task(title, start, end, interval);
                    task.setActive(true);
                    model.add(task);
                    view.displayTaskInfo(task);
                    view.print("The task was successfully added to the list.");
                    break;
                } else {
                    view.print("Wrong time period. Try again!");
                }
            }
        } else {
            view.print("\nTask completion time: ");
            LocalDateTime time = view.parseDateTime();
            task = new Task(title, time);
            task.setActive(true);
            model.add(task);
            view.displayTaskInfo(task);
            view.print("The task was successfully added to the list.");
            logger.info("The task: " + task.getTitle() + " was successfully added to the list.");
        }
        TaskIO.saveToFile(model, Main.SAVE_FILE);
    }

    public void editTask() {
        int index = view.selectTask(model) - 1;
        Task task = model.getTask(index);
        LocalDateTime start, end;
        actionEditTaskInit();
        while (true) {
            view.showEditMenu();
            String line = view.getOption();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (Exception e) {
                view.print("Please enter a number: \n");
                continue;
            }
            if (choice == 0) {
                view.print("Do you want to return to previous menu? (Yes/No): ");
                if (view.checkUserAnswer()) {
                    break;
                }
            } else{
                Consumer<Task> action = editAction.get(choice);
                if (action != null) {
                    action.accept(task);
                } else{
                    view.println("Choose existing option (0-5)");
                }
            }
        }
        view.println("The task has been successfully changed!");
        logger.info("The task: " + task.getTitle() + "has been successfully changed!");
    }

    public void editTitle(Task task){
        task.setTitle(view.getTitle());
        view.print("Title was changed to: " + task.getTitle());
    }
    public void editStatus(Task task){
        boolean aBoolean = view.getActiveStatus();
        task.setActive(aBoolean);
        view.print("Activity status was changed to: " + task.isActive());
    }
    public void editTime(Task task){
        task.setTime(view.parseDateTime());
        view.print("Time was changed to: " + task.getTime());
    }
    public void editRepeatableTime(Task task){
        LocalDateTime start, end;
        while (true) {
            view.print("Enter start time: ");
            start = view.parseDateTime();
            view.print("Enter end time: ");
            end = view.parseDateTime();
            view.print("Enter interval time: ");
            int interval = view.getInterval();

            if (!((start.isAfter(end) || start.isEqual(end)) &&
                    start.plusSeconds(interval).isAfter(end))) {
                task.setTime(start, end, interval);
                view.print("Start time was changed to: " + task.getStartTime() +
                        "\nEnd time was changed to: " + task.getEndTime() +
                        "\nInterval was changed to: " + task.getRepeatInterval());
                break;
            } else {
                view.println("Wrong time period. Please try again.");
            }
        }
    }

    private void actionMenuInit(){
        menuAction.put(1, () -> {
            getList();
        });
        menuAction.put(2,() -> {
            addTask();
        });
        menuAction.put(3,() -> {
            editTask();
        });
        menuAction.put(4,() -> {
            removeTask();
        });
        menuAction.put(5,() -> {
            getCalendar();
        });
    }

    public void actionEditTaskInit(){
        editAction.put(1, (t) -> {
            editTitle(t);
        });
        editAction.put(2, (t) -> {
            editStatus(t);
        });
        editAction.put(3, (t) -> {
            editTime(t);
        });
        editAction.put(4, (t) -> {
            editRepeatableTime(t);
        });
    }

    public void removeTask() {
        int taskID = view.removeTask(model);
        Task task;
        if (taskID == 0) {
            view.print("The task list is empty! Add at least one task.");
        } else if (taskID == -1) {
            view.print("You did not confirm the delete operation.");
        } else if (taskID > 0) {
            task = model.getTask(taskID - 1);
            model.remove(task);
            view.print("Task №: " + taskID + " was deleted!");
            logger.info("Task №: " + taskID + " was deleted!");
            TaskIO.saveToFile(model, Main.SAVE_FILE);
        }
    }

    public void getCalendar() {
        view.getCalendar(model);
    }

    public void updateTaskStatus(AbstractTaskList tasks){
        LocalDateTime currTime = LocalDateTime.now();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.getTask(i);
            if(task.nextTimeAfter(currTime) == null){
                task.setActive(false);
            }
        }
    }

    public String checkNextTime(AbstractTaskList tasks){
        LocalDateTime currTime = LocalDateTime.now();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.getTask(i);
            if(task.isActive()){
                //logger.info("Task " + task.getTitle() + " is active");
                if(task.nextTimeAfter(currTime) != null){
                    //logger.info(task.nextTimeAfter(currTime));
                    return task.nextTimeAfter(currTime).format(formatter);
                } else{
                    return null;
                }
            }
        }
        return null;
    }
}