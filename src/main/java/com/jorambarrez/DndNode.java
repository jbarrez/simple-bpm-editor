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

import com.vaadin.ui.DragAndDropWrapper;


/**
 * @author Joram Barrez
 */
public class DndNode extends DragAndDropWrapper implements Node {

  private static final long serialVersionUID = 1L;
  
  protected static final String STYLE_DND_NODE = "dnd-node";
  
  protected BasicNode wrappedNode;

  public DndNode(STATE state) {
    super(new BasicNode());
    this.wrappedNode = (BasicNode) getCompositionRoot(); // DndWrapper extends custom component
      
    setDragStartMode(DragStartMode.COMPONENT);
    addStyleName(STYLE_DND_NODE);
    
    // The change state can update the size (so changing the size is done afterwards)
    changeState(state);
    setHeight(wrappedNode.getNodeHeight(), UNITS_PIXELS);
    setWidth(wrappedNode.getWidth(), UNITS_PIXELS);
    
    wrappedNode.addListener(new NodeClickListener(this));
  }
  
  public DndNode(STATE state, int width, int height) {
    this(state);
    setNodeWidth(width);
    setNodeHeight(height);
  }
  
  public DndNode(STATE state, String text) {
    this(state);
    setText(text);
  }
  
  public DndNode(STATE state, String text, int width, int height) {
    this(state, width, height);
    setText(text);
  }

  public boolean isProcessStep() {
    return wrappedNode.isProcessStep();
  }

  public boolean isEmpty() {
    return wrappedNode.isEmpty();
  }

  public boolean isCandidate() {
    return wrappedNode.isCandidate();
  }

  public void changeState(STATE state) {
    wrappedNode.changeState(state);
    
    if (isEmpty() || isCandidate()) {
      setDragStartMode(DragStartMode.NONE);
      if (getDropHandler() == null) {
        setDropHandler(new NodeDropHandler(this));
      }
    } else {
      setDragStartMode(DragStartMode.COMPONENT);
      setDropHandler(null);
    }
  }
  
  public void makeEditable() {
    wrappedNode.makeEditable();
  }

  public int getNodeWidth() {
    return wrappedNode.getNodeWidth();
  }

  public void setNodeWidth(int width) {
    setWidth(width, UNITS_PIXELS);
    wrappedNode.setNodeHeight(width);
  }

  public void setNodeHeight(int height) {
    wrappedNode.setNodeHeight(height);
    setHeight(height, UNITS_PIXELS);
  }

  public int getNodeHeight() {
    return wrappedNode.getNodeHeight();
  }

  public void setText(String text) {
    wrappedNode.setText(text);
  }
  
  public void setIndex(int index) {
    wrappedNode.setIndex(index);
  }

  public int getIndex() {
    return wrappedNode.getIndex();
  }
  
  @Override
  public String toString() {
    return wrappedNode.toString();
  }

}
