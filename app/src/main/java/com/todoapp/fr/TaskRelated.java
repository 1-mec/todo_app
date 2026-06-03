package com.todoapp.fr;

import java.util.ArrayList;

public class TaskRelated {
    public ArrayList<String> tasks;
    public TaskRelated(){
        tasks = new ArrayList<String>();
    }
    public ArrayList<String> getTasks(){
        return tasks;
    }
    public String getIndexTasks(int index){
        return tasks.get(index);
    }
    public void addTasks(String str){
        tasks.add(str);
    }
    public void removeTasks(int index){
        tasks.remove(index);
    }
}
