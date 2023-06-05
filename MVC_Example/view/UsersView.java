package miniProjects.MVC_Example.view;

import miniProjects.MVC_Example.controller.Controller;
import miniProjects.MVC_Example.model.ModelData;

public class UsersView implements View {
    private Controller controller = new Controller();

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void refresh(ModelData modelData) {
        if (!modelData.isDisplayDeletedUserList()){
            System.out.println("All users:");
        }else System.out.println("All deleted users:");
        modelData.getUsers().forEach(user -> System.out.println("\t" + user));
        System.out.println("===================================================");
    }

    public void fireEventShowAllUsers(){
        controller.onShowAllUsers();
    }

    public void fireEventShowDeletedUsers() {
        controller.onShowAllDeletedUsers();
    }

    public void fireEventOpenUserEditForm(long id) {
        controller.onOpenUserEditForm(id);
    }

}
