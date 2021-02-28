package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleAuthService implements AuthService {

    private class UserData {
        String login;
        String password;
        String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    private List<UserData> users;
    private HashMap<String, String> censorList;

    public SimpleAuthService() {
        users = new ArrayList<>();
        censorList = new HashMap();
        init();
        createTableUser();
        selectAllUsers();

        createTableCensor();
//        insertCens("спб", "СПБ");
//        insertCens("жопа", "****");
        selectAllCensor();
    }

    private void selectAllCensor() {
        String sql = "SELECT word, cens FROM censorList";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                censorList.put(rs.getString("word"), rs.getString("cens"));
//                System.out.println(rs.getString("word") +" "+ rs.getString("cens"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void init() {
        try {
            Class.forName("org.sqlite.SQLiteJDBCLoader");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        String URL = "jdbc:sqlite:chatBase.db";
        return DriverManager.getConnection(URL);
    }

    private void createTableUser() {
        String sql = "CREATE TABLE IF NOT EXISTS user (\n"
                + "	login text NOT NULL,\n"
                + "	password text NOT NULL,\n"
                + "	nickname text NOT NULL\n"
                + ");";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTableCensor() {
        String sql = "CREATE TABLE IF NOT EXISTS censorList (\n"
                + "	word text NOT NULL,\n"
                + "	cens text NOT NULL\n"
                + ");";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void selectAllUsers() {
        String sql = "SELECT login, nickname, password FROM user";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new UserData(rs.getString("login"), rs.getString("password"), rs.getString("nickname")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insert(String login, String nickname, String password) {
        String sql = "INSERT INTO user(login,nickname,password) VALUES(?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, nickname);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertCens(String word, String cens) {
        String sql = "INSERT INTO censorList(word, cens) VALUES(?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, word);
            pstmt.setString(2, cens);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean update(String login, String nickName) {
        String sql = "UPDATE user SET nickname = ? "
                + "WHERE login = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nickName);
            pstmt.setString(2, login);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (UserData user : users) {
            if (user.login.equals(login) && user.password.equals(password)) {
                return user.nickname;
            }
        }
        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        for (UserData user : users) {
            if (user.login.equals(login) || user.nickname.equals(nickname)) {
                return false;
            }
        }
        users.add(new UserData(login, password, nickname));
        insert(login, password, nickname);
        return true;
    }

    @Override
    public boolean changeNicknameByLogin(String login, String newNickName) {
        if (update(login, newNickName))
            for (UserData user : users) {
                if (user.login.equals(login))
                    user.setNickname(newNickName);
            }
        return true;
    }

    public String censor(String readUTF) {
        for (Map.Entry<String, String> cens : censorList.entrySet()) {
            if (readUTF.contains(cens.getKey())) {
                return readUTF.replace(cens.getKey(), cens.getValue());
            }
        }
        return readUTF;
    }

}
