package dev.forecast;

public class ForecastResponse {
    public String description;
    public boolean cached;
    public String zip;
    public String area;
    public int temp;
    public int temp_min;
    public int temp_max;

    public String format() {
        StringBuilder sb = new StringBuilder();

        if (cached) {
            sb.append("cached ");
        }

        sb.append("weather data for ZIP ");
        sb.append(zip);
        sb.append(" (");
        sb.append(area);
        sb.append(")\n");
        sb.append("summary: ");
        sb.append(description);
        sb.append("temp/min/max: ");
        sb.append(temp);
        sb.append("/");
        sb.append(temp_min);
        sb.append("/");
        sb.append(temp_max);
        sb.append("\n");

        return sb.toString();
    }
}