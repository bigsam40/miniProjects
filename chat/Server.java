package miniProjects.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Введите порт сервера");
        int port = ConsoleHelper.readInt();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ConsoleHelper.writeMessage("Сервер запущен");
            while (true) {
                Socket socket = serverSocket.accept();
                new Handler(socket).start();
            }
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Произошла ошибка при запуске или работе сервера.");
        }
    }

    public static void sendBroadcastMessage(Message message) {
        for (Map.Entry<String, Connection> connectionEntry : connectionMap.entrySet()) {
            try {
                connectionEntry.getValue().send(message);
            } catch (IOException e) {
                ConsoleHelper.writeMessage("Произошла ошибка при отправке сообщения пользователю");
            }
        }
    }

    private static class Handler extends Thread {
        private final Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage("Установлено соединение с адресом " + this.socket.getRemoteSocketAddress());
            String userName = null;
            //открываем соединение
            try (Connection connection = new Connection(socket)) {
                userName = serverHandshake(connection);
                //рассылка имени нового юзера другим пользователям
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));
                //отправка новому пользователю данных об остальных юзерах
                notifyUsers(connection, userName);
                // запуск главного цикла обмена сообщениями
                serverMainLoop(connection, userName);
            } catch (IOException | ClassNotFoundException e) {
                // в случае исключений просто ловим им и выводим сообщение о сбое связи
                ConsoleHelper.writeMessage("Ошибка при обмене данными с " + socket.getRemoteSocketAddress());
            }
            //в конце в любом случае пробуем удалить пользователя из connectionMap
            if (userName != null) {
                sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
                connectionMap.remove(userName);
            }
            ConsoleHelper.writeMessage("Соединение с удалённым адресом " + socket.getRemoteSocketAddress() + " закрыто");
        }

        private void serverMainLoop(Connection connection, String userName) {
            while (true) {
                Message userMessage = null;
                try {
                    userMessage = connection.receive();
                } catch (IOException | ClassNotFoundException e) {
                    continue;
                }
                if (userMessage.getType() == MessageType.TEXT && userMessage.getData() != null) {
                    sendBroadcastMessage(new Message(MessageType.TEXT, userName + ": " + userMessage.getData()));
                } else ConsoleHelper.writeMessage("Сообщение пользователя не соответствует протоколу");
            }
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message messageFromClient = connection.receive();
                if (messageFromClient.getType() != MessageType.USER_NAME) {
                    ConsoleHelper.writeMessage("Получено сообщение от " + socket.getRemoteSocketAddress() + ". Тип сообщения не соответствует протоколу.");
                    continue;
                }
                String userName = messageFromClient.getData();
                if (userName.isEmpty()) {
                    ConsoleHelper.writeMessage("Попытка подключения к серверу с пустым именем от " + socket.getRemoteSocketAddress());
                    continue;
                }
                if (connectionMap.containsKey(userName)) {
                    ConsoleHelper.writeMessage("Попытка подключения к серверу с уже используемым именем от " + socket.getRemoteSocketAddress());
                    continue;
                }
                connectionMap.put(userName, connection);
                connection.send(new Message(MessageType.NAME_ACCEPTED, "Соединение принято"));
                return userName;
            }
        }

        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (String name : connectionMap.keySet()) {
                if (name != userName) connection.send(new Message(MessageType.USER_ADDED, name));
            }
        }
    }

}
