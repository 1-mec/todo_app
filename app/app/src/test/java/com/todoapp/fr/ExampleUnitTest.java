package com.todoapp.fr;

import org.junit.Test;

import static org.junit.Assert.*;

import com.todoapp.fr.sql.DataBaseFunctions;

import java.util.ArrayList;

/**
 * Example local unit DataBaseFunctions, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    DataBaseFunctions db;
    TaskRelated tr = new TaskRelated();
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void list_is_empty(){
        ArrayList<String> t = new ArrayList<>();
        assertEquals(t,tr.getTasks());}

    @Test
    public void test(){}


}