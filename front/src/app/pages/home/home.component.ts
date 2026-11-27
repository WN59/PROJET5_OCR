import { Component } from '@angular/core';
import { Topic } from '../../core/models/topic.model';
import { TopicService } from '../../core/services/topic.service';

/**
 * Page d’accueil — Feature/4 : bouton qui charge les thèmes via l’API.
 */
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
  topics: Topic[] = [];
  loading = false;
  error: string | null = null;
  loaded = false;

  constructor(private readonly topicService: TopicService) {}

  /**
   * Déclenche `GET /api/topics` et met à jour l’UI (liste / erreur).
   * Affiche le `message` JSON du back s’il est présent.
   */
  start(): void {
    this.loading = true;
    this.error = null;
    this.topicService.list().subscribe({
      next: (topics) => {
        this.topics = topics;
        this.loaded = true;
        this.loading = false;
      },
      error: (err) => {
        const apiMessage = err?.error?.message as string | undefined;
        this.error =
          apiMessage ||
          'Impossible de charger les thèmes. Vérifie que Docker (MySQL) et le back (:8080) tournent.';
        this.loading = false;
      },
    });
  }
}
