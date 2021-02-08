import java.util.Arrays;
import java.util.Scanner;

public class Gaus {
    public static void main(String[] args) {
        Scanner fileScanner = new Scanner("file.txt");
        SimpleFraction[][] last = new SimpleFraction[fileScanner.nextInt()][fileScanner.nextInt()];
        SimpleFraction[][] now = new SimpleFraction[last.length][last[0].length];
        for(int k = 0; k < last.length; k++){
            if(!last[k][k].isNull()){
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

    }

    private static void printMatrix(SimpleFraction[][] matrix){
        for(SimpleFraction[] arr: matrix){
            System.out.println(Arrays.toString(arr));
        }
        System.out.println("----------------------------");
    }
}
