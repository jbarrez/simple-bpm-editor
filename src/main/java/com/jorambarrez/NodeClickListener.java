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
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Label;


/**
 * @author Joram Barrez
 */
public class NodeClickListener implements LayoutClickListener {
  
  private static final long serialVersionUID = 1L;
  
  protected Node node;
  
  protected static int counter = 2; // temporary
  
  public NodeClickListener(Node node) {
    this.node = node;
  }

  public void layoutClick(LayoutClickEvent event) {
    
    // If clicked anywhere on a candidate node, make it a process step
    if (node.isCandidate()) {
      node.changeState(STATE.PROCESS_STEP);
      node.setText("Step " + counter++);
      
      // TODO: this should be done by event router!
      ModelerApp.get().getFlowEditor().notifyNodeTypeChanged();
      ModelerApp.get().getWindow().scrollIntoView(node);
      
    } else if (node.isProcessStep()) {
      
      // If clicked on the text, or double clicked, the label should become editable
      if (event.isDoubleClick() || event.getClickedComponent() instanceof Label) {
        node.makeEditable();
      }

      // If clicked (no double click) the property panel should just change
    }
  }

}
