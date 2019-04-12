package com.tandegui.annotation;

import java.lang.annotation.*;

/**
 * @author Waiter
 * @project tandeguimanage
 * @class com.tandegui.annotation.SystemLog
 * @copyright www.tandegui.com
 * @date 2019-04-11
 * @description 系统日志
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

    String module()  default "";  //模块名称 系统管理-用户管理－列表页面
    String methods()  default "";  //新增用户
    String description()  default "";  //执行状态

}
