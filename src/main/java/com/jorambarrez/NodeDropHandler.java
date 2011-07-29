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

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.DragAndDropWrapper.WrapperTargetDetails;
import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;


/**
 * @author Joram Barrez
 */
public class NodeDropHandler implements DropHandler {

  private static final long serialVersionUID = 1L;
  
  protected Node targetNode;
  
  public NodeDropHandler(Node node) {
    this.targetNode = node;
  }

  public AcceptCriterion getAcceptCriterion() {
    return AcceptAll.get();
  }

  public void drop(DragAndDropEvent event) {
    
    WrapperTransferable wrapperTransferable =
      (WrapperTransferable) event.getTransferable();
    WrapperTargetDetails details =
      (WrapperTargetDetails) event.getTargetDetails();
    
    Node srcNode = (Node) wrapperTransferable.getSourceComponent();
    if (targetNode.isEmpty()) {
      
      // TODO: use event router
      ModelerApp.get().getFlowEditor().replaceEmptyNode(targetNode, srcNode);
    }
  }
}
