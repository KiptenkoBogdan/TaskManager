package ua.edu.sumdu.j2se.kiptenko.tasks.controller;

import ua.edu.sumdu.j2se.kiptenko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.kiptenko.tasks.view.View;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.Task;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Controller {
    private AbstractTaskList model;
    private View view;
    private Scanner scanner;

    public Controller(AbstractTaskList list, View view) {
        model = list;
        this.view = view;
        scanner = new Scanner(System.in);
    }

    public void execute() {
        while (true) {
            view.showMenu();
            String line = scanner.nextLine();
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
                switch (choice) {
                    case 1:
                        getList();
                        break;
                    case 2:
                        addTask();
                        break;
                    case 3:
                        editTask();
                        break;
                    case 4:
                        removeTask();
                        break;
                    case 5:
                        getCalendar();
                        break;
                    default:
                }
            }
        }
    }

    public void getList() {
        view.showTaskList(model);
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
        }
    }

    public void editTask() {
        int index = view.selectTask(model) - 1;
        Task task = model.getTask(index);
        LocalDateTime start, end;
        while (true) {
            view.showEditMenu();
            String line = scanner.nextLine();
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
            } else {
                switch (choice) {
                    case 1:
                        task.setTitle(view.getTitle());
                        view.print("Title was changed to: " + task.getTitle());
                        break;
                    case 2:
                        boolean aBoolean = view.getActiveStatus();
                        task.setActive(aBoolean);
                        view.print("Activity status was changed to: " + task.isActive());
                        break;
                    case 3:
                        task.setTime(view.parseDateTime());
                        view.print("Time was changed to: " + task.getTime());
                        break;
                    case 4:
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
                        break;
                    default:
                }
            }
        }
        view.println("The task has been successfully changed!");
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
        }
    }

    public void getCalendar() {
        view.getCalendar(model);
    }
}