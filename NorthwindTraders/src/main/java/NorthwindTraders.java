import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class NorthwindTraders {
    public static void main(String[] args) {
        String username = "root";
        String password = "yearup";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/northwind",
                    username,
                    password);

            Statement statement = connection.createStatement();

            String query = "SELECT ProductID, ProductName, ROUND(UnitPrice, 2) AS UnitPrice, UnitsInStock FROM Products";
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                int id = results.getInt("ProductID");
                String name = results.getString("ProductName");
                double price = results.getDouble("UnitPrice");
                int stock = results.getInt("UnitsInStock");

                System.out.printf("%-10d  %-35s  $-10%.2f  %-10d  %n", id, name, price, stock);
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
