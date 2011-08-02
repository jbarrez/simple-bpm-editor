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

import com.vaadin.ui.Component;



/**
 * @author Joram Barrez
 */
public interface Node extends Component {

  static enum STATE {EMPTY, PROCESS_STEP, CANDIDATE};
  
  public static final int DEFAULT_NODE_WIDTH = 350;
  public static final int DEFAULT_NODE_HEIGHT = 30;
  public static final int EMPTY_NODE_HEIGHT = 15;
  
  public boolean isProcessStep();
  public boolean isEmpty();
  public boolean isCandidate();
  public void changeState(STATE state);
  public void makeEditable();
  
  public int getNodeWidth();
  public void setNodeWidth(int width);
  public void setNodeHeight(int height);
  public int getNodeHeight();
  public String getText();
  public void setText(String text);
  public void setIndex(int index);
  public int getIndex();
  
  public String getProperty(String key);
  public String getPropertyType(String key);
  public void setProperty(String key, String value, String type);
  
}
