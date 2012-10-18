/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package example.test;

import java.util.ArrayList;
import java.io.*;
import peersim.config.*;
import peersim.util.IncrementalStats;
import peersim.cdsim.CDState;
import peersim.common.*;




public class  GlobalData {
    public static final long node_count = Configuration.getInt("network.size");
    public static final int slot_count = Configuration.getInt("simulation.slots");
    public static final int known_count = 10;
    public static final int wait_cycle = 10;
    public static final int grp_limit=Configuration.getInt("simulation.groupsize");
    public static final int varied_uptime=Configuration.getInt("simulation.uptime");
    public static final int BETA = Configuration.getInt("simulation.beta");
    public static final int slot_cycle =10;
    public static final int deny_counter =3;
    public static long Req_counter =0;
    public static int Reply_counter =0;
    public static int gid=0;
    public static int down_grps;
    public static int converged_cycle=0;
    public static int converged95_cycle=0;
    public static int converged99_cycle=0;
    public static int done_cycle=0;
    public static int current_grpcount=0;
    public static int prev_grpcount=0;
    public static double avg_contribution;
    public static long alive_count=0;
    public static double contribution_threshold;
    public static int maxHammingDistance=4;
    public static int max_pattern=0;
    public static int min_pattern=24;
    public static int avg_pattern1=0;
    public static double alpha = Configuration.getDouble("simulation.input");
    public static final int percentage=Configuration.getInt("simulation.percentage");
    public static double down_per[]=new double[slot_count];
    public static double up_per[]=new double[slot_count];
    public static ArrayList<GroupNode> grouplist=new ArrayList<GroupNode>();
    public static ArrayList<KnownNode> knownList=new ArrayList<KnownNode>();
    public static Golay24Code golayCode=Golay24Code.getInstance();
    public static ArrayList<genericMessage> msgList=new ArrayList<genericMessage>();
    GlobalData()
    {

    }
}

