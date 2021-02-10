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
            for (int n : PrimeNumber.getInstance().primeDivisors(denominator)) {
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
        for(int n: PrimeNumber.getInstance().primeDivisors(nominator)){
            if(denominator % n == 0){
                nominator /= n;
                denominator /= n;
            }
        }
        SimpleFraction now = new SimpleFraction(this);
        now.nominator *= other.nominator;
        now.denominator *= other.denominator;
        if(now.denominator < 0){
            now.denominator *= -1;
            now.inversion();
        }
        return now;
    }

    public SimpleFraction div(SimpleFraction other){
        for(int n: PrimeNumber.getInstance().primeDivisors(nominator)){
            if(denominator % n == 0){
                nominator /= n;
                denominator /= n;
            }
        }
        SimpleFraction now = new SimpleFraction(this);
        now.nominator *= other.denominator;
        now.denominator *= other.nominator;
        if(now.denominator < 0){
            now.denominator *= -1;
            now.inversion();
        }
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

    public boolean isPositive(){ return nominator >= 0; }

    @Override
    public String toString() {
        int nom = nominator;
        int den = denominator;
        for(int n: PrimeNumber.getInstance().primeDivisors(nom)){
            if(den % n == 0){
                nom /= n;
                den /= n;
            }
        }
        return (den == 1 || nom == 0? Integer.toString(nom) : nom + "/" + den);
    }
}
