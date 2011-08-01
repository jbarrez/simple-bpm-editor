package com.jorambarrez;

import java.beans.PropertyChangeEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pantar.widget.graph.server.DefaultNode;
import com.pantar.widget.graph.server.GraphComponent;
import com.pantar.widget.graph.server.GraphModel;
import com.pantar.widget.graph.server.elements.BeginNode;
import com.pantar.widget.graph.server.elements.EndNode;
import com.pantar.widget.graph.server.elements.ProcessCreationNode;
import com.pantar.widget.graph.server.elements.SplitNode;
import com.pantar.widget.graph.server.events.NodeEventType;
import com.pantar.widget.graph.server.events.PropertyChangeCallback;
import com.pantar.widget.graph.server.factories.GraphModelFactory;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class ModelerApp extends Application implements HttpServletRequestListener {

  protected static ThreadLocal<ModelerApp> current = new ThreadLocal<ModelerApp>();
  
  protected Window window;
  protected FlowEditor flowEditor;
  
  public void init() {
    window = new Window("Flow editor prototype");
    window.setSizeFull();
    setTheme("activiti");
    setMainWindow(window);

    flowEditor = new FlowEditor();
    window.setContent(flowEditor);
    
//    tryGraphWidget(window);
  }

  protected void tryGraphWidget(Window window) {
    // = Create the model.
    final GraphModel graphModel = GraphModelFactory.getGraphModelInstance();

    // = Support for single click to select nodes.
    graphModel.setSingleSelectionSupport(Boolean.TRUE);

    // = Create some nodes.
    com.pantar.widget.graph.server.Node begin = new BeginNode();
    begin.setPosition(100.0, 200.0);
    graphModel.addNode(begin);
    

    com.pantar.widget.graph.server.Node splitNode = new SplitNode();
    splitNode.setPosition(100.0, 250.0);
    graphModel.addNode(splitNode);
    

    final com.pantar.widget.graph.server.Node nodeA = new DefaultNode();
    nodeA.setLabel("NodeA");
    nodeA.setPosition(200.0, 200.0);
    graphModel.addNode(nodeA);
    
    com.pantar.widget.graph.server.Node processCreation = new ProcessCreationNode();
    processCreation.setLabel("Process");
    processCreation.setPosition(200.0, 300.0);
    graphModel.addNode(processCreation);

    com.pantar.widget.graph.server.Node end = new EndNode();
    end.setPosition(300.0, 200.0);
    graphModel.addNode(end);

//    // = Nodes meet nodes!.
//    final DefaultRelationStyle dashedBlue = new DefaultRelationStyle();
//
//    final DefaultRelationStyle straightRed = new DefaultRelationStyle();
//
//    final DefaultRelationStyle defaultNormalBlack = new DefaultRelationStyle();
//
//    // = Tie all the stuff.
//    graphModel.connect(begin, splitNode, straightRed);
//    graphModel.connect(splitNode, nodeA, dashedBlue);
//    graphModel.connect(splitNode, nodeB, defaultNormalBlack);
//    graphModel.connect(nodeA, end, RelationTypeEnum.BEZIER);
//    graphModel.connect(nodeB, processCreation, RelationTypeEnum.BEZIER, new DefaultRelationStyle().strokeColor(GraphConstants.DOM.CSS_GREEN_VALUE));
//    graphModel.connect(processCreation, end, RelationTypeEnum.LINE);
    
    // = Create the component and pass the model on its constructor.
    final GraphComponent component = new GraphComponent(graphModel);
    component.setHeight(500, Label.UNITS_PIXELS);
    component.setWidth(500, Label.UNITS_PIXELS);

    // = Add the component to a container...
    final VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);
    layout.setSpacing(true);
    layout.addComponent(component);
    
    graphModel.registerCallback(NodeEventType.SELECTED, new PropertyChangeCallback() {
      
      public void onPropertyChange(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("Property=" + propertyChangeEvent.getPropertyName());
        System.out.println("New Value:" + propertyChangeEvent.getNewValue());
        System.out.println("Old Value:" + propertyChangeEvent.getOldValue());
      }
});

    
    window.setContent(layout);
  }
  
  public Window getWindow() {
    return window;
  }
  
  public FlowEditor getFlowEditor() {
    return flowEditor;
  }
  
  public static ModelerApp get() {
    return current.get();
  }
 
  public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
    current.set(this);
  }
  
  public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
    current.remove();
  }

}
