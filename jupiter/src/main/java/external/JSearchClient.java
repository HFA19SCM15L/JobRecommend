package external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import entity.Item;
import entity.Item.ItemBuilder;

public class JSearchClient {
	private static final String URL_TEMPLATE = "https://jsearch.p.rapidapi.com/search?query=%s&page=%d&num_pages=%d";
    private static final String HOST = "jsearch.p.rapidapi.com";
    private static final String API_KEY = "869eafec7amsh6a977f33333eb9cp13bcb1jsn4f7ea8bdc517";
	private static final String DEFAULT_KEYWORD = "software engineer";

	public List<Item> search(String keyword, String location, int page, int numPages) {
		if (keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}

		// Encoding keyword and location to ensure they are URL safe
		String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
		String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);

		// Construct the query parameter using both keyword and location
		String query = encodedKeyword + "%20in%20" + encodedLocation;

		// Construct the complete URL
		String url = String.format(URL_TEMPLATE, query, page, numPages);

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("X-RapidAPI-Key", API_KEY)
				.header("X-RapidAPI-Host", HOST)
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                JSONArray array = jsonResponse.getJSONArray("data"); // Adjust according to actual response structure
                return getItemList(array);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return new ArrayList<>();
	}

	private List<Item> getItemList(JSONArray array) {
		List<Item> itemList = new ArrayList<>();

		for (int i = 0; i < array.length(); ++i) {
			JSONObject object = array.getJSONObject(i);
			ItemBuilder builder = new ItemBuilder();

			builder.setJobTitle(getStringFieldOrEmpty(object, "job_title"));
			builder.setEmployerName(getStringFieldOrEmpty(object, "employer_name"));
			builder.setEmployerLogo(getStringFieldOrEmpty(object, "employer_logo"));
			builder.setJobApplyLink(getStringFieldOrEmpty(object, "job_apply_link"));
			builder.setJobCity(getStringFieldOrEmpty(object, "job_city"));
			builder.setJobState(getStringFieldOrEmpty(object, "job_state"));

			Item item = builder.build();
			itemList.add(item);
		}

		return itemList;
	}

	private String getStringFieldOrEmpty(JSONObject obj, String field) {
		return obj.isNull(field) ? "" : obj.getString(field);
	}

}
