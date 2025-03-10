package dev.forecast;

/** Encapsulate a request to the weather service */
public class ForecastRequest {
    public String cmd;
    public String zip;
    public boolean verbose;

    /**
     * Create a new instance of this class.
     *
     * @param cmd the command to execute. Currently only supports 'forecast'
     * @param zip the US ZIP code for which to get weather data
     * @param verbose control result verbosity
     */
    public ForecastRequest(String cmd, String zip, boolean verbose) {
        this.cmd = cmd;
        this.zip = zip;
        this.verbose = verbose;
    }

    public ForecastRequest() {
    }

    public String toString() {
        return "[request cmd: " + cmd + ", zip: " + zip + ", verbose: " + verbose + "]";
    }
}
