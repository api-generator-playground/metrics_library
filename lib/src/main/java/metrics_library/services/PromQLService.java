package metrics_library.services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import metrics_library.entities.PrometheusApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

@Service
public class PromQLService {

    @Value("${promql.host}")
    private String prometheusHost;

    public String getResponseFromQuery(String queryType, String metricName, long start, long end, Integer step) {
        String hostQueryPath = String.format(Locale.ROOT, "%s/api/v1/%s?query=%s&start=%d&end=%d&step=%d",
                                    prometheusHost, queryType, metricName, start, end, step);
        return getResponseFromPrometheusAPI(hostQueryPath);
    }

    private String readResponseFromConnection(HttpURLConnection conn) {
        BufferedReader in;
        String response = "";
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            in.lines().forEach(content::append);
            in.close();
            response = content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getResponseFromPrometheusAPI(String hostQueryPath) {
        String response = "";
        URL url;
        try {
            url = new URL(hostQueryPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            response = readResponseFromConnection(conn);
            conn.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public List<String> getAllMetrics() {
        String hostQueryPath = String.format(Locale.ROOT, "%s/api/v1/label/__name__/values", prometheusHost);
        String response = getResponseFromPrometheusAPI(hostQueryPath);
        PrometheusApiResponses.LabelNameValuesResponse responseObject = new PrometheusApiResponses.LabelNameValuesResponse();
        try {
            responseObject = new ObjectMapper()
                    .readValue(response, PrometheusApiResponses.LabelNameValuesResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return responseObject.getData();
    }
}
