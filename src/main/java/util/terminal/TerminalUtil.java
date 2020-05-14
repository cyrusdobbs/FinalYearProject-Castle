package util.terminal;


import java.util.InputMismatchException;
import java.util.Scanner;


public class TerminalUtil {

    private static Scanner in = new Scanner(System.in);

    public static int getParsedIntegerInput(String regex) {
        while (true) {
            try {
                return Integer.parseInt(in.next(regex));
            } catch (InputMismatchException e) {
                in.next();
                System.out.println("That is not an option.");
            }
        }
    }

    public static String getParsedInput(String regex) {
        while (true) {
            try {
                return in.next(regex);
            } catch (InputMismatchException e) {
                in.next();
                System.out.println("That is not an option.");
            }
        }
    }

    public static void println() {
        println("");
    }

    public static void println(String print) {
        System.out.println(print);
    }


}
