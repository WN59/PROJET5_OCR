# Pourquoi ces choix ? (version simple)

Petit guide pour comprendre **ce qu’on a choisi** pour MDD, et **pourquoi pas autre chose**.  
Tu peux t’en servir pour le rapport / l’oral. Le détail technique est dans `architecture-mvp.md`.

---

## L’idée générale

ORION impose déjà le cadre :

- un **front** TypeScript / Angular  
- un **back** Java / Spring  
- front et back **séparés**, reliés par une **API sécurisée**  
- **un seul** dépôt GitHub  
- respecter **SOLID** quand c’est utile  

Donc on ne « invente » pas la stack : on **choisit comment** l’utiliser pour le MVP, sans se compliquer la vie.

---

## 1. Front = Angular (et pas autre chose)

| On a choisi | On n’a pas choisi | Pourquoi |
|-------------|-------------------|----------|
| **Angular** | React, Vue, etc. | ORION le demande. Le starter fourni est déjà en Angular. Changer de framework = refaire tout pour rien. |
| **Angular Material** | Tout refaire en CSS / Bootstrap / Tailwind | Material est **déjà branché** dans le starter (bouton, thème). Ça va plus vite pour une UI correcte et responsive. |
| **Garder Angular 14 du starter** (pour commencer) | Tout migrer tout de suite vers Angular 19/20 | Moderniser plus tard si besoin. D’abord : faire marcher le MVP. |

**En une phrase :** on suit la contrainte ORION + on réutilise ce qui est déjà là.

---

## 2. Back = Spring Boot (et pas autre chose)

| On a choisi | On n’a pas choisi | Pourquoi |
|-------------|-------------------|----------|
| **Spring Boot 2.7.18** | Java « brut », autre framework, Boot 3 tout de suite | ORION veut Spring (IoC / DI). Boot simplifie la config. On reste en 2.7.x (javax) pour coller au starter ; 2.7.18 = dernière ligne 2.7. |
| **Spring Data JPA** | JDBC à la main, Hibernate « nu » | ORION dit de préférer un **module Spring Data** pour la base. Moins de code, repositories prêts. |
| **MySQL** | PostgreSQL, MongoDB… | Déjà prévu dans le `pom` du starter. Les données MDD sont bien **relationnelles** (users, thèmes, articles, abonnements). |
| **Java 11 / Boot 2.7 du starter** (départ) | Passer tout de suite en Java 21 / Boot 3 | Même logique que le front : d’abord avancer, moderniser si le gain est clair. |

**En une phrase :** stack imposée + starter + base relationnelle adaptée au métier.

---

## 3. Comment on découpe l’appli (architecture)

### Client–serveur + API REST → **oui**

```text
Toi (navigateur) → Angular → API Spring → MySQL
```

- Le navigateur ne parle **pas** directement à la base.  
- Angular affiche et appelle l’API.  
- Spring contient la logique métier et la sécurité.

| Autre option | Pourquoi non (MVP) |
|--------------|--------------------|
| Tout en un (pages générées côté serveur) | Contredit « front et back séparés ». |
| Microservices (plein de petits services) | Beaucoup trop lourd pour un projet solo / MVP. |
| Event-driven (bus d’événements, CQRS…) | Utile à grande échelle, inutile ici. |

### Organisation du back en **couches** → **oui**

```text
Controller (HTTP) → Service (règles métier) → Repository (base)
```

| Autre option | Pourquoi pas tout de suite |
|--------------|----------------------------|
| Hexagonale / ports-adapters complète | Très propre, mais beaucoup d’interfaces « pour plus tard ». Trop pour démarrer. |

**En une phrase :** séparation claire, simple à expliquer, alignée Spring et SOLID (une responsabilité par couche).

---

## 4. Sécurité = JWT (et pas des sessions serveur)

La spec dit : la connexion doit **persister** entre les sessions (recharger la page = toujours connecté).

| On a choisi | On n’a pas choisi | Pourquoi |
|-------------|-------------------|----------|
| **JWT** (token dans le navigateur, envoyé à chaque requête) | Session HTTP classique sur le serveur | Avec une SPA Angular séparée, le JWT est le plus simple et le plus courant. |
| Stocker le token côté client | OAuth Google/GitHub, etc. | Hors MVP. On reste sur email / username + mot de passe. |

