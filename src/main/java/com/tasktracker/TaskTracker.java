package com.tasktracker;

import org.json.JSONException;

public class TaskTracker {
    static String helpAdd = """
            - Adding a new task
            ./taskTracker add "Buy groceries"
            - Output: Task added successfully (ID: 1)

            """;
    static String helpUpdateOrDelete = """
            - Updating and deleting tasks
            ./taskTracker update 1 "Buy groceries and cook dinner"
            ./taskTracker delete 1

            """;
    static String helpMark = """
            - Marking a task as in progress or done
            ./taskTracker markInProgress 1
            ./taskTracker markDone 1

            """;
    static String helpList = """
            - Listing all tasks
            ./taskTracker list

            - Listing tasks by status
            ./taskTracker list done
            ./taskTracker list todo
            ./taskTracker list inProgress

            """;
    static String help = helpAdd + helpUpdateOrDelete + helpMark + helpList;

    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println(help);
            return;
        }

        TaskCRUD crud = new TaskCRUD();

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
                    System.out.println(helpAdd);
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
                    System.out.println(helpUpdateOrDelete);
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
                    System.out.println(helpUpdateOrDelete);
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
                } else {
                    System.out.println(helpMark);
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
                } else {
                    System.out.println(helpMark);
                }
                break;
            case "list":
                if (args.length >= 2) {
                    String status = args[1];
                    if (!crud.listTasks(status)) {
                        System.err.println("Invalid state " + status);
                        System.out.println(helpList);
                    }
                } else {
                    crud.listTasks();
                }
                break;
            default:
                System.out.println(help);
                break;
        }
    }
}
