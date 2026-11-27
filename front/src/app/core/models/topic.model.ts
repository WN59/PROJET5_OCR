/**
 * Modèle front d’un thème — miroir du `TopicDto` renvoyé par `GET /api/topics`.
 */
export interface Topic {
  id: number;
  name: string;
  description: string;
}
