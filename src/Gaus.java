import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

public class Gaus {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("File name: ");
        Scanner fileScanner = new Scanner(new File(new Scanner(System.in).next().trim() + ".txt"));
        SimpleFraction[][] last = new SimpleFraction[fileScanner.nextInt()][fileScanner.nextInt()];
        int[] basis = new int[last[0].length];
        String[] s;
        boolean containBasis = false;

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

        for(int i = 0, ind; i < last.length; i++, containBasis = false){
            for(int j = 0; j < last[0].length; j++){
                if(last[i][j].mayBeBasis()){
                    if(!last[i][j].isPositive()){
                        for(int k = 0; k < last.length; k++){
                            last[i][k] = last[i][k].inversion();
                        }
                    }
                    ind = j;
                    containBasis = true;
                    break;
                }
            }
            if(containBasis){

            }
        }

        for (SimpleFraction[] simpleFractions : last) {
            if (Arrays.stream(simpleFractions).limit(last[0].length - 1).allMatch(SimpleFraction::isNull)
                    && !simpleFractions[last[0].length - 1].isNull()) {
                System.out.println("Нет решений!");
                return;
            }
        }



    }

    private static void printMatrix(SimpleFraction[][] matrix){
        for(SimpleFraction[] arr: matrix){
            //if(!Arrays.stream(arr).allMatch(SimpleFraction::isNull)) {
                System.out.println(Arrays.toString(arr));
            //}
        }
        System.out.println("----------------------------");
    }

    private static int f(int in){
        return IntStream.rangeClosed(1, in).reduce((x, y) -> x * y).orElse(1);
    }

    private static void makeBasis(SimpleFraction[][] last, int y, int x){
        SimpleFraction[][] now = new SimpleFraction[last.length][last[0].length];
        if(!last[y][x].isNull()){
            printMatrix(last);
            SimpleFraction num = last[y][x];
            for(int i = 0; i < last[0].length; i++){ // строка с опорным элементом
                now[y][i] = last[y][i].div(num);
            }
            for(int j = 0; j < last.length; j++){ // столбец с опорным элементом
                if(j != y){
                    now[j][x] = new SimpleFraction(0);
                }
            }
            for(int i = 0; i < last.length; i++){ // всё остальное
                if(i != y) {
                    for (int j = 0; j < last[0].length; j++) {
                        if(j != x)
                        now[i][j] = last[i][j].minus(last[i][x].multi(last[y][j]).div(num));
                    }
                }
            }
            for (int i = 0; i < last.length; i++){
                last[i] = now[i].clone();
            }
        }
    }
}
