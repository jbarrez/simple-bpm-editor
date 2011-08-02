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

import com.vaadin.ui.HorizontalLayout;

/**
 * @author Joram Barrez
 */
public class FlowEditor extends HorizontalLayout {

  private static final long serialVersionUID = 1L;
  
  protected static final String STYLE_FLOW_EDITOR = "flow-editor";
  
  protected ModelingPanel modelingPanel;
  protected PropertyPanel propertyPanel;
  
  public FlowEditor() {
    setSizeFull();
    setSpacing(true);
  }
  
  @Override
  public void attach() {
    super.attach();
    
    modelingPanel = new ModelingPanel();
    modelingPanel.setWidth(100, UNITS_PERCENTAGE);
    addComponent(modelingPanel);
    setExpandRatio(modelingPanel, 0.75f);
    
    propertyPanel = new PropertyPanel();
    propertyPanel.setWidth(100, UNITS_PERCENTAGE);
    propertyPanel.setHeight(100, UNITS_PERCENTAGE);
    addComponent(propertyPanel);
    setExpandRatio(propertyPanel, 0.25f);
  }
  
  public void replaceEmptyNode(Node emptyNode, Node newNode) {
    modelingPanel.replaceEmptyNode(emptyNode, newNode);
    modelingPanel.notifyNodesChanged();
  }
  
  public void removeNode(Node node) {
    modelingPanel.removeNode(node);
    modelingPanel.notifyNodesChanged();
  }
  
  public void notifyNodeTypeChanged() {
    modelingPanel.notifyNodesChanged();
  }
  
  public void showNodeProperties(Node node) {
    propertyPanel.showNodeProperties(node);
  }
  
}
