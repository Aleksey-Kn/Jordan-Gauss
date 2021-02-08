public class SimpleFraction {
    private int nominator;
    private int denominator;
    private final PrimeNumber primeNumber = new PrimeNumber();

    public SimpleFraction(int n, int d){
        nominator = n;
        denominator = d;
    }

    public SimpleFraction(int number){
        nominator = number;
        denominator = 1;
    }

    public SimpleFraction(SimpleFraction other){
        nominator = other.nominator;
        denominator = other.denominator;
    }

    private void toOneDenominator(SimpleFraction other){
        int d = other.denominator;
        for(int n: primeNumber.primeDivisors(denominator)){
            if(d % n == 0){
                d /= n;
            }
        }
        int resultDenominator = d * denominator;
        nominator *= resultDenominator / denominator;
        other.nominator *= resultDenominator / other.denominator;
        denominator = resultDenominator;
        other.denominator = resultDenominator;
    }

    public void multi(SimpleFraction other){
        nominator *= other.nominator;
        denominator *= other.denominator;
    }

    public void div(SimpleFraction other){
        nominator *= other.denominator;
        denominator *= other.nominator;
    }

    public void plus(SimpleFraction other){
        SimpleFraction oth = new SimpleFraction(other);
        toOneDenominator(oth);
        nominator += oth.nominator;
    }

    public void minus(SimpleFraction other){
        SimpleFraction oth = new SimpleFraction(other);
        toOneDenominator(oth);
        nominator -= oth.nominator;
    }

    @Override
    public String toString() {
        for(int n: primeNumber.primeDivisors(nominator)){
            if(denominator % n == 0){
                nominator/= n;
                denominator /= n;
            }
        }
        return (denominator == 1? Integer.toString(nominator) : nominator + "/" + denominator);
    }
}
