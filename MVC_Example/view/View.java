package miniProjects.MVC_Example.view;

import miniProjects.MVC_Example.controller.Controller;
import miniProjects.MVC_Example.model.ModelData;

public interface View {
    void refresh(ModelData modelData);
    void setController(Controller controller);
}
