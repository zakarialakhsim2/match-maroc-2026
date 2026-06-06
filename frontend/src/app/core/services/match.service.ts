import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Match } from '../models/match.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class MatchService {
  private api = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getUpcoming(): Observable<Match> {
    return this.http.get<Match>(`${this.api}/matches/upcoming`);
  }

  getMoroccoMatches(): Observable<Match[]> {
    return this.http.get<Match[]>(`${this.api}/matches/morocco`);
  }

  getById(id: number): Observable<Match> {
    return this.http.get<Match>(`${this.api}/matches/${id}`);
  }
}
