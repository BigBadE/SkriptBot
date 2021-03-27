package com.bigbade.skriptbot.testutils;

import java.util.concurrent.ThreadLocalRandom;

public class TestIDHandler {
    private static int id = ThreadLocalRandom.current().nextInt();

    public static int getId() {
        return id++;
    }
}
