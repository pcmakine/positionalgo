/**
 * Created by Pete on 1.12.2015.
 */

public class Observation {
    private int rssi;
    private String mac;

    public Observation() {
    }

    public Observation(Integer rssi, String mac) {
        this.rssi = rssi;
        this.mac = mac;
    }

    public int getRssi() {
        return rssi;
    }

    public String getMac() {
        return mac;
    }

    @Override
    public String toString() {
        String ret = mac + ": " + rssi;
        return ret;
    }

}
