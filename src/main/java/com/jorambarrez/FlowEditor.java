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

import java.util.ArrayList;
import java.util.List;

import com.jorambarrez.Node.STATE;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Joram Barrez
 */
public class FlowEditor extends VerticalLayout {

  private static final long serialVersionUID = 1L;
  
  protected int currentWidth;
  protected int currentHeight;
  
  protected List<Node> nodes = new ArrayList<Node>();

  public FlowEditor() {
    addStyleName("border");
  }
  
  @Override
  public void attach() {
    setSpacing(false);
    setMargin(false);
    
    currentWidth = Node.DEFAULT_NODE_WIDTH * 3;
    currentHeight = Node.DEFAULT_NODE_HEIGHT * 3;
    setWidth(currentWidth, UNITS_PIXELS);
    setHeight(currentHeight, UNITS_PIXELS);
    
    // Initial setup
    addDefaultStartNode();
    addCandidateNode();
  }
  
  protected void addDefaultStartNode() {
    Node startNode = createNode(STATE.PROCESS_STEP, "First step");
    addComponent(startNode);
  }
  
  public Node addEmptyNode() {
    Node emptyNode = createNode(STATE.EMPTY);
    addComponent(emptyNode);
    return emptyNode;
  }
  
  public Node addEmptyNode(int row) {
    Node emptyNode = createNode(STATE.EMPTY);
    addComponent(emptyNode, row);
    return emptyNode;
  }
  
  public Node addCandidateNode() {
    Node candidateNode = createNode(STATE.CANDIDATE);
    addComponent(candidateNode);
    return candidateNode;
  }
  
  public Node addCandidateNode(int row) {
    Node candidateNode = createNode(STATE.CANDIDATE);
    addComponent(candidateNode, row);
    return candidateNode;
  }
  
  protected Node createNode(STATE state) {
    return new DndNode(state);
  }
  
  protected Node createNode(STATE state, String text) {
    return new DndNode(state, text);
  }
  
  public Node getNode(int row) {
    if (row >= getComponentCount()) {
      return null;
    }
    return (Node) getComponent(row);
  }
  
  public int getRow(Node node) {
    return getComponentIndex(node); 
  }
  
  public void removeNode(Node node) {
    removeComponent(node);
  }
  
  public void removeNode(int row) {
    removeNode(row);
  }
  
  public void replaceEmptyNode(Node emptyNode, Node newNode) {
    if (!emptyNode.isEmpty()) {
      throw new RuntimeException("Only possible to replace empty nodes");
    }
    replaceComponent(emptyNode, newNode); // and place it where the empty node was
    notifyNodesChanged();
  }
  
  public void notifyNodesChanged() {
    
    // Add empty node when there is not one at the start
    if (!getNode(0).isEmpty()) {
      addEmptyNode(0);
    }
    
    int index = 0;
    int newWidth = 0;
    int newHeight = 0;
    
    while (index < getComponentCount()) {
      
      boolean nextNodeRemoved = false;
      Node node = getNode(index);
      Node nextNode = getNode(index + 1);
      if (node.isProcessStep() && nextNode != null && !nextNode.isEmpty()) {
        addEmptyNode(index + 1);
      } else if (node.isEmpty() && nextNode != null && nextNode.isEmpty()) {
        removeNode(nextNode);
        nextNodeRemoved = true;
      }
        
      newWidth += node.getWidth();
      newHeight += node.getHeight();
      
      if (!nextNodeRemoved) {
        index++;
      }
    }
      
    // Alway add candidate node at the end
    if (!getNode(getComponentCount() - 1).isCandidate()) {
      Node candidateNode = addCandidateNode();
      newWidth += candidateNode.getNodeWidth();
      newHeight += candidateNode.getNodeHeight();
    }
    
    // Change layout size
    setWidth(newWidth, UNITS_PIXELS);
    setHeight(newHeight, UNITS_PIXELS);
      
  }
  
}
