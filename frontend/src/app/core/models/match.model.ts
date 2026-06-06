export interface Match {
  id: number;
  teamA: string;
  teamB: string;
  flagA: string;
  flagB: string;
  dateTimeUtc: string;
  dateTimeMaroc: string;
  dateFormatted: string;
  timeFormatted: string;
  stadium: string;
  city: string;
  competition: string;
  tvChannel: string;
  groupStage: string;
  played: boolean;
  scoreA: number | null;
  scoreB: number | null;
  secondsUntilMatch: number;
}
export interface PredictionRequest {
  matchId: number; scoreTeamA: number; scoreTeamB: number; deviceId: string;
}
export interface PredictionStats {
  matchId: number; totalVotes: number;
  winCount: number; drawCount: number; loseCount: number;
  winPct: number; drawPct: number; losePct: number;
  avgScoreA: number; avgScoreB: number;
}
export interface ReminderRequest {
  matchId: number; email: string; deviceId: string;
}
