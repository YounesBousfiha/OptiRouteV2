#!/bin/bash

# === CONFIG ===
POSTMAN_API_KEY="${POSTMAN_API_KEY}"   # set env variable
COLLECTION_ID="32927173-43cd8144-0cd0-4667-b20d-c27832425b83"
SWAGGER_URL="http://localhost:8080/v3/api-docs"
COLLECTION_FILE="postman_collection.json"
TMP_FILE="tmp_postman.json"

# === CHECK REQUIRED TOOLS ===
for cmd in curl jq openapi2postmanv2; do
  if ! command -v $cmd &>/dev/null; then
    echo "❌ '$cmd' is not installed."
    exit 1
  fi
done

# === CHECK API KEY ===
if [ -z "$POSTMAN_API_KEY" ]; then
  echo "❌ Missing POSTMAN_API_KEY env variable."
  exit 1
fi

echo "🌐 Downloading Swagger JSON from ${SWAGGER_URL}..."
curl -s "$SWAGGER_URL" -o swagger.json
if [ $? -ne 0 ]; then
  echo "❌ Failed to download Swagger JSON."
  exit 1
fi

echo "🛠️ Converting Swagger to Postman collection..."
openapi2postmanv2 -s swagger.json -o "$COLLECTION_FILE" -p
if [ $? -ne 0 ]; then
  echo "❌ Conversion failed."
  exit 1
fi

echo "📤 Updating collection on Postman..."
jq '{ collection: . }' "$COLLECTION_FILE" > "$TMP_FILE"

response=$(curl -s -o /dev/null -w "%{http_code}" \
  -X PUT "https://api.getpostman.com/collections/${COLLECTION_ID}" \
  -H "X-Api-Key: ${POSTMAN_API_KEY}" \
  -H "Content-Type: application/json" \
  -d @"$TMP_FILE")

if [ "$response" -eq 200 ]; then
  echo "✅ Collection successfully updated on Postman."
else
  echo "❌ Failed to update collection. HTTP code: $response"
  exit 1
fi

# Clean up
rm -f "$TMP_FILE" swagger.json
echo "🧹 Cleaned temporary files."
echo "🎉 Done!"

