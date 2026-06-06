import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ReminderService {
  private api = environment.apiUrl;

  constructor(private http: HttpClient) {}

  register(matchId: number, email: string): Observable<string> {
    const deviceId = this.getDeviceId();
    return this.http.post(`${this.api}/reminders`, { matchId, email, deviceId }, { responseType: 'text' });
  }

  getDeviceId(): string {
    let id = localStorage.getItem('mm_device_id');
    if (!id) {
      id = crypto.randomUUID();
      localStorage.setItem('mm_device_id', id);
    }
    return id;
  }
}
