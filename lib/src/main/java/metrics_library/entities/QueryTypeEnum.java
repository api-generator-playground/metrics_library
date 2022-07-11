package metrics_library.entities;

public enum QueryTypeEnum {
    QUERY_RANGE("query_range"),
    QUERY("query"),
    SERIES("series"),
    LABELS("labels");

    private final String value;

    QueryTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
