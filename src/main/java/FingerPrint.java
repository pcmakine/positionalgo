import java.util.*;

/**
 * Created by Pete on 1.12.2015.
 */

public class FingerPrint{
    private Float z;
    private Float x;
    private Float y;
    private List<Observation> observations;
    private Map<String, Integer> averageRssi;
    private Double distance;
    public FingerPrint(Float z, Float x, Float y){
        this.z = z;
        this.x = x;
        this.y = y;
        this.observations = new ArrayList<>();
        this.averageRssi = new LinkedHashMap<>();
    }

    public void addObservation(Observation obs){
        observations.add(obs);
    }

    public List<Observation> getObservations(){
        return this.observations;
    }

    public void setAverageRssi(String mac, Integer avg){
        averageRssi.put(mac, avg);
    }

    public int getAverageRssi(String mac){
        return averageRssi.get(mac);
    }

    public Set<String> getDistinctMacs(){
        Set<String> ret = new HashSet<>();
        for(int i = 0; i < observations.size(); i++){
            ret.add(observations.get(i).getMac());
        }
        return ret;
    }

    public Map<String, Integer> getAveragesByMac(){
        if( averageRssi.isEmpty() ){
            calculateAveragesByMac();
        }
        return averageRssi;
    }

    private void calculateAveragesByMac(){
        Map<String, Integer> sumRssi = new LinkedHashMap<>();
        Map<String, Integer> macCount = new HashMap<>();

//first sum all the rssi into averageRssi map (by mac address)
//keep the count of observations by a specific mac address in macCount map
        for(int i = 0; i <  observations.size(); i++){
            Observation observation = observations.get(i);
            Integer count = macCount.get(observation.getMac());
            if( count == null){
                count = 0;
            }
            macCount.put(observation.getMac(), count+1);

            Integer sum = sumRssi.get(observation.getMac());
            if( sum == null){
                sum = 0;
            }
            sumRssi.put(observation.getMac(), sum+observation.getRssi());
        }

//Finally go through the macs and calculate the average for each mac
        Iterator<String> iter = sumRssi.keySet().iterator();
        while( iter.hasNext() ){
            String key = iter.next();
            Integer sum = sumRssi.get(key);
            Integer count = macCount.get(key);

            Integer average = sum/count;
            setAverageRssi(key, average);
        }
    }

    public String getDistanceDebugPrint(){
        return "z: " + z + ", x: " + x + ", y: " + y + ", Distance: " + distance;
    }

    public Float getX(){
        return x;
    }
    public Float getY(){
        return y;
    }
    public Float getZ(){
        return z;
    }
    public Double getDistance(){
        return this.distance;
    }
    public void setDistance(Double distance){
        this.distance = distance;
    }
}