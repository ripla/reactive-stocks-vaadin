package ui

import com.vaadin.server.VaadinServlet
import javax.servlet.annotation.WebServlet
import javax.servlet.annotation.WebInitParam

@WebServlet(asyncSupported = true, urlPatterns = Array("/*"), initParams = Array( new WebInitParam(name = "UI", value = "ui.StockUI"),
        new WebInitParam(name = "pushmode", value = "automatic") ))
class StockServlet extends VaadinServlet {

}