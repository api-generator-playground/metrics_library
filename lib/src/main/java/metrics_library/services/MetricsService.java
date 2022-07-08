package metrics_library.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import io.prometheus.client.CollectorRegistry;
import metrics_library.entities.Metric;

@Service
public class MetricsService {
    
    @Autowired
    private CollectorRegistry collectorRegistry;

    @Autowired
    private ApplicationContext applicationContext;

    private List<Metric> metrics;

    

    private void updateMetrics() {
        List<Metric> metrics = new ArrayList<>();

        this.collectorRegistry.metricFamilySamples()
                                .asIterator().forEachRemaining(mfs -> metrics.add(new Metric(mfs)));

        this.metrics = metrics;
    }


    public List<String> getMetrics() {
        this.updateMetrics();
        List<String> metricsNames = new ArrayList<>();
        this.metrics.stream().forEach(m -> metricsNames.add(m.getName() + " (Detailed)"));
        this.getMetricsEndpoint().listNames().getNames().forEach(m -> metricsNames.add(m + " (Generic)"));
        return metricsNames;
    }


    public List<Metric> getDetailedMetrics() {
        this.updateMetrics();
        return this.metrics;
    }


    public Optional<Metric> getDetailedMetricByName(String name) {
        this.updateMetrics();
        return this.metrics.stream().filter(m -> m.getName().equals(name)).findFirst();
    }


    private MetricsEndpoint getMetricsEndpoint() {
        return (MetricsEndpoint) applicationContext.getBean("metricsEndpoint", MetricsEndpoint.class);
    }


    public List<MetricsEndpoint.MetricResponse> getGenericMetrics() {
        List<MetricsEndpoint.MetricResponse> metrics = new ArrayList<>();
        
        MetricsEndpoint metricsEndpoint = this.getMetricsEndpoint();

        metricsEndpoint.listNames().getNames().iterator()
                        .forEachRemaining(name -> metrics.add(metricsEndpoint.metric(name, null)));

        return metrics;
    }


    public MetricsEndpoint.MetricResponse getGenericMetricByName(String name) {
        return this.getMetricsEndpoint().metric(name, null);
    }


}
