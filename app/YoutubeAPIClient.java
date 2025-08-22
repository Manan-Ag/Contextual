package app;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * YoutubeAPIClient fetches YouTube video transcripts by invoking a Python
 * script that uses the youtube-transcript-api package.
 */
public class YoutubeAPIClient {
    /**
     * Constructs a YoutubeAPIClient. No initialization required.
     */
    public YoutubeAPIClient() {
        // No initialization needed for Python process approach
    }

    /**
     * Fetches the transcript for a given YouTube video ID by calling a Python
     * script.
     * Uses default format (text) and language (en).
     *
     * @param videoID The YouTube video ID
     * @return The transcript as a string, or an error message if failed
     */
    public String getTranscript(String videoID) {
        return getTranscript(videoID, "text", "en");
    }

    /**
     * Fetches the transcript for a given YouTube video ID, output format, and
     * language.
     *
     * @param videoID  The YouTube video ID
     * @param format   Output format: "text" or "json"
     * @param language Language code (e.g., "en", "de")
     * @return The transcript as a string, or an error message if failed
     */
    public String getTranscript(String videoID, String format, String language) {
        ProcessBuilder pb = new ProcessBuilder("python3", "get_transcript.py", videoID, format, language);
        pb.redirectErrorStream(true);
        StringBuilder output = new StringBuilder();
        try {
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output.append("Python script exited with code ").append(exitCode);
            }
        } catch (IOException | InterruptedException e) {
            output.append("Error: ").append(e.getMessage());
        }
        return output.toString();
    }
}
