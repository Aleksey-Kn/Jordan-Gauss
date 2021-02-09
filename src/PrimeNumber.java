import java.util.LinkedList;
import java.util.List;

public class PrimeNumber {
    private static int limit = 3;
    private static final LinkedList<Integer> prime = new LinkedList<>(List.of(2, 3));

    public static LinkedList<Integer> primeDivisors(int number){
        if(number > limit){
            count(number);
        }
        LinkedList<Integer> result = new LinkedList<>();
        if(number != 0) {
            while (Math.abs(number) != 1) {
                for (int p : prime) {
                    if (number % p == 0) {
                        number /= p;
                        result.add(p);
                        break;
                    }
                }
            }
        }
        return result;
    }

    private static void count(int newLimit){
        boolean flag;
        for(int i = limit + 1; i <= newLimit; i++){
            flag = true;
            for(int p: prime){
                if(i % p == 0){
                    flag = false;
                    break;
                }
            }
            if(flag){
                prime.add(i);
            }
        }
        limit = newLimit;
    }
}
