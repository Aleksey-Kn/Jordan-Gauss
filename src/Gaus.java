import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

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
        HashMap<Integer, SimpleFraction>[] buffer;
        for(int i = n - r; i < n; i++){
            combination[i] = true;
        }
        localCombination = combination.clone();
        for(int it = 0; it < c; it++, localCombination = nextPermutation(combination)) {
            buffer = new HashMap[n];
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
                    buffer[j] = new HashMap<>(last[i].length);
                    buffer[j].put(-1, last[i][last[i].length - 1].div(last[i][j]));
                    for (int k = 0; k < last[0].length - 1; k++) {
                        if (k != j && combination[k] && !last[i][k].isNull()) {
                            buffer[j].put(k, last[i][k].div(last[i][j]).inversion());
                        }
                    }
                }
            }
            if (buffer != null) {
                boolean flag = false;
                for(int i = 0; i < n; i++){
                    if(buffer[i] != null){
                        HashMap<Integer, SimpleFraction>[] finalBuffer = buffer;
                        int finalI = i;
                        if(buffer[i].keySet().stream()
                                .filter(o -> o > 0)
                                .flatMap(ind -> finalBuffer[ind].keySet().stream())
                                .anyMatch(o -> o.equals(finalI))){
                            flag = true;
                            break;
                        }
                    }
                }
                if(!flag) {
                    for (int number = 0; number < n; number++) {
                        if (buffer[number] != null) {
                            System.out.println("x" + number + "= " + count(buffer, number).toString());
                        }
                    }
                    System.out.println();
                }
            }
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

    private static SimpleFraction count(HashMap<Integer, SimpleFraction>[] data, int index){
        HashMap<Integer, SimpleFraction> now = data[index];
        if(now.size() == 1){
            return now.values().iterator().next();
        }
        SimpleFraction result = new SimpleFraction(0);
        for(Map.Entry<Integer, SimpleFraction> entry: now.entrySet()){
            result = result.plus((entry.getKey() == -1? entry.getValue():
                    count(data, entry.getKey()).multi(entry.getValue()))); // складываем все переменные, умножая на их значения, входящие в состав данной
        }
        return result;
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
