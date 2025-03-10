package dev.forecast;

public class ForecastResponse {
    private String description;
    private boolean cached;
    private String zip;
    private String area;
    private int temp;
    private int temp_min;
    private int temp_max;

    public String getDescription() {
        return description;
    }

    public boolean getCached() {
        return cached;
    }

    public String getZip() {
        return zip;
    }

    public String getArea() {
        return area;
    }

    public int getTemp() {
        return temp;
    }

    public int getTempMin() {
        return temp_min;
    }

    public int getTempMax() {
        return temp_max;
    }

    public String format() {
        StringBuilder sb = new StringBuilder();

        sb.append("weather data for ZIP ");
        sb.append(zip);
        sb.append(" (");
        sb.append(area);
        sb.append(")\n");
        sb.append("summary: ");
        sb.append(description);
        sb.append("temp/nin/max: ");
        sb.append(temp);
        sb.append("/");
        sb.append(temp_min);
        sb.append("/");
        sb.append(temp_max);
        sb.append("\n");

        return sb.toString();
    }
}