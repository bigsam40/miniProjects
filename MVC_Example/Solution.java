package miniProjects.MVC_Example;

import miniProjects.MVC_Example.controller.Controller;
import miniProjects.MVC_Example.model.MainModel;
import miniProjects.MVC_Example.model.Model;
import miniProjects.MVC_Example.view.EditUserView;
import miniProjects.MVC_Example.view.UsersView;

public class Solution {
    public static void main(String[] args) {
        Model model = new MainModel();
        UsersView usersView = new UsersView();
        Controller controller = new Controller();
        EditUserView editUserView = new EditUserView();

        usersView.setController(controller);
        editUserView.setController(controller);
        controller.setModel(model);
        controller.setUsersView(usersView);
        controller.setEditUserView(editUserView);

        usersView.fireEventShowAllUsers();
        usersView.fireEventOpenUserEditForm(126L);
        editUserView.fireEventUserDeleted(124L);
        editUserView.fireEventUserChanged("A", 123, 2);
        usersView.fireEventShowDeletedUsers();
    }
}