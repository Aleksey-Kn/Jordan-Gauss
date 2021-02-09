import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Gaus {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("File name: ");
        Scanner fileScanner = new Scanner(new File(new Scanner(System.in).next().trim() + ".txt"));
        SimpleFraction[][] last = new SimpleFraction[fileScanner.nextInt()][fileScanner.nextInt()];
        SimpleFraction[][] now = new SimpleFraction[last.length][last[0].length];
        String[] s;
        SimpleFraction num, temp;
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
                num = last[k][k];
                for(int i = 0; i < last[0].length; i++){ // строка с опорным элементом
                    now[k][i] = last[k][i].div(num);
                }
                for(int j = 0; j < last.length; j++){ // столбец с опорным элементом
                    if(j != k){
                        now[j][k] = new SimpleFraction(0);
                    }
                }
                for(int i = 0; i < last.length; i++){ // всё остальное
                    if(i != k) {
                        for (int j = k + 1; j < last[0].length; j++) {
                            temp = last[i][k].multi(last[k][j]).div(num);
                            now[i][j] = last[i][j].minus(temp);
                        }
                    }
                }
                for (int i = 0; i < last.length; i++){
                    last[i] = now[i].clone();
                }
            }
        }
        printMatrix(last);
        for (SimpleFraction[] simpleFractions : last) {
            if (Arrays.stream(simpleFractions).limit(last[0].length - 1).allMatch(SimpleFraction::isNull)
                    && !simpleFractions[last[0].length - 1].isNull()) {
                System.out.println("Нет решений!");
                return;
            }
        }
        for(int i = 0, j = 0; i < last.length; i++, j = 0){
            while (j < last[0].length && last[i][j].isNull()){
                j++;
            }
            if(j == last[0].length){
                continue;
            }
            System.out.printf("x%d = %s", j + 1, last[i][last[0].length - 1].toString());
            while(j < last[0].length - 2){
                j++;
                if (!last[i][j].isNull()) {
                    last[i][j].inversion();
                    if(last[i][j].isPositive()){
                        System.out.print('+');
                    }
                    System.out.print(last[i][j] + "x" + (j + 1));
                }
            }
            System.out.println();
        }
    }

    private static void printMatrix(SimpleFraction[][] matrix){
        for(SimpleFraction[] arr: matrix){
            if(!Arrays.stream(arr).allMatch(SimpleFraction::isNull)) {
                System.out.println(Arrays.toString(arr));
            }
        }
        System.out.println("----------------------------");
    }
}
