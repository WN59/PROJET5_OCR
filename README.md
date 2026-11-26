# PROJET5_OCR — MDD (Monde de Dév)

Application full-stack : réseau social technique (Angular + Spring Boot + MySQL).

## Prérequis

- Git, Docker / Docker Compose
- Java 11+ (JDK), Maven 3.8+
- Node.js 16+ / npm (compatible Angular 14)

## Démarrage

```bash
# Base de données + phpMyAdmin
cp .env.example .env   # optionnel
docker compose up -d

# API
cd back && mvn spring-boot:run
# → http://localhost:8080

# Front
cd front && npm install && npm start
# → http://localhost:4200  (proxy /api → 8080)
```

| Outil | URL |
|-------|-----|
| Front | http://localhost:4200 |
| API | http://localhost:8080 |
| phpMyAdmin | http://localhost:8081 |

Seed : utilisateurs `alice` / `bob`, mot de passe `Password1!`.

Doc détaillée : [`docs/environnement-dev.md`](docs/environnement-dev.md).

## Structure

```text
├── back/          # API Spring Boot
├── front/         # SPA Angular
├── docs/          # Specs, architecture, env
└── docker-compose.yml
```

## Workflow Git

- `main` — branche stable
- `develop` — intégration
- `feature/...` — une branche par étape

## Front (starter)

Généré avec Angular CLI 14.1.3. UI : Angular Material.
