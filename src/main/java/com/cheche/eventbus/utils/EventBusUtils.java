package com.cheche.eventbus.utils;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author cheche
 * @date 2022/12/29
 */
public class EventBusUtils {

  /**
   * 扩展topic
   * /rule-engine/device/alarm/abc/abc1/abc2
   *
   * @param topicName 原topic
   * @return 拓展后的topic
   */
  public static String topicNameExpand(String topicName) {
    int index = topicName.indexOf(StrPool.SLASH, topicName.indexOf(StrPool.SLASH,
      topicName.indexOf(StrPool.SLASH, topicName.indexOf("/") + 1) + 1) + 1);
    String resultPre = topicName.substring(0, index + 1);
    String result = topicName.substring(index + 1);
    int length = result.split(StrPool.SLASH).length - 1;
    StringBuffer tempStr = new StringBuffer();
    for (int i = 0; i <= length; i++) {
      tempStr = tempStr.append("*").append(StrPool.SLASH);
    }
    return StringUtils.join(resultPre, StrUtil.strip(tempStr, StrPool.SLASH));
  }

  /**
   * 匹配 ？ 批次单个字符， * 匹配
   *
   * @param s 原字符
   * @param p 批次字符串
   * @return 批次结果
   */
  public static boolean isMatch(String s, String p) {
    boolean[][] value = new boolean[p.length() + 1][s.length() + 1];
    value[0][0] = true;
    for (int i = 1; i <= s.length(); i++) {
      value[0][i] = false;
    }
    //value[i][j]就是p的第j个字符是不是匹配和s的第i个字符
    for (int i = 1;i <= p.length(); i++) {
      if (p.charAt(i-1) == '*') {
        value[i][0] = value[i-1][0];
        for (int j = 1; j <= s.length(); j++) {
          //*代表n个字符      *代表0个字符
          value[i][j] = (value[i][j-1] || value[i-1][j]);
        }
      } else if (p.charAt(i-1) == '?') {
        value[i][0] = false;
        for (int j = 1;j <= s.length(); j++) {
          //？只能代表上一个
          value[i][j] = value[i-1][j-1];
        }
      } else {
        value[i][0] = false;
        for (int j = 1; j <= s.length(); j++) {
          //其他时刻  必须是完全相等的时候才可以
          value[i][j] = s.charAt(j - 1) == p.charAt(i - 1) && value[i - 1][j - 1];
        }
      }
    }
    return value[p.length()][s.length()];
  }

}
