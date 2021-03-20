package ua.edu.sumdu.j2se.kiptenko.tasks.controller;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.kiptenko.tasks.Main;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.Task;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.TaskIO;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.Tasks;
import ua.edu.sumdu.j2se.kiptenko.tasks.view.View;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class Controller extends Thread{
    private static final Logger logger = Logger.getLogger(Controller.class);
    private final static long TIMER = 300000; // 5 MIN
    private AbstractTaskList model;
    private View view;

    public Controller(AbstractTaskList list, View view) {
        model = list;
        this.view = view;
    }

    public void execute(){
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
            logger.info("The task: " + task.getTitle() + " was successfully added to the list.");
        }
        TaskIO.saveToFile(model, Main.SAVE_FILE);
    }

    public void editTask() {
        int index = view.selectTask(model) - 1;
        Task task = model.getTask(index);
        LocalDateTime start, end;
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
                TaskIO.saveToFile(model, Main.SAVE_FILE);
            }
        }
        view.println("The task has been successfully changed!");
        logger.info("The task: " + task.getTitle() + "has been successfully changed!");
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

    // system tray notification popup
//    public void getNotification() throws AWTException {
//        if (SystemTray.isSupported()) {
//            SystemTray tray = SystemTray.getSystemTray();
//
//            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("images/tray.gif");
//            TrayIcon trayIcon = new TrayIcon(image);
//            tray.add(trayIcon);
//            trayIcon.displayMessage("Test.", "This is a message to test notifications in Windows 10",
//                    TrayIcon.MessageType.INFO);
//        }
//    }
//    public void displayTray() throws AWTException {
//        //Obtain only one instance of the SystemTray object
//        SystemTray tray = SystemTray.getSystemTray();
//
//        //If the icon is a file
//        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
//        //Alternative (if the icon is on the classpath):
//        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));
//
//        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
//        //Let the system resize the image if needed
//        trayIcon.setImageAutoSize(true);
//        //Set tooltip text for the tray icon
//        trayIcon.setToolTip("System tray icon demo");
//        tray.add(trayIcon);
//
//        trayIcon.displayMessage("Hello, World", "notification demo", TrayIcon.MessageType.INFO);
//    }
    @Override
    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        SortedMap<LocalDateTime, Set<Task>> map;
        while (true) {
            map = Tasks.calendar(model, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
            System.out.println("\n----------------------------");
            System.out.println("\tNOTIFICATION: ");
            System.out.println("----------------------------");
            if (map.isEmpty()) {
                logger.info("Calendar has no tasks for the next hour");
            } else {
                System.out.println("Tasks for the upcoming hour: ");
                for (Map.Entry<LocalDateTime, Set<Task>> entry : map.entrySet()) {
                    String date = entry.getKey().format(formatter);
                    System.out.println("Date: " + date);
                    for (Task task : entry.getValue()) {
                        System.out.println("\tTask: " + task.getTitle());
                    }
                }
            }
            try {
                Thread.sleep(TIMER);
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error("Interrupted exception.", e);
            }
        }
    }
}