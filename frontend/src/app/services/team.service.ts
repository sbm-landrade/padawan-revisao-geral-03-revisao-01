import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Team } from '../models/team';
@Injectable({
  providedIn: 'root'
})
export class TeamService {

  private apiUrl = 'http://localhost:8080/api/teams';

  constructor(private http: HttpClient) {}
     // Método para obter todos os times
  getAllTeams(): Observable<Team[]> {
    return this.http.get<Team[]>(this.apiUrl);
  }

  // Método para buscar times com filtros
  searchTeams(teamName?: string, country?: string, coachName?: string): Observable<Team[]> {
    let params = new HttpParams();
    if (teamName) params = params.set('teamName', teamName);
    if (country) params = params.set('country', country);
    if (coachName) params = params.set('coachName', coachName);

    return this.http.get<Team[]>(this.apiUrl, { params });
  }

  getFilteredTeams(
    teamName?: string,
    country?: string,
    coachName?: string,
    teamValueMin?: number,
    teamValueMax?: number,
    createdAtFrom?: string,
    createdAtTo?: string,
    updatedAtFrom?: string,
    updatedAtTo?: string
  ): Observable<Team[]> {
    let params = new HttpParams();

    if (teamName) params = params.set('teamName', teamName);
    if (country) params = params.set('country', country);
    if (coachName) params = params.set('coachName', coachName);
    if (teamValueMin) params = params.set('teamValueMin', teamValueMin.toString());
    if (teamValueMax) params = params.set('teamValueMax', teamValueMax.toString());
    if (createdAtFrom) params = params.set('createdAtFrom', createdAtFrom);
    if (createdAtTo) params = params.set('createdAtTo', createdAtTo);
    if (updatedAtFrom) params = params.set('updatedAtFrom', updatedAtFrom);
    if (updatedAtTo) params = params.set('updatedAtTo', updatedAtTo);

    return this.http.get<Team[]>(this.apiUrl, { params });
  }
  createTeam(team: Team): Observable<Team> {
    return this.http.post<Team>(this.apiUrl, team);
  }

  updateTeam(team: Team): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${team.id}`, team);
  }

  deleteTeam(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}
