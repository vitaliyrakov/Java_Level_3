
import java.sql.*;

public class Main {

    private static final String URL = "jdbc:sqlite:/home/vit77/Programming/geekbrains/Java_Level_3/lesson_2/test1.db";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        init();
        try (Connection connection = getConnection()) {
//            createNewDatabase(connection);
//            createNewTable(connection);

            insert(connection, "Raw Materials", 3000);
            insert(connection, "Semifinished Goods", 4000);
            insert(connection, "Finished Goods", 5000);
            selectAll(connection);
        }
    }

    private static void init() throws ClassNotFoundException {
        Class.forName("org.sqlite.SQLiteJDBCLoader");
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private static void createNewDatabase(Connection connection) throws SQLException {
        if (connection != null) {
            DatabaseMetaData meta = connection.getMetaData();
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("A new database has been created.");
        }
    }

    private static void createNewTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	capacity real\n"
                + ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static void insert(Connection connection, String name, double capacity) throws SQLException {
        String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, capacity);
            pstmt.executeUpdate();
        }
    }

    private static void selectAll(Connection connection) throws SQLException {
        String sql = "SELECT id, name, capacity FROM warehouses";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("name") + "\t" +
                        rs.getDouble("capacity"));
            }
        }

    }

}