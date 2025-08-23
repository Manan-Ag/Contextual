import os
from google import genai
from pytube import YouTube

# Try to read API key from environment
api_key = os.getenv("GOOGLE_API_KEY")

# If not found, fallback to hardcoded (replace with your key)
if not api_key:
    api_key = "AIzaSyBvZB8p9kBhvl9Xvpd80QUzh_o1ETAoWnw"

client = genai.Client(api_key=api_key)

with open("transcript.txt", "r") as file:
    first_line = file.readline().strip()


video_id = first_line.split("'")[3]

resp = client.models.generate_content(
    model="gemini-2.0-flash",
    contents={"This is a text file containing the transcript of a yt video titled {}"
              }
)

print(resp.text)
