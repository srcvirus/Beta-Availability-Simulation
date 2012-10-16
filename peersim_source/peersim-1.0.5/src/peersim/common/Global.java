/*
 * Global.java
 *
 * Created on November 14, 2006, 8:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package peersim.common;
import java.io.PrintStream;
import java.io.FileOutputStream;
import peersim.config.Configuration;


/**
 *
 * @author Nill
 */
public class Global {
    //public static Statistics stats;
    public static int queryCycleLength = 10;
    public static int nQuery;
    public static int maxAdvIndx;
    public static int protocolID;
    public static int linkableID;
    public static String expName;
    public static int netSize;
 //   private static QueryDB qdb;
    private static boolean simulationDone;
    /** Creates a new instance of Global */
    public Global() {
        
    }
    static {
    }
    
    public static String getFileName() {
        return "data/"+expName+"@"+netSize+".txt";
    }
    
    public static PrintStream getPS() {
        PrintStream ps = null;
        try {        
            FileOutputStream os = new FileOutputStream(getFileName(), false);
            ps = new PrintStream(os);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return ps;
    }        
    public static void Init() {
        protocolID = Configuration.getInt("simulation.protocolID");
        linkableID = Configuration.getInt("simulation.linkableID");
        nQuery = Configuration.getInt("simulation.nquery");
        maxAdvIndx = Configuration.getInt("simulation.maxAdvIndx");
        netSize = Configuration.getInt("overlay.size");
        expName = Configuration.getString("simulation.name");
        simulationDone = false;
        Debug();
    }
    public static void Debug() {
        System.out.println("Netsize:"+netSize+"  exp:"+expName+"  nAdv:"+maxAdvIndx+"  nQry:"+nQuery);
    }


    public static boolean isSimulationDone() {
        return simulationDone;
    }

    public static void setSimulationDone() {
        simulationDone = true;
    }
    
}

