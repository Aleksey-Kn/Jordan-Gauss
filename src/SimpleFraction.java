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

    public SimpleFraction multi(SimpleFraction other){
        SimpleFraction now = new SimpleFraction(this);
        now.nominator *= other.nominator;
        now.denominator *= other.denominator;
        return now;
    }

    public SimpleFraction div(SimpleFraction other){
        SimpleFraction now = new SimpleFraction(this);
        now.nominator *= other.denominator;
        now.denominator *= other.nominator;
        return now;
    }

    public SimpleFraction plus(SimpleFraction other){
        SimpleFraction oth = new SimpleFraction(other);
        SimpleFraction now = new SimpleFraction(this);
        now.toOneDenominator(oth);
        now.nominator += oth.nominator;
        return now;
    }

    public SimpleFraction minus(SimpleFraction other){
        SimpleFraction oth = new SimpleFraction(other);
        SimpleFraction now = new SimpleFraction(this);
        now.toOneDenominator(oth);
        now.nominator -= oth.nominator;
        return now;
    }

    public boolean isNull(){
        return nominator == 0;
    }

    @Override
    public String toString() {
        for(int n: primeNumber.primeDivisors(nominator)){
            if(denominator % n == 0){
                nominator/= n;
                denominator /= n;
            }
        }
        return (denominator == 1 || nominator == 0? Integer.toString(nominator) : nominator + "/" + denominator);
    }
}
