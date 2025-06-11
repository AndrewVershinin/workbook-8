package com.pluralsight;

import com.pluralsight.data.DataManager;
import com.pluralsight.model.Actor;
import com.pluralsight.model.Film;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
//        dataSource.setUsername(args[0]);
//        dataSource.setPassword(args[1]);
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//
//        try (Connection connection = dataSource.getConnection()){
//            System.out.print("Enter the actor's last name: ");
//            String lastName = scanner.nextLine();
//
//            String actorQuery = "SELECT actor_id, first_name, last_name FROM actor WHERE last_name = ?";
//
//            try (PreparedStatement actorSt = connection.prepareStatement(actorQuery)) {
//                actorSt.setString(1, lastName);
//                try(ResultSet results = actorSt.executeQuery()) {
//                    if (results.next()) {
//                        System.out.println("Actors :\n");
//                        do {
//                            int id = results.getInt("actor_id");
//                            String first = results.getString("first_name");
//                            String last = results.getString("last_name");
//                            System.out.printf("ID: %d - %s %s%n", id, first, last);
//                        } while (results.next());
//                    } else {
//                        System.out.println("No matches!");
//                        return;
//                    }
//                }
//            }
//
//            System.out.print("\nEnter actor's first name: ");
//            String firstName = scanner.nextLine();
//
//            System.out.print("Enter actor's last name again: ");
//            String lastNameAgain = scanner.nextLine();
//
//            String filmQuery = " SELECT f.title FROM film f JOIN film_actor fa ON f.film_id = fa.film_id JOIN actor a ON fa.actor_id = a.actor_id WHERE a.first_name = ? AND a.last_name = ? ";
//
//            try (PreparedStatement filmSt = connection.prepareStatement(filmQuery)) {
//                filmSt.setString(1, firstName);
//                filmSt.setString(2, lastNameAgain);
//
//                try (ResultSet filmResults = filmSt.executeQuery()) {
//                    if (filmResults.next()) {
//                        System.out.println("\nMovies with that actor:\n");
//                        do {
//                            System.out.println(filmResults.getString("title"));
//                        } while (filmResults.next());
//                    } else {
//                        System.out.println("Actor not found or has no films");
//                    }
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        if (args.length != 2) {
            System.out.println("Usage: java App <username> <password>");
            return;
        }

        String username = args[0];
        String password = args[1];

        // Setup DataSource
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        DataManager dataManager = new DataManager(dataSource);

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter actor's last name: ");
            String lastName = scanner.nextLine();

            List<Actor> actors = dataManager.searchActorsByLastName(lastName);

            if (actors.isEmpty()) {
                System.out.println("No matches!");
                return;
            }

            for (Actor actor : actors) {
                System.out.printf("ID: %d - %s %s%n", actor.getActorId(), actor.getFirstName(), actor.getLastName());
            }

            System.out.print("\nEnter actor ID to see their films: ");
            int actorId = scanner.nextInt();

            List<Film> films = dataManager.getFilmsByActorId(actorId);

            if (films.isEmpty()) {
                System.out.println("No films found for that actor.");
            } else {
                System.out.println("\nFilms:");
                for (Film film : films) {
                    System.out.printf("%s (%d) - %s [%d min]%n",
                            film.getTitle(),
                            film.getReleaseYear(),
                            film.getDescription(),
                            film.getLength());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
