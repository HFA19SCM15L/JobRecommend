package external;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SpaCyKeywordExtractor {
    private static final String PYTHON_SCRIPT_PATH = "/Users/liang/Downloads/simple/JobRecommend/keyword_extraction/keyword_extractor.py";

    public static void main(String[] args) {
        String[] textList = {"Elon Musk has shared a photo of the spacesuit designed by SpaceX."};
        List<List<String>> words = extractKeywords(textList);
        for (List<String> ws : words) {
            for (String w : ws) {
                System.out.println(w);
            }
            System.out.println();
        }
    }

    public static List<List<String>> extractKeywords(String[] text) {
        List<List<String>> results = new ArrayList<>();
        for (String t : text) {
            results.add(callPythonScript(t));
        }
        return results;
    }

    private static List<String> callPythonScript(String text) {
        List<String> keywords = new ArrayList<>();
        try {
            // text might need to be escaped if it contains special characters
            String escapedText = text.replace("\"", "\\\"");
            String command = "python3 " + PYTHON_SCRIPT_PATH + " \"" + escapedText + "\"";
            String[] cmd = {"bash", "-c", command};

            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                keywords.add(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return keywords;
    }
}