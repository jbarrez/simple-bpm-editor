/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jorambarrez;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.incubator.dragdroplayouts.DDGridLayout;
import com.vaadin.incubator.dragdroplayouts.client.ui.LayoutDragMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

/**
 * @author Joram Barrez
 */
public class FlowEditor extends CustomComponent {

  private static final long serialVersionUID = 1L;
  
  protected static final int NODE_WIDTH = 100;
  protected static final int NODE_HEIGHT = 100;
  
  protected int currentWidth;
  protected int currentHeight;
  protected DDGridLayout layout;

  public FlowEditor() {

    layout = new DDGridLayout();
    setCompositionRoot(layout);

    layout.setColumns(2);
    layout.setRows(2);
    layout.setSpacing(false);
    layout.setMargin(false);
    
    currentWidth = 200;
    currentHeight = 200;
    layout.setWidth(currentWidth, UNITS_PIXELS);
    layout.setHeight(currentHeight, UNITS_PIXELS);
    
    // Drop zone should be the whole center area
    layout.setComponentHorizontalDropRatio(0);
    layout.setComponentVerticalDropRatio(0);

    // Enable dragging components
    layout.setDragMode(LayoutDragMode.CLONE);

    // Enable dropping components
    layout.setDropHandler(new FlowEditorDropHandler(this));

    // Enable clicking for new nodes
    layout.addListener(new LayoutClickListener() {
      public void layoutClick(LayoutClickEvent event) {
//        event.isDoubleClick();
      }
    });
    
    // Initial: all empty
    addEmptyNode(0, 0);
    addEmptyNode(0, 1);
    addEmptyNode(1, 0);
    addEmptyNode(1, 1);
  }
  
  public Node getNode(int column, int row) {
    return (Node) layout.getComponent(column, row);
  }
  
  public int getColumn(Node node) {
    return layout.getComponentArea(node).getColumn1(); // col1 or col2, it doesnt matter here
  }
  
  public int getRow(Node node) {
    return layout.getComponentArea(node).getRow1(); // row 1 or row2, it doesnt matter here
  }
  
  public void setInitialNode(String text) {
    addNode(text, 0, 0);
  }
  
  public void addNode(String text, int row, int column) {
    Node node = new Node(text);
    node.addStyleName("node");
    addNode(node, row, column);
  }
  
  public void addEmptyNode(int column, int row) {
    Node node = new Node("&nbsp;", Label.CONTENT_XHTML);
    node.setEmptyNode(true);
    addNode(node, row, column);
  }
  
  protected void addNode(Node node, int row, int column) {
    node.setWidth(NODE_WIDTH, UNITS_PIXELS);
    node.setHeight(NODE_HEIGHT, UNITS_PIXELS);
    
    Component component = layout.getComponent(column, row);
    if (component != null) {
      layout.replaceComponent(component, node);
    } else {
      layout.addComponent(node, column, row);
    }
    
    layout.setComponentAlignment(node, Alignment.MIDDLE_CENTER);
  }
  
  public void removeNode(Node node) {
    layout.removeComponent(node);
  }
  
  public void replaceNode(Node originalNode, Node newNode) {
    layout.replaceComponent(originalNode, newNode); // and place it where the empty node was
    layout.setComponentAlignment(newNode, Alignment.MIDDLE_CENTER);
  }
  
  public void ensureCorrectGridSize() {
    boolean horizontalSizeChange = false;
    
    // Remove column if column before last two column contains empty node
    int secondLastColumn = layout.getColumns() - 2;
    int lastColumn = secondLastColumn + 1;
    
    Node secondLastNode = (Node) layout.getComponent(secondLastColumn, 0);
    Node lastNode = (Node) layout.getComponent(lastColumn, 0);

    if (secondLastNode.isEmptyNode() && lastNode.isEmptyNode()) {
      
      // Find all empty nodes
      boolean nonEmptyNodeFound = false;
      int nrOfExtraEmptyNodes = 0;
      while(!nonEmptyNodeFound) {
        Node node = (Node) layout.getComponent(secondLastColumn - nrOfExtraEmptyNodes - 1, 0);
        if (node.isEmptyNode()) {
          nrOfExtraEmptyNodes++;
        } else {
          nonEmptyNodeFound = true;
        }
      }
      
      // Delete all empty nodes
      for (int i = lastColumn; i >= lastColumn - nrOfExtraEmptyNodes; i--) {
        layout.removeComponent(i, 0);
      }
      layout.setColumns(lastColumn - nrOfExtraEmptyNodes);
      
      currentWidth -= NODE_WIDTH + (nrOfExtraEmptyNodes * NODE_WIDTH);
      layout.setWidth(currentWidth, UNITS_PIXELS);
      horizontalSizeChange = true;
    }
    
    // Add column if last column contains non-empty node
    if (!horizontalSizeChange && lastNode.getData() == null) {
      int nrOfColumns = layout.getColumns() + 1;
      layout.setColumns(nrOfColumns);
      
      currentWidth += NODE_WIDTH;
      layout.setWidth(currentWidth, UNITS_PIXELS);
      
      addEmptyNode(nrOfColumns - 1, 0);
    }
  }

}
