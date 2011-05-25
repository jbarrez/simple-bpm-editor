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

import com.vaadin.ui.Label;


/**
 * @author Joram Barrez
 */
public class Node extends Label {

  private static final long serialVersionUID = 1L;
  
  protected boolean isEmptyNode;
  
  public Node(String text) {
    super(text);
  }
  
  public Node(String text, int mode) {
    super(text, mode);
  }

  public boolean isEmptyNode() {
    return isEmptyNode;
  }

  public void setEmptyNode(boolean isEmptyNode) {
    this.isEmptyNode = isEmptyNode;
  }

}
