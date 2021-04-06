import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Gaus {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("File name: ");
        Scanner fileScanner = new Scanner(new File(new Scanner(System.in).next().trim() + ".txt"));
        final int h = fileScanner.nextInt();
        final int w = fileScanner.nextInt();
        SimpleFraction[][] last = new SimpleFraction[h][w];
        DualMap<Integer, Integer> basis = new DualMap<>(); // номер строки : номер x
        String[] s;
        boolean containBasis = false;
        boolean cleanColumn, needPositive, bIsNull;
        SimpleFraction min, now;
        int x = 0, y;

        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                s = fileScanner.next().split("/");
                if(s.length == 1){
                    last[i][j] = new SimpleFraction(Integer.parseInt(s[0]));
                } else{
                    last[i][j] = new SimpleFraction(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
                }
            }
        }

        for(int i = 0, ind = -1; i < h - 1; i++, containBasis = false){
            needPositive = !last[i][w - 1].isPositive();
            bIsNull = last[i][w - 1].isNull();
            for(int j = 0; j < w - 1; j++){
                if(last[i][j].mayBeBasis() && (last[i][j].isPositive() == needPositive || bIsNull)){
                    cleanColumn = true;
                    for(int k = 0; k < h; k++){
                        if(k != i && !last[k][j].isNull()){
                            cleanColumn = false;
                            break;
                        }
                    }
                    if(cleanColumn) {
                        if (!last[i][j].isPositive()) {
                            last[i] = Arrays.stream(last[i]).map(SimpleFraction::inversion).toArray(SimpleFraction[]::new);
                        }
                        ind = j;
                        containBasis = true;
                        break;
                    }
                }
            }
            if(containBasis){
                basis.put(i, ind);
            } else{
                int b = 0;
                while (b < w - 1
                        && (basis.containsValue(b)
                        || last[i][b].isNull()
                        || last[i][b].isPositive() != needPositive && !bIsNull)){
                    b++;
                }
                if(b == w - 1) {
                    if (!last[i][w - 1].isNull()) {
                        System.out.println("Нет решений!");
                        return;
                    }
                    continue;
                }
                last = makeBasis(last, i, b);
                basis.put(i, b);
            }
        }

        for (SimpleFraction[] simpleFractions : last) {
            if (Arrays.stream(simpleFractions).limit(w - 1).allMatch(SimpleFraction::isNull)
                    && !simpleFractions[w - 1].isNull()) {
                System.out.println("Нет решений!");
                return;
            }
        }

        for(int i = 0; i < w - 1; i++){
            last[h - 1][i] = last[h - 1][i].inversion();
        }

        while (Arrays.stream(last).limit(h - 1).anyMatch(n -> !n[w - 1].isPositive())){
            printSymplix(last, basis);
            min = last[0][w - 1];
            y = 0;
            for(int i = 0; i < h - 1; i++){
                if(last[i][w - 1].compareTo(min) < 0){
                    y = i;
                    min = last[i][w - 1];
                }
            }
            min = new SimpleFraction(Integer.MAX_VALUE);
            for(int i = 0; i < w - 1; i++){
                if(!last[y][i].isPositive() && last[h - 1][i].isPositive() && !last[h - 1][i].isNull()) { // в Z строке брать только положительные?
                    now = last[h - 1][i].div(last[y][i].inversion());
                    if (min.compareTo(now) > 0) {
                        min = now;
                        x = i;
                    }
                }
            }
            last = makeBasis(last, y, x);
            basis.put(y, x);
        }
        printSymplix(last, basis);
    }

    private static void printMatrix(SimpleFraction[][] matrix){
        Arrays.stream(matrix)
                .limit(matrix.length - 1)
                .filter(now -> !Arrays.stream(now).allMatch(SimpleFraction::isNull))
                .forEach(now -> System.out.println(Arrays.toString(now)));
        System.out.println("----------------------------");
    }

    private static void printSymplix(SimpleFraction[][] matrix, DualMap<Integer, Integer> map){
        System.out.printf("б.п.|%-6d| ", 1);
        final int w = matrix[0].length;
        final int h = matrix.length;
        for (int i = 0; i < w - 1; i++){
            System.out.printf("x%-5d", i);
        }
        System.out.println();
        System.out.println("---------------------------------------------");
        final int size = map.size();
        for(int i = 0; i < size; i++){
            System.out.print('x');
            System.out.print(map.get(i));
            System.out.printf("  |%-6s| ", matrix[i][w - 1].toString());
            for(int j = 0; j < w - 1; j++){
                System.out.printf("%-6s", matrix[i][j].toString());
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------");
        System.out.printf("Z   |%-6s| ", matrix[h - 1][w - 1].toString());
        for(int i = 0; i < w - 1; i++){
            System.out.printf("%-6s", matrix[h - 1][i].toString());
        }
        System.out.println();
        System.out.println();
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
