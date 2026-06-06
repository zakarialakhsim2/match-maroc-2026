import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Observable, switchMap, of } from 'rxjs';
import { Match, PredictionStats } from '../../core/models/match.model';
import { MatchService } from '../../core/services/match.service';
import { PredictionService } from '../../core/services/prediction.service';
import { ReminderService } from '../../core/services/reminder.service';
import { CountdownComponent } from '../countdown/countdown.component';
import { HistoryComponent } from '../history/history.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, CountdownComponent, HistoryComponent],
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  match: Match | null = null;
  stats: PredictionStats | null = null;
  loading = true;
  error = false;
  scoreA = 2;
  scoreB = 1;
  voted = false;
  reminded = false;
  emailInput = '';
  showEmailForm = false;

  get flagUrl() {
    const flagMap: Record<string, string> = {
      'ma': '🇲🇦', 'pt': '🇵🇹', 'ar': '🇦🇷',
      'sk': '🇸🇰', 'fr': '🇫🇷', 'es': '🇪🇸',
      'br': '🇧🇷', 'de': '🇩🇪', 'us': '🇺🇸'
    };
    return flagMap;
  }

  constructor(
    private matchSvc: MatchService,
    private predSvc: PredictionService,
    private remSvc: ReminderService
  ) {}

  ngOnInit() {
    this.matchSvc.getUpcoming().subscribe({
      next: (m) => {
        this.match = m;
        this.loading = false;
        this.loadStats(m.id);
      },
      error: () => { this.loading = false; this.error = true; }
    });
  }

  loadStats(matchId: number) {
    this.predSvc.getStats(matchId).subscribe(s => this.stats = s);
  }

  vote() {
    if (!this.match || this.voted) return;
    const deviceId = this.remSvc.getDeviceId();
    this.predSvc.vote({
      matchId: this.match.id,
      scoreTeamA: this.scoreA,
      scoreTeamB: this.scoreB,
      deviceId
    }).subscribe(() => {
      this.voted = true;
      this.loadStats(this.match!.id);
    });
  }

  submitReminder() {
    if (!this.match || !this.emailInput) return;
    this.remSvc.register(this.match.id, this.emailInput).subscribe(() => {
      this.reminded = true;
      this.showEmailForm = false;
    });
  }

  shareWhatsApp() {
    if (!this.match) return;
    const text = `🇲🇦 ${this.match.teamA} vs ${this.match.teamB}\n${this.match.dateTimeMaroc}\n${this.match.stadium}, ${this.match.city}\nTV: ${this.match.tvChannel}\n\nAllez les Lions de l'Atlas ! 🦁\nhttps://matchmaroc2026.ma`;
    window.open(`https://wa.me/?text=${encodeURIComponent(text)}`, '_blank');
  }

  copyLink() {
    navigator.clipboard.writeText('https://matchmaroc2026.ma');
  }

  getOutcomeLabel(): string {
    if (this.scoreA > this.scoreB) return 'Victoire du Maroc';
    if (this.scoreA < this.scoreB) return 'Défaite du Maroc';
    return 'Match nul';
  }
}
