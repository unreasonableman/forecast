package dev.forecast;

public class ForecastResponse {
    public String description;
    public boolean cached;
    public String zip;
    public String area;
    public int temp;
    public int temp_min;
    public int temp_max;

    public ForecastResponse() {
    }

    public String format(boolean verbose) {
        StringBuilder sb = new StringBuilder();

        if (cached) {
            sb.append("_cached_ ");
        }

        sb.append("weather data for ZIP ");
        sb.append(zip);
        sb.append(" (");
        sb.append(area);
        sb.append(")\n");

        if (verbose) {
            sb.append("summary: ");
            sb.append(description);
            sb.append("\n");

            sb.append("temp/min/max: ");
            sb.append(temp);
            sb.append("F/");
            sb.append(temp_min);
            sb.append("F/");
            sb.append(temp_max);
            sb.append("F\n");
        } else {
            sb.append("temp: ");
            sb.append(temp);
            sb.append("F");
        }

        return sb.toString();
    }
}