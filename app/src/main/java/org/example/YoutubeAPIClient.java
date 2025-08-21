import io.github.thoroldvix.youtube.transcript.TranscriptApiFactory;
import io.github.thoroldvix.youtube.transcript.TranscriptContent;
import io.github.thoroldvix.youtube.transcript.TranscriptFormatter;
import io.github.thoroldvix.youtube.transcript.TranscriptFormatters;
import io.github.thoroldvix.youtube.transcript.YoutubeTranscriptApi;
import java.util.concurrent.TimeUnit;

public class YoutubeAPIClient{
    private final YoutubeTranscriptApi api;
    public YoutubeAPIClient() {
        // Initialize the YouTube Transcript API
        this.api = TranscriptApiFactory.createDefault();
    }
    public String getTranscript(String videoID) {
        TranscriptContent transcriptContent = api.getTranscript(videoID);

        // Create custom formatter
        TranscriptFormatter customFormatter = new TranscriptFormatter() {
            @Override
            public String format(TranscriptContent content) {
                StringBuilder sb = new StringBuilder();
                for (TranscriptContent.Fragment fragment : content.getFragments()) {
                    String time = formatTime(fragment.getStart());
                    sb.append(time)
                      .append(" = \"")
                      .append(fragment.getText())
                      .append("\"\n");
                }
                return sb.toString();
            }

            private String formatTime(double seconds) {
                long totalSeconds = (long) seconds;
                long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds);
                long secs = totalSeconds % 60;
                return String.format("%d:%02d", minutes, secs);
            }
        };

        return customFormatter.format(transcriptContent);
    }
}
