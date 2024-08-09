import { Component, OnInit } from '@angular/core';
import { TeamService } from '../services/team.service';
import { Team } from '../models/team';

@Component({
  selector: 'app-teams-list',
  templateUrl: './teams-list.component.html',
  styleUrls: ['./teams-list.component.css']
})
export class TeamsListComponent implements OnInit {

  teams: Team[] = [];
  selectedTeam: Team | null = null;
  newTeam: Partial<Team> = {

  };
  teamName: string = '';
  country: string = '';
  coachName: string = '';
  teamValueMin?: number;
  teamValueMax?: number;
  createdAtFrom?: string;
  createdAtTo?: string;
  updatedAtFrom?: string;
  updatedAtTo?: string;

  constructor(private teamService: TeamService) { }

  ngOnInit(): void {
    this.loadTeams();
  }

  loadTeams(): void {
    this.teamService.getAllTeams().subscribe(data => {
      this.teams = data;
    });
  }

  searchTeams() {
    this.teamService.getFilteredTeams(
      this.teamName,
      this.country,
      this.coachName,
      this.teamValueMin,
      this.teamValueMax,
      this.createdAtFrom,
      this.createdAtTo,
      this.updatedAtFrom,
      this.updatedAtTo
    ).subscribe(teams => {
      this.teams = teams;
    });
  }

  createTeam() {
    if (this.newTeam.teamName && this.newTeam.country && this.newTeam.coachName && this.newTeam.teamValue) {
      this.teamService.createTeam(this.newTeam as Team).subscribe(() => {
        this.searchTeams(); // Atualiza a lista após criação
        this.newTeam = { teamName: '', country: '', coachName: '', teamValue: 0 }; // Limpa o formulário
      });
    } else {
      alert('Por favor, preencha todos os campos.');
    }
  }

  onUpdate(team: Team) {
    console.log('Atualizando team:', team); // Verifica os dados do time antes de enviar
    if (team.id != null) {
      this.teamService.updateTeam(team).subscribe(() => {
        console.log('Atualizado sucesso:', Response);
        this.searchTeams(); // Atualiza a lista após atualização
        this.selectedTeam = null; // Desmarca o time selecionado
      });
    }
  }

  onDelete(id: number) {
    this.teamService.deleteTeam(id).subscribe(() => {
      this.searchTeams(); // Atualiza a lista após exclusão
    });
  }

  selectTeam(team: Team) {
    this.selectedTeam = { ...team };
  }

  clearSelection() {
    this.selectedTeam = null;
  }

  saveTeam(): void {
    if (this.selectedTeam) {
      if (this.selectedTeam.id) {
        // Atualizar equipe existente
        this.onUpdate(this.selectedTeam);
      } else {
        // Criar nova equipe
        this.createTeam();
      }
    }
  }

  formatCurrency(value: number): string {
    const formattedValue = value.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    return formattedValue.replace(',', '.'); // Substitui vírgula por ponto
  }
}
