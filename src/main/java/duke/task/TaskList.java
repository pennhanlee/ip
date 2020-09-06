package duke.task;

import duke.command.DukeException;
import duke.command.Parser;
import duke.ui.Ui;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class TaskList {

    private ArrayList<Task> tasklist;
    private PriorityQueue<Task> taskPriority;

    private TaskList() {
        this.tasklist = new ArrayList<>();
        this.taskPriority = new PriorityQueue<>(new TaskComparator());
    }

    /**
     * Adds a task to the tasklist
     * @param task The task as instantiated from Parser to a Task object
     * @return String representation of the Task object that is added
     */
    public String addTask(Task task) {
        tasklist.add(task);
        taskPriority.add(task);
        return task.toString();
    }

    /**
     * Deletes a task from the tasklist
     * @param taskNumber The index of the task in the tasklist
     * @return String representation of the Task object that is deleted
     */
    public String deleteTask(int taskNumber) {
        int i = taskNumber - 1;
        Task removedTask = this.tasklist.get(i);
        this.tasklist.remove(i);
        this.taskPriority.remove(removedTask);
        return removedTask.toString();
    }

    /**
     * Updates a task in the tasklist to mark it as done
     * @param taskNumber The index of the task in the tasklist
     * @return String representation of the Task object that is updated
     */
    public String updateTask(int taskNumber) {
        int i = taskNumber - 1;
        Task updatedTask = this.tasklist.get(i);
        this.taskPriority.remove(updatedTask);
        updatedTask.setDone();
        this.taskPriority.add(updatedTask);
        return updatedTask.toString();
    }


    /**
     * Refreshes the Tasklist arraylist to remove all tasks in it
     */
    public void refreshTasklist() {
        this.tasklist = new ArrayList<>();
    }

    /**
     * Returns the current size of the task list
     * @return Integer value of the size of task list
     */
    public int getListSize() {
        return this.tasklist.size();
    }

    /**
     * Returns the task that is indicated by its index in the Tasklist
     * @param index The index of the task in the taskList
     * @return Task object
     */
    public Task getTask(int index) {
        return this.tasklist.get(index);
    }

    public Task getTask() {
        return this.taskPriority.poll();
    }

    public PriorityQueue<Task> getTaskPriorityQueue() {
        return this.taskPriority;
    }

    /**
     * Creates a tasklist object
     * @return TaskList Object
     */
    public static TaskList createTaskList() {
        return new TaskList();
    }
}
