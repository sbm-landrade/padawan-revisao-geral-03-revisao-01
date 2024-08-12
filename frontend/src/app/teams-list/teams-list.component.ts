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
      console.log('Times carregados:', data);
      this.teams = data;
    });
  }

  searchTeams() {
    console.log('Buscando times com parâmetros:', {
      teamName: this.teamName,
      country: this.country,
      coachName: this.coachName,
      teamValueMin: this.teamValueMin,
      teamValueMax: this.teamValueMax,
      createdAtFrom: this.createdAtFrom,
      createdAtTo: this.createdAtTo,
      updatedAtFrom: this.updatedAtFrom,
      updatedAtTo: this.updatedAtTo
    });
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
    ).subscribe(teams => {//Assina um "observable"
      console.log('Times encontrados:', teams);
      this.teams = teams;
    });
  }

  createTeam() {
    if (this.newTeam.teamName && this.newTeam.country && this.newTeam.coachName && this.newTeam.teamValue) {
      console.log('Dados para criação do time:', this.newTeam);
      this.teamService.createTeam(this.newTeam as Team).subscribe(() => {
        console.log('Resposta da criação do time:', Response);
        this.searchTeams(); // Atualiza a lista após criação
        this.newTeam = { teamName: '', country: '', coachName: '', teamValue: 0 }; // Limpa o formulário
      });
    } else {
      alert('Por favor, preencha todos os campos.');
    }
  }

  onUpdate(team: Team) {
    //Atualiza um time existente. Após a atualização, atualiza a lista de times e desmarca o time selecionado.
    console.log('Atualizando team:', team); // Verifica os dados do time antes de enviar
    if (team.id != null) {
          // Log para verificar se o ID está presente e o serviço será chamado
      console.log('ID do time encontrado:', team.id);
      this.teamService.updateTeam(team).subscribe(() => {
        // Log para verificar a resposta do serviço
        console.log('Atualizado com sucesso:', Response);
        this.searchTeams(); // Atualiza a lista após atualização
        this.selectedTeam = null; // Desmarca o time selecionado
      });
    }
  }

  onDelete(id: number) {
    console.log('Deletando time com ID:', id);
    this.teamService.deleteTeam(id).subscribe(() => {
      this.searchTeams(); // Atualiza a lista após exclusão
    });
  }

  selectTeam(team: Team) {
    console.log('Time selecionado:', team);
    this.selectedTeam = { ...team };
  }

  clearSelection() {
    console.log('Limpar seleção de time');
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
    //console.log('Valor formatado:', formattedValue);
    return formattedValue.replace(',', '.'); // Substitui vírgula por ponto
  }
}
