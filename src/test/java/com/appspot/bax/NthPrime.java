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
public class NthPrime {

    final List<Integer> primes = new ArrayList<>();
    long sum;

    private void compute() {
        int i = 2;
        while (primes.size() < 10001) {
            isPrime(i);
            i++;
        }
        System.out.println(primes.get(primes.size() - 1));
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

    public static void main(String[] args) {
        new NthPrime().computeSum();
    }

    private void computeSum() {
        int i = 2;
        while (i <= 2_000_000) {
            if (isPrime(i)) {
                sum += i;
            }
            i++;
        }
        System.out.println(sum);
    }


}
