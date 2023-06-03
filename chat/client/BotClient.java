package miniProjects.chat.client;

import miniProjects.chat.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BotClient extends Client {
    public static void main(String[] args) {
        new BotClient().run();
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {
        return String.format("date_bot_%d", (int) (Math.random() * 100));
    }

    public class BotSocketThread extends Client.SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
            String[] splitMessage = message.split(": ");
            if (splitMessage.length != 2) return;
            StringBuilder stringBuilder = new StringBuilder("Информация для ").append(splitMessage[0]).append(": ");
            SimpleDateFormat simpleDateFormat = null;
            switch (splitMessage[1]) {
                case "дата":
                    simpleDateFormat = new SimpleDateFormat("d.MM.yyyy");
                    break;
                case "день":
                    simpleDateFormat = new SimpleDateFormat("d");
                    break;
                case "месяц":
                    simpleDateFormat = new SimpleDateFormat("MMMM");
                    break;
                case "год":
                    simpleDateFormat = new SimpleDateFormat("yyyy");
                    break;
                case "время":
                    simpleDateFormat = new SimpleDateFormat("H:mm:ss");
                    break;
                case "час":
                    simpleDateFormat = new SimpleDateFormat("H");
                    break;
                case "минуты":
                    simpleDateFormat = new SimpleDateFormat("m");
                    break;
                case "секунды":
                    simpleDateFormat = new SimpleDateFormat("s");
                    break;
            }
            if (simpleDateFormat != null) {
                stringBuilder.append(simpleDateFormat.format(new Date()));
                sendTextMessage(stringBuilder.toString());
            }
        }
    }
}
