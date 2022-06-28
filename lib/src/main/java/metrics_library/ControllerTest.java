package metrics_library;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.context.ApplicationContext;
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

    @Autowired
    private ApplicationContext applicationContext;

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

    @GetMapping("/generic")
    public ResponseEntity<?> getGenericMetrics() {

        MetricsEndpoint metricsEndpoint = (MetricsEndpoint) applicationContext.getBean("metricsEndpoint", MetricsEndpoint.class);

        List<MetricsEndpoint.MetricResponse> metrics = new ArrayList<>();

        metricsEndpoint.listNames().getNames().iterator().forEachRemaining(name -> metrics.add(metricsEndpoint.metric(name, null)));

        return new ResponseEntity<>(metrics, HttpStatus.OK);
    }

}
