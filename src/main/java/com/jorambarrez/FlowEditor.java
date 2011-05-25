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
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.And;
import com.vaadin.incubator.dragdroplayouts.DDGridLayout;
import com.vaadin.incubator.dragdroplayouts.DDGridLayout.GridLayoutTargetDetails;
import com.vaadin.incubator.dragdroplayouts.client.ui.LayoutDragMode;
import com.vaadin.incubator.dragdroplayouts.events.HorizontalLocationIs;
import com.vaadin.incubator.dragdroplayouts.events.LayoutBoundTransferable;
import com.vaadin.incubator.dragdroplayouts.events.VerticalLocationIs;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

/**
 * @author Joram Barrez
 */
public class FlowEditor extends CustomComponent {

  private static final long serialVersionUID = 1L;
  
  protected DDGridLayout layout;

  public FlowEditor() {

    layout = new DDGridLayout();
    setCompositionRoot(layout);

    layout.setColumns(2);
    layout.setRows(2);
    layout.setSpacing(false);
    layout.setMargin(false);
    
    layout.setWidth(200, UNITS_PIXELS);
    layout.setHeight(200, UNITS_PIXELS);
    
    // Drop zone should be the whole center area
    layout.setComponentHorizontalDropRatio(0);
    layout.setComponentVerticalDropRatio(0);

    // Enable dragging components
    layout.setDragMode(LayoutDragMode.CLONE);

    // Enable dropping components
    layout.setDropHandler(new DropHandler() {

      public AcceptCriterion getAcceptCriterion() {
        // Only allow dropping in the middle of the cell
        return new And(VerticalLocationIs.MIDDLE, HorizontalLocationIs.CENTER);
      }

      public void drop(DragAndDropEvent event) {
        GridLayoutTargetDetails details = (GridLayoutTargetDetails) event.getTargetDetails();
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event.getTransferable();

        int column = details.getOverColumn();
        int row = details.getOverRow();

        // Get the dragged component
        Component c = transferable.getComponent();

        // If cell is empty then drop it there
        if (layout.getComponent(column, row) == null) {
          layout.removeComponent(c);
          layout.addComponent(c, column, row);
          layout.setComponentAlignment(c, Alignment.MIDDLE_CENTER);
        }
      }
    });

    layout.addListener(new LayoutClickListener() {
      public void layoutClick(LayoutClickEvent event) {
        
      }
    });
  }
  
  protected void createLabel(String text, int row, int column) {
    Label lbl = new Label(text);
    lbl.addStyleName("node");
    lbl.setWidth(100, UNITS_PIXELS);
    lbl.setHeight(100, UNITS_PIXELS);
    layout.addComponent(lbl, column, row);
    layout.setComponentAlignment(lbl, Alignment.MIDDLE_CENTER);
  }
  
  public void setInitialNode(String text) {
    createLabel(text, 0, 0);
  }
  
}
