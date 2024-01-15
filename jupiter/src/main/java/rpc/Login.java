package rpc;

import java.io.IOException;
import java.io.Serial;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import db.MySQLConnection;

/**
 * Abstract class HttpServlet handles HTTP requests and responses
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
    // update serialVersionUID number if the class definition changes
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        // TODO Auto-generated constructor stub
        // call the superclass constructor of HttpServlet
        super();

    }

    /**
     * Executed when an HTTP GET request is made to the servlet
     * Check for an existing HTTP session and fetch the user's details if logged in
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // retrieve the current HttpSession associated with this request
        HttpSession session = request.getSession(false);

        JSONObject obj = new JSONObject();
        if (session != null) {
            // an existing session was found
            MySQLConnection connection = new MySQLConnection();
            String userId = session.getAttribute("user_id").toString();
            obj.put("status", "OK").put("user_id", userId).put("name", connection.getFullname(userId));
            connection.close();
        } else {
            obj.put("status", "Invalid Session");
            response.setStatus(403);
        }
        RpcHelper.writeJsonObject(response, obj);
    }

    /**
     * Process user login attempts by verifying credentials and managing user sessions
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // parse JSON data from the request and returns it as a JSONObject to extract login credentials
        JSONObject input = RpcHelper.readJSONObject(request);
        String userId = input.getString("user_id");
        String password = input.getString("password");

        MySQLConnection connection = new MySQLConnection();
        JSONObject obj = new JSONObject();

        // check if the credentials are valid from the users table
        if (connection.verifyLogin(userId, password)) {
            // retrieve the current HttpSession or creates a new one if it does not exist
            // this session will be used to maintain state across multiple requests from the same user
            HttpSession session = request.getSession();
            session.setAttribute("user_id", userId);

            // the HttpSession will be kept alive for 600 seconds
            session.setMaxInactiveInterval(600);
            obj.put("status", "OK").put("user_id", userId).put("name", connection.getFullname(userId));
        } else {
            obj.put("status", "User Doesn't Exist");
            response.setStatus(401);
        }
        connection.close();
        RpcHelper.writeJsonObject(response, obj);
    }
}