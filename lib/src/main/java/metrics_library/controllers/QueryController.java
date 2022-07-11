package metrics_library.controllers;

import io.swagger.v3.oas.annotations.Parameter;
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
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
