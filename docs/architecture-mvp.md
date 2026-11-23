# Architecture logicielle & API — Feature/2

> Issue : [#3 Feature/2-definissez-architecture-logicielle-api](https://github.com/WN59/PROJET5_OCR/issues/3)  
> Branche : `feature/2-definissez-architecture-logicielle-api`  
> Date : 2026-11-23  
> Sources : `docs/infos/` (specs + contraintes ORION), `docs/analyse-existant.md`  
> Note : document **évolutif** — les choix seront affinés au fil du développement et finalisés en fin de projet.

---

## 1. Décision cadre (un front, un back)

| Côté | Choix retenu | Pourquoi |
|------|--------------|----------|
| **Front** | **Angular** (+ Angular Material, déjà dans le starter) | Imposé / recommandé par ORION ; starter fourni ; Material déjà branché |
| **Back** | **Java + Spring Boot** (+ Spring Data JPA, Spring Security) | Spring Core (IoC/DI) obligatoire ; Boot et Spring Data recommandés ; starter déjà présent |

Versions de départ du MVP : celles du **starter** (Angular ~14, Spring Boot 2.7, Java 11, MySQL). Une modernisation (Angular récent / Boot 3 / Java 21) reste possible plus tard si le gain le justifie, sans changer le choix de stack.

---

## 2. Architecture logicielle

### 2.1 Style retenu : client–serveur + couches

```text
[ Navigateur ]
      │  HTTPS / HTTP (dev)
      ▼
[ Angular SPA ]  ── JWT Bearer ──►  [ API Spring Boot ]
   UI / state / HTTP                    Controllers
                                        Services métier
                                        Repositories (Spring Data)
                                              │
                                              ▼
                                         [ MySQL ]
```

| Style | Avantages | Inconvénients | Décision |
|-------|-----------|---------------|----------|
| **Client–serveur + API REST** | Front/back séparés (contrainte ORION) ; déploiement indépendant ; tests ciblés | Deux runtimes à gérer | **Retenu** |
| Monolithe full-stack (SSR / templates serveur) | Un seul déploiement | Contredit la séparation front/back ORION | Rejeté |
| Architecture événementielle (bus, CQRS…) | Scalabilité asynchrone | Surdimensionné pour le MVP | Rejeté pour le MVP |
| Microservices | Isolation forte | Complexité ops inutile pour un MVP mono-équipe | Rejeté |

| Organisation back | Avantages | Inconvénients | Décision |
|-------------------|-----------|---------------|----------|
| **En couches** (controller → service → repository) | Aligné Spring ; lisible ; SOLID (SRP) ; déjà dans nos conventions | Couches « plates » si mal découpées | **Retenu** |
| Hexagonale / ports-adapters complète | Très testable, découplage fort | Coût d’abstraction élevé au démarrage | Trop tôt pour le MVP |
| Package by feature uniquement | Cohésion métier | Moins idiomatique pour un petit API Spring pédagogique | Possible en complément plus tard |

### 2.2 Organisation des dossiers (cible)

```text
back/src/main/java/com/openclassrooms/mddapi/
├── MddApiApplication.java
├── config/          # Security, CORS, OpenAPI éventuel
├── security/        # JWT filter, UserDetails…
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── mapper/          # optionnel (MapStruct) ou mapping manuel
└── exception/       # @ControllerAdvice

front/src/app/
├── core/            # interceptors, guards, auth service
├── shared/          # UI réutilisable
├── features/
│   ├── auth/
│   ├── feed/
│   ├── topics/
│   ├── posts/
│   └── profile/
└── ...
```

### 2.3 Communication front ↔ back

| Élément | Choix |
|---------|--------|
| Protocole | REST JSON sur `/api/**` |
| Auth | **JWT** dans le header `Authorization: Bearer <token>` |
| Persistance session (spec) | Token stocké côté client (`localStorage` ou équivalent) pour survivre au rechargement |
| CORS | Origines front autorisées (ex. `http://localhost:4200`) configurées côté Spring |
| Dev | Proxy Angular `/api` → back **ou** `environment.apiUrl` absolue |
| Erreurs | Codes HTTP + corps JSON homogène (`message`, éventuellement `code`) |

---

## 3. Comparatifs de choix techniques

### 3.1 Authentification

| Option | Avantages | Inconvénients | Décision |
|--------|-----------|---------------|----------|
| **JWT (stateless)** | Simple pour SPA ; pas de session serveur ; standard sur ce type de projet OCR | Révocation plus délicate ; stockage XSS à maîtriser | **Retenu** |
| Sessions HTTP + cookie | Révocation facile ; anti-CSRF gérable | Moins naturel avec SPA séparée ; sticky sessions | Non pour le MVP |
| OAuth2 / IdP externe | Délégation forte | Hors périmètre MVP | Non |

### 3.2 UI front

| Option | Avantages | Inconvénients | Décision |
|--------|-----------|---------------|----------|
| **Angular Material** | Déjà dans le starter ; composants accessibles ; doc riche | Look « Material » générique | **Retenu** |
| Bootstrap / Tailwind seul | Liberté visuelle | Reprise à zéro vs starter ; plus de travail UI | Non pour le MVP |
| Lib custom from scratch | Contrôle total | Coût trop élevé | Non |

### 3.3 Accès données & outillage back

| Option | Avantages | Inconvénients | Décision |
|--------|-----------|---------------|----------|
| **Spring Data JPA + MySQL** | Contrainte ORION (module Spring Data) ; déjà dans le `pom` | Schéma à concevoir / migrer | **Retenu** |
| JDBC template seul | Contrôle SQL fin | Plus de boilerplate | Non |
| MongoDB / NoSQL | Flexibilité documents | Hors starter ; relations (abonnements) moins naturelles | Non |
| **Lombok** | Moins de boilerplate (getters, builders) | Magie annotation ; courbe d’outil IDE | **Retenu** (si confortable) |
| Java records seuls | Standards, immutables pour DTO | Moins d’aide sur les entités JPA mutables | Complément possible pour DTO |
| MapStruct | Mapping Entity↔DTO typé | Dépendance + config | Optionnel ; mapping manuel OK au début |
| Bean Validation (`@Valid`) | Règles mdp / champs au bordure API | — | **Retenu** |

### 3.4 Design patterns & bonnes pratiques

| Pattern / principe | Usage prévu |
|--------------------|-------------|
| **SOLID** (surtout S, D) | Services à responsabilité claire ; injection d’abstractions Spring |
| **DTO** | Ne pas exposer les entités JPA en JSON |
| **Repository** | Spring Data `JpaRepository` |
| **DI / IoC** | Spring Core (obligatoire ORION) + `inject()` côté Angular |
| Guard + Interceptor (Angular) | Routes privées ; ajout du Bearer |
| `@ControllerAdvice` | Gestion d’erreurs centralisée |

---

## 4. Schéma de données (MVP)

```text
User 1──* Post
User 1──* Comment
User *──* Topic          (via Subscription)
Topic 1──* Post
Post 1──* Comment        (commentaires non récursifs)
```

| Entité | Champs principaux | Règles |
|--------|-------------------|--------|
| **User** | id, email (unique), username (unique), passwordHash, createdAt | Mdp ORION : ≥8, maj, min, chiffre, spécial |
| **Topic** | id, name, description | Liste consultable par tous les connectés |
| **Subscription** | id, userId, topicId, createdAt | Unique (userId, topicId) |
| **Post** | id, title, content, topicId, authorId, createdAt | Auteur + date **auto** à la création |
| **Comment** | id, content, postId, authorId, createdAt | Pas de parentCommentId ; auteur + date auto |

---

## 5. Endpoints API (contrat initial)

Préfixe : `/api`  
Sauf auth publique : **Bearer JWT** requis.

### Auth & utilisateur

| Méthode | Endpoint | Description | Corps / réponse |
|---------|----------|-------------|-----------------|
| POST | `/api/auth/register` | Inscription | `{ email, username, password }` → `{ token, user }` |
| POST | `/api/auth/login` | Connexion (email **ou** username + password) | `{ login, password }` → `{ token, user }` |
| GET | `/api/users/me` | Profil connecté (+ abonnements) | → `{ id, email, username, subscriptions[] }` |
| PUT | `/api/users/me` | Modifier profil (email, username, password optionnel) | body partiel → user à jour |
| *(front)* | déconnexion | Suppression du token côté client | pas d’endpoint obligatoire |

### Thèmes & abonnements

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/topics` | Liste tous les thèmes (+ indicateur `subscribed` si utile) |
| POST | `/api/topics/{id}/subscribe` | S’abonner |
| DELETE | `/api/topics/{id}/subscribe` | Se désabonner (aussi depuis le profil) |

### Articles & commentaires

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/posts?sort=desc\|asc` | Fil d’actualité (thèmes suivis), tri chronologique |
| POST | `/api/posts` | Créer un article `{ topicId, title, content }` |
| GET | `/api/posts/{id}` | Détail + commentaires |
| POST | `/api/posts/{id}/comments` | Ajouter un commentaire `{ content }` |

Codes d’erreur attendus : `400` validation, `401` non authentifié, `403` interdit, `404` introuvable, `409` conflit (email/username déjà pris).

---

## 6. Tableau de synthèse « choix techniques » (pour le template)

| Élément choisi | Type | Lien doc | Objectif | Justification |
|----------------|------|----------|---------|---------------|
| Angular 14 (starter) | Framework front | https://angular.io | SPA structurée, routing, formulaires | Contrainte ORION + starter fourni |
| Angular Material | Lib UI | https://material.angular.io | Composants UI rapides / responsive | Déjà configuré dans le projet |
| Spring Boot 2.7 | Framework back | https://spring.io/projects/spring-boot | API REST + IoC/DI | Boot recommandé ; Core obligatoire |
| Spring Data JPA | Persistance | https://spring.io/projects/spring-data-jpa | Accès MySQL via repositories | Module Spring Data demandé par ORION |
| Spring Security + JWT | Sécurité | https://spring.io/projects/spring-security | Sécuriser l’API | Interaction front/back sécurisée (contrainte) |
| MySQL | SGBD | — | Persistance relationnelle | Déjà prévu dans le `pom` starter |
| Lombok | Productivité Java | https://projectlombok.org | Réduire le boilerplate | Courant sur projets Spring pédagogiques |
| Architecture en couches + REST | Style / pattern | — | Séparation UI / métier / données | Simple, SOLID, adapté MVP |
| GitHub (mono-repo) | Gestion de code | — | Un seul dépôt | Contrainte ORION |

---

## 7. Points de vigilance (rappel OCR)

- Un seul choix front (**Angular**), un seul choix back (**Spring Boot**).  
- Document **vivant** : endpoints et libs pourront être ajustés à l’implémentation.  
- Ne pas sur-ingénierier (pas d’event-driven / microservices pour le MVP).  
- Respecter responsive + règles mot de passe + auteur/date automatiques (specs).

---

*Livrable Feature/2 — à reporter / coller aussi dans le template ODT de justification.*