**Attention (point de vigilance) :** il faudra faire attention au XSS (ne pas mettre le token n’importe où sans précaution). C’est OK pour le MVP, à garder en tête.

**En une phrase :** token JWT = session « portable » pour une appli Angular séparée.

---

## 5. Outils / libs « pratiques »

| Choix | Pourquoi oui | Pourquoi pas le contraire |
|-------|--------------|---------------------------|
| **Lombok** | Moins de getters/setters ennuyeux en Java | Sans Lombok : plus de code répétitif (possible, mais plus long) |
| **Bean Validation** (`@Valid`, etc.) | Règles (mdp ORION, champs obligatoires) au bord de l’API | Tout valider « à la main » dans chaque service = erreurs et duplications |
| **DTO** (objets dédiés à l’API) | On n’expose pas les entités JPA brutes | Renvoyer l’entité directe = risque de fuite de champs (ex. hash du mdp) |
| **Guards + Interceptor Angular** | Routes protégées + ajout auto du Bearer | Tout gérer page par page = oubli facile |
| **ControllerAdvice** (back) | Erreurs HTTP homogènes | `try/catch` partout = bordel |

---

## 6. API : pourquoi ces endpoints ?

On découpe selon le **métier** de la spec :

| Besoin spec | Endpoint (idée) | Pourquoi comme ça |
|-------------|-----------------|-------------------|
| S’inscrire / se connecter | `POST /api/auth/register` et `/login` | Publics, sans token |
| Voir / modifier mon profil | `GET/PUT /api/users/me` | « me » = utilisateur du token, pas besoin d’id dans l’URL |
| Liste des thèmes + abo | `GET /api/topics` + `POST/DELETE .../subscribe` | Clair, REST classique |
| Fil d’actu + tri | `GET /api/posts?sort=` | Un seul fil, le tri en query param |
| Créer / lire un article | `POST/GET /api/posts` | CRUD simple |
| Commenter | `POST /api/posts/{id}/comments` | Commentaire **attaché** à un article (pas de sous-commentaires) |

**Pourquoi pas** `/api/login` tout seul au root ?  
On préfère tout préfixer par `/api` et grouper l’auth sous `/api/auth/...` : plus propre et plus facile à sécuriser (tout `/api/**` sauf auth = JWT).

---

## 7. Environnement local (déjà en place)

| Choix | Pourquoi oui |
|-------|--------------|
| **Docker Compose** (MySQL 8 + phpMyAdmin) | Même base pour tout le monde, sans installer MySQL à la main. phpMyAdmin = voir le schéma / seed facilement. |
| **Seed JPA** (`alice` / `bob`) | Tester l’API sans tout saisir à la main. |
| **Proxy Angular `/api`** | Front et back sur des ports différents, sans galère CORS en local. |

Détails : `docs/environnement-dev.md`.

---

## 8. Ce qu’on assume volontairement « plus tard »

Ces trucs sont **OK à compléter au fil de l’eau** (comme le dit OCR) :

- diagramme UML/C4 joli en annexe  
- éventuellement monter les versions Angular / Spring (Boot 3 / Jakarta)  
- MapStruct, OpenAPI/Swagger, Flyway/Liquibase… si utiles  
- affiner les endpoints quand on code pour de vrai  
- maquettes Figma branchées  

**Règle d’or OCR :** un choix front, un choix back — on les a (Angular / Spring Boot). On ne change pas de stack toutes les semaines.

---

## Mémo ultra-court (à retenir)

1. **Angular + Material** → imposé / déjà là.  
2. **Spring Boot + Data JPA + MySQL** → imposé / déjà là / adapté au métier.  
3. **Client–serveur + couches** → simple, séparé, pédagogique.  
4. **JWT** → adapté à une SPA, session persistante.  
5. **Docker + seed** → env de dev reproductible (Feature/3).  
6. **Pas de microservices / event-driven** → surdimensionné pour le MVP.  
7. **Document évolutif** → on pourra ajuster en codant, sans renier ces choix de base.

---

*Liens utiles :* `docs/architecture-mvp.md` (détail) · `docs/environnement-dev.md` (démarrage) · `docs/revue-pratiques.md` (versions) · `docs/infos/Template-Justification-des-choix-techniques.odt` (livrable rapport) · `docs/analyse-existant.md` (état du projet)
