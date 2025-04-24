# TaskTracker

CLI taskTracker roadmap.sh beginner backend project using java.

## Description

Task tracker is a project used to track and manage your tasks.
It lets you create, read, update, and delete many tasks and keep track of the state (todo, inProgress, done) of each task.

The requirement for the project are found in [here⬆️]. (https://roadmap.sh/projects/task-tracker)

## Getting Started

### Dependencies

* At least JRE 17 is needed and maven to build the projects pom.xml file.

### Installing

* Clone the repo
* Run next command to build the project's jar file
```bash
    mvn clean package
```

### Executing program

* Once the jar is built run the bash script taskTracker like this.
```bash
    ./taskTracker 
```

* The tracker has the following commands:
```bash
    # Adding a new task
    ./taskTracker add "Buy groceries"
    # Output: Task added successfully (ID: 1)

    # Updating and deleting tasks
    ./taskTracker update 1 "Buy groceries and cook dinner"
    ./taskTracker delete 1

    # Marking a task as in progress or done
    ./taskTracker mark-in-progress 1
    ./taskTracker mark-done 1

    # Listing all tasks
    ./taskTracker list

    # Listing tasks by status
    ./taskTracker list done
    ./taskTracker list todo
    ./taskTracker list in-progress
```

## Authors

Aldair Avalos 
al2222005685@azc.uam.mx

## Acknowledgments

Inspiration, code snippets, etc.
* [readme.sh](https://roadmap.sh/)
* [readme template](https://gist.github.com/DomPizzie/7a5ff55ffa9081f2de27c315f5018afc)
