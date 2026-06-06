#!/bin/bash
set -e

echo "🇲🇦 Match Maroc 2026 — Déploiement"
echo "====================================="

# 1. Vérifier .env
if [ ! -f .env ]; then
  echo "⚠️  Fichier .env manquant. Copie de .env.example..."
  cp .env.example .env
  echo "📝 Éditez .env avec vos vraies valeurs, puis relancez: ./deploy.sh"
  exit 1
fi

# 2. Build + lancement
echo "🔨 Build des images Docker..."
docker compose build --no-cache

echo "🚀 Lancement des services..."
docker compose up -d

echo "⏳ Attente que le backend soit prêt..."
sleep 15

# 3. Health check
STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/health)
if [ "$STATUS" = "200" ]; then
  echo "✅ Backend OK"
else
  echo "❌ Backend non disponible (status: $STATUS)"
  docker compose logs backend
  exit 1
fi

echo ""
echo "🎉 Déploiement réussi !"
echo "   Frontend : http://localhost"
echo "   Backend  : http://localhost:8080"
echo "   API      : http://localhost:8080/api/health"
echo ""
echo "📋 Commandes utiles :"
echo "   Voir les logs   : docker compose logs -f"
echo "   Arrêter         : docker compose down"
echo "   Mise à jour     : git pull && ./deploy.sh"
