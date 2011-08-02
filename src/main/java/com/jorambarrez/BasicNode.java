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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * @author Joram Barrez
 */
public class BasicNode extends VerticalLayout implements Node {

  private static final long serialVersionUID = 1L;
  
  // Static stuff
  protected static final String STYLE_NODE = "node";
  protected static final String STYLE_PROCESS_STEP = "process-step";
  protected static final String STYLE_PROCESS_STEP_TEXT = "process-step-text"; 
  protected static final String STYLE_CANDIDATE = "candidate";
  protected static final String STYLE_EMPTY = "empty";
  protected static final String STYLE_PROCESS_STEP_TEXTFIELD = "process-step-textfield ";

  // Instance vars
  protected STATE currentState = STATE.PROCESS_STEP;
  protected int width;
  protected int height;
  protected int index;
  
  protected Map<String, String> properties = new HashMap<String, String>();
  protected Map<String, String> propertyTypes = new HashMap<String, String>();
  
  // Ui
  protected DragAndDropWrapper dragAndDropWrapper;
  protected boolean editable;
  protected Label label;
  protected String originalText;
  protected Label invisibleLabel;
  protected TextField textField;
  
  public BasicNode() {
    setNodeWidth(DEFAULT_NODE_WIDTH);
    setNodeHeight(DEFAULT_NODE_HEIGHT);
    
    initLabel(null);
    addDemoProperties();
  }
  
  protected void addDemoProperties() {
    String[] assignees = new String[] {"John Doe", "Kermit The Frog", "Speedy Gonzales"};
    setProperty("Assignee", assignees[new Random().nextInt(assignees.length)], "text");
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    setProperty("Deadline", dateFormat.format(new Date(new Date().getTime() + new Random().nextLong())), "date");
  }
  
  protected void initLabel(String text) {
    if (text != null) {
      label = new Label(text);
    } else {
      label = new Label();
    }
    label.addStyleName(STYLE_PROCESS_STEP_TEXT);
    label.setSizeUndefined();
    addComponent(label);
    setComponentAlignment(label, Alignment.MIDDLE_CENTER);
    
    originalText = text;
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
      addStyleName(STYLE_EMPTY);
    } else if(currentState.equals(STATE.CANDIDATE)) {
      addStyleName(STYLE_CANDIDATE);
    } else {
      addStyleName(STYLE_PROCESS_STEP);
    }
  }
  
  protected void clearStyles() {
    removeStyleName(STYLE_PROCESS_STEP);
    removeStyleName(STYLE_CANDIDATE);
    removeStyleName(STYLE_EMPTY);
  }
  
  public void makeEditable() {
    if (!currentState.equals(STATE.PROCESS_STEP)) {
      throw new RuntimeException("It is only possible to make a process step editable");
    }
    
    if (editable) {
      throw new RuntimeException("Node is already editable");
    }
    
    editable = true;
    String labelText = (String) label.getValue();
    removeComponent(label);
    initTextField(labelText);
  }
  
  protected void initTextField(String text) {
    textField = new TextField();
    if (text != null) {
      textField.setValue(text);
      textField.selectAll();
    }
    textField.setWidth(100, UNITS_PERCENTAGE); // see explanation above in comments
    textField.addStyleName(STYLE_PROCESS_STEP_TEXTFIELD);
    textField.focus();
    
    addComponent(textField);
    setComponentAlignment(textField, Alignment.MIDDLE_CENTER);
    
    // Listeners: for enter key (= accept value), escape (= cancel) and auto expanding
    
    textField.addShortcutListener(new ShortcutListener(null, KeyCode.ENTER, null) {
      private static final long serialVersionUID = 1L;
      public void handleAction(Object sender, Object target) {
        switchBackToLabel((String) textField.getValue());
      }
    });
    
    textField.addShortcutListener(new ShortcutListener(null, KeyCode.ESCAPE, null) {
      private static final long serialVersionUID = 1L;
      public void handleAction(Object sender, Object target) {
        switchBackToLabel(originalText);
      }
    });

  }
  
  protected void switchBackToLabel(String text) {
    removeComponent(textField);
    initLabel(text);
    editable = false;
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
    originalText = text;
  }
  
  public String getText() {
    if (label != null) {
      return (String) label.getValue();
    } else {
      return originalText;
    }
  }
  
  public int getIndex() {
    return index;
  }
  
  public void setIndex(int index) {
    this.index = index;
  }
  
  public String getProperty(String key) {
    return properties.get(key);
  }
  
  public String getPropertyType(String key) {
    return propertyTypes.get(key);
  }
  
  public void setProperty(String key, String value, String type) {
    properties.put(key, value);
    propertyTypes.put(key, type);
  }

  @Override
  public String toString() {
    return "[" + currentState + "] '" + label +"'";
  }

}
