package miniProjects.MVC_Example.model;

import miniProjects.MVC_Example.bean.User;
import miniProjects.MVC_Example.model.service.UserService;
import miniProjects.MVC_Example.model.service.UserServiceImpl;

import java.util.List;

public class MainModel implements Model{
    private UserService userService = new UserServiceImpl();
    private ModelData modelData = new ModelData();

    @Override
    public ModelData getModelData() {
        return modelData;
    }

    @Override
    public void loadUsers() {
        modelData.setUsers(getAllUsers());
        modelData.setDisplayDeletedUserList(false);
    }
    @Override
    public void loadDeletedUsers() {
        modelData.setUsers(userService.getAllDeletedUsers());
        modelData.setDisplayDeletedUserList(true);
    }
    @Override
    public void loadUserById(long userId) {
        User user = userService.getUsersById(userId);
        modelData.setActiveUser(user);
    }

    @Override
    public void deleteUserById(long id) {
        userService.deleteUser(id);
        modelData.setUsers(getAllUsers());
    }

    public void changeUserData(String name, long id, int level){
        userService.createOrUpdateUser(name, id, level);
        modelData.setUsers(getAllUsers());
    }

    private List<User> getAllUsers(){
        return userService.filterOnlyActiveUsers(userService.getUsersBetweenLevels(1, 100));
    }
}
