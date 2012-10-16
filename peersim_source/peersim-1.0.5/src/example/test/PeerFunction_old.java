/*
 * Copyright (c) 2003-2005 The BISON Project
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package example.test;

/*
 * Copyright (c) 2003-2005 The BISON Project
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */


import peersim.config.*;
import peersim.core.*;
import peersim.vector.*;
import peersim.util.IncrementalStats;
import peersim.cdsim.CDState;
import java.io.*;

/**
 * Print statistics for an average aggregation computation. Statistics printed
 * are defined by {@link IncrementalStats#toString}
 *
 * @author Alberto Montresor
 * @version $Revision: 1.17 $
 */
public class PeerFunction_old implements Control {

    // /////////////////////////////////////////////////////////////////////
    // Constants
    // /////////////////////////////////////////////////////////////////////

    /**
     * Config parameter that determines the accuracy for standard deviation
     * before stopping the simulation. If not defined, a negative value is used
     * which makes sure the observer does not stop the simulation
     *
     * @config
     */
    private static final String PAR_ACCURACY = "accuracy";

    /**
     * The protocol to operate on.
     *
     * @config
     */
    private static final String PAR_PROT = "protocol";

    // /////////////////////////////////////////////////////////////////////
    // Fields
    // /////////////////////////////////////////////////////////////////////

    /**
     * The name of this observer in the configuration. Initialized by the
     * constructor parameter.
     */
    private final String name;

    /**
     * Accuracy for standard deviation used to stop the simulation; obtained
     * from config property {@link #PAR_ACCURACY}.
     */
    private final double accuracy;

    /** Protocol identifier; obtained from config property {@link #PAR_PROT}. */
    private final int pid;
    int []per_node;
    int len;

    // /////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////

    /**
     * Creates a new observer reading configuration parameters.
     */
    public PeerFunction_old(String name) {
        this.name = name;
        accuracy = Configuration.getDouble(name + "." + PAR_ACCURACY, -1);
        pid = Configuration.getPid(name + "." + PAR_PROT);
    }

    // /////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////

    /**
     * Print statistics for an average aggregation computation. Statistics
     * printed are defined by {@link IncrementalStats#toString}. The current
     * timestamp is also printed as a first field.
     *
     * @return if the standard deviation is less than the given
     *         {@value #PAR_ACCURACY}.
     */

public void show(int grpcount)
   {

      int slot_count=GlobalData.slot_count;
      int []hist=new int[slot_count+1];
      for(int i=0;i<slot_count;i++)
      {
          hist[i]=0;
      }
      for(int i=0;i<grpcount;i++)
      {
          hist[per_node[i]]++;
      }
      for(int i=0;i<=slot_count;i++)
      {
           System.out.println("Number of peers having slot_availability<0.5 in "+i+" slots is "+hist[i]);
      }

 }
  public double Round(double Rval, int Rpl) {
      double p = (double)Math.pow(10,Rpl);
      Rval = Rval * p;
      double tmp = Math.round(Rval);
      return (double)tmp/p;
   }

