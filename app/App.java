public class App {
    public static void main(String[] args) {
        String videoID = args.length > 0 ? args[0] : "";
        String format = args.length > 1 ? args[1] : "text";
        String language = args.length > 2 ? args[2] : "en";
        YoutubeAPIClient client = new YoutubeAPIClient();
        String transcript = client.getTranscript(videoID, format, language);
        System.out.println(transcript);
    }
}
