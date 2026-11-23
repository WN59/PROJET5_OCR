# Analyse de l’existant — Feature/1

> Issue : [#1 Feature/1-examinez-spec-code-existant](https://github.com/WN59/PROJET5_OCR/issues/1)  
> Branche : `feature/1-examinez-spec-code-existant`  
> Date : 2026-11-23  
> Dépôt : [WN59/PROJET5_OCR](https://github.com/WN59/PROJET5_OCR)

---

## 1. Contexte et documents

### 1.1 Documents du dépôt

| Document / artefact | Rôle |
|---------------------|------|
| `README.md` | Starter front Angular 14.1.3, page d’accueil MDD, Angular Material, commandes `npm install` / `ng serve` / `ng build` |
| `docs/infos/Spécifications+fonctionnelles.pdf` | **Spec métier ORION** — fonctionnalités MVP MDD |
| `docs/infos/Contraintes+techniques.pdf` | **Contraintes techniques ORION** — stack, SOLID, Git |
| `docs/infos/Template+–+Justification+des+choix+techniques+.odt` | **Livrable rapport** — à compléter au fil du projet (début §1 déjà amorcé) |
| Code `front/` / `back/` | Squelettes Angular + Spring Boot |
| Historique Git | `Init back` → `Init front` → amélioration page d’accueil → correction README |

Le README s’intitule **P6-Full-Stack-reseau-dev** alors que le dépôt est **PROJET5_OCR** : décalage de numérotation OCR (même sujet MDD).

### 1.2 Spécifications fonctionnelles (MVP)

Source : `docs/infos/Spécifications+fonctionnelles.pdf` (ORION — Monde de Dév).  
Pas de back-office dans le MVP.

| Domaine | Fonctionnalités attendues | État code actuel |
|---------|---------------------------|------------------|
| Accueil non connecté | Accès aux formulaires connexion / inscription | Amorcé (Home + bouton « Commencer » → `alert`) |
| Utilisateurs | Inscription (email, mdp, username) ; connexion email **ou** username + mdp ; **session persistante** ; profil (voir / modifier email, username, mdp, abonnements) ; déconnexion | À faire |
| Abonnements | Liste de tous les thèmes ; s’abonner (page thèmes) ; se désabonner (page profil) ; bouton « Déjà abonné » inactif après abonnement | À faire |
| Articles / fil | Fil chronologique des thèmes suivis ; tri récent↔ancien ; créer article (thème, titre, contenu) ; détail (thème, titre, auteur, date, contenu, commentaires) | À faire |
| Commentaires | Ajouter un commentaire (contenu) ; non récursifs ; auteur + date auto | À faire |
| Exigences transverses | UI **responsive** ; règles mdp (≥8, maj, min, chiffre, spécial) ; auteur/date auto à la création article/commentaire | À faire |

**Glossaire** : User, Topic/Subject, Post, Subscription, Feed.

### 1.3 Contraintes techniques ORION

Source : `docs/infos/Contraintes+techniques.pdf`.

| Contrainte | Implication pour ce repo |
|------------|--------------------------|
| Front et back **distincts**, liés par une **API** | Monorepo `front/` + `back/` déjà conforme |
| Interaction front ↔ back **sécurisée** (méthode au choix) | JWT recommandé / cohérent avec le template rapport |
| Respect des principes **SOLID** | Aligné avec la règle projet `pratiques-conception` |
| Back : **Java / Spring** (Core IoC/DI obligatoire ; Boot fortement recommandé ; privilégier modules Spring Data) | Starter Spring Boot 2.7 + JPA : base OK, à enrichir |
| Front : **TypeScript / Angular** (+ bonnes pratiques sécurité Angular ; CLI recommandé) | Starter Angular 14 + Material : base OK |
| **Un seul** repository GitHub | `WN59/PROJET5_OCR` |

### 1.4 Livrable « Justification des choix techniques »

Le template ODT demande notamment :

1. Présentation (objectifs, périmètre) — **§1.1 et §1.2 préremplis** à partir des specs / de l’existant  
2. Architecture, choix techniques, API / schémas — à compléter plus tard  
3. Tests, perf, revue technique — à compléter plus tard  
4. FAQ + tâches déléguées à l’IA — à compléter plus tard  
5. Annexes (captures, schémas, rapports) — à compléter plus tard  

---

## 2. Modules et composants déjà amorcés

### 2.1 Vue d’ensemble

```text
PROJET5_OCR/
├── README.md
├── docs/
│   ├── analyse-existant.md          # ce document
│   └── infos/                       # specs ORION + template rapport
├── back/                            # API Spring Boot (squelette)
└── front/                           # SPA Angular 14 (Home + Material)
```

### 2.2 Backend (`back/`)

| Élément | État |
|---------|------|
| Spring Boot **2.7.3** / Java **11** (déclaré) | Amorcé |
| `spring-boot-starter-web` + `data-jpa` + MySQL connector | Présents |
| `MddApiApplication` | Point d’entrée uniquement |
| `application.properties` | **Vide** |
| Controllers / Services / Repositories / Entités / DTO / Security / JWT | **Absents** |
| Tests | `contextLoads` uniquement |

### 2.3 Frontend (`front/`)

| Élément | État |
|---------|------|
| Angular **~14.1**, Material **14.2**, thème `deeppurple-amber` | Amorcé (NgModules) |
| `HomeComponent` | Logo, « Welcome to MDD », bouton Material |
| Routes | `''` → Home ; commentaire hint pour futur guard |
| `environment` | Pas d’URL API |
| Auth, services HTTP, guards, pages métier | **Absents** |
| `node_modules` | Non installés |

### 2.4 Outillage transversal

GitHub OK ; Docker installé sur la machine mais **absent du repo** ; pas de Postman / CI versionnés.

---

## 3. Éléments à compléter ou à corriger

### 3.1 Corrections immédiates

| Priorité | Sujet | Détail |
|----------|--------|--------|
| Haute | Fins de ligne (CRLF) | Bruit massif sur les fichiers tracked sans vrai diff de contenu — nettoyer avant commit |
| Haute | `application.properties` vide | Datasource MySQL / JPA indispensables au runtime |
| Haute | Wrapper `mvnw` sous WSL | Échec d’exécution (souvent shebang/CRLF) — utiliser `mvn` ou réparer |
| Moyenne | Versions Java | Projet en **11** ; machines en **21** / **25** — trancher avant features métier |
| Moyenne | README | Titre P6 vs P5 ; documenter back, MySQL, ports, auth |
| Basse | NgModules vs standalone | Rester cohérent avec le starter, migrer progressivement si besoin |

### 3.2 Back — aligné specs + contraintes

- Config : port, MySQL, CORS, profils, secrets hors Git  
- Modèle : User, Topic, Post, Comment, Subscription  
- Couches SOLID : controller → service → repository + DTO + validation (dont règles mdp)  
- Sécurité API (ex. Spring Security + JWT, session persistante côté client)  
- Endpoints pour auth, profil, thèmes/abonnements, fil, articles, commentaires  
- Tests + éventuellement OpenAPI / Postman / Docker Compose  

### 3.3 Front — aligné specs

- URL API / proxy ; auth (login, register, token persistant, interceptor, guard)  
- Pages : accueil/fil, thèmes, détail article + commentaires, profil  
- Responsive ; états « S’abonner » / « Déjà abonné »  
- Remplacer l’`alert()` du bouton « Commencer »  

### 3.4 Déjà conforme aux contraintes

- Séparation front / back dans un **seul** repo  
- Stack Java/Spring + TypeScript/Angular  
- Material et page d’accueil comme point d’entrée UX  
- Compilation Maven du squelette OK (`mvn -DskipTests compile`)  

---

## 4. Vérification de l’environnement

### 4.1 Constat machine (2026-11-23)

| Outil | Constat | Compatible starter ? |
|-------|---------|----------------------|
| Git / remote GitHub | OK | Oui |
| Java WSL / Windows | 21 / 25 | ≠ Java 11 déclaré |
| Maven 3.8.7 | Compile le back | Oui |
| `./mvnw` (WSL) | Échec | Non fiable |
| Node / npm (Windows) | v24 / 11.6 | OK pour Angular 14 |
| Node (PATH bash) | Absent | Passer par Windows ou installer en WSL |
| `front/node_modules` | Absent | `npm install` à faire |
| MySQL | Non détecté | À installer ou Docker |
| Docker / Compose | 29.9 / v5.6 | Dispo pour plus tard |

### 4.2 Commandes utiles

```bash
cd back && mvn -DskipTests compile
cd front && npm install && npx ng serve   # http://localhost:4200/
```

### 4.3 Suivi GitHub

- Branche : `feature/1-examinez-spec-code-existant`  
- Project : [@WN59's PROJET5_OCR](https://github.com/users/WN59/projects/5)  
- Issue #1 liée au kanban  

---

## 5. Synthèse

| Question | Réponse courte |
|----------|----------------|
| Specs / contraintes dans le repo ? | Oui — `docs/infos/` (fonctionnel + technique + template rapport) |
| Qu’est-ce qui est amorcé ? | Squelettes Angular 14 (Home) et Spring Boot 2.7 (sans métier) |
| Qu’est-ce qui manque ? | Tout le MVP métier + sécurisation API + config DB + doc rapport (sauf début §1) |
| Environnement OK ? | **Partiel** (Git, Maven, Node Windows, Docker) ; MySQL et `node_modules` manquants |

### Prochaines étapes suggérées

1. Nettoyer CRLF / `.gitattributes` avant les commits utiles.  
2. MySQL (local ou Docker) + config Spring.  
3. `npm install` + vérifier `ng serve`.  
4. Trancher les versions (rester sur le starter vs moderniser) **avant** les features.  
5. Issues / branches par brique MVP (auth → thèmes → articles → commentaires → polish responsive + rapport).  

---

*Document produit / mis à jour dans le cadre de l’issue #1 — croisé avec `docs/infos/`.*
