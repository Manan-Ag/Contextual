import sys
import json
from youtube_transcript_api import YouTubeTranscriptApi
from youtube_transcript_api.formatters import TextFormatter, JSONFormatter

print("sys.argv:", sys.argv, file=sys.stderr)
if len(sys.argv) < 2:
    print("Usage: get_transcript.py <video_id> [format] [lang]")
    sys.exit(1)

video_id = sys.argv[1]
output_format = sys.argv[2] if len(sys.argv) > 2 else "text"
language = sys.argv[3] if len(sys.argv) > 3 else "en"

try:
    ytt_api = YouTubeTranscriptApi()
    transcript = ytt_api.fetch(video_id, languages=[language])
    if output_format == "json":
        formatter = JSONFormatter()
        print(formatter.format_transcript(transcript, indent=2))
    else:
        formatter = TextFormatter()
        print(formatter.format_transcript(transcript))
except Exception as e:
    print(f"Error: {e}")
    sys.exit(2)
