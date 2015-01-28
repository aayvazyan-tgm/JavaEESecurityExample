package servlet;

import FinderStrategies.PrimeFinder;

import java.math.BigInteger;

/**
 * Finder finds primes using a defined PrimeFinder
 * <p/>
 * Author: Ari Michael Ayvazyan
 * Date: 18.03.14
 */
public class Finder implements Runnable, Stoppable {
    private boolean stop = false;
    /**
     * lastPrime describes the last Prime Found, it is Important to Initialise it with a Default value!
     */
    private BigInteger lastPrime = BigInteger.ZERO;
    private PrimeFinder pf;
    private long counter = 0;

    /**
     * @param pf the PrimeFinder used to find primes.
     */
    public Finder(PrimeFinder pf) {
        this.pf = pf;
    }

    /**
     * @return returns the amount of primes Found
     */
    public long getCounter() {
        return counter;
    }

    /**
     * Starts to search for a Prime until stop is set to false
     */
    @Override
    public void run() {
        while (this.stop == false) {
            lastPrime = pf.nextPrime();
            counter++;
            try {
                Thread.sleep(10);//Slows down the search to keep the cpu workload low
            } catch (Exception e) {
                break;
            }
        }
    }

    /**
     * @see servlet.Stoppable
     */
    @Override
    public void stop() {
        this.stop = true;
    }

    /**
     * @return returns the last prime found. (Zero if none Found yet)
     */
    public BigInteger getLastPrime() {
        return lastPrime;
    }
}
