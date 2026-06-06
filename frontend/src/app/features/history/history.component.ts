import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Match } from '../../core/models/match.model';
import { MatchService } from '../../core/services/match.service';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="history-card" *ngIf="matches.length > 0">
      <h2 class="card-title">📋 Tous les matchs</h2>
      <div class="hist-row" *ngFor="let m of matches">
        <div class="hist-badge" [class.badge-played]="m.played" [class.badge-upcoming]="!m.played">
          {{ m.played ? 'FIN' : 'À VENIR' }}
        </div>
        <div class="hist-info">
          <p class="hist-teams">{{ getFlagEmoji(m.flagA) }} {{ m.teamA }} vs {{ m.teamB }} {{ getFlagEmoji(m.flagB) }}</p>
          <p class="hist-date">{{ m.dateFormatted }} · {{ m.timeFormatted }} · {{ m.city }}</p>
        </div>
        <div class="hist-score" *ngIf="m.played">{{ m.scoreA }} – {{ m.scoreB }}</div>
      </div>
    </div>
  `
})
export class HistoryComponent implements OnInit {
  matches: Match[] = [];
  private flagMap: Record<string, string> = {
    'ma': '🇲🇦', 'pt': '🇵🇹', 'ar': '🇦🇷', 'sk': '🇸🇰',
    'fr': '🇫🇷', 'es': '🇪🇸', 'br': '🇧🇷', 'de': '🇩🇪'
  };

  constructor(private matchSvc: MatchService) {}

  ngOnInit() {
    this.matchSvc.getMoroccoMatches().subscribe(m => this.matches = m);
  }

  getFlagEmoji(code: string): string { return this.flagMap[code] || '🏳️'; }
}
