package metrics_library.controllers;

import metrics_library.services.MetricsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @Autowired
    private MetricsService metricsService;

    @GetMapping
    public ResponseEntity<?> getMetrics() {

        return new ResponseEntity<>(this.metricsService.getMetrics(), HttpStatus.OK);
    }

    @GetMapping("/detailed/{name}")
    public ResponseEntity<?> getDetailedMetricByName(@PathVariable String name) {

        return new ResponseEntity<>(this.metricsService.getDetailedMetricByName(name), HttpStatus.OK);
    }


    @GetMapping("/detailed")
    public ResponseEntity<?> getDetailedMetrics() {
        return new ResponseEntity<>(this.metricsService.getDetailedMetrics(), HttpStatus.OK);
    }

    @GetMapping("/generic")
    public ResponseEntity<?> getGenericMetrics() {
        return new ResponseEntity<>(this.metricsService.getGenericMetrics(), HttpStatus.OK);
    }


    @GetMapping(value="/generic/{name}")
    public ResponseEntity<?> getGenericMetricByName(@PathVariable String name) {
        return new ResponseEntity<>(this.metricsService.getGenericMetricByName(name), HttpStatus.OK);
    }
    

}
