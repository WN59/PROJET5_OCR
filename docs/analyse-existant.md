# Analyse de l’existant — Feature/1 (+ suivi)

> Issue initiale : [#1](https://github.com/WN59/PROJET5_OCR/issues/1)  
> Dernière mise à jour : **2026-11-24** (après Feature/3 — env Docker / ORM)  
> Dépôt : [WN59/PROJET5_OCR](https://github.com/WN59/PROJET5_OCR)

---

## 1. Documents du dépôt

| Document | Rôle |
|----------|------|
| `docs/infos/Spécifications+fonctionnelles.pdf` | Spec métier ORION (MVP) |
| `docs/infos/Contraintes+techniques.pdf` | Contraintes stack / SOLID / Git |
| `docs/infos/Template-Justification-des-choix-techniques.odt` | Livrable rapport (complété progressivement) |
| `docs/architecture-mvp.md` | Architecture + API (Feature/2) |
| `docs/explications-choix.md` | Pourquoi tel choix (version simple) |
| `docs/environnement-dev.md` | Démarrage Docker / ports / seed (Feature/3) |
| `docs/revue-pratiques.md` | Passe versions / bonnes pratiques |
| `README.md` | Quick start monorepo |

---

## 2. État du code (au 24/11/2026)

### 2.1 Structure

```text
PROJET5_OCR/
├── docker-compose.yml      # MySQL 8 + phpMyAdmin
├── .env.example / .gitignore / .gitattributes
├── back/                   # Spring Boot 2.7.18
│   ├── entity/ repository/ config/ (CORS, seed)
│   └── application.properties (datasource + JPA)
├── front/                  # Angular 14 + Material + proxy /api
└── docs/
```

### 2.2 Backend

| Élément | État |
|---------|------|
| Spring Boot **2.7.18**, Java 11 (pom), Lombok 1.18.38 | OK |
| Driver **`mysql-connector-j`** | OK |
| Entités JPA : User, Topic, Subscription, Post, Comment | OK — schéma via `ddl-auto=update` |
| Repositories Spring Data | OK |
| Seed (`DataInitializer`) | OK — alice/bob, `Password1!` |
| Controllers / Services métier / JWT Security | **À faire** |
| Tests | `contextLoads` + H2 en test |

### 2.3 Frontend

| Élément | État |
|---------|------|
| Angular 14 + Material | OK (NgModules) |
| `environment.apiUrl` + `proxy.conf.json` | OK |
| `npm install` | Fait en local |
| Auth / pages métier / guards | **À faire** |
| Home | Toujours `alert()` sur « Commencer » |

### 2.4 Infra

| Élément | État |
|---------|------|
| Docker Compose MySQL + phpMyAdmin | OK |
| GitHub Project + issues/features | OK |
| Branches `main` / `develop` / `feature/*` | OK (develop locale) |
| Postman / CI / Figma branché | **À faire** |

---

## 3. MVP fonctionnel — reste à faire

Toujours issu des specs ORION (inchangé) :

- Auth (register / login persistante JWT)
- Profil, thèmes / abonnements, fil, articles, commentaires
- UI responsive + règles mdp
- Remplacer l’`alert()` Home

---

## 4. Environnement — commandes

Voir **`docs/environnement-dev.md`**.

```bash
docker compose up -d
cd back && mvn spring-boot:run      # :8080
cd front && npm start               # :4200 , proxy /api
# phpMyAdmin : http://localhost:8081
```

Point de vigilance : Maven Windows tourne souvent sous **JDK 25** → Lombok doit rester à jour (voir `revue-pratiques.md`).

---

## 5. Synthèse

| Question | Réponse |
|----------|---------|
| Specs dans le repo ? | Oui (`docs/infos/`) |
| Env de dev opérationnel ? | **Oui** (Docker DB + seed + front/back démarrables) |
| MVP métier ? | **Pas encore** (modèle + seed prêts, API/UI à coder) |

*Document historique Feature/1, tenu à jour pour refléter Features 2 et 3.*
