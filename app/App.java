package app;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) {
        String videoID = args.length > 0 ? args[0] : "";
        String format = args.length > 1 ? args[1] : "custom"; // use custom for timestamped transcript
        String language = args.length > 2 ? args[2] : "en";
        String userPrompt = args.length > 3 ? args[3] : null;
        String geminiApiKey = "AIzaSyAjZyfLMucqZtRS-_rDUljofEKoa5b5QDo"; // Hardcoded API key

        // 1. Fetch transcript
        YoutubeAPIClient ytClient = new YoutubeAPIClient();
        String transcript = ytClient.getTranscript(videoID, format, language);
        String transcriptFile = "transcript.txt";
        try (FileWriter writer = new FileWriter(transcriptFile)) {
            writer.write(transcript);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            return;
        }

        // 2. Call gemini_api_client.py with API key, transcript file, and optional user
        // prompt
        try {
            ProcessBuilder pb;
            if (userPrompt != null) {
                pb = new ProcessBuilder("python", "gemini_api_client.py", geminiApiKey, transcriptFile, userPrompt);
            } else {
                pb = new ProcessBuilder("python", "gemini_api_client.py", geminiApiKey, transcriptFile);
            }
            pb.redirectErrorStream(true);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Gemini API script exited with code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error running Gemini API script: " + e.getMessage());
        }
    }
}
