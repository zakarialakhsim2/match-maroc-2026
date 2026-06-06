# 🇲🇦 Match Maroc 2026

Application web pour suivre les matchs du Maroc à la Coupe du Monde 2026.

## Fonctionnalités
- ⏱️ Compte à rebours en temps réel
- 🕘 Heure du match convertie automatiquement en timezone Maroc
- 📺 Chaînes TV de diffusion
- 🎯 Système de prédictions/votes
- 🔔 Rappels email (24h et 1h avant le match)
- 📋 Historique des matchs
- 📱 Partage WhatsApp

## Stack
- **Frontend** : Angular 18, RxJS, SCSS
- **Backend** : Spring Boot 3, PostgreSQL
- **Deploy** : Docker Compose

## Démarrage rapide

### Prérequis
- Docker + Docker Compose installés

### 1. Clone + configuration
```bash
git clone https://github.com/ton-user/match-maroc-2026.git
cd match-maroc-2026
cp .env.example .env
# Éditez .env avec vos valeurs
```

### 2. Déploiement (une seule commande)
```bash
./deploy.sh
```

### 3. Accès
- Site : http://localhost
- API : http://localhost:8080/api

## Développement local

### Backend
```bash
cd backend
# Démarrer PostgreSQL
docker run -d -e POSTGRES_DB=matchmaroc -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:16-alpine
# Lancer Spring Boot
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm start
# → http://localhost:4200
```

## API Endpoints
| Méthode | URL | Description |
|---------|-----|-------------|
| GET | /api/matches/upcoming | Prochain match du Maroc |
| GET | /api/matches/morocco | Tous les matchs du Maroc |
| GET | /api/matches/{id} | Détail d'un match |
| POST | /api/predictions | Voter un score |
| GET | /api/predictions/{matchId} | Stats des votes |
| POST | /api/reminders | S'inscrire aux rappels |
| GET | /api/health | Santé de l'application |

## Déploiement sur serveur

### VPS (DigitalOcean / OVH / Hetzner)
```bash
# Sur votre serveur Ubuntu
sudo apt update && sudo apt install -y docker.io docker-compose-plugin
git clone https://github.com/ton-user/match-maroc-2026.git
cd match-maroc-2026
cp .env.example .env && nano .env
./deploy.sh
```

### Railway.app (gratuit, sans VPS)
1. Créer un compte sur railway.app
2. "New Project" → "Deploy from GitHub"
3. Ajouter les variables d'env depuis .env.example
4. Railway détecte automatiquement le docker-compose.yml

### Render.com (alternative gratuite)
1. Créer un compte sur render.com
2. "New" → "Web Service" → connecter le repo GitHub
3. Configurer les variables d'environnement
4. Deploy !

## Structure du projet
```
match-maroc-2026/
├── backend/                    ← Spring Boot 3
│   ├── src/main/java/ma/matchmaroc/
│   │   ├── config/             ← CORS, DataInitializer
│   │   ├── controller/         ← REST endpoints
│   │   ├── dto/                ← Request/Response objects
│   │   ├── entity/             ← JPA entities
│   │   ├── repository/         ← Spring Data JPA
│   │   ├── scheduler/          ← Email reminders
│   │   └── service/            ← Business logic
│   └── Dockerfile
├── frontend/                   ← Angular 18
│   ├── src/app/
│   │   ├── core/models/        ← TypeScript interfaces
│   │   ├── core/services/      ← HTTP services
│   │   └── features/           ← Components
│   ├── Dockerfile
│   └── nginx.conf
├── docker-compose.yml
├── deploy.sh
└── .env.example
```

## Allez les Lions de l'Atlas ! 🇲🇦🦁
