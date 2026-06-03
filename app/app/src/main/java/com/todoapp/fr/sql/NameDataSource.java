package com.todoapp.fr.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class NameDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME};

    public NameDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Tasks createTasks(String name) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        long insertId = database.insert(MySQLiteHelper.TABLE_TASK, null,values);
        Cursor cursor = database.query(
                MySQLiteHelper.TABLE_TASK,
                allColumns,
                MySQLiteHelper.COLUMN_ID + " = " + insertId,
                null,
                null,
                null,
                null);
        Tasks newTasks = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                newTasks = cursorToTasks(cursor);
            }
            cursor.close();
        }
        return newTasks;
    }

    public void deleteTasks(Tasks name) {
        long id = name.getId();
        System.out.println("Tasks deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_TASK, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllTasks(){
        for(Tasks task : getAllTasks()){
            deleteTasks(task);
        }
    }
    @Override
    public String toString() {
        return "NameDataSource{" +
                "database=" + database +
                ", dbHelper=" + dbHelper +
                ", allColumns=" + Arrays.toString(allColumns) +
                '}';
    }

    public ArrayList<Tasks> getAllTasks() {
        ArrayList<Tasks> names = new ArrayList<Tasks>();
        Cursor cursor = database.query(
                MySQLiteHelper.TABLE_TASK,
                allColumns,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Tasks name = cursorToTasks(cursor);
            names.add(name);
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }

    private Tasks cursorToTasks(Cursor cursor) {
        Tasks name = new Tasks();
        name.setId(cursor.getLong(0));
        name.setTask(cursor.getString(1));
        return name;
    }

    public boolean isTasksEmpty(){
        return getAllTasks().isEmpty();
    }
}
