import java.util.*;

/**
 * Created by Pete on 1.12.2015.
 */
public class Main {
    public final static int PRINT_N_NEIGHBOURS = 10;

    public static void main(String[] args){
        if( args.length < 2){
            System.out.println("Give the paths to the files as arguments. 2 arguments expected!\n" +
            "First argument is the file path for the fingerprint file and second is the path to the file containing the trainingdata\n" +
            "Please use absolute paths");
            System.exit(1);
        }
        Parser parser = new Parser();
        List<FingerPrint> kClosest = getKNeighbours(parser.parsePrints(args[0], false).get(0), parser.parsePrints(args[1], true), 1);

        FingerPrint closest = kClosest.get(0);
        System.out.println("");
        System.out.println("The estimated location is at coordinates z:" + closest.getZ() + " x: " + closest.getX() + " y: " + closest.getY());
    }

    private static List<FingerPrint> getKNeighbours(FingerPrint print, List<FingerPrint> trainingData, int k){
        for(FingerPrint trainingPrint: trainingData){
            double dist = avgEuclideanDistance(print, trainingPrint);
            trainingPrint.setDistance(dist);
        }
        trainingData.sort(new Comparator<FingerPrint>(){
            public int compare(FingerPrint o1, FingerPrint o2) {
                return o1.getDistance().compareTo(o2.getDistance());
            }
        });

        int count = 1;
        System.out.println(PRINT_N_NEIGHBOURS + " closest prints ordered by distance: ");
        for( FingerPrint trainingPrint: trainingData){
            if( count > PRINT_N_NEIGHBOURS || count >= trainingData.size()){
                break;
            }
            System.out.println(count + ": " + trainingPrint.getDistanceDebugPrint());
            count++;
        }
        return trainingData.subList(0, k);
    }

    private static double avgEuclideanDistance(FingerPrint print, FingerPrint trainingPrint){
        Map<String, Integer> rssiByMac = print.getAveragesByMac();
        Map<String, Integer> trainingRssiByMac = trainingPrint.getAveragesByMac();
        Iterator<String> iter = rssiByMac.keySet().iterator();
        int distanceSquaredSum = 0;
        int count = 0;          //how many observations
        while( iter.hasNext() ){
            String mac = iter.next();
            int trainingRssi = -100;        //min possible value
            int observerdRssi = rssiByMac.get(mac);
            if( trainingRssiByMac.get(mac) != null ){
                trainingRssi = trainingRssiByMac.get(mac);
            }
            int distance = observerdRssi - trainingRssi;
            int differenceSquared = distance * distance;
            distanceSquaredSum += differenceSquared;
            count ++;
        }
        double euclideanDiff = Math.sqrt(distanceSquaredSum);

        double averageEuclideanDiff = euclideanDiff/count;
        return averageEuclideanDiff;

    }



}
