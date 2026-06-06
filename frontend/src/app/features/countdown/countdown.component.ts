import { Component, Input, OnInit, OnDestroy, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-countdown',
  standalone: true,
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="countdown-wrap">
      <p class="cd-title">COMPTE À REBOURS</p>
      <div class="cd-units">
        <div class="cd-unit">
          <span class="cd-num">{{ pad(days) }}</span>
          <span class="cd-lbl">JOURS</span>
        </div>
        <span class="cd-sep">:</span>
        <div class="cd-unit">
          <span class="cd-num">{{ pad(hours) }}</span>
          <span class="cd-lbl">HEURES</span>
        </div>
        <span class="cd-sep">:</span>
        <div class="cd-unit">
          <span class="cd-num">{{ pad(mins) }}</span>
          <span class="cd-lbl">MINS</span>
        </div>
        <span class="cd-sep">:</span>
        <div class="cd-unit">
          <span class="cd-num">{{ pad(secs) }}</span>
          <span class="cd-lbl">SECS</span>
        </div>
      </div>
      <p class="cd-sub" *ngIf="days === 0 && hours === 0 && mins === 0 && secs === 0">
        🔴 MATCH EN DIRECT
      </p>
    </div>
  `
})
export class CountdownComponent implements OnInit, OnDestroy {
  @Input() dateTimeUtc!: string;
  days = 0; hours = 0; mins = 0; secs = 0;
  private sub!: Subscription;

  constructor(private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.sub = interval(1000).subscribe(() => {
      const diff = new Date(this.dateTimeUtc).getTime() - Date.now();
      if (diff <= 0) { this.days = this.hours = this.mins = this.secs = 0; this.cdr.markForCheck(); return; }
      this.days = Math.floor(diff / 86400000);
      this.hours = Math.floor((diff % 86400000) / 3600000);
      this.mins = Math.floor((diff % 3600000) / 60000);
      this.secs = Math.floor((diff % 60000) / 1000);
      this.cdr.markForCheck();
    });
  }

  pad(n: number): string { return String(n).padStart(2, '0'); }
  ngOnDestroy() { this.sub?.unsubscribe(); }
}
