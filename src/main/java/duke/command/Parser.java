package duke.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import duke.storage.Storage;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.TaskList;
import duke.task.Todo;
import duke.ui.Ui;

/**
 * <h1> Duke Parser class </h1>
 * This class is the class that processes the
 * commands to create readable Tasks that will be stored in
 * the arraylist of tasks and arraylist of records
 *
 * @author Lee Penn Han
 * @version 1.0
 * @since 2020-25-08
 */
public class Parser {

    /**
     * Processes the command and filters
     * it the correct private methods to
     * instantiate the Task objects to be recorded
     *
     * @param command This is the user input
     * @param taskList This is the taskList class that stores the current lists of tasks
     * @param storage This is the Storage object that records the tasks
     * @throws DukeException Exception for unidentified commands
     */
    public static String process(String command, TaskList taskList, Storage storage) throws DukeException {
        String[] stringarr = command.split(" ");
        String finalString;
        if (stringarr[0].equals("list")) {
            String response = processorList(taskList);
            finalString = Ui.showResponse(response, command);
        } else if (stringarr[0].equals("done")) {
            int index = Integer.parseInt(stringarr[1]);
            String response = taskList.updateTask(index);
            storage.updateRecord(response, index);
            finalString = Ui.showResponse(response, command);
        } else if (stringarr[0].equals("delete")) {
            int index = Integer.parseInt(stringarr[1]);
            String response = taskList.deleteTask(index);
            storage.deleteRecord(index);
            finalString = Ui.showResponse(response, command);
        } else if (stringarr[0].equals("find")) {
            String key = stringarr[1];
            String response = processorFind(taskList, key);
            finalString = Ui.showResponse(response, command);
        } else {
            String response = processorAdd(command, taskList);
            storage.saveRecord(response);
            finalString = Ui.showResponse(response, command);
        }
        return finalString;
    }

    private static String processorFind(TaskList taskList, String key) {
        int counter = 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Ui.showCommandMessage("Here are the matching tasks in your list:"));
        for (int i = 0; i < taskList.getListSize(); i++) {
            if (taskList.getTask(i).getTask().contains(key)) {
                String findResponse = counter + "." + taskList.getTask(i).toString() + "\n";
                stringBuilder.append(findResponse);
                counter++;
            }
        }
        stringBuilder.append(Ui.showLine());
        return stringBuilder.toString();
    }

    private static String processorList(TaskList taskList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Ui.showCommandMessage("Here are the tasks in your list:"));
        for (int i = 0; i < taskList.getListSize(); i++) {
            int index = i + 1;
            String listResponse = index + "." + taskList.getTask(i).toString() + "\n";
            stringBuilder.append(listResponse);
        }
        stringBuilder.append(Ui.showLine());
        return stringBuilder.toString();
    }

    private static String processorAdd(String cmd, TaskList taskList) throws DukeException {
        String[] stringarr = cmd.split(" ", 2);
        if (stringarr[0].equals("todo")) {
            if (stringarr.length <= 1) {
                String message = "The description of a Todo cannot be empty";
                throw new DukeException(message);
            } else {
                Todo todo = Todo.createTodo(stringarr[1]);
                taskList.addTask(todo);
                return todo.toString();
            }
        } else if (stringarr[0].equals("deadline")) {
            if (stringarr.length <= 1) {
                String message = "The description of a Deadline cannot be empty";
                throw new DukeException(message);
            } else {
                try {
                    String[] secondarr = stringarr[1].split("/by", 2);
                    LocalDate date = LocalDate.parse(secondarr[1].trim());
                    Deadline deadline = Deadline.createDeadline(secondarr[0], date);
                    taskList.addTask(deadline);
                    return deadline.toString();
                } catch (DateTimeParseException e) {
                    String message = "That does not look like a proper Date. Please input YYYY-MM-DD";
                    throw new DukeException(message);
                }
            }
        } else if (stringarr[0].equals("event")) {
            if (stringarr.length <= 1) {
                String message = "The description of an Event cannot be empty";
                throw new DukeException(message);
            } else {
                String[] secondarr = stringarr[1].split("/at", 2);
                Event event = Event.createEvent(secondarr[0], secondarr[1]);
                taskList.addTask(event);
                return event.toString();
            }
        } else {
            String message = "OOPS!!! I'm sorry, but I don't know what that means :-(";
            throw new DukeException(message);
        }
    }
}
