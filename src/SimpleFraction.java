public class SimpleFraction {
    private int nominator;
    private int denominator;

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
        if(other.nominator != 0) {
            int d = other.denominator;
            for (int n : PrimeNumber.primeDivisors(denominator)) {
                if (d % n == 0) {
                    d /= n;
                }
            }
            int resultDenominator = d * denominator;
            nominator *= resultDenominator / denominator;
            other.nominator *= resultDenominator / other.denominator;
            denominator = resultDenominator;
            other.denominator = resultDenominator;
        }
    }

    public void inversion(){
        nominator *= -1;
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
        int n = now.nominator;
        int o = other.nominator;
        return new SimpleFraction(n + o, now.denominator);
    }

    public SimpleFraction minus(SimpleFraction other){
        SimpleFraction oth = new SimpleFraction(other);
        SimpleFraction now = new SimpleFraction(this);
        now.toOneDenominator(oth);
        int n = now.nominator;
        int o = other.nominator;
        return new SimpleFraction(n - o, now.denominator);
    }

    public boolean isNull(){
        return nominator == 0;
    }

    @Override
    public String toString() {
        int nom = nominator;
        int den = denominator;
        for(int n: PrimeNumber.primeDivisors(nom)){
            if(den % n == 0){
                nom /= n;
                den /= n;
            }
        }
        return (den == 1 || nom == 0? Integer.toString(nom) : nom + "/" + den);
    }
}
