package ua.edu.sumdu.j2se.kiptenko.tasks.model;

import ua.edu.sumdu.j2se.kiptenko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.ArrayTaskList;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.LinkedTaskList;
import ua.edu.sumdu.j2se.kiptenko.tasks.model.ListTypes;

public class TaskListFactory{
    public static AbstractTaskList createTaskList(ListTypes.types type){
        if(type == ListTypes.types.ARRAY){
            return new ArrayTaskList();
        } else if(type == ListTypes.types.LINKED){
            return new LinkedTaskList();
        }
        return null;
    }
}
