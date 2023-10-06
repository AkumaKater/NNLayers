package FFNetworkNoBiasNoBatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ConfigLoader {

    private int[] Layers = {784, 300, 100, 10};
    private int splitIndex = 60000; // 60.000
    private int TrainingCycles = 1;
    private double learnRate = 0.25;
    private int BatchSize = 50;

    private String[] mapValues;

    private static ConfigLoader config;
    HashMap<String, String[]> map;

    public static void main(String[] args) {
       ConfigLoader cl = new ConfigLoader();
       System.out.println(cl.getLearnRate());
       for(String s : cl.getMapValues()){
            System.out.println(s);
       }
    }

    public static ConfigLoader getConfig() {
        if(config==null){
            config = new ConfigLoader();
        }
        return config;
    }

    public ConfigLoader() {
        map = getPropertys();

        setSplitIndex(map.get("SplitIndex")[0]);
        setTrainingCycles(map.get("TrainingCycles")[0]);
        setLearnRate(map.get("LearnRate")[0]);
        setBatchSize(map.get("BatchSize")[0]);   
        setLayers(map.get("LayerSizes"));

        mapValues = map.keySet().toArray(new String[0]);
    }

    public static HashMap<String, String[]> getPropertys() {
        HashMap<String, String[]> map = new HashMap<String, String[]>();
        try (BufferedReader bReader = new BufferedReader(new FileReader("config.json"))) {
            String line = bReader.readLine();
            while (line != null) {
                String[] string = line.split("=");
                map.put(string[0], string[1].split(","));
                line = bReader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public int getSplitIndex() {
        return splitIndex;
    }

    public void setSplitIndex(String splitIndex) {
        this.splitIndex = Integer.parseInt(splitIndex);
    }

    public int getTrainingCycles() {
        return TrainingCycles;
    }

    public void setTrainingCycles(String trainingCycles) {
        TrainingCycles = Integer.parseInt(trainingCycles);
    }

    public double getLearnRate() {
        return learnRate;
    }

    public void setLearnRate(String learnRate) {
        this.learnRate = Double.parseDouble(learnRate);
    }

    public int getBatchSize() {
        return BatchSize;
    }

    public void setBatchSize(String batchSize) {
        BatchSize = Integer.parseInt(batchSize);
    }

    public void setLayers(String[] layers){
        int[] lay = new int[layers.length];
        for(int i=0; i< lay.length; i++){
            lay[i] = Integer.parseInt(layers[i]);
        }
        this.Layers = lay;
    }
    
    public int[] getLayers() {
        return Layers;
    }

    public String[] getMapValues() {
        return mapValues;
    }
    public String getValue(String key){
        return map.get(key)[0];
    }
}
