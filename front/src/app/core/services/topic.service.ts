import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Topic } from '../models/topic.model';

/**
 * Accès HTTP aux thèmes.
 * En local, `environment.apiUrl` vaut `/api` et le proxy Angular forward vers `:8080`.
 */
@Injectable({ providedIn: 'root' })
export class TopicService {
  private readonly url = `${environment.apiUrl}/topics`;

  constructor(private readonly http: HttpClient) {}

  /** `GET /api/topics` → liste des thèmes depuis MySQL. */
  list(): Observable<Topic[]> {
    return this.http.get<Topic[]>(this.url);
  }
}
