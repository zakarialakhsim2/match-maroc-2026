import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PredictionRequest, PredictionStats } from '../models/match.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PredictionService {
  private api = environment.apiUrl;

  constructor(private http: HttpClient) {}

  vote(req: PredictionRequest): Observable<any> {
    return this.http.post(`${this.api}/predictions`, req);
  }

  getStats(matchId: number): Observable<PredictionStats> {
    return this.http.get<PredictionStats>(`${this.api}/predictions/${matchId}`);
  }
}
