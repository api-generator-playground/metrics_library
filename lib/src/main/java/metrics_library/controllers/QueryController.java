package metrics_library.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import metrics_library.entities.PrometheusApiResponses;
import metrics_library.entities.QueryTypeEnum;
import metrics_library.services.PromQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/promql")
public class QueryController {

    @Autowired
    PromQLService promQLService;

    @GetMapping("/metrics")
    public ResponseEntity<?> getAllMetricsFromPrometheusAPI() {
        List<String> metricsResponse = promQLService.getAllMetrics();
        if (metricsResponse == null) {
            return new ResponseEntity<>("No response from remote host", HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity<>(metricsResponse, HttpStatus.OK);
    }

    @GetMapping("/query")
    public ResponseEntity<?> getQueryResponse(
            @RequestParam
            String metricName,
            @RequestParam(defaultValue = "QUERY_RANGE")
            QueryTypeEnum queryType,
            @Parameter(example = "2022-01-01T00:00:00Z")
            @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now(T(java.time.ZoneOffset).UTC).minusHours(1)}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @Parameter(example = "2022-01-01T01:00:00Z")
            @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now(T(java.time.ZoneOffset).UTC)}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end,
            @RequestParam(defaultValue = "14")
            Integer step
    ) {
        String response = promQLService.getResponseFromQuery(queryType.getValue(),
                metricName,
                start.toInstant(ZoneOffset.UTC).getEpochSecond(),
                end.toInstant(ZoneOffset.UTC).getEpochSecond(), step);
        if (response.equals("")) {
            return new ResponseEntity<>("No response from remote host", HttpStatus.SERVICE_UNAVAILABLE);
        }

        PrometheusApiResponses.MatrixResponse matrixResponse = new PrometheusApiResponses.MatrixResponse();
        try {
            matrixResponse = new ObjectMapper().readValue(response, PrometheusApiResponses.MatrixResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(matrixResponse, HttpStatus.OK);
    }
}
