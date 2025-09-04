package zbot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import zbot.task.Todo;
import zbot.tasklist.TaskList;

public class TaskListTest {
    
    private TaskList taskList;
    
    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }
    
    @Test
    public void taskList_newInstance_isEmpty() {
        assertTrue(taskList.isEmpty());
        assertEquals(0, taskList.getSize());
    }
    
    @Test
    public void addTask_singleTodo_increasesSize() {
        Todo todo = new Todo("test task");
        taskList.addTask(todo);
        
        assertFalse(taskList.isEmpty());
        assertEquals(1, taskList.getSize());
        assertEquals(todo, taskList.getTask(0));
    }
    
    @Test
    public void deleteTask_existingTask_removesFromList() {
        Todo todo = new Todo("test task");
        taskList.addTask(todo);
        taskList.deleteTask(0);
        
        assertTrue(taskList.isEmpty());
        assertEquals(0, taskList.getSize());
    }
    
    @Test
    public void markTask_existingTask_changesTaskStatus() {
        Todo todo = new Todo("test task");
        taskList.addTask(todo);
        
        taskList.markTask(0);
        assertTrue(taskList.getTask(0).isDone());
        
        taskList.unmarkTask(0);
        assertFalse(taskList.getTask(0).isDone());
    }
}