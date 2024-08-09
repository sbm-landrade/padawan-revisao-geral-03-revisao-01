package br.com.api.futebol.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.futebol.model.Team;
import br.com.api.futebol.repository.TeamRepository;

@Service
public class TeamService {

	@Autowired
    private TeamRepository teamRepository;

	public List<Team> getTeamsByCriteria(String teamName, String country, String coachName, BigDecimal teamValueMin, BigDecimal teamValueMax, LocalDateTime createdAtFrom, LocalDateTime createdAtTo, LocalDateTime updatedAtFrom, LocalDateTime updatedAtTo) {
	    return teamRepository.findTeamsByCriteria(teamName, country, coachName, teamValueMin, teamValueMax, createdAtFrom, createdAtTo, updatedAtFrom, updatedAtTo);
	}

    public Optional<Team> getTeamById(Integer id) {
        return teamRepository.findById(id);
    }

    public void saveTeam(Team team) {
        teamRepository.save(team);
    }

    public void deleteTeam(Integer id) {
        teamRepository.deleteById(id);
    }
}
