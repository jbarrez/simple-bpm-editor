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

/* Copyright (C) 2005-2011 of Alfresco. All rights reserved.
 *
 * This file is part of Alfresco Pangu.
 * 
 * Alfresco Pangu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Alfresco Pangu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Alfresco Pangu.  If not, see <http://www.gnu.org/licenses/>. 
 */

package com.jorambarrez;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;


/**
 * @author Joram Barrez
 */
/**
 * @author jbarrez
 */
public class PropertyPanel extends VerticalLayout {

  private static final long serialVersionUID = 1L;
  
  protected static final String STYLE_PROPERTY_LAYOUT = "property-layout";
  
  public PropertyPanel() {
    setWidth(250, UNITS_PIXELS);
    setHeight(100, UNITS_PERCENTAGE);
    addStyleName(STYLE_PROPERTY_LAYOUT);
    setMargin(false, false, true, false);
  }
  
  @Override
  public void attach() {
    super.attach();
    
    Label label = new Label("&nbsp;", Label.CONTENT_XHTML);
    addComponent(label);
    setExpandRatio(label, 1.0f);
    
    addTrash();
  }

  protected void addTrash() {
    Embedded trashIcon = new Embedded(null, Images.MODELER_TRASH);
    trashIcon.setWidth(63, UNITS_PIXELS);
    trashIcon.setHeight(61, UNITS_PIXELS);
    trashIcon.setType(Embedded.TYPE_IMAGE);
    
    DragAndDropWrapper dragAndDropWrapper = new DragAndDropWrapper(trashIcon);
    dragAndDropWrapper.setDragStartMode(DragStartMode.NONE);
    dragAndDropWrapper.setSizeUndefined();
    addComponent(dragAndDropWrapper);
    setComponentAlignment(dragAndDropWrapper, Alignment.BOTTOM_CENTER);
    
    dragAndDropWrapper.setDropHandler(new DropHandler() {
      public AcceptCriterion getAcceptCriterion() {
        return AcceptAll.get();
      }
      public void drop(DragAndDropEvent event) {
        WrapperTransferable wrapperTransferable =
                (WrapperTransferable) event.getTransferable();
        Node srcNode = (Node) wrapperTransferable.getSourceComponent();
        
        // TODO: use eventrouter!
        ModelerApp.get().getFlowEditor().removeNode(srcNode);
      }
    });
  }

}
