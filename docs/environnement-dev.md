# Environnement de développement — Feature/3

> Issue : [#5 Feature/3-prepare-dev-env](https://github.com/WN59/PROJET5_OCR/issues/5)  
> Branche : `feature/3-prepare-dev-env`

## Stack locale

| Service | URL / port | Identifiants |
|---------|------------|--------------|
| MySQL (Docker) | `localhost:3306` | base `mdd` / user `mdd` / mdp `mdd_secret` |
| phpMyAdmin | http://localhost:8081 | mêmes identifiants MySQL |
| API Spring Boot | http://localhost:8080 | — |
| Front Angular | http://localhost:4200 | proxy `/api` → `8080` |

Mot de passe des users de seed : **`Password1!`** (`alice` / `bob`).

## Démarrage rapide

```bash
# 1. Variables d'environnement (optionnel si tu gardes les défauts)
cp .env.example .env

# 2. Base + phpMyAdmin
docker compose up -d

# 3. Backend (crée le schéma via Hibernate ddl-auto=update + seed si DB vide)
cd back
mvn spring-boot:run

# 4. Frontend
cd front
npm install
npm start
```

## Ce qui a été mis en place

- `docker-compose.yml` : MySQL 8 + phpMyAdmin
- Entités JPA (`User`, `Topic`, `Subscription`, `Post`, `Comment`) → schéma via ORM
- Seed automatique au premier démarrage (`DataInitializer`)
- Config Spring (`application.properties`) + CORS
- Front : `environment.apiUrl` + `proxy.conf.json`
- `.env.example` / `.gitignore` / `.gitattributes`
- Branches Gitflow : `main` (prod-like) + `develop` + features

## IDE (Cursor / VS Code)

Ouvrir la racine du monorepo. Extensions recommandées dans `.vscode/extensions.json` (Java Pack, Angular Language Service).

## Figma

Les maquettes seront branchées plus tard (lien Figma + éventuellement plugin). L’environnement front Material est prêt pour l’intégration UI.

## Versions back (à jour)

- Spring Boot **2.7.18**
- Driver MySQL **`com.mysql:mysql-connector-j`**
- Lombok **1.18.38** (+ annotation processor Maven — nécessaire sous JDK 21/25)

## Impact des choix techniques (rappel)

Voir `docs/architecture-mvp.md`, `docs/explications-choix.md` et `docs/revue-pratiques.md` : Angular + Spring Boot + JPA + MySQL + JWT (auth à implémenter ensuite).
