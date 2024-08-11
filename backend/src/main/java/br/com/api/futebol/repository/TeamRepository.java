package br.com.api.futebol.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.api.futebol.model.Team;

@Repository
public class TeamRepository {

	private final DataSource dataSource;

	// Criar conexão
	@Autowired
	public TeamRepository(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void save(Team team) {
		// Se a equipe não tiver ID, insere; se tiver, atualiza.
		String sql;
		if (team.getId() == null) {
			sql = "INSERT INTO teams (team_name, country, coach_name, team_value) VALUES (?, ?, ?, ?)";
		} else {
			sql = "UPDATE teams SET team_name = ?, country = ?, coach_name = ?, team_value = ? WHERE id = ?";
		}

		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			statement.setString(1, team.getTeamName());
			statement.setString(2, team.getCountry());
			statement.setString(3, team.getCoachName());
			statement.setBigDecimal(4, team.getTeamValue());

			if (team.getId() != null) {
				statement.setInt(5, team.getId());
			}

			statement.executeUpdate();

			if (team.getId() == null) {
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						team.setId(generatedKeys.getInt(1));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Team> findAll() {
		return findTeamsByCriteria(null, null, null, null, null, null, null, null, null);
	}

	public List<Team> findTeamsByCriteria(String teamName, String country, String coachName, BigDecimal teamValueMin, BigDecimal teamValueMax, LocalDateTime createdAtFrom, LocalDateTime createdAtTo, LocalDateTime updatedAtFrom, LocalDateTime updatedAtTo) {
	    List<Team> teams = new ArrayList<>();
	    StringBuilder sql = new StringBuilder("SELECT * FROM teams WHERE 1=1");//O WHERE 1=1 é uma técnica comum para
	    //simplificar a adição de condições adicionais(facilita o uso de AND)

	    if (teamName != null && !teamName.isEmpty()) {//verifica se não é nulo, se o campo não é null
	        sql.append(" AND team_name LIKE ?");//filtra o team_name	
	    }

	    if (country != null && !country.isEmpty()) {
	        sql.append(" AND country LIKE ?");
	    }

	    if (coachName != null && !coachName.isEmpty()) {
	        sql.append(" AND coach_name LIKE ?");
	    }

	    if (teamValueMin != null) {
	        sql.append(" AND team_value >= ?");
	    }

	    if (teamValueMax != null) {
	        sql.append(" AND team_value <= ?");
	    }

	    if (createdAtFrom != null) {
	        sql.append(" AND created_at >= ?");
	    }

	    if (createdAtTo != null) {
	        sql.append(" AND created_at <= ?");
	    }

	    if (updatedAtFrom != null) {
	        sql.append(" AND updated_at >= ?");
	    }

	    if (updatedAtTo != null) {
	        sql.append(" AND updated_at <= ?");
	    }

	    try (Connection connection = dataSource.getConnection();
	         PreparedStatement statement = connection.prepareStatement(sql.toString())) {

	        int index = 1;

	        if (teamName != null && !teamName.isEmpty()) {
	            statement.setString(index++, "%" + teamName + "%");//"%" + teamName + "%": Adiciona % ao redor de teamName para buscar 
	            //qualquer texto que contenha o valor de teamName dentro dele.
	        }

	        if (country != null && !country.isEmpty()) {
	            statement.setString(index++, "%" + country + "%");
	        }

	        if (coachName != null && !coachName.isEmpty()) {
	            statement.setString(index++, "%" + coachName + "%");
	        }

	        if (teamValueMin != null) {
	            statement.setBigDecimal(index++, teamValueMin);
	        }

	        if (teamValueMax != null) {
	            statement.setBigDecimal(index++, teamValueMax);
	        }

	        if (createdAtFrom != null) {
	            statement.setObject(index++, createdAtFrom);
	        }

	        if (createdAtTo != null) {
	            statement.setObject(index++, createdAtTo);
	        }

	        if (updatedAtFrom != null) {
	            statement.setObject(index++, updatedAtFrom);
	        }

	        if (updatedAtTo != null) {
	            statement.setObject(index++, updatedAtTo);
	        }

	        try (ResultSet resultSet = statement.executeQuery()) {//Armazena os resultados da consulta.
	            while (resultSet.next()) {
	                Team team = mapRow(resultSet);
	                teams.add(team);//Adiciona o objeto Team à lista teams
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return teams;
	}


	public Optional<Team> findById(Integer id) {
		String sql = "SELECT * FROM teams WHERE id = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(mapRow(resultSet));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public void deleteById(Integer id) {
		String sql = "DELETE FROM teams WHERE id = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Team mapRow(ResultSet resultSet) throws SQLException {
		//Converte resultados de banco de dados em objetos Team
		Team team = new Team();
		team.setId(resultSet.getInt("id"));
		//Obtém o valor do campo id da linha atual do ResultSet e define no objeto team.
		team.setTeamName(resultSet.getString("team_name"));
		team.setCountry(resultSet.getString("country"));
		team.setCoachName(resultSet.getString("coach_name"));
		team.setTeamValue(resultSet.getBigDecimal("team_value"));
		team.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));
		team.setUpdatedAt(resultSet.getObject("updated_at", LocalDateTime.class));
		return team;
	}

}
