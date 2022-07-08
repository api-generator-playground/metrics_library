package metrics_library.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

@RestController
@RequestMapping("/promql")
public class QueryController {

    @Value("${promql.host}")
    private String prometheusHost;

    @GetMapping("/query")
    public ResponseEntity<?> getQueryResponse(@RequestParam String metricName, @RequestParam String queryType,
                                              @RequestParam Integer start, @RequestParam Integer end,
                                              @RequestParam Integer step) {
        String hostQueryPath = String.format("%s/api/v1/%s?query=%s&start=%d&end=%d&step=%d", prometheusHost, queryType, metricName,
                start, end, step);
        URL url = null;
        try {
            url = new URL(hostQueryPath);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            return new ResponseEntity<>(content, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
