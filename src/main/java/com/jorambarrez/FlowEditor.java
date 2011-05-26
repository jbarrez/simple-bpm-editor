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
    ensureHorizontalGridSize();
    ensureVerticalGridSize();
  }

  protected void ensureHorizontalGridSize() {
    NodeCoordinates rightMostNodeCoordinates = findRightMostNode();
    int oldLastColumn = layout.getColumns() - 1;
    int newLastColumn = rightMostNodeCoordinates.getColumn() + 1;
    
    if (oldLastColumn > newLastColumn) { // Shrink
    
      // Remove obsolete empty nodes
      for (int column = oldLastColumn; column > newLastColumn; column--) {
        for (int row = 0; row < layout.getRows(); row++) {
          layout.removeComponent(column, row);
        }
      }
      layout.setColumns(newLastColumn + 1);
      
      // Resize grid
      currentWidth -= (oldLastColumn - newLastColumn) * NODE_WIDTH;
      layout.setWidth(currentWidth, UNITS_PIXELS);
    
    } else if (oldLastColumn < newLastColumn) { // Expand
      
      int nrOfColumns = newLastColumn + 1;
      layout.setColumns(nrOfColumns);
      
      currentWidth += NODE_WIDTH;
      layout.setWidth(currentWidth, UNITS_PIXELS);
      
      for (int row = 0; row < layout.getRows(); row++) {
        addEmptyNode(nrOfColumns - 1, row);
      }
    }
  }
  
  protected void ensureVerticalGridSize() {
    NodeCoordinates lowestNodeCoordinates = findLowestNode();
    int oldLastRow = layout.getRows() - 1;
    int newLastRow = lowestNodeCoordinates.getRow() + 1;
    
    if (oldLastRow > newLastRow) { // Shrink
      
      // Remove obsolete empty nodes
      for (int row = oldLastRow; row > newLastRow; row--) {
        for (int column = 0; column < layout.getColumns(); column++) {
          layout.removeComponent(column, row);
        }
      }
      layout.setRows(newLastRow + 1);
      
      // Resize grid
      currentHeight -= (oldLastRow - newLastRow) * NODE_HEIGHT;
      layout.setHeight(currentHeight, UNITS_PIXELS);
      
    } else if (oldLastRow < newLastRow) { // Expad
      
      int nrOfRows = newLastRow + 1;
      layout.setRows(nrOfRows);
      
      currentHeight += NODE_HEIGHT;
      layout.setHeight(currentHeight, UNITS_PIXELS);
      
      for (int column = 0; column < layout.getColumns(); column ++) {
        addEmptyNode(column, nrOfRows - 1); 
      }
      
    }
  }

  protected NodeCoordinates findRightMostNode() {
    for (int column = layout.getColumns() - 1; column >= 0; column--) {
      for (int row = 0; row <= layout.getRows() - 1; row++) {
        if (!getNode(column, row).isEmptyNode()) {
          return new NodeCoordinates(column, row);
        }
      }
    }
    
    throw new RuntimeException("Programming error: there should *always* be a rightmost node");
  }
  
  protected NodeCoordinates findLowestNode() {
    for (int row = layout.getRows() - 1; row >= 0; row--) {
      for (int column = 0; column < layout.getColumns(); column++) {
        if (!getNode(column, row).isEmptyNode()) {
          return new NodeCoordinates(column, row);
        }
      }
    }
    throw new RuntimeException("Programming error: there should *always* be a lowest node");
  }
  
}
