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

import com.jorambarrez.Node.STATE;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Joram Barrez
 */
public class ModelingPanel extends VerticalLayout {

  private static final long serialVersionUID = 1L;
  
  public ModelingPanel() {
  }
  
  @Override
  public void attach() {
    setSpacing(false);
    setMargin(false);
    
    setHeight(Node.DEFAULT_NODE_HEIGHT * 2 + 2 * Node.EMPTY_NODE_HEIGHT, UNITS_PIXELS);
    
    // Initial setup
    addEmptyNode();
    addDefaultStartNode();
    addEmptyNode();
    addCandidateNode();
  }
  
  protected void addDefaultStartNode() {
    Node startNode = createNode(STATE.PROCESS_STEP, "Step 1");
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
  
  @Override
  public void addComponent(Component c) {
    super.addComponent(c);
    setComponentAlignment(c, Alignment.MIDDLE_CENTER);
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
    if (getComponentCount() > 4) { // 2 empty, 1 process step and 1 candidate
      removeComponent(node);
    }
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
    int newHeight = 0;
    
    while (index < getComponentCount()) {
      
      boolean nextNodeRemoved = false;
      Node node = getNode(index);
      Node nextNode = getNode(index + 1);
      if (node.isProcessStep()) {
        if ( (nextNode != null && !nextNode.isEmpty())
                || nextNode == null) {
          addEmptyNode(index + 1);
        }
      } else if (node.isEmpty() && nextNode != null && nextNode.isEmpty()) {
        removeNode(nextNode);
        nextNodeRemoved = true;
      }
        
      if (!nextNodeRemoved) {
        node.setIndex(index);
        newHeight += node.getHeight();
        index++;
      }
    }
      
    // Alway add candidate node at the end
    if (!getNode(getComponentCount() - 1).isCandidate()) {
      Node candidateNode = addCandidateNode();
      candidateNode.setIndex(index+1);
      newHeight += candidateNode.getNodeHeight();
    }
    
    // Change total layout size
    setHeight(newHeight, UNITS_PIXELS);
      
  }
  
}
