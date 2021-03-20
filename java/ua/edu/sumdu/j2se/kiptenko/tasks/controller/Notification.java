package ua.edu.sumdu.j2se.kiptenko.tasks.controller;

import org.apache.log4j.Logger;

import java.time.format.DateTimeFormatter;


public class Notification extends Thread{

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final Logger logger = Logger.getLogger(Controller.class);
    private final static long TIMER = 300000; // 5 MIN

    private Controller controller;

    public Notification(Controller controller){
        this.controller = controller;
        setDaemon(true);
    }
    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("Interrupted exception.", e);
        }
        String nextTaskTime = controller.checkNextTime(controller.getModel());
        controller.getView().println("\n----------------------------");
        controller.getView().println("\tNOTIFICATION: ");
        controller.getView().println("----------------------------");

        if(nextTaskTime == null){
            controller.getView().println("You have no active tasks at the moment");
        } else{
            controller.getView().println("You have an acive task set on: " + nextTaskTime);
        }
        try {
            Thread.sleep(TIMER);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("Interrupted exception.", e);
        }
    }
}
