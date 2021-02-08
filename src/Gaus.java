import java.util.Scanner;

public class Gaus {
    public static void main(String[] args) {
        //Scanner fileScanner = new Scanner("file.txt");
        SimpleFraction first = new SimpleFraction(7, 2);
        SimpleFraction second = new SimpleFraction(8, 3);
        first.plus(second);
        System.out.println(first.toString());

    }
}
