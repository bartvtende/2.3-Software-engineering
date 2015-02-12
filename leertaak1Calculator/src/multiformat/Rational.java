package multiformat;

/**
 * Class representing a rational ('breuk').
 *
 * @author Bart van 't Ende and Jan-Bert van Slochteren
 * @version 1.0
 */
public class Rational {
    static final double PRECISION = 10;
    static final double EPSILON = Math.pow(10, -PRECISION);

    double numerator = 0.0; // teller
    double denominator = 1.0; // noemer

    /**
     * Create a new Rational
     *
     * @param num Numerator
     * @param den Denominator
     */
    public Rational(double num, double den) {
        numerator = num;
        denominator = den;
        simplify();
    }

    /**
     * Create a new Rational
     */
    public Rational() {
    }


    /**
     * Create a new Rational
     *
     * @param number
     */
    public Rational(double number) {
        numerator = number;
        denominator = 1.0;
        canonical();
        simplify();
    }

    /**
     * Get rid of any decimals in the numerator. E.g. 12.5/1.0 becomes 125.0/10.0
     * (Note that any decimals in the denominator aren't handled, eg 10/0.5. This seems an omission.)
     * Seen also unittest TestRational.java
     */
    public void canonical() {
        double num = Math.abs(numerator);
        double decimal = num - Math.floor(num);
        int num_digits = 0;

        while (decimal > EPSILON && num_digits < PRECISION) {
            num = num * 10;
            decimal = num - Math.floor(num);
            num_digits++;
        }

        numerator = numerator * Math.pow(10.0, num_digits);
        denominator = denominator * Math.pow(10.0, num_digits);
    }

    /**
     * Simplify the rational. 125/10 becomes 25/2.
     * Seen also unittest TestRational.java
     */
    public void simplify() {
        // Take the smallest from the two (10.0)
        double divisor = Math.min(Math.abs(numerator), denominator);
        // Step from 10.0 to 9.0 to ... 1.0
        for (; divisor > 1.0; divisor -= 1.0) {
            double rn = Math.abs(
                    Math.IEEEremainder(Math.abs(numerator), divisor));
            double rd = Math.abs(
                    Math.IEEEremainder(denominator, divisor));
            // If both the numerator and denominator have a very small remainder
            // then they can both be divided by devisor (in our example 5).
            if (rn < EPSILON && rd < EPSILON) {
                numerator /= divisor;
                denominator /= divisor;
                divisor = Math.min(Math.abs(numerator), denominator);
            }
        }
    }

    /**
     * Add two rationals
     *
     * @param other Another Rational to add to this.
     * @return A new Rational representing the sum.
     */
    public Rational plus(Rational other) {
        if (denominator == other.denominator)
            return new Rational(numerator + other.numerator
                    , other.denominator);
        else
            // a/x + b/y =
            // (breuken gelijknamig maken)
            // a*y/x*y + b*x/x*y = (a*y + b*x)/x*y
            return new Rational(numerator * other.denominator +
                    denominator * other.numerator
                    , denominator * other.denominator);
    }

    /**
     * Subtracts two rationals
     *
     * @param other Another Rational to add to this.
     * @return A new Rational representing the subtraction.
     */
    public Rational minus(Rational other) {
        if (denominator == other.denominator)
            return new Rational(numerator - other.numerator, denominator);
        else
            return new Rational(numerator * other.denominator -
                    denominator * other.numerator
                    , denominator * other.denominator);
    }

    /**
     * Multiplies two rationals
     *
     * @param other Another Rational to add to this.
     * @return A new Rational representing the multiplication.
     */
    public Rational mul(Rational other) {
        return new Rational(
                numerator * other.numerator,
                denominator * other.denominator);
    }

    /**
     * Divides two rationals
     *
     * @param other Another Rational to add to this.
     * @return A new Rational representing the division.
     */
    public Rational div(Rational other) {
        return new Rational(
                numerator * other.denominator,
                denominator * other.numerator);
    }

    public void copyOf(Rational other) {
        this.numerator = other.numerator;
        this.denominator = other.denominator;
    }

    /**
     * Getter for the numerator
     *
     * @return
     */
    public double getNumerator() {
        return numerator;
    }

    /**
     * Getter for the demoninator
     *
     * @return
     */
    public double getDenominator() {
        return denominator;
    }

    /**
     * Setter for the numerator
     *
     * @param num
     */
    public void setNumerator(double num) {
        numerator = num;
    }

    /**
     * Setter for the denumerator
     *
     * @param den
     */
    public void setDenominator(double den) {
        denominator = den;
    }
}