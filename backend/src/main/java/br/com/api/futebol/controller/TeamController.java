package br.com.api.futebol.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.futebol.model.Team;
import br.com.api.futebol.service.TeamService;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

	@Autowired
	private TeamService teamService;

	// listar todos os times
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping
	public ResponseEntity<List<Team>> getAllTeams(
	    @RequestParam(value = "teamName", required = false) String teamName,
	    @RequestParam(value = "country", required = false) String country,
	    @RequestParam(value = "coachName", required = false) String coachName,
	    @RequestParam(value = "teamValueMin", required = false) BigDecimal teamValueMin,
	    @RequestParam(value = "teamValueMax", required = false) BigDecimal teamValueMax,
	    @RequestParam(value = "createdAtFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtFrom,
	    @RequestParam(value = "createdAtTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtTo,
	    @RequestParam(value = "updatedAtFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAtFrom,
	    @RequestParam(value = "updatedAtTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAtTo) {

	    List<Team> teams = teamService.getTeamsByCriteria(
	        teamName, country, coachName, 
	        teamValueMin, teamValueMax, 
	        createdAtFrom, createdAtTo, 
	        updatedAtFrom, updatedAtTo
	    );
	    return ResponseEntity.ok(teams);
	}


	@GetMapping("/{id}")
	public ResponseEntity<Team> getTeamById(@PathVariable Integer id) {
		Optional<Team> team = teamService.getTeamById(id);
		return team.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Team> createTeam(@RequestBody Team team) {
		teamService.saveTeam(team);
		return ResponseEntity.status(HttpStatus.CREATED).body(team);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Team> updateTeam(@PathVariable Integer id, @RequestBody Team team) {
		if (teamService.getTeamById(id).isPresent()) {
			team.setId(id);
			teamService.saveTeam(team);
			return ResponseEntity.ok(team);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTeam(@PathVariable Integer id) {
		if (teamService.getTeamById(id).isPresent()) {
			teamService.deleteTeam(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
