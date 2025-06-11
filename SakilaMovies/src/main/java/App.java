import org.apache.commons.dbcp2.BasicDataSource;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
        dataSource.setUsername(args[0]);
        dataSource.setPassword(args[1]);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = dataSource.getConnection()){
            System.out.print("Enter the actor's last name: ");
            String lastName = scanner.nextLine();

            String actorQuery = "SELECT actor_id, first_name, last_name FROM actor WHERE last_name = ?";

            try (PreparedStatement actorSt = connection.prepareStatement(actorQuery)) {
                actorSt.setString(1, lastName);
                try(ResultSet results = actorSt.executeQuery()) {
                    if (results.next()) {
                        System.out.println("Actors :\n");
                        do {
                            int id = results.getInt("actor_id");
                            String first = results.getString("first_name");
                            String last = results.getString("last_name");
                            System.out.printf("ID: %d - %s %s%n", id, first, last);
                        } while (results.next());
                    } else {
                        System.out.println("No matches!");
                        return;
                    }
                }
            }

            System.out.print("\nEnter actor's first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter actor's last name again: ");
            String lastNameAgain = scanner.nextLine();

            String filmQuery = " SELECT f.title FROM film f JOIN film_actor fa ON f.film_id = fa.film_id JOIN actor a ON fa.actor_id = a.actor_id WHERE a.first_name = ? AND a.last_name = ? ";

            try (PreparedStatement filmSt = connection.prepareStatement(filmQuery)) {
                filmSt.setString(1, firstName);
                filmSt.setString(2, lastNameAgain);

                try (ResultSet filmResults = filmSt.executeQuery()) {
                    if (filmResults.next()) {
                        System.out.println("\nMovies with that actor:\n");
                        do {
                            System.out.println(filmResults.getString("title"));
                        } while (filmResults.next());
                    } else {
                        System.out.println("Actor not found or has no films");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
