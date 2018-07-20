package com.navigamez.vuepeople.util;

import java.util.Random;

public class RandomManager {

    private static final ThreadLocal<Random> randomLocal = ThreadLocal.withInitial(Random::new);

    public static Random get() {
        return randomLocal.get();
    }
}
