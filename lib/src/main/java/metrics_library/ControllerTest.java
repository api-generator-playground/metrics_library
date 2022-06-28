package metrics_library;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/metrics")
public class ControllerTest {

    @Autowired
    private CollectorRegistry collectorRegistry;

    @GetMapping
    public ResponseEntity<?> getMetrics() {
        return new ResponseEntity<>("test metrics", HttpStatus.OK);
    }

    @GetMapping("/detailed")
    public ResponseEntity<?> getDetailedMetrics() {
        List<Collector.MetricFamilySamples> samplesList = new ArrayList<>();

        this.collectorRegistry.metricFamilySamples().asIterator().forEachRemaining(samplesList::add);

        return new ResponseEntity<>(samplesList, HttpStatus.OK);
    }

}
