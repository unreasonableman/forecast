package dev.forecast;

/** Encapsulate a response from the weather service */
public class ForecastResponse {
    // TODO: generate automatically with reflection
    public static final String[] FIELD_NAMES = {
            "description", "name", "temp", "temp_min", "temp_max"
    };

    public String description;
    public boolean cached;
    public String zip;
    public String area;
    public int temp;
    public int temp_min;
    public int temp_max;

    /** Create a new instance */
    public ForecastResponse() {
    }

    /**
     * Create a formatted string representation of the instance suitable
     * for display to the user.
     * @param verbose Control output verbosity
     * @return the formatted output
     */
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