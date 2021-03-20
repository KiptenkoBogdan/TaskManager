package ua.edu.sumdu.j2se.kiptenko.tasks;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import ua.edu.sumdu.j2se.kiptenko.tasks.controller.Controller;
import ua.edu.sumdu.j2se.kiptenko.tasks.controller.Notification;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.*;
import ua.edu.sumdu.j2se.kiptenko.tasks.view.View;


public class Main {

    public static final String SAVE_FILE = "data.json";
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args){
        PropertyConfigurator.configure("log4j.properties");
		AbstractTaskList list = TaskListFactory.createTaskList(ListTypes.types.ARRAY);
        TaskIO.loadFromFile(list, SAVE_FILE);
		View view = new View();
		Controller controller = new Controller(list, view);
        Notification notification = new Notification(controller);
        notification.start();
//        Thread thread = new Thread(controller);
//        thread.setDaemon(true);
//        thread.start();
		controller.execute();
        TaskIO.saveToFile(list, SAVE_FILE);
        logger.info("Closing program");
	}
}