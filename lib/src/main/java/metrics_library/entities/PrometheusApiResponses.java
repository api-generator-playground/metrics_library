package metrics_library.entities;

import java.util.List;
import java.util.Map;

public class PrometheusApiResponses {
    public static class LabelNameValuesResponse {
        private String status;
        private List<String> data;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }
    }

    public static class MatrixResponse {
        private String status;
        private MatrixData data;

        public String getStatus() {
            return status;
        }

        public MatrixData getData() {
            return data;
        }

        static class MatrixData {
            private String resultType;
            private List<MatrixResult> result;

            public String getResultType() {
                return resultType;
            }

            public List<MatrixResult> getResult() {
                return result;
            }
        }

        static class MatrixResult {
            private Map<String, String> metric;
            private List<List<Float>> values;

            public Map<String, String> getMetric() {
                return metric;
            }

            public List<List<Float>> getValues() {
                return values;
            }

            @Override
            public String toString() {
                return String.format(
                        "metric: %s\nvalues: %s",
                        metric.toString(),
                        values == null ? "" : values.toString()
                );
            }
        }
    }
}
