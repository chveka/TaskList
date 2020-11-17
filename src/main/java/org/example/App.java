package org.example;


import java.text.SimpleDateFormat;
import java.util.*;

public class App {
    private static XMLWorker xmlWorker;
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private ArrayList<Task> deleteTaskList = new ArrayList<>();
    private ArrayList<Task> taskList = new ArrayList<>();
    private boolean flag = true;
    private String filePath = "src/main/java/org/example/files/tasks.xml";



    public static void main(String[] args) {
        App app = new App();
        app.fillingLists();
        app.selectAction();
        app.saveListInDoc();
    }

    /**
     * Метод для работы с классом Actions
     */
    public void selectAction() {
        int id;
        String caption;
        String description;
        int priority;
        Date deadline;
        boolean exitFlag = true;
        String command;
        Actions actions = new Actions();
        String[] commandParse;
        Task deleteTask = new Task();
        String wrongCommand = "Такой команды не существует, для просмотра списка команд введите \"help\"";

            do {
                try {
                System.out.println("\n\n Введите команду (для просмотра списка команд используйте команду \"help\"," +
                        "для выхода используйте команду \"exit\" )");
                command = scan();
                commandParse = command.split(" ");
                switch (commandParse[0]) {

                    case ("task"):
                        if (commandParse[1].equals("new")) {
                            System.out.println("Введите заголовок задачи");
                            caption = scan();
                            System.out.println("Введите описание задачи");
                            description = scan();
                            do {
                                System.out.println("Введите приоритетность задачи от 0 до 100");
                                priority = Integer.parseInt(scan());
                            } while (priority < 0 || priority > 100);
                            System.out.println("Введите дату окончания срока выполнения задачи в формате \"дд.мм.гггг\"");
                            do {
                                actions.setDateFlag(true);
                                deadline = actions.dateFormat(scan());
                            } while (!actions.isDateFlag());
                            taskList = actions.newTask(taskList, caption, description, priority, format.format(deadline));
                            System.out.println("Новая задача сохранена");
                        } else if (commandParse[1].equals("remove")) {
                            if (idCheck(commandParse[2])) {
                                id = Integer.parseInt(commandParse[2]);
                                do {
                                    System.out.println("Вы точно хотите удалить выбранную задачи? " +
                                            "Для удаления введите \"yes\", для отмены введите \"no\"" +
                                            "(при выборе \"no\" команда закончит действие)");
                                    command = scan();
                                    flag = true;
                                    if (command.equals("yes")) {
                                        taskList = actions.removeTask(taskList, id);
                                        for (Task task : taskList) {
                                            if (task.getStatus().equals("deleted")) {
                                                deleteTask = task;
                                                deleteTaskList.add(task);
                                            }
                                        }
                                        taskList.remove(deleteTask);
                                        System.out.println("Задача удалена");
                                    } else if (command.equals("no")) {
                                        System.out.println("Возвращение к началу программы...");
                                    } else {
                                        System.out.println("Неверная команда, введите только \"yes\", либо \"no\"");
                                        flag = false;
                                    }
                                } while (!flag);
                            }
                        } else if (commandParse[1].equals("in_progress") || commandParse[1].equals("complete")) {
                            if (idCheck(commandParse[2])) {
                                id = Integer.parseInt(commandParse[2]);
                                taskList = actions.markProgressTask(taskList, id, commandParse[1]);
                            } else {
                                break;
                            }
                        } else if (commandParse[1].equals("edit")) {
                            if (idCheck(commandParse[2])) {
                                id = Integer.parseInt(commandParse[2]);
                                System.out.println("Введите заголовок задачи");
                                caption = scan();
                                System.out.println("Введите описание задачи");
                                description = scan();
                                do {
                                    System.out.println("Введите приоритетность задачи от 0 до 100");
                                    priority = Integer.parseInt(scan());
                                } while (priority < 0 || priority > 100);
                                System.out.println("Введите дату окончания срока выполнения задачи в формате \"дд.мм.гггг\"");
                                do {
                                    actions.setDateFlag(true);
                                    deadline = actions.dateFormat(scan());
                                } while (!actions.isDateFlag());
                                do {
                                    System.out.println("Вы точно хотите заменить текущие данные выбранной задачи? " +
                                            "Для сохранения введите \"yes\", для отмены введите \"no\"" +
                                            "(при выборе \"no\" команда закончит действие без сохранения данных)");
                                    command = scan();
                                    flag = true;
                                    if (command.equals("yes")) {
                                        taskList = actions.editTask(taskList, id, caption, description, priority, format.format(deadline));
                                        System.out.println("Изменения в задачу внесены");
                                    } else if (command.equals("no")) {
                                        System.out.println("Возвращение к началу программы...");
                                    } else {
                                        System.out.println("Неверная команда, введите только \"yes\", либо \"no\"");
                                        flag = false;
                                    }
                                } while (!flag);
                            } else {
                                break;
                            }
                        } else {
                            System.out.println(wrongCommand);
                        }

                        break;

                    case ("list"):
                        if (commandParse.length == 1) {
                            for (Task task : taskList) {
                                System.out.println(task);
                            }
                            break;
                        } else if (commandParse[1].equals("-s")) {
                            if (commandParse[2].equals("new") || commandParse[2].equals("done")) {
                                actions.listTasksByParameters(taskList, commandParse[2]);
                                break;
                            } else {
                                System.out.println(wrongCommand);

                            }
                        } else {
                            System.out.println(wrongCommand);
                        }

                        break;

                    case ("help"):
                        actions.help();
                        break;

                    case ("save"):
                        saveListInDoc();
                        break;

                    case ("exit"):
                        exitFlag = false;
                        break;

                    default:
                        System.out.println(wrongCommand);
                }
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println(wrongCommand);
            }
            } while (exitFlag);

    }

    /**
     * Метод проверки на существование id и его формат
     */
    private boolean idCheck(String idString) {
        try {
            int id = Integer.parseInt(idString);
            for (Task task : taskList) {
                if (id == task.getId()) {
                    flag = false;
                } else {
                    flag = true;
                }
            }
            if (!flag) {
                System.out.println("Такой задачи не существует или была удалена," +
                        "для просмотра списка задач введите команду \"list\"");
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат числа");
            flag = true;
        }
        return flag;
    }

    /**
     * Метод для заполнения списка, с которым будет работать программа всё остальное время
     */
    private void fillingLists() {
        System.out.println("Заполнение списка задач из файла...");
        xmlWorker = new XMLWorker();
        ArrayList<Task> fullTaskList = xmlWorker.xmlReader(filePath);
        for (Task task : fullTaskList) {
            if (task.getStatus().equals("new") || task.getStatus().equals("in_progress") || task.getStatus().equals("done")) {
                taskList.add(task);
            } else if (task.getStatus().equals("delete")) {
                deleteTaskList.add(task);
            }
        }
        System.out.println("Конец чтения файла");
    }

    /**
     * Метод для сохранения задач в xml файл
     */
    private void saveListInDoc() {
        System.out.println("Сохранение списка задач в файл...");
        xmlWorker = new XMLWorker();
        taskList.addAll(deleteTaskList);
        Collections.sort(taskList, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getId() - o2.getId();
            }
        });
        xmlWorker.xmlWriter(taskList, filePath);
        System.out.println("Задачи сохранены");
    }


    private String scan() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
