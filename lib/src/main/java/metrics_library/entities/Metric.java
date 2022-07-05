package metrics_library.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.prometheus.client.Collector;
import io.prometheus.client.Collector.MetricFamilySamples.Sample;

@Entity
@Table(name = "metric")
public class Metric {
    
    @Id
    private String name;

    @Column
    private String metricType;

    @Column
    private String baseUnit;

    @Column
    private String description;

    @Column
    private List<Map<String, Object>> samples;

    public Metric(String name, String metricType, String description, List<Map<String, Object>> samples) {

        this.name = name;
        this.metricType = metricType;
        this.description = description;
        this.samples = samples;
    }

    public Metric(Collector.MetricFamilySamples metric) {

        this.name = metric.name;
        this.description = metric.help;
        this.metricType = metric.type.toString();
        this.baseUnit = metric.unit;
        this.samples = new ArrayList<>();
        
        for (Sample sample : metric.samples) {
            Map<String, Object> s = new HashMap<>();
            s.put("name", sample.name);
            for (int i = 0; i < sample.labelNames.size(); i++) { s.put(sample.labelNames.get(i), sample.labelValues.get(i)); }
            s.put("value", sample.value);
            s.put("timestampsMs", sample.timestampMs);
            s.put("exemplar", sample.exemplar);
            samples.add(s);
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Map<String, Object>> getSamples() {
        return samples;
    }

    public void setSamples(List<Map<String, Object>> samples) {
        this.samples = samples;
    }


}
