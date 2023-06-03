package miniProjects.chat.client;

public class ClientGuiController extends Client{
    private ClientGuiModel model = new ClientGuiModel();
    private ClientGuiView view = new ClientGuiView(this);

    public static void main(String[] args) {
        new ClientGuiController().run();
    }

    public ClientGuiModel getModel(){
        return model;
    }

    @Override
    protected SocketThread getSocketThread() {
        return new GuiSocketThread();
    } //- должен создавать и возвращать объект типа GuiSocketThread.

    @Override
    public void run() {
        getSocketThread().run();
    }//- должен получать объект SocketThread через метод getSocketThread() и вызывать у него метод run().
    //Разберись, почему нет необходимости вызывать метод run() в отдельном потоке, как мы это делали для консольного клиента.

    @Override
    protected String getServerAddress() {
        return view.getServerAddress();
    }

    @Override
    protected int getServerPort() {
        return view.getServerPort();
    }

    @Override
    protected String getUserName() {
        return view.getUserName();
    }//Они должны вызывать одноименные методы из представления (view).

    public class GuiSocketThread extends SocketThread{
        @Override
        protected void processIncomingMessage(String message){
            model.setNewMessage(message);
            view.refreshMessages();
        } //- должен устанавливать новое сообщение у модели и вызывать обновление вывода сообщений у представления.

        @Override
        protected void informAboutAddingNewUser(String userName) {
            model.addUser(userName);
            view.refreshUsers();
        }//- должен добавлять нового пользователя в модель и вызывать обновление вывода пользователей у отображения.

        @Override
        protected void informAboutDeletingNewUser(String userName) {
            model.deleteUser(userName);
            view.refreshUsers();
        }//- должен удалять пользователя из модели и вызывать обновление вывода пользователей у отображения.

        @Override
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            view.notifyConnectionStatusChanged(clientConnected);
        }//- должен вызывать аналогичный метод у представления.

    }
}
