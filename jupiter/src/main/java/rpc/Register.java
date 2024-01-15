package rpc;

import java.io.IOException;
import java.io.Serial;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import db.MySQLConnection;


public class Register extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject input = RpcHelper.readJSONObject(request);
        String userId = input.getString("user_id");
        String password = input.getString("password");
        String firstname = input.getString("first_name");
        String lastname = input.getString("last_name");

        MySQLConnection connection = new MySQLConnection();
        JSONObject obj = new JSONObject();
        if (connection.addUser(userId, password, firstname, lastname)) {
            obj.put("status", "OK");
        } else {
            obj.put("status", "User Already Exists");
        }
        connection.close();
        RpcHelper.writeJsonObject(response, obj);
    }
}