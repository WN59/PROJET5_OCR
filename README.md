# PROJET5_OCR — MDD (Monde de Dév)

Réseau social technique : **Angular 14** + **Spring Boot 2.7.18** + **MySQL 8**.

## Démarrage

```bash
cp .env.example .env   # optionnel
docker compose up -d
cd back && mvn spring-boot:run    # :8080
cd front && npm install && npm start   # :4200 (proxy /api → 8080)
```

| Outil | URL |
|-------|-----|
| Front | http://localhost:4200 |
| API | http://localhost:8080 |
| phpMyAdmin | http://localhost:8081 |

Seed : `alice` / `bob` — `Password1!`  
Ordre : **Docker (MySQL) avant le back**.

## Documentation

| Fichier | Contenu |
|---------|---------|
| [`docs/Justificatif_choix_techniques.odt`](docs/Justificatif_choix_techniques.odt) | Livrable OCR (choix techniques) |
| [`docs/Spécifications+fonctionnelles.pdf`](docs/Spécifications+fonctionnelles.pdf) | Specs métier |
| [`docs/Contraintes+techniques.pdf`](docs/Contraintes+techniques.pdf) | Contraintes stack |

Notes de travail / journal de features : dossier local `local/` (hors Git).

## Git

`main` (stable) · `develop` (intégration) · `feature/...` (une branche par étape)
