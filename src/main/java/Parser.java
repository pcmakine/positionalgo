import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pete on 2.12.2015.
 */
//Parses text files into Observation and FingerPrint objects
public class Parser {

    public List<FingerPrint> parsePrints(String filePath, boolean includeCoords){
        String data = fileToString(filePath);
        return dataToFingerprints(data, includeCoords);
    }

    private String fileToString(String path){
        File file = new File(path);
        String contents = "";
        try {
            contents = FileUtils.readFileToString(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }
    private static List<FingerPrint> dataToFingerprints(String data, boolean includesCoords){
        List<FingerPrint> prints = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        try {
            lines = IOUtils.readLines(new StringReader(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String line: lines){
            prints.add(lineToFingerprint(line, includesCoords));
        }
        return prints;
    }

    public static FingerPrint lineToFingerprint(String row, boolean includesCoords){
        String[] arr = row.split(";");

        String x = null;
        String y = null;
        String z = null;
        int obserVationStartLocation = 0;
        if( includesCoords ){
            x = arr[1];
            y = arr[2];
            z = arr[0];
            obserVationStartLocation = 3;
        }
        FingerPrint print = new FingerPrint(stringToFloat(z), stringToFloat(x), stringToFloat(y));

        while( obserVationStartLocation < arr.length - 1 ){
            print.addObservation(new Observation( Integer.parseInt(arr[obserVationStartLocation +1]), arr[obserVationStartLocation]));
            obserVationStartLocation += 2;
        }
        return print;
    }

    private static Float stringToFloat(String str){
        if(str == null){
            return null;
        }
        return Float.parseFloat(str);
    }

    private static String decToMacAddress(String dec){
        String mac = Long.toHexString(Long.parseLong(dec));
        String newMac = "";
        for(int i = 0; i < mac.length(); i++){
            if( i > 0 && i < mac.length() - 1 && i % 2 == 0){
                newMac = newMac + ":" + mac.charAt(i);
            }else{
                newMac = newMac + mac.charAt(i);
            }
        }
        return newMac;
    }
}
