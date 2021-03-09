package server;

import commands.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientHandler implements Runnable {
    private Server server;
    private Socket socket;

    private DataInputStream in;
    private DataOutputStream out;

    private String nickname;
    private String login;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            Server.log.error(e.getMessage(), e);
        }
    }

    @Override
    public void run() {
        try {
            socket.setSoTimeout(120000);
            //цикл аутентификации
            while (true) {
                String str = in.readUTF();
                if (str.startsWith("/")) {
                    if (str.equals(Command.END)) {
                        Server.log.info("client want to disconnected ");
                        out.writeUTF(Command.END);
                        throw new RuntimeException("client want to disconnected");
                    }
                    if (str.startsWith(Command.AUTH)) {
                        String[] token = str.split("\\s");
                        String newNick = server.getAuthService()
                                .getNicknameByLoginAndPassword(token[1], token[2]);
                        login = token[1];
                        if (newNick != null) {
                            if (!server.isLoginAuthenticated(login)) {
                                nickname = newNick;
                                sendMsg(Command.AUTH_OK + " " + nickname);
                                server.subscribe(this);
                                Server.log.info("Успешная аутентификация " + nickname);
                                break;
                            } else {
                                sendMsg("С этим логинов уже вошли");
                                Server.log.warn("С этим логинов уже вошли");
                            }
                        } else {
                            sendMsg("Неверный логин / пароль");
                            Server.log.warn("Неверный логин / пароль");
                        }
                    }

                    if (str.startsWith(Command.REG)) {
                        String[] token = str.split("\\s");
                        if (token.length < 4) {
                            continue;
                        }
                        boolean regSuccessful = server.getAuthService()
                                .registration(token[1], token[2], token[3]);
                        if (regSuccessful) {
                            sendMsg(Command.REG_OK);
                            Server.log.info("Успешная регистрация");
                        } else {
                            sendMsg(Command.REG_NO);
                            Server.log.warn("Регистрация не удалась");
                        }
                    }
                }
            }

            socket.setSoTimeout(0);

            //цикл работы
            while (true) {
                String str = server.getAuthService().censor(in.readUTF());

                if (str.startsWith("/")) {
                    if (str.startsWith(Command.NICK_CHG)) {
                        String[] token = str.split("\\s+", 2);
                        if (server.getAuthService().changeNicknameByLogin(this.login, token[1])) {
                            server.broadcastMsg(this, "change nick on: " + token[1]);
                            out.writeUTF(Command.END);
                            Server.log.info("Смена ника на: " + token[1]);
                            break;
                        }
                        continue;
                    }
                    if (str.equals(Command.END)) {
                        out.writeUTF(Command.END);
                        Server.log.info("Отключение пользователя");
                        break;
                    }

                    if (str.startsWith(Command.PRIVATE_MSG)) {
                        String[] token = str.split("\\s+", 3);
                        if (token.length < 3) {
                            continue;
                        }
                        server.privateMsg(this, token[1], token[2]);
                    }
                } else {
                    server.broadcastMsg(this, str);
                }
            }

//               SocketTimeoutException
        } catch (SocketTimeoutException e) {
            Server.log.warn(e.getMessage(), e);
            try {
                out.writeUTF(Command.END);
            } catch (IOException ioException) {
                Server.log.error(ioException.getMessage(), ioException);
            }
        } catch (RuntimeException e) {
            Server.log.error(e.getMessage(), e);
        } catch (IOException e) {
            Server.log.error(e.getMessage(), e);
        } finally {
//            System.out.println("Client disconnected");
            Server.log.info("client disconnected ");
            server.unsubscribe(this);
            try {
                socket.close();
            } catch (IOException e) {
                Server.log.error(e.getMessage(), e);
            }
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            Server.log.error(e.getMessage(), e);
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }
}
