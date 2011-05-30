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
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.And;
import com.vaadin.incubator.dragdroplayouts.DDGridLayout.GridLayoutTargetDetails;
import com.vaadin.incubator.dragdroplayouts.events.HorizontalLocationIs;
import com.vaadin.incubator.dragdroplayouts.events.LayoutBoundTransferable;
import com.vaadin.incubator.dragdroplayouts.events.VerticalLocationIs;


/**
 * @author Joram Barrez
 */
public class FlowEditorDropHandler implements DropHandler {

  private static final long serialVersionUID = 1L;
  
  protected FlowEditor flowEditor;
  
  public FlowEditorDropHandler(FlowEditor flowEditor) {
    this.flowEditor = flowEditor;
  }

  public AcceptCriterion getAcceptCriterion() {
    // Only allow dropping in the middle of the cell
    return new And(VerticalLocationIs.MIDDLE, HorizontalLocationIs.CENTER);
  }

  public void drop(DragAndDropEvent event) {
    GridLayoutTargetDetails details = (GridLayoutTargetDetails) event.getTargetDetails();
    LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();

    // get drag-drop information
    Node draggedNode = (Node) transferable.getComponent();
    int srcColum = flowEditor.getColumn(draggedNode);
    int srcRow = flowEditor.getRow(draggedNode);
    int targetColumn = details.getOverColumn();
    int targetRow = details.getOverRow();

    // If cell is empty then drop it there
    Node targetNode = (Node) flowEditor.getNode(targetColumn, targetRow);
    if (targetNode.isEmpty()) {
      
      // Remove dragged node from its original place
      flowEditor.removeNode(draggedNode);
      
      // Put it on the target node's place
      flowEditor.replaceNode(targetNode, draggedNode);
      
      // create an empty node on the original node place
      flowEditor.addEmptyNode(srcColum, srcRow);
      
      // Finally, make the grid resize if needed
      flowEditor.ensureCorrectGridSize();
    }
  }
}
