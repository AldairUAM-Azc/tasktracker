package com.tasktracker;

import java.util.HashMap;
import java.util.Map;

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
        System.out.println("args length " + args.length);
        if (args.length <= 0) {
            System.out.println(help);
            return;
        }

        Map<String, State> states = new HashMap<String, State>();
        states.put("todo", State.todo);
        states.put("inProgress", State.inProgress);
        states.put("done", State.done);

        TaskCRUD crud = new TaskCRUD();

        String option = args[0];
        System.out.println("Option: " + option);
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
                        crud.updateTask(taskId, newDescription);
                        System.out.println("Task (ID " + taskId + ") updated successfully");
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
                    crud.deleteTask(taskId);
                } else {
                    System.out.println(help);
                }
                break;
            case "markInProgress":
                if (args.length >= 2) {
                    int taskId = Integer.parseInt(args[1]);
                    crud.markInProgress(taskId);
                }
                break;
            case "markDone":
                if (args.length >= 2) {
                    int taskId = Integer.parseInt(args[1]);
                    crud.markDone(taskId);
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
