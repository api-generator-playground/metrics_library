package metrics_library.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import metrics_library.services.PromQLService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;

@RestController
@RequestMapping("/promql")
public class QueryController {

    @Autowired
    PromQLService promQLService;

    LocalDateTime nowTime = LocalDateTime.now();

    @GetMapping("/query")
    public ResponseEntity<?> getQueryResponse(
            @RequestParam String metricName,
            @RequestParam(defaultValue = "query_range") String queryType,
            @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now(T(java.time.ZoneOffset).UTC).minusHours(1)}") LocalDateTime start,
            @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now(T(java.time.ZoneOffset).UTC)}") LocalDateTime end,
            @RequestParam(defaultValue = "14") Integer step) {
        String response = promQLService.getResponseFromQuery(queryType, metricName,
                start.toInstant(ZoneOffset.UTC).getEpochSecond(),
                end.toInstant(ZoneOffset.UTC).getEpochSecond(), step);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
