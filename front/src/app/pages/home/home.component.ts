import { NgOptimizedImage } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatButtonModule } from '@angular/material/button';
import { Topic } from '../../core/models/topic.model';
import { TopicService } from '../../core/services/topic.service';

/**
 * Page d’accueil — charge les thèmes via l’API (Feature/4).
 * Patterns Angular 22 : standalone (défaut), inject(), signals, control flow.
 */
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [MatButtonModule, NgOptimizedImage],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  private readonly topicService = inject(TopicService);
  private readonly destroyRef = inject(DestroyRef);

  readonly topics = signal<Topic[]>([]);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly loaded = signal(false);

  /** Déclenche GET /api/topics et met à jour l’état signal. */
  start(): void {
    this.loading.set(true);
    this.error.set(null);

    this.topicService
      .list()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (topics) => {
          this.topics.set(topics);
          this.loaded.set(true);
          this.loading.set(false);
        },
        error: (err: HttpErrorResponse) => {
          const apiMessage =
            typeof err.error?.message === 'string' ? err.error.message : undefined;
          this.error.set(
            apiMessage ??
              'Impossible de charger les thèmes. Vérifie que Docker (MySQL) et le back (:8080) tournent.',
          );
          this.loading.set(false);
        },
      });
  }
}
