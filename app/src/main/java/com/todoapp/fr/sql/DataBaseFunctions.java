package com.todoapp.fr.sql;

import android.content.Context;
import java.util.ArrayList;

public class DataBaseFunctions {
    private NameDataSource datasource;
    public Tasks task;
    public ArrayList<Tasks> adapter = new ArrayList<>();

    public DataBaseFunctions(Context context) {
        datasource = new NameDataSource(context);
        datasource.open();
    }

    public void addTaskDb(String taskName){
        System.out.println("DataBaseFunctions 1");
        task = datasource.createTasks(taskName);
        System.out.println("DataBaseFunctions 2");
        if (task != null) adapter.add(task);
        System.out.println("DataBaseFunctions 3");
    }

    public Tasks findTask(String taskName, ArrayList<Tasks> adapter){
        int i = 0;
        while (i < adapter.size()) {
            if (adapter.get(i).getTask().equals(taskName)) {
                break;
            }
            i++;
        }
        if (i < adapter.size()) {
            return adapter.get(i);
        }
        return null;
    }

    public void deleteTaskDb(String taskName){
        if (!adapter.isEmpty()) {
            Tasks tmp = findTask(taskName, adapter);
            if (tmp != null) {
                datasource.deleteTasks(tmp);
                adapter.remove(tmp);
            }
        }
    }

    public void open() {
        if (datasource != null) {
            datasource.open();
        }
    }

    public void close() {
        if (datasource != null) {
            datasource.close();
        }
    }
}
