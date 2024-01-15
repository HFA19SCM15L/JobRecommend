package rpc;

import java.io.IOException;
import java.io.Serial;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Logout extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    public Logout() {
        // TODO Auto-generated constructor stub
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("index.html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // the logout functionality is the same whether the servlet is accessed via GET or POST
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}