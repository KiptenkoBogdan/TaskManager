package ua.edu.sumdu.j2se.kiptenko.tasks;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.kiptenko.tasks.controller.Controller;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.*;
import ua.edu.sumdu.j2se.kiptenko.tasks.view.View;

import java.awt.*;

public class Main {

    public static final String SAVE_FILE = "data.json";
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws AWTException {
        BasicConfigurator.configure();
		AbstractTaskList list = TaskListFactory.createTaskList(ListTypes.types.ARRAY);
        TaskIO.loadFromFile(list, SAVE_FILE);
		View view = new View();
		Controller controller = new Controller(list, view);
        Thread thread = new Thread(controller);
        thread.start();
		controller.execute();
        TaskIO.saveToFile(list, SAVE_FILE);
        logger.info("Closing program");
	}
}