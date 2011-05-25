package com.jorambarrez;

import com.vaadin.Application;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class MyVaadinApplication extends Application {

  private Window window;

  public void init() {
    window = new Window("Flow editor prototype");
    setTheme("activiti");
    setMainWindow(window);

    FlowEditor flowEditor = new FlowEditor();
    flowEditor.setInitialNode("Task A");
    
    window.addComponent(flowEditor);
  }

}
