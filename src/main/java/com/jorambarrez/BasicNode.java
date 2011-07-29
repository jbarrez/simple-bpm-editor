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

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;


/**
 * @author Joram Barrez
 */
public class BasicNode extends CssLayout implements Node {

  private static final long serialVersionUID = 1L;
  
  // Static stuff: heights, styles, etc.
  public static final int DEFAULT_NODE_WIDTH = 100;
  public static final int DEFAULT_NODE_HEIGHT = 50;
  public static final int EMPTY_NODE_HEIGHT = 25;
  public static final int HEIGHT_BETWEEN_NODES = DEFAULT_NODE_HEIGHT / 2;
  
  // Static stuff
  protected static final String STYLE_PROCESS_STEP = "process-step";
  protected static final String STYLE_PROCESS_STEP_TEXT = "process-step-text"; 
  protected static final String STYLE_CANDIDATE = "candidate";
  protected static final String STYLE_EMPTY = "empty";

  // Instance vars
  protected STATE currentState = STATE.PROCESS_STEP;
  protected int width;
  protected int height;
  
  protected DragAndDropWrapper dragAndDropWrapper;
  protected HorizontalLayout innerLayout;
  protected Label label;
  
  public BasicNode() {
    setNodeWidth(DEFAULT_NODE_WIDTH);
    setNodeHeight(DEFAULT_NODE_HEIGHT);
    
    innerLayout = new HorizontalLayout();
    innerLayout.setSizeFull();
    addComponent(innerLayout);
    
    label = new Label();
    label.addStyleName(STYLE_PROCESS_STEP_TEXT);
    label.setSizeUndefined();
    innerLayout.addComponent(label);
    innerLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
    
    addListener(new NodeClickListener(this));
  }
  
  public boolean isProcessStep() {
    return currentState.equals(STATE.PROCESS_STEP);
  }
  
  public boolean isEmpty() {
    return currentState.equals(STATE.EMPTY);
  }
  
  public boolean isCandidate() {
    return currentState.equals(STATE.CANDIDATE);
  }
  
  public void changeState(STATE state) {
    currentState = state;
    clearStyles();
 
    // Change styling
    if (currentState.equals(STATE.EMPTY)) {
      label.setValue("&nbsp");
      label.setContentMode(Label.CONTENT_XHTML);
      setNodeHeight(EMPTY_NODE_HEIGHT);
      innerLayout.addStyleName(STYLE_EMPTY);
    } else if(currentState.equals(STATE.CANDIDATE)) {
      innerLayout.addStyleName(STYLE_CANDIDATE);
    } else {
      innerLayout.addStyleName(STYLE_PROCESS_STEP);
    }
  }
  
  protected void clearStyles() {
    innerLayout.removeStyleName(STYLE_PROCESS_STEP);
    innerLayout.removeStyleName(STYLE_CANDIDATE);
    innerLayout.removeStyleName(STYLE_EMPTY);
  }
  
  public int getNodeWidth() {
    return width;
  }

  public void setNodeWidth(int width) {
    this.width = width;
    setWidth(width, UNITS_PIXELS);
  }
  
  public void setNodeHeight(int height) {
    this.height = height;
    setHeight(height, UNITS_PIXELS);
  }
  
  public int getNodeHeight() {
    return height;
  }

  public void setText(String text) {
    label.setValue(text);
  }
  
  @Override
  public String toString() {
    return "[" + currentState + "] '" + label +"'";
  }
  
}
