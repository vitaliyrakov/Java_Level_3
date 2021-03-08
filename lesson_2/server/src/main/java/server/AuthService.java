package server;

public interface AuthService {
    String getNicknameByLoginAndPassword(String login, String password);

    String censor(String message);

    boolean registration(String login, String password, String nickname);

    boolean changeNicknameByLogin(String login, String s);
}
