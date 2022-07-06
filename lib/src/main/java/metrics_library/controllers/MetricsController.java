package metrics_library.controllers;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.Collector.MetricFamilySamples.Sample;
import metrics_library.entities.Metric;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @Autowired
    private CollectorRegistry collectorRegistry;

    @Autowired
    private ApplicationContext applicationContext;

    private List<Metric> metrics;

    @GetMapping
    public ResponseEntity<?> getMetrics() {

        this.updateMetrics();

        List<String> metricsList = new ArrayList<>();

        this.metrics.stream().forEach(m -> { metricsList.add("Detailed: " + m.getName()); metricsList.add("Generic: " + m.getName().replace("_", ".")); });

        return new ResponseEntity<>(metricsList, HttpStatus.OK);
    }

    @GetMapping("/detailed/{name}")
    public ResponseEntity<?> getDetailedMetricByName(@PathVariable String name) {
        this.updateMetrics();

        return new ResponseEntity<>(this.metrics.stream().filter(metric -> metric.getName().equals(name)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/diferenciado")
    public ResponseEntity<?> getDetailedMetricDif() {
        List<Collector.MetricFamilySamples> metrics = new ArrayList<>();

        this.collectorRegistry.metricFamilySamples().asIterator().forEachRemaining(mfs -> metrics.add(mfs));

        List<Map<String, Object>> mapa = new ArrayList<>();

        for (MetricFamilySamples metric : metrics) {
            for (Sample sample : metric.samples) {
                Map<String, Object> s = new HashMap<>();
                s.put("name", sample.name);
                for (int i = 0; i < sample.labelNames.size(); i++) { s.put("name", s.get("name").toString() + sample.labelValues.get(i).toString()); }
                s.put("value", sample.value);
                mapa.add(s);
            }
        }

        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    @GetMapping("/detailed")
    public ResponseEntity<?> getDetailedMetrics() {
        this.updateMetrics();
        return new ResponseEntity<>(this.metrics, HttpStatus.OK);
    }

    @GetMapping("/generic")
    public ResponseEntity<?> getGenericMetrics() {

        MetricsEndpoint metricsEndpoint = (MetricsEndpoint) applicationContext.getBean("metricsEndpoint", MetricsEndpoint.class);

        List<MetricsEndpoint.MetricResponse> metrics = new ArrayList<>();

        metricsEndpoint.listNames().getNames().iterator().forEachRemaining(name -> metrics.add(metricsEndpoint.metric(name, null)));

        return new ResponseEntity<>(metrics, HttpStatus.OK);
    }


    @GetMapping(value="/generic/{name}")
    public ResponseEntity<?> getGenericMetricByName(@PathVariable String name) {
        MetricsEndpoint metricsEndpoint = (MetricsEndpoint) applicationContext.getBean("metricsEndpoint", MetricsEndpoint.class);

        List<MetricsEndpoint.MetricResponse> metrics = new ArrayList<>();

        metricsEndpoint.listNames().getNames().iterator().forEachRemaining(metricName -> metrics.add(metricsEndpoint.metric(metricName, null)));

        return new ResponseEntity<>(metrics.stream().filter(mr -> mr.getName().equals(name)).collect(Collectors.toList()), HttpStatus.OK);
    }


    private void updateMetrics() {
        List<Metric> metrics = new ArrayList<>();

        this.collectorRegistry.metricFamilySamples().asIterator().forEachRemaining(mfs -> metrics.add(new Metric(mfs)));

        this.metrics = metrics;

    }
    

}
