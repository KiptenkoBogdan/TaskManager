package ua.edu.sumdu.j2se.kiptenko.tasks.view;

import ua.edu.sumdu.j2se.kiptenko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.Task;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.Tasks;
import ua.edu.sumdu.j2se.kiptenko.tasks.util.TaskTable;
import ua.edu.sumdu.j2se.kiptenko.tasks.util.TimeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;

public class View {
    private Scanner scanner = new Scanner(System.in);

    public View() {
    }

    public void print(String prompt) {
        System.out.print(prompt);
    }

    public void println(String prompt) {
        System.out.println(prompt);
    }

    public void showMenu() {
        println("\n============ Task Manager ===========");
        println("1. Show task list");
        println("2. Add Task");
        println("3. Edit Task");
        println("4. Delete Task");
        println("5. Show Calendar");
        println("0. Exit");
        println("=====================================");
        print("Enter your choice: ");
    }

    public void showEditMenu() {
        println("\n============= Task editing menu =============");
        println("1. Edit title");
        println("2. Edit task status");
        println("3. Edit time or make task non-repeating");
        println("4. Edit time/interval or make task repeating");
        println("0. Return to main menu");
        println("=============================================");
        print("Enter your choice: ");
    }

    public void showTaskList(AbstractTaskList tasks) {
        if (tasks.size() == 0) {
            emptyList();
        } else {
            notEmptyList(tasks);
        }
    }

    public int selectTask(AbstractTaskList list) {
        int id;
        if (list.size() == 0) {
            emptyList();
            return 0;
        } else {
            notEmptyList(list);
            print("Enter the task number that you want to edit: ");
            id = readInt(1, list.size());
        }
        return id;
    }

    public int removeTask(AbstractTaskList list) {
        if (list.size() == 0) {
            emptyList();
            return 0;
        } else {
            notEmptyList(list);
            print("Enter the task number you want to remove: ");
            int id = readInt(1, list.size());
            print("To remove the task, enter \"Yes\"" + "\nDo you want to exit(Yes/No)? ");
            if (checkUserAnswer()) {
                return id;
            }
        }
        return -1;
    }

    public void getCalendar(AbstractTaskList list) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        while (true) {
            print("Enter start date of the period: ");
            LocalDateTime start = parseDateTime();
            print("Enter end date of the period: ");
            LocalDateTime end = parseDateTime();

            if (!start.isAfter(end)) {
                SortedMap<LocalDateTime, Set<Task>> calendar = Tasks.calendar(list, start, end);
                println("*** UPCOMING CALENDAR ***");
                if (calendar.isEmpty()) {
                    println("There are no upcoming tasks");
                } else {
                    println("Tasks for the selected period: ");
                    for (Map.Entry<LocalDateTime, Set<Task>> entry : calendar.entrySet()) {
                        String date = entry.getKey().format(formatter);
                        println("Date: " + date);
                        for (Task task : entry.getValue()) {
                            println("\tTask: " + task.getTitle());
                        }
                    }
                }
                break;
            } else {
                print("Wrong parameters! Enter the correct time period for the calendar!");
            }
        }
    }

    public String getTitle() {
        String title = "";
        print("Enter task name: ");
        while (title.isEmpty()) {
            title = scanner.nextLine();
            if (title.isEmpty()) {
                print("Task name2" +
                        " is empty. Please enter correct value: ");
            }
        }
        return title;
    }

    public boolean getActiveStatus() {
        String s = "";
        print("Enter activity status (true/false): ");
        while (!(s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"))) {
            s = scanner.nextLine();
            if (s.isEmpty()) {
                print("Activity status is empty. Please enter correct value: ");
            } else if (!(s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"))) {
                print("Incorrect status. Please enter correct value: ");
            }
        }
        return Boolean.parseBoolean(s);
    }

    public int getIsTaskRepeated() {
        int choice;
        do {
            print("Set task to be non-repeating [1] or set task to be repeating [2]: ");
            choice = readInt(1, 2);
            if (choice == 1) {
                print("You selected non-repeating task!");
                return choice;
            } else if (choice == 2) {
                print("You selected repeating task!");
                return choice;
            } else if (choice == -1) {
                break;
            }
        } while (choice != -1);
        return choice;
    }

    public int getInterval() {
        print("Enter the interval in minutes: ");
        return readInt(1, Integer.MAX_VALUE) * 60;
    }

    public LocalDateTime parseDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String date = "";
        LocalDateTime time;
        print("Enter the date in the following format: dd-MM-yyyy HH:mm: ");
        while (true) {
            try {
                date = scanner.nextLine();
                time = LocalDateTime.parse(date, formatter);
                break;
            } catch (DateTimeParseException e) {
                print("Incorrect! Please enter the date of the following time: dd-MM-yyyy HH:mm: ");
            }
        }
        return time;
    }

    public void displayTaskInfo(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        StringBuilder builder = new StringBuilder();
        builder.append("Task title: " + task.getTitle());
        if (!task.isRepeated()) {
            builder.append(", time: " + task.getTime().format(formatter));
        } else {
            builder.append(", start time: " + task.getStartTime().format(formatter)
                    + ", end time: " + task.getEndTime().format(formatter))
                    .append(", interval: " + TimeConverter.SecondsToDays(task.getRepeatInterval()));
        }
        builder.append(", active: " + task.isActive() + ".");
        println(builder.toString());
    }


    public boolean checkUserAnswer() {
        return userAnswer().equalsIgnoreCase("yes");
    }

    private void emptyList() {
        print("The task list is empty! Please create at least one task.\n");
    }

    private void notEmptyList(AbstractTaskList tasks) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        TaskTable st = new TaskTable();
        st.setShowVerticalLines(true);
        st.setHeaders("№", "Title", "Start time", "End time", "Interval", "Status");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.getTask(i);
            String title = task.getTitle();
            String time = task.getTime().format(formatter);
            String start = task.getStartTime().format(formatter);
            String end = task.getEndTime().format(formatter);
            String interval = TimeConverter.SecondsToDays(task.getRepeatInterval());
            String active = task.isActive() ? "active" : "inactive";

            if (task.isRepeated()) {
                st.addRow(("" + (i + 1)), title, start, end, interval, active);
            } else if (!task.isRepeated()) {
                st.addRow(("" + (i + 1)), title, time, "   -", "non-repeating", active);
            }
        }
        st.print();
    }

    private String userAnswer() {
        String userAnswer = scanner.nextLine();
        while (!userAnswer.equalsIgnoreCase("yes") & !userAnswer.equalsIgnoreCase("no")) {
            println("You should select [yes] or [no]: ");
            userAnswer = scanner.nextLine();
        }
        return userAnswer;
    }

    private int readInt(int min, int max) {
        while (true) {
            String line = scanner.nextLine();

            if (line.equalsIgnoreCase("quit")) {
                return -1;
            }
            int n = Integer.parseInt(line);
            if (n >= min && n <= max) {
                return n;
            }
            print("Please, enter correct value or [quit] to cancel: ");
        }
    }
}