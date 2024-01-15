package rpc;

import java.io.IOException;
import java.io.Serial;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;

import entity.Item;
import recommendation.Recommendation;

public class RecommendItem extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    public RecommendItem() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            return;
        }
        String userId = request.getParameter("user_id");
        String location = request.getParameter("region");
        String userIp = request.getParameter("ip");
        String requestUrl = request.getParameter("hostname");
        String userAgent = request.getParameter("userAgent");

        Recommendation recommendation = new Recommendation();
        List<Item> items = recommendation.recommendItems(userId, location, userIp, userAgent, requestUrl);
        JSONArray array = new JSONArray();
        for (Item item : items) {
            array.put(item.toJSONObject());
        }
        RpcHelper.writeJsonArray(response, array);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}