public class SimpleFraction {
    private long nominator;
    private long denominator;

    public SimpleFraction(int n, int d) {
        long nd = nod(n, d);
        nominator = n / nd;
        denominator = d / nd;
    }

    public SimpleFraction(int number) {
        nominator = number;
        denominator = 1;
    }

    public SimpleFraction(SimpleFraction other) {
        nominator = other.nominator;
        denominator = other.denominator;
    }

    private long nod(long f, long s){
        f = Math.abs(f);
        s = Math.abs(s);
        while (f != 0 && s != 0){
            if(f > s){
                f %= s;
            } else {
                s %= f;
            }
        }
        return f + s;
    }

    private void minimisation(){
        long n = nominator;
        long d = denominator;
        long nd = nod(n, d);
        nominator /= nd;
        denominator /= nd;
    }

    private void toOneDenominator(SimpleFraction other) {
        if (other.nominator != 0) {
            long resultDenominator = denominator * other.denominator / nod(denominator, other.denominator);
            nominator *= resultDenominator / denominator;
            other.nominator *= resultDenominator / other.denominator;
            denominator = resultDenominator;
            other.denominator = resultDenominator;
        }
    }

    public void inversion() {
        nominator *= -1;
    }

    public SimpleFraction multi(SimpleFraction other) {
        minimisation();
        SimpleFraction now = new SimpleFraction(this);
        now.nominator *= other.nominator;
        now.denominator *= other.denominator;
        if (now.denominator < 0) {
            now.denominator *= -1;
            now.inversion();
        }
        return now;
    }

    public SimpleFraction div(SimpleFraction other) {
        minimisation();
        SimpleFraction now = new SimpleFraction(this);
        now.nominator *= other.denominator;
        now.denominator *= other.nominator;
        if (now.denominator < 0) {
            now.denominator *= -1;
            now.inversion();
        }
        return now;
    }

    public SimpleFraction plus(SimpleFraction other) {
        SimpleFraction oth = new SimpleFraction(other);
        SimpleFraction now = new SimpleFraction(this);
        now.toOneDenominator(oth);
        now.nominator += oth.nominator;
        return now;
    }

    public SimpleFraction minus(SimpleFraction other) {
        SimpleFraction oth = new SimpleFraction(other);
        SimpleFraction now = new SimpleFraction(this);
        now.toOneDenominator(oth);
        now.nominator -= oth.nominator;
        return now;
    }

    public boolean isNull() {
        return nominator == 0;
    }

    public boolean isPositive() {
        return nominator >= 0;
    }

    @Override
    public String toString() {
        long nom = nominator;
        long den = denominator;
        long nd = nod(nom, den);
        nom /= nd;
        den /= nd;
        return (den == 1 || nom == 0 ? Long.toString(nom) : nom + "/" + den);
    }
}
