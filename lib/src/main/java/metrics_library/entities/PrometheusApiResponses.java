package metrics_library.entities;

import java.util.List;

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
}
