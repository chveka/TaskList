package org.example;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ActionsTest {
    Actions actions = new Actions();
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    String caption;
    String description;
    ArrayList<Task> taskList = new ArrayList<>();

    @Before
    public void getTaskList() throws ParseException {
        caption  = "Заголовок";
        description = "Текст задания";
        taskList = new ArrayList<>();
        taskList.add(new Task(1, caption, description, 10, format.parse("10.10.2020") , "complete", format.parse("10.12.2020")));
        taskList.add(new Task(2, caption, description, 20, format.parse("10.10.2020") , "new", null));
        taskList.add(new Task(3, caption, description, 40, format.parse("10.10.2020") , "in_progress", null));
    }

    @Test
    public void testNewTaskIfListEmpty() throws ParseException {
        ArrayList<Task> expected = new ArrayList<>();
        ArrayList<Task> actual = actions.newTask(expected, caption, description, 35, "10.10.2020");
        expected.add(new Task(1, caption, description, 35, format.parse("10.10.2020") , "new", null));
        Assert.assertEquals("testNewTaskIfListEmpty", expected, actual);
    }

    @Test
    public void testNewTaskIfListNotEmpty() throws ParseException {
        ArrayList<Task> expected = taskList;
        expected.add(new Task(4, caption, description, 35, format.parse("10.10.2020") , "new", null));
        ArrayList<Task> actual = actions.newTask(taskList, caption, description, 35, "10.10.2020");
        Assert.assertEquals("testNewTaskIfListEmpty", expected, actual);
    }

    @Test
    public void testEditTask() throws ParseException {
        ArrayList<Task> expected = taskList;
        expected.remove(2);
        expected.add(new Task(3, "caption", "description", 45, format.parse("25.10.2020") , "delete", format.parse("10.12.2020")));
        ArrayList<Task> actual = actions.editTask(taskList, 3, "caption", "description", 45, "25.10.2020");
        Assert.assertEquals("testEditTask", expected, actual);
    }

    @Test
    public void testRemoveTask() throws ParseException {
        ArrayList<Task> expected = taskList;
        expected.remove(0);
        expected.add(new Task(1, caption, description, 10, format.parse("10.10.2020") , "delete", format.parse("10.12.2020")));
        ArrayList<Task> actual = actions.removeTask(taskList, 1);
        Assert.assertEquals("testRemoveTask", expected, actual);
    }

    @Test
    public void testMarkProgressTaskInProgress() throws ParseException {
        ArrayList<Task> expected = taskList;
        expected.remove(1);
        expected.add(new Task(2, caption, description, 10, format.parse("10.10.2020") , "in_progress", null));
        Collections.sort(expected, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getId() - o2.getId();
            }
        });
        ArrayList<Task> actual = actions.markProgressTask(taskList, 3, "done");
        Assert.assertEquals("testMarkProgressTaskComplete", expected, actual);
    }

    @Test
    public void testMarkProgressTaskComplete() throws ParseException {
        ArrayList<Task> expected = taskList;
        Date date = new Date();
        expected.remove(2);
        expected.add(new Task(3, caption, description, 10, format.parse("10.10.2020") , "complete", date));
        ArrayList<Task> actual = actions.markProgressTask(taskList, 3, "done");
        Assert.assertEquals("testMarkProgressTaskComplete", expected, actual);
    }

    @Test
    public void testListTasksByParametersNew(){
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(taskList.get(0));
        ArrayList<Task> actual = actions.listTasksByParameters(taskList, "complete");
        Assert.assertEquals("testListTasksByParametersNew", expected, actual);
    }

    @Test
    public void testListTasksByParametersInProgress(){
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(taskList.get(1));
        ArrayList<Task> actual = actions.listTasksByParameters(taskList, "new");
        Assert.assertEquals("testListTasksByParametersInProgress", expected, actual);
    }

    @Test
    public void testListTasksByParametersComplete(){
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(taskList.get(2));
        ArrayList<Task> actual = actions.listTasksByParameters(taskList, "in_progress");
        Assert.assertEquals("testListTasksByParametersComplete", expected, actual);
    }
}
