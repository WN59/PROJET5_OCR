# Petite revue pratiques / versions (Feature/3)

Date : 2026-11-24 — sources : doc Spring Boot **2.7.18** (Context7), état du starter OCR.

## Constats machine

| Outil | Chez toi | Impact |
|-------|----------|--------|
| Maven (Windows) | 3.10 + **JDK 25** | Lombok doit être récent + déclaré en *annotation processor*, sinon `builder()` « cannot find symbol » |
| Java (WSL) | 21 | Compile OK avec Lombok 1.18.x |
| Docker | Desktop 4.98 / Engine 29.9 | Compose MySQL + phpMyAdmin OK |
| Node | 24 | OK pour installer Angular 14 (warnings possibles) |

## Ajustements faits dans cette passe

| Sujet | Avant | Après | Pourquoi |
|-------|--------|--------|----------|
| Spring Boot | 2.7.3 | **2.7.18** | Dernière ligne 2.7 (correctifs) sans passer à Boot 3 / Jakarta |
| MySQL driver | `mysql-connector-java` | **`mysql-connector-j`** | Artefact officiel actuel |
| Lombok | 1.18.36 + pas de processor path | **1.18.38** + `annotationProcessorPaths` | Bonne pratique Maven / compat JDK récents |
| `ddl-auto` | `update` | inchangé | Adapté au dev (doc Boot 2.7) ; en prod → `validate` / migrations plus tard |

## Ce qu’on ne change **pas** maintenant (volontairement)

- **Pas de Boot 3 / Jakarta** : gros chantier (packages `jakarta.*`, Security 6, etc.).
- **Pas d’Angular 17+ standalone/signals** : le starter est en NgModules 14 ; à traiter en feature dédiée si besoin.
- **Pas de Flyway/Liquibase** encore : `ddl-auto=update` + seed suffisent pour préparer l’env ; migrations = bonne suite.

## Recommandation pratique

Pour éviter les surprises, idéalement aligner le JDK Maven Windows sur **17 ou 21 LTS** (au lieu de 25), tout en gardant `java.version=11` dans le `pom` pour rester compatible starter — ou monter explicitement vers 17 quand on passera Boot 3.
