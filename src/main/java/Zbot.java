import java.util.Scanner;

public class Zbot {
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
        String input = scanner.nextLine();
        
        System.out.println("Bye. Hope to see you again soon!");
        scanner.close();
    }
}
