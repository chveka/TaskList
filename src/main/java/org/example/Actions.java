package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Actions {
    private boolean dateFlag = true;

    /**
     * Метод для создания новой задачи
     */
    public ArrayList<Task> newTask(ArrayList<Task> taskList, String caption, String description, int priority, String deadline) {
        int idMax = 1;
        ArrayList<Integer> idList = new ArrayList<>();
        if (taskList.isEmpty()) {
            taskList.add(new Task(idMax, caption, description, priority, dateFormat(deadline), "new", null));
        } else {
            for (Task task : taskList) {
                idList.add(task.getId());
            }
            idMax = Collections.max(idList) + 1;
            taskList.add(new Task(idMax, caption, description, priority, dateFormat(deadline), "new", null));
        }
        return taskList;
    }

    /**
     * Метод для изменения существующей задачи
     */
    public ArrayList<Task> editTask(ArrayList<Task> taskList, int id, String caption, String description, int priority, String deadline) {
        ArrayList<Task> editTaskList = new ArrayList<>();
        for (Task task : taskList) {
            if (id == task.getId()) {
                task.setCaption(caption);
                task.setDescription(description);
                task.setPriority(priority);
                task.setDeadline(dateFormat(deadline));
                editTaskList.add(task);
            } else {
                editTaskList.add(task);
            }
        }
        return editTaskList;
    }

    /**
     * Метод для присвоения выбранной задаче статуса "deleted"
     */
    public ArrayList<Task> removeTask(ArrayList<Task> taskList, int id) {
        ArrayList<Task> progressTaskList = new ArrayList<>();
        for (Task task : taskList) {
            if (id == task.getId()) {
                task.setStatus("deleted");
                progressTaskList.add(task);
            } else {
                progressTaskList.add(task);
            }
        }
        return progressTaskList;
    }

    /**
     * Метод изменения статуса задачи
     */
    public ArrayList<Task> markProgressTask(ArrayList<Task> taskList, int id, String status) {
        ArrayList<Task> progressTaskList = new ArrayList<>();
        Date date = new Date();
        for (Task task : taskList) {
            if (id == task.getId()) {
                if (status.equals("in_progress")) {
                    task.setStatus(status);
                    progressTaskList.add(task);
                } else if (status.equals("done")) {
                    task.setStatus(status);
                    task.setComplete(date);
                    progressTaskList.add(task);
                }
            } else {
                progressTaskList.add(task);
            }
        }
        return progressTaskList;
    }

    /**
     * Метод вывода списка задач по определённому статусу, переданному через переменную "status"
     */
    public ArrayList<Task> listTasksByParameters(ArrayList<Task> taskList, String status) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        for (Task task : taskList) {
            if (task.getStatus().equals(status)) {
                System.out.println(task);
                tasks.add(task);
            }
        }
        return tasks;
    }

    public void help() {
        System.out.println("Список команд(команды вводятся без кавычек): \n" +
                "help - вывод списка команд;\n" +
                "save - сохранение задач в файл\n" +
                "list - список доступных задач;\n" +
                "list -s new - список новых задач;\n" +
                "list -s done - список выполненых задач задач;\n" +
                "task new - создать новую задачу;\n" +
                "task edit \"id\" - редактирование существующей задачи, вместо \"id\" впишите id нужной задачи;\n" +
                "task remove \"id\" - удаление существующей задачи, вместо \"id\" впишите id нужной задачи;\n" +
                "task complete \"id\" - перевести задачу в статус \"Выполнена\" (done)," +
                "вместо \"id\" впишите id нужной задачи;\n" +
                "task in_progress \"id\" - перевести задачу в статус \"В процессе\" (done)," +
                "вместо \"id\" впишите id нужной задачи;\n" +
                "exit - выход из программы");
    }

    /**
     * Метод перевода дати из String в Date с определением формата даты и сравнением на правильность введённого формата
     * даты с эталоном
     */
    public Date dateFormat(String stringDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        try {
            date = formatter.parse(stringDate);
        } catch (ParseException e) {
            System.out.println("Не верный формат даты, введите дату в формате дд.мм.гггг");
            dateFlag = false;
        }
        return date;
    }

    public void setDateFlag(boolean dateFlag) {
        this.dateFlag = dateFlag;
    }

    public boolean isDateFlag() {
        return dateFlag;
    }
}
