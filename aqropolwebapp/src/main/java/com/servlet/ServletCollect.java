package com.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet(name = "ServletCollect", urlPatterns = "/collect")
public class ServletCollect extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    @Override
    public void destroy() {
        super.destroy();
    }


    Properties vProp = new Properties();

    @Override
    public void init() throws ServletException {
        super.init();
        InputStream vInputStream = null;

        vInputStream = ServletCollect.class.getResourceAsStream("/info.properties");
        try {
            vProp.load(vInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(vInputStream != null) {
                try {
                    vInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("<html>\n" +
                "<body>\n" +
                "<h2>Hello Servlet !</h2>\n" +
                vProp.getProperty("istic.aqropol.aqropol-webapp.version", "?") + "\n" +
                "</body>\n" +
                "</html>\n");
        out.flush();
    }
}
