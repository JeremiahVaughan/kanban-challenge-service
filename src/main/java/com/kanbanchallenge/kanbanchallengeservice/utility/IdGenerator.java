package com.kanbanchallenge.kanbanchallengeservice.utility;

import java.util.UUID;

public class IdGenerator {
    public static String createId() {
        return UUID.randomUUID().toString();
    }
}
