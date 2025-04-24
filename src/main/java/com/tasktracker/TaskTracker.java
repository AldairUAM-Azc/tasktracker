package com.tasktracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

public class TaskTracker {

    static String help = """
            - Adding a new task
            task-cli add "Buy groceries"
            - Output: Task added successfully (ID: 1)

            - Updating and deleting tasks
            task-cli update 1 "Buy groceries and cook dinner"
            task-cli delete 1

            - Marking a task as in progress or done
            task-cli markInProgress 1
            task-cli markDone 1

            - Listing all tasks
            task-cli list

            - Listing tasks by status
            task-cli list done
            task-cli list todo
            task-cli list inProgress
                        """;

    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println(help);
            return;
        }

        Map<String, State> states = new HashMap<String, State>();
        states.put("todo", State.todo);
        states.put("inProgress", State.inProgress);
        states.put("done", State.done);

        String content = "";
        JSONArray tasks = new JSONArray();
        try {
            File tasksFile = new File("tasks.json");
            if (tasksFile.createNewFile()) {
                FileWriter fw = new FileWriter("tasks.json");
                fw.write(new JSONArray().toString());
                fw.close();
            }
            content = new String(Files.readAllBytes(Paths.get("tasks.json")));
            tasks = new JSONArray(content);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int nextId = 1;
        File idFile;
        try {
            idFile = new File("trackId.txt");
            if (idFile.createNewFile()) {
                FileWriter fw = new FileWriter("trackId.txt");
                fw.write("1");
                fw.close();

            }
            BufferedReader br = new BufferedReader(new FileReader("trackId.txt"));
            nextId = Integer.parseInt(br.readLine());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TaskCRUD crud = new TaskCRUD();
        crud.setTasks(tasks);
        crud.setId(nextId);

        String option = args[0];

        switch (option) {
            case "add":
                if (args.length >= 2) {
                    try {
                        String description = args[1];
                        int taskId = crud.addTask(description);
                        System.out.println("Task added successfully (ID: " + taskId + ")");
                    } catch (JSONException e) {
                        System.out.println("Error json");
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(help);
                }
                break;

            case "update":
                if (args.length >= 3) {
                    int taskId = Integer.parseInt(args[1]);
                    String newDescription = args[2];
                    try {
                        if (crud.updateTask(taskId, newDescription)) {
                            System.out.println("Task (ID " + taskId + ") updated successfully");
                        } else {
                            System.err.println("Task (ID " + taskId + ") not found");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(help);
                }
                break;
            case "delete":
                if (args.length >= 2) {
                    int taskId = Integer.parseInt(args[1]);
                    if (crud.deleteTask(taskId)) {
                        System.out.println("Task (ID " + taskId + ") deleted successfully");
                    } else {
                        System.err.println("Task (ID " + taskId + ") not found");
                    }
                } else {
                    System.out.println(help);
                }
                break;
            case "markInProgress":
                if (args.length >= 2) {
                    int taskId = Integer.parseInt(args[1]);
                    if (crud.markInProgress(taskId)) {
                        System.out.println("Task (ID " + taskId + ") marked as in progress");
                    } else {
                        System.err.println("Task (ID " + taskId + ") not found");
                    }
                }
                break;
            case "markDone":
                if (args.length >= 2) {
                    int taskId = Integer.parseInt(args[1]);
                    if (crud.markDone(taskId)) {
                        System.out.println("Task (ID " + taskId + ") marked as done");
                    } else {
                        System.err.println("Task (ID " + taskId + ") not found");
                    }
                }
                break;
            case "list":
                if (args.length >= 2) {
                    String state = args[1];
                    if (states.containsKey(state)) {
                        crud.listTasks(states.get(state));
                    } else {
                        System.out.println("Invalid state");
                    }
                } else {
                    crud.listTasks();
                }
                break;
            default:
                break;
        }
    }
}
