import sys
import requests
import json

API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent"

def get_gemini_response(api_key, transcript, user_prompt=None):
    url = f"{API_URL}?key={api_key}"
    headers = {"Content-Type": "application/json"}

    # If user_prompt is provided, use it for chat; otherwise, ask for a summary
    if user_prompt:
        prompt = f"Here is a transcript:\n{transcript}\n\n{user_prompt}"
    else:
        prompt = f"Summarize the following transcript:\n{transcript}"

    data = {
        "contents": [
            {
                "parts": [
                    {"text": prompt}
                ]
            }
        ]
    }

    try:
        response = requests.post(url, headers=headers, data=json.dumps(data))
        response.raise_for_status()
        result = response.json()
        # Extract the generated text from the response
        if "candidates" in result:
            candidate = result["candidates"][0]
            content = candidate.get("content", {})
            if "parts" in content:
                return content["parts"][0].get("text", "")
        return result
    except Exception as e:
        return f"Error: {e}"

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python gemini_api_client.py <API_KEY> <transcript_file> [user_prompt]")
        sys.exit(1)

    api_key = sys.argv[1]
    transcript_file = sys.argv[2]
    user_prompt = sys.argv[3] if len(sys.argv) > 3 else None

    # Read transcript from file
    with open(transcript_file, "r", encoding="utf-8") as f:
        transcript = f.read()

    result = get_gemini_response(api_key, transcript, user_prompt)
    print(result)
