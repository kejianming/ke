package com.nfdw.util;

import lombok.Getter;
import lombok.Setter;

/**

 * 复选框类
 */
@Getter
@Setter
public class Checkbox {

  private String id;
  private String name;
  /**默认未选择*/
  private boolean check=false;

}
