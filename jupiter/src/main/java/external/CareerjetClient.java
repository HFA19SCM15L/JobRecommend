package external;

import com.careerjet.webservice.api.Client;
import entity.Item;
import entity.Item.ItemBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class CareerjetClient {
    private static final String DEFAULT_KEYWORD = "software engineer";
    private static final String DEFAULT_LOCATION = "USA";
    private static final String AFFILIATE_ID = "242674bc1ba5f670b867703f1943e73d";

    public List<Item> searchCareerjetJobs(String keyword, String location, int page, String userIp, String userAgent, String requestUrl) {
        if (keyword == null) {
            keyword = DEFAULT_KEYWORD;
        }
        if (location == null) {
            location = DEFAULT_LOCATION;
        }

        Client c = new Client("en_GB");

        Map<String, String> args = new HashMap<>();
        args.put("keywords", keyword);
        args.put("location", location);
        args.put("affid", AFFILIATE_ID);
        args.put("user_ip", userIp);
        args.put("user_agent", userAgent);
        args.put("url", requestUrl);
        args.put("page", String.valueOf(page));

        org.json.simple.JSONObject simpleResults = (org.json.simple.JSONObject) c.search(args);
        JSONObject results = new JSONObject(simpleResults.toJSONString());

        return processResults(results);
    }

    /*
    {
      "site": "www.xxx.com",
      "salary_type": "Y",
      "title": "Java Developer",
      "locations": "España",
      "description": "Java, PHP...",
      "company": "AAA",
      "salary_currency_code": "EUR",
      "salary": "€24000 - 36000 per year",
      "salary_min": "24000",
      "salary_max": "36000",
      "date": "Fri, 15 May 2015 08:41:59 GMT",
      "url": "www.ooo.com"
     },
     */
    private List<Item> processResults(JSONObject results) {
        List<Item> itemList = new ArrayList<>();

        if (results.get("type").equals("JOBS")) {
            JSONArray jobs = (JSONArray) results.get("jobs");
            List<String> descriptionList = new ArrayList<>();

            for (int i = 0; i < jobs.length(); i++) {
                String description = getStringFieldOrEmpty(jobs.getJSONObject(i), "description");
                if (description.equals("") || description.equals("\n")) {
                    descriptionList.add(getStringFieldOrEmpty(jobs.getJSONObject(i), "title"));
                } else {
                    descriptionList.add(description);
                }
            }

            List<List<String>> keywords = SpaCyKeywordExtractor
                    .extractKeywords(descriptionList.toArray(new String[descriptionList.size()]));

            for (int i = 0; i < jobs.length(); i++) {
                JSONObject job = jobs.getJSONObject(i);
                ItemBuilder builder = new ItemBuilder();

                builder.setJobTitle(getStringFieldOrEmpty(job, "title"));
                builder.setEmployerName(getStringFieldOrEmpty(job, "company"));
                builder.setEmployerLogo(getStringFieldOrEmpty(job, "employer_logo"));
                builder.setJobApplyLink(getStringFieldOrEmpty(job, "url"));
                builder.setJobLocation(getStringFieldOrEmpty(job, "locations"));
                builder.setJobPostDate(getStringFieldOrEmpty(job, "date"));

                builder.setKeywords(new HashSet<String>(keywords.get(i)));

                Item item = builder.build();
                itemList.add(item);
            }
        }
        return itemList;
    }

    private String getStringFieldOrEmpty(JSONObject obj, String field) {
        return obj.isNull(field) ? "" : obj.getString(field);
    }

    public static void main(String[] args) {
        CareerjetClient client = new CareerjetClient();
        int page = 2;
        String userIp = "123.123.123.123";
        String userAgent = "\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36\"";
        String requestUrl = "http://www.example.com";
        for (Item item : client.searchCareerjetJobs(DEFAULT_KEYWORD, DEFAULT_LOCATION, page, userIp, userAgent, requestUrl)) {
            System.out.println(item.toJSONObject().toString(2)); // Print each item as a formatted JSON
        }
    }
}
