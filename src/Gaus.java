import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

public class Gaus {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("File name: ");
        Scanner fileScanner = new Scanner(new File(new Scanner(System.in).next().trim() + ".txt"));
        SimpleFraction[][] last = new SimpleFraction[fileScanner.nextInt()][fileScanner.nextInt()];
        Map<Integer, Integer> basis = new HashMap<>(last.length); // индекс: номер строки
        String[] s;
        boolean containBasis = false;
        boolean cleanColumn;

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

        for(int i = 0, ind = -1; i < last.length - 1; i++, containBasis = false){
            printMatrix(last);
            for(int j = 0; j < last[0].length; j++){
                if(last[i][j].mayBeBasis()){
                    cleanColumn = true;
                    for(int k = 0; k < last.length - 1; k++){
                        if(!last[k][j].isNull()){
                            cleanColumn = false;
                            break;
                        }
                    }
                    if(cleanColumn) {
                        if (!last[i][j].isPositive()) {
                            for (int k = 0; k < last.length; k++) {
                                last[i][k] = last[i][k].inversion();
                            }
                        }
                        ind = j;
                        containBasis = true;
                        break;
                    }
                }
            }
            if(containBasis){
                basis.put(ind, i);
            } else{
                int b = 0;
                while (b < last[0].length - 1 && (basis.containsKey(b) || last[i][b].isNull())){
                    b++;
                }
                if(b == last[i].length - 1) {
                    if (!last[i][last[i].length - 1].isNull()) {
                        System.out.println("Нет решений!");
                        return;
                    }
                    continue;
                }
                last = makeBasis(last, i, b);
                basis.put(b, i);
            }
        }

        for (SimpleFraction[] simpleFractions : last) {
            if (Arrays.stream(simpleFractions).limit(last[0].length - 1).allMatch(SimpleFraction::isNull)
                    && !simpleFractions[last[0].length - 1].isNull()) {
                System.out.println("Нет решений!");
                return;
            }
        }

        printMatrix(last);
        basis.forEach((k, v) -> System.out.println(v + " str.: " + k));

    }

    private static void printMatrix(SimpleFraction[][] matrix){
        for(int i = 0; i < matrix.length - 1; i++){
            if(!Arrays.stream(matrix[i]).allMatch(SimpleFraction::isNull)) {
                System.out.println(Arrays.toString(matrix[i]));
            }
        }
        System.out.println("----------------------------");
    }

    private static int f(int in){
        return IntStream.rangeClosed(1, in).reduce((x, y) -> x * y).orElse(1);
    }

    private static SimpleFraction[][] makeBasis(SimpleFraction[][] last, int y, int x){
        SimpleFraction[][] now = new SimpleFraction[last.length][last[0].length];
        if(!last[y][x].isNull()){
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
        }
        return now;
    }
}
