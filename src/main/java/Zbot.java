import java.util.Scanner;

public class Zbot {
    private static String[] tasks = new String[100];
    private static boolean[] taskDone = new boolean[100];
    private static int taskCount = 0;
    
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);
        
        System.out.println("Hello! I'm Zbot");
        System.out.println("What can I do for you?");
        
        Scanner scanner = new Scanner(System.in);
        String input;
        
        while (!(input = scanner.nextLine()).equals("bye")) {
            if (input.equals("list")) {
                if (taskCount == 0) {
                    System.out.println("No tasks in your list yet!");
                } else {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        String status = taskDone[i] ? "[X]" : "[ ]";
                        System.out.println((i + 1) + ". " + status + " " + tasks[i]);
                    }
                }
            } else if (input.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                taskDone[taskIndex] = true;
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  [X] " + tasks[taskIndex]);
            } else if (input.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                taskDone[taskIndex] = false;
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  [ ] " + tasks[taskIndex]);
            } else {
                tasks[taskCount] = input;
                taskCount++;
                System.out.println("added: " + input);
            }
        }
        
        System.out.println("Bye. Hope to see you again soon!");
        scanner.close();
    }
}
