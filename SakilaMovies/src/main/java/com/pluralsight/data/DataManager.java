package com.pluralsight.data;

import com.pluralsight.model.Actor;
import com.pluralsight.model.Film;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private final DataSource dataSource;

    public DataManager(String url, String username, String password) {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        this.dataSource = ds;
    }
    public DataManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Actor> searchActorsByLastName(String lastName) {
        List<Actor> actors = new ArrayList<>();
        String query = "SELECT actor_id, first_name, last_name FROM actor WHERE last_name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, lastName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Actor actor = new Actor(
                            rs.getInt("actor_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name")
                    );
                    actors.add(actor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return actors;
    }

    public List<Film> getFilmsByActorName(String firstName, String lastName) {
        List<Film> films = new ArrayList<>();
        String query = "SELECT f.film_id, f.title, f.description, f.release_year, f.length " +
                "FROM film f " +
                "JOIN film_actor fa ON f.film_id = fa.film_id " +
                "JOIN actor a ON fa.actor_id = a.actor_id " +
                "WHERE a.first_name = ? AND a.last_name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Film film = new Film(
                            rs.getInt("film_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getInt("release_year"),
                            rs.getInt("length")
                    );
                    films.add(film);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return films;
    }
    public List<Film> getFilmsByActorId(int actorId) {
        List<Film> films = new ArrayList<>();
        String query = """
        SELECT f.film_id, f.title, f.description, f.release_year, f.length
        FROM film f
        JOIN film_actor fa ON f.film_id = fa.film_id
        WHERE fa.actor_id = ?
    """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, actorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Film film = new Film(
                            rs.getInt("film_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getInt("release_year"),
                            rs.getInt("length")
                    );
                    films.add(film);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return films;
    }
}
