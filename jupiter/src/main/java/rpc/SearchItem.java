package rpc;

import java.io.IOException;
import java.io.Serial;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import external.CareerjetClient;
import org.json.JSONArray;
import org.json.JSONObject;

import db.MySQLConnection;
import entity.Item;

public class SearchItem extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    public SearchItem() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            return;
        }

        String userId = request.getParameter("user_id");
        String keyword = request.getParameter("keyword");
        String location = request.getParameter("location");
        int page = Integer.parseInt(request.getParameter("page"));
        String userIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String requestUrl = request.getRequestURL().toString();

        CareerjetClient client = new CareerjetClient();
        List<Item> items = client.searchCareerjetJobs(keyword, location, page, userIp, userAgent, requestUrl);

        MySQLConnection connection = new MySQLConnection();
        Set<String> favoritedItemIds = connection.getFavoriteItemIds(userId);
        connection.close();

        JSONArray array = new JSONArray();
        for (Item item : items) {
            JSONObject obj = item.toJSONObject();
            obj.put("favorite", favoritedItemIds.contains(item.getItemId()));
            array.put(obj);
        }
        RpcHelper.writeJsonArray(response, array);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}