package com.mayur.offline_UPI_system.offline;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class OfflineSession {

    private static final String SESSION_FILE = "offline-data/session.txt";

    public static void saveSession(
            int userId,
            String token) {

        try {

            File directory = new File("offline-data");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String session = userId + "\n" + token;

            Files.writeString(
                    Path.of(SESSION_FILE),
                    session);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to save offline session", e);
        }
    }

    public static int getUserId() {

        try {

            String content = Files.readString(
                    Path.of(SESSION_FILE));

            String[] lines = content.split("\\R");

            return Integer.parseInt(
                    lines[0].trim());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Offline session not found. "
                            + "Login while online first.",
                    e);
        }
    }

    public static String getToken() {

        try {

            String content = Files.readString(
                    Path.of(SESSION_FILE));

            String[] lines = content.split("\\R", 2);

            if (lines.length < 2) {
                throw new RuntimeException(
                        "JWT not found");
            }

            return lines[1].trim();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Offline authentication token "
                            + "not found.",
                    e);
        }
    }
}