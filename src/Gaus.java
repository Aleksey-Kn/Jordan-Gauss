import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Gaus {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new File("file.txt"));
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
                SimpleFraction num = last[k][k];
                for(int i = 0; i < last[0].length; i++){
                    now[k][i] = last[k][i].div(num);
                }
                for(int j = 0; j < last.length; j++){
                    if(j != k){
                        now[j][k] = new SimpleFraction(0);
                    }
                }
                for(int i = 0; i < last.length; i++){
                    if(i != k) {
                        for (int j = k + 1; j < last[0].length; j++) {
                            now[i][j] = last[i][j].minus(last[k][j].multi(last[i][k]).div(last[k][k]));
                        }
                    }
                }
                last = now;
            }
        }
        for(int i = 0; i < last.length; i++){
            if(Arrays.stream(last[i]).limit(last[0].length - 1).allMatch(SimpleFraction::isNull)){
                System.out.println("Нет решений!");
                return;
            }
        }
        for(int i = 0, j = 0; i < last.length; i++){
            while (last[i][j].isNull()){
                j++;
            }
            System.out.printf("x%d = %s", j, last[i][last.length - 1].toString());
            while(j < last.length - 1){
                if (!last[i][j].isNull()) {
                    last[i][j].inversion();
                    System.out.print(last[i][j]);
                }
                j++;
            }
            System.out.println();
        }
    }

    private static void printMatrix(SimpleFraction[][] matrix){
        for(SimpleFraction[] arr: matrix){
            System.out.println(Arrays.toString(arr));
        }
        System.out.println("----------------------------");
    }
}
