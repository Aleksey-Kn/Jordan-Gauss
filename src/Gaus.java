import java.util.Arrays;
import java.util.Scanner;

public class Gaus {
    public static void main(String[] args) {
        Scanner fileScanner = new Scanner("file.txt");
        SimpleFraction[][] last = new SimpleFraction[fileScanner.nextInt()][fileScanner.nextInt()];
        SimpleFraction[][] now = new SimpleFraction[last.length][last[0].length];
        String[] s;
        for(int i = 0; i < last.length; i++){
            for(int j = 0; j < last[0].length; j++){
                s = fileScanner.next().split("/");
                if(s.length == 1){
                    last[i][j] = new SimpleFraction(Integer.parseInt(s[0]));
                } else{
                    last[i][j] = new SimpleFraction(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
                }
            }
        }
        for(int k = 0; k < last.length; k++){
            if(!last[k][k].isNull()){
                printMatrix(last);
                for(int j = 0; k < last.length; k++){
                    if(j == k){
                        now[j][k] = new SimpleFraction(1);
                    }else{
                        now[j][k] = new SimpleFraction(0);
                    }
                }
                for(int i = 0; i < last.length; i++){
                    for(int j = k + 1; j < last[0].length; j++){
                        now[i][j] = last[i][j].minus(last[k][j].multi(last[i][k]).div(last[i][j]));
                    }
                }
                last = now;
            }
        }
        int[] count = new int[last.length];
        boolean[][] contains = new boolean[last.length][last[0].length];
        for(int i = 0; i < last.length; i++){
            count[i] = (int)Arrays.stream(last[i]).limit(last[0].length - 1).filter(o -> !o.isNull()).count();
            for (int j = 0; j < last[0].length - 1; j++){
                contains[i][j] = !last[i][j].isNull();
            }
        }
        if(Arrays.stream(count).anyMatch(o -> o == 0)){
            System.out.println("Нет решений");
            return;
        }
    }

    private static void printMatrix(SimpleFraction[][] matrix){
        for(SimpleFraction[] arr: matrix){
            System.out.println(Arrays.toString(arr));
        }
        System.out.println("----------------------------");
    }
}
