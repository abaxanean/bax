/*
 * Copyright 2015 Mobile Iron, Inc.
 * All rights reserved.
 */

package com.appspot.bax;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Document this class.
 */
public class LargestPrimeFactor {

    final List<Integer> primeFactors = new ArrayList<>();
    final List<Integer> primes = new ArrayList<>();

    final long n;

    public static void main(String[] args) {
        new LargestPrimeFactor(600851475143L).compute();
    }

    public LargestPrimeFactor(final long n) {
        this.n = n;
    }

    public void compute() {
        long sqrt = (long)Math.sqrt(n) + 1;
        for (int i = 2; i <= sqrt; i++) {
            if (isPrime(i) && n % i == 0) {
                primeFactors.add(i);
            }
        }
        System.out.println(primeFactors);
    }

    private boolean isPrime(final int i) {
        int sqrt = (int)Math.sqrt(i);
        for (int prime : primes) {
            if (i % prime == 0) {
                return false;
            }
            if (prime > sqrt) {
                break;
            }
        }
        primes.add(i);
        return true;
    }

}
