#!/bin/sh
set -a
. /vault-init/.env.vault
set +a

echo "Initializing Vault secrets..."

vault kv put secret/application \
  SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/${DB_NAME} \
  SPRING_DATASOURCE_USERNAME=${DB_USER} \
  SPRING_DATASOURCE_PASSWORD=${DB_PASS} \
  SPRING_AI_API_KEY=${DEEPSEEK_APIKEY}

echo "✅ Secrets securely added to Vault"