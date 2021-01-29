package ua.edu.sumdu.j2se.kiptenko.tasks;

import ua.edu.sumdu.j2se.kiptenko.tasks.controller.Controller;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.ListTypes;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.TaskListFactory;
import ua.edu.sumdu.j2se.kiptenko.tasks.view.View;

public class Main {

	public static void main(String[] args) {
		AbstractTaskList list = TaskListFactory.createTaskList(ListTypes.types.ARRAY);;
		View view = new View();
		Controller controller = new Controller(list, view);
		controller.execute();
	}
}