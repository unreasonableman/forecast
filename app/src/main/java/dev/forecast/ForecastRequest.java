package dev.forecast;

public class ForecastRequest {
    public String cmd;
    public String zip;
    public boolean verbose;

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
