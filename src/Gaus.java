import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

public class Gaus {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("File name: ");
        Scanner fileScanner = new Scanner(new File(new Scanner(System.in).next().trim() + ".txt"));
        SimpleFraction[][] last = new SimpleFraction[fileScanner.nextInt()][fileScanner.nextInt()];
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

        int r = printMatrix(last);
        int n = last[0].length - 1;
        int c = f(n) / (f(r) * f(n - r));
        for (SimpleFraction[] simpleFractions : last) {
            if (Arrays.stream(simpleFractions).limit(n).allMatch(SimpleFraction::isNull)
                    && !simpleFractions[n].isNull()) {
                System.out.println("Нет решений!");
                return;
            }
        }

        boolean[] combination = new boolean[n];
        boolean[] localCombination;
        HashMap<Integer, Integer> buffer; // ind x: string numb
        for(int i = n - r; i < n; i++){
            combination[i] = true;
        }
        localCombination = combination.clone();
        for(int it = 0; it < c; it++) {
            buffer = new HashMap<>();
            for (int i = 0, j = 0; i < last.length; i++, j = 0) {
                if (!Arrays.stream(last[i]).allMatch(SimpleFraction::isNull)) {
                    while (j < last[0].length - 1 && (last[i][j].isNull() || !localCombination[j])) {
                        j++;
                    }
                    if (j == last[0].length - 1) {
                        buffer = null;
                        break;
                    }
                    localCombination[j] = false;
                    buffer.put(j, i);
                    makeBasis(last, i, j);
                }
            }
            if (buffer != null) {
                for(Map.Entry<Integer, Integer> entry: buffer.entrySet()){
                    System.out.println("x" + (entry.getKey() + 1) + ": " + last[entry.getValue()][last[0].length - 1]);
                }
                System.out.println();
            }
            localCombination = nextPermutation(combination);
        }
    }

    private static int printMatrix(SimpleFraction[][] matrix){
        int count = 0;
        for(SimpleFraction[] arr: matrix){
            if(!Arrays.stream(arr).allMatch(SimpleFraction::isNull)) {
                System.out.println(Arrays.toString(arr));
                count++;
            }
        }
        System.out.println("----------------------------");
        return count;
    }

    private static int f(int in){
        return IntStream.rangeClosed(1, in).reduce((x, y) -> x * y).orElse(1);
    }

    private static void makeBasis(SimpleFraction[][] last, int y, int x){
        SimpleFraction[][] now = new SimpleFraction[last.length][last[0].length];
        SimpleFraction num, temp;
        if(!last[y][x].isNull()){
            num = last[y][x];
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
                        if(j != x) {
                            temp = last[i][x].multi(last[y][j]).div(num);
                            now[i][j] = last[i][j].minus(temp);
                        }
                    }
                }
            }
            for (int i = 0; i < last.length; i++){
                last[i] = now[i].clone();
            }
        }
    }

    private static boolean[] nextPermutation(boolean[] arr){
        if(arr[0]){
            int count = 0, ind = 0;
            while (ind < arr.length && arr[ind]){ // считаем 1 в начале
                arr[ind] = false;
                ind++;
                count++;
            }
            if(ind == arr.length){
                return arr;
            }
            while (!arr[ind]){ // двигаемся до след. 1
                ind++;
            }
            arr[ind - 1] = true;
            arr[ind] = false;
            for(int i = ind - 2, it = 0; i > 0 && it < count; i--, it++){
                arr[i] = true;
            }
        } else{
            int ind = 0;
            while (ind < arr.length && !arr[ind]){
                ind++;
            }
            if(ind == arr.length){
                return arr;
            }
            arr[ind] = false;
            arr[ind - 1] = true;
        }
        return arr.clone();
    }
}
