import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Topic } from '../models/topic.model';

/**
 * Accès HTTP aux thèmes.
 * `environment.apiUrl` vaut `/api` en local (proxy Angular → :8080).
 */
@Injectable({ providedIn: 'root' })
export class TopicService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/topics`;

  /** GET /api/topics */
  list(): Observable<Topic[]> {
    return this.http.get<Topic[]>(this.url);
  }
}