    public boolean execute() {
        int cycle=CDState.getCycle();
        long time = peersim.core.CommonState.getTime();
        if(cycle%GlobalData.slot_cycle==0)
         {
        int slot_count=GlobalData.slot_count;
        double all_sum[]=new double[slot_count];
        int never_alive=0;
        Writer output = null;
        int grp_count=0;
        int all_grp_count=0;
        int ungrp_node_count=0;
        int max_grp_size=0;
        int min_grp_size=Network.size();
        int avg_grp_size=0;
        int sum_grp_size=0;
        double single_sum=0;
        double grp_sum=0;
        Writer data = null;
        int max_cycles = Configuration.getInt("simulation.cycles");
        len= Network.size();
        per_node=new int[len];

        for(int i=0;i<len;i++)
       {
          per_node[i]=0;
       }
        //search for ungrouped peers

        for (int i = 0; i < Network.size(); i++) {
             SingleNode s = (SingleNode) Network.get(i);
             if(s.alive_once==false)
              {
                         never_alive++;
             }
             if(!s.grp_flag){
                 grp_sum=0;
                 sum_grp_size++;
                 max_grp_size=1;
                 min_grp_size=1;

                 try {
                 data = new BufferedWriter(new FileWriter("G:\\simulation data store\\output\\fopt4_1ava_"+GlobalData.grp_limit+"_n"+len+"_c"+max_cycles+".txt",true));
            //    data.write("Node Id: ");
           //      data.write(Long.toString(s.getID()));
           //      data.write(" ");
                 for(int j=0;j<slot_count;j++)
                 {
                     double temp=s.slot[j];
                     all_sum[j]+=temp;
                     single_sum+=temp;
                     grp_sum+=temp;
                     if(cycle==max_cycles-1)
                     {
                         data.write(Double.toString(Round(temp,6)));
                         data.write(" ");
                         if(temp<0.5)
                            per_node[ungrp_node_count]++;
                     }
                 }
                 ungrp_node_count++;
                 if(cycle==max_cycles-1)
                 {
                     data.write(Double.toString(Round(grp_sum/12,6)));
                     data.write(" ");
                     data.write(Integer.toString(1));
                     data.write("\n");
                     data.close();             
                 }
                 }
                 catch(Exception e)
                 {
                 }
            }
        }

        System.out.println("Ungrped peer is: "+ ungrp_node_count);
        System.out.println("Never alive count: "+ never_alive);

        single_sum=single_sum/(slot_count*ungrp_node_count);
        grp_count=0;
        sum_grp_size=0;
        for(int k=0;k<GlobalData.grouplist.size();k++){
          if(GlobalData.grouplist.get(k).isvalid){
                grp_sum=0;
                //prints groups and members
                try {
                data = new BufferedWriter(new FileWriter("G:\\simulation data store\\output\\fopt4_1ava_"+GlobalData.grp_limit+"_n"+len+"_c"+max_cycles+".txt",true));

        /*        data.write("Group Id: ");
                data.write(Integer.toString(GlobalData.grouplist.get(k).grp_id));
                data.write(" Group Size: ");
                data.write(Integer.toString(GlobalData.grouplist.get(k).grp_size));
             //   data.write(" Leader: ");
             //   data.write(Long.toString(GlobalData.grouplist.get(k).leader.getID()));
                data.write(" Members: ");
                for(int l=0;l<GlobalData.grouplist.get(k).grp_size;l++)
                {
                    data.write(Long.toString(GlobalData.grouplist.get(k).memberlist.get(l).getID()));
                    data.write(", ");
                }  */
                for(int j=0;j<slot_count;j++)
                {
                    double temp=GlobalData.grouplist.get(k).groupslot[j];
                    if(cycle==max_cycles-1)
                    {
                        data.write(Double.toString(Round(temp,6)));
                        data.write(" ");
                        if(temp<0.5)
                            per_node[ungrp_node_count+grp_count]++;
                    }

                    all_sum[j]+=temp;
                    grp_sum+=temp;
                    
                }
                if(cycle==max_cycles-1)
                {
                    data.write(Double.toString(Round(grp_sum/12,6)));
                    data.write(" ");
                    data.write(Integer.toString(GlobalData.grouplist.get(k).grp_size));
                    data.write("\n");
                    data.close();
                }
            //    System.out.println();
                if(GlobalData.grouplist.get(k).grp_size>max_grp_size)
                    max_grp_size=GlobalData.grouplist.get(k).grp_size;
                if(GlobalData.grouplist.get(k).grp_size<min_grp_size)
                    min_grp_size=GlobalData.grouplist.get(k).grp_size;
                sum_grp_size+=GlobalData.grouplist.get(k).grp_size;
                grp_count++;
                }
                catch(Exception e)
                {
                }
             }
          }
        if(grp_count>0)
        {
            avg_grp_size=sum_grp_size/grp_count;
            int intz= (int)sum_grp_size/grp_count;
            float fraction=avg_grp_size-intz;
            if(fraction>=0.5)
                 avg_grp_size=intz+1;
            else
                 avg_grp_size=intz;
        }
     //   grp_sum=grp_sum/(slot_count*grp_count);
        all_grp_count=ungrp_node_count+grp_count;
        if(cycle==0){
        try {
        output = new BufferedWriter(new FileWriter("G:\\simulation data store\\output\\write4_"+GlobalData.grp_limit+"_n"+len+".txt",true));
        output.write("\n");
        output.write(Integer.toString(cycle));
        output.write(" ");
        output.write(Integer.toString(all_grp_count));
        output.write(" ");
        output.write(Integer.toString(grp_count));
        output.write(" ");
        output.write(Integer.toString(ungrp_node_count));
        output.write(" ");
        output.write(Integer.toString(0));
        output.write(" ");
        output.write(Integer.toString(max_grp_size));
        output.write(" ");
        output.write(Double.toString(Round(avg_grp_size,2)));
        output.write(" ");
        output.write(Integer.toString(min_grp_size));
        output.write(" ");
        output.write(Double.toString(Round(single_sum,4)));
        output.write(" ");
    //    output.write(Double.toString(Round(grp_sum,4)));
    //    output.write(" ");

        for(int j=0;j<slot_count;j++)
        {  
            String text = Double.toString(Round(all_sum[j]/Network.size(),4));
            output.write(text);
            output.write(" ");
        }
            output.write("\n");
            output.close();
         }
         catch(Exception e)
         {
         }
        }
        if(cycle!=0)
        {
        try {
        output = new BufferedWriter(new FileWriter("G:\\simulation data store\\output\\write4_"+GlobalData.grp_limit+"_n"+len+".txt",true));
        output.write("\n");
        output.write(Integer.toString(cycle));
        output.write(" ");
        output.write(Integer.toString(all_grp_count));
        output.write(" ");
        output.write(Integer.toString(grp_count));
        output.write(" ");
        output.write(Integer.toString(ungrp_node_count));
        output.write(" ");
        output.write(Double.toString(Round(((double)GlobalData.down_grps*100)/(double)all_grp_count,2)));
        output.write(" ");
        output.write(Integer.toString(max_grp_size));
        output.write(" ");
        output.write(Double.toString(Round(avg_grp_size,2)));
        output.write(" ");
        output.write(Integer.toString(min_grp_size));
        output.write(" ");
        output.write(Double.toString(Round(single_sum,4)));
        output.write(" ");
    //    output.write(Double.toString(Round(grp_sum,4)));
    //    output.write(" ");

        for(int j=0;j<slot_count;j++)
        {
            String text = Double.toString(Round(all_sum[j]/all_grp_count,4));
            output.write(text);
            output.write(" ");
        }
            output.write("\n");
            output.close();
         }
         catch(Exception e)
         {
         }

         System.out.println("Total grps: "+ grp_count);
        }
        if(cycle==max_cycles-1)
        {
          show(all_grp_count);
          System.out.println("Total Req messages "+ GlobalData.Req_counter);
          System.out.println("Total Reply messages "+ GlobalData.Reply_counter);
        }
        }
        return false;
    }
}
