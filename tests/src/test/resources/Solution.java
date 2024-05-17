import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] givenDigits = scanner.nextLine().split(" ");
        System.out.println(Integer.parseInt(givenDigits[0]) + Integer.parseInt(givenDigits[1]));
    }
}