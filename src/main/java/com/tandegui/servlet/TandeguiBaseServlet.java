package com.tandegui.servlet;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @author Waiter
 * @project tandeguimanage
 * @class com.tandegui.servlet.TandeguiBaseServlet
 * @copyright www.scinan.com
 * @date 2019-04-10
 * @description
 */
public class TandeguiBaseServlet extends HttpServlet {

    protected final Logger logger = Logger.getLogger(TandeguiBaseServlet.class);

    @Override
    public void init() throws ServletException {
        //后续初始化城市信息，方便数据查询列表的展现
        logger.info("==================== tandegui manage init some information ====================");
    }
}
