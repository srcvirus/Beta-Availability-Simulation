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
public class PeerFunction implements Control {

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
    public PeerFunction(String name) {
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

      String Get24BitsString(long aCodeWord)
    {

        String str=Long.toBinaryString(aCodeWord);
        if(str.length()<=24)
        {
            char []aSubsStr=new char[24-str.length()];
            for(int i=0;i<24-str.length();i++)
            {
                aSubsStr[i]='0';
            }
            String as=new String(aSubsStr);
            str=as.concat(str);
        }
        return str;
    }

    public boolean execute() {
        int cycle=CDState.getCycle();
        long time = peersim.core.CommonState.getTime();
        int run_cycles = Configuration.getInt("run.cycles");
        int max_cycles = Configuration.getInt("simulation.cycles");
        if((cycle<run_cycles && cycle%GlobalData.slot_cycle==0)|| cycle>=run_cycles)
        {
        int slot_count=GlobalData.slot_count;
        double all_sum[]=new double[slot_count];
        int never_alive=0;
        Writer output = null;
        Writer indexcount = null;
        Writer data = null;
        Writer up = null;
        Writer down = null;
        Writer req = null;
        Writer data_2 = null;
        Writer nine = null;
        int grp_count=0;
        int all_grp_count=0;
        int ungrp_node_count=0;
        int max_grp_size=0;
        int min_grp_size=Network.size();
        int avg_grp_size=0;
        int sum_grp_size=0;
        int above_percentage,above_percentage90,above_percentage95,above_percentage75,above_percentage85;
        double single_sum=0;
        double grp_sum=0;
        double grp_sum2=0;
        double per_grp_1avail=0;
        double avg_grp_1avail=0;
        double min_grp_1avail=1;
        double max_grp_1avail=0;
        double per_grp_2avail=0;
        double avg_grp_2avail=0;
        double min_grp_2avail=1;
        double max_grp_2avail=0;
        int len=Network.size();

        per_node=new int[len];
        above_percentage=above_percentage90=above_percentage95=above_percentage75=above_percentage85=0;
        for(int i=0;i<len;i++)
        {
          per_node[i]=0;
        }

        //search for ungrouped peers
        for (int i = 0; i < Network.size(); i++) {
             SingleNode s = (SingleNode) Network.get(i);
             if(!s.grp_flag){
                 grp_sum=0;
                 ungrp_node_count++;
            //     System.out.println("Ungroup "+ungrp_node_count+" is :"+s.getID());
                 sum_grp_size++;
                 max_grp_size=1;
                 min_grp_size=1;
                 if(s.alive_once==false)
                 {
                         never_alive++;
                 }
                 try {
                 data = new BufferedWriter(new FileWriter("output\\fopt5_1ava_"+GlobalData.grp_limit+"_n"+len+"_c"+max_cycles+"_a"+GlobalData.alpha+".txt",true));
                 data_2 = new BufferedWriter(new FileWriter("output\\fopt5_2ava_"+GlobalData.grp_limit+"_n"+len+"_c"+max_cycles+"_a"+GlobalData.alpha+".txt",true));
          /*       data.write("Node Id: ");
                 data.write(Long.toString(s.getID()));
                 data.write(" ");
                 data.write("Pattern: ");
                 data.write(Get24BitsString(s.pattern));
                 data.write(" ");

           * 
           */
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
                         data_2.write(Double.toString(Round(0,6)));
                         data_2.write(" ");
                     }
                 }
                 if(cycle==max_cycles-1)
                 {
                     data.write(Double.toString(Round(grp_sum/slot_count,6)));
                     data.write(" ");
                     data.write(Integer.toString(1));
                     data.write("\n");
                     data.close();

                     data_2.write(Double.toString(Round(0,6)));
                     data_2.write(" ");
                     data_2.write(Integer.toString(1));
                     data_2.write("\n");
                     data_2.close();
                 }
                 }
                 catch(Exception e)
                 {
                 }
            }
        }

        if(cycle<run_cycles)
        {
            System.out.println("Ungrped peer is: "+ ungrp_node_count);
            System.out.println("Never alive count: "+ never_alive);
            if(ungrp_node_count==0 && GlobalData.converged_cycle==0)
                GlobalData.converged_cycle=cycle;
            if(ungrp_node_count<=(len*0.05) && GlobalData.converged95_cycle==0)
                GlobalData.converged95_cycle=cycle;
            if(ungrp_node_count<=(len*0.01) && GlobalData.converged99_cycle==0)
                GlobalData.converged99_cycle=cycle;
        }

        single_sum=single_sum/(slot_count*ungrp_node_count);
        grp_count=0;
        sum_grp_size=0;
        for(int k=0;k<GlobalData.grouplist.size();k++){
          if(GlobalData.grouplist.get(k).isvalid){
                grp_sum=0;
                grp_sum2=0;
                //prints groups and members
                try {
                data = new BufferedWriter(new FileWriter("output\\fopt5_1ava_"+GlobalData.grp_limit+"_n"+len+"_c"+max_cycles+"_a"+GlobalData.alpha+".txt",true));
                data_2 = new BufferedWriter(new FileWriter("output\\fopt5_2ava_"+GlobalData.grp_limit+"_n"+len+"_c"+max_cycles+"_a"+GlobalData.alpha+".txt",true));
           /*
                data.write("Group Id: ");
                data.write(Integer.toString(GlobalData.grouplist.get(k).grp_id));
                data.write(" Group Size: ");
                data.write(Integer.toString(GlobalData.grouplist.get(k).memberlist.get(0).grp_size));
                data.write(" Pattern: ");
                data.write(Get24BitsString(GlobalData.grouplist.get(k).memberlist.get(0).pattern));
                data.write("  ");
         */
         
             //   data.write(" Leader: ");
             //   data.write(Long.toString(GlobalData.grouplist.get(k).leader.getID()));
         
                for(int j=0;j<slot_count;j++)
                {
                    double temp=GlobalData.grouplist.get(k).groupslot[j];
                    if(cycle==max_cycles-1)
                    {
                        //1-availability
                        data.write(Double.toString(Round(temp,6)));
                        data.write(" ");
                        if(temp<0.5)
                            per_node[ungrp_node_count]++;
                        //2availibility
                        double t[]=new double[GlobalData.grouplist.get(k).grp_size];
                        for(int l=0;l<GlobalData.grouplist.get(k).grp_size;l++){
                               t[l]=GlobalData.grouplist.get(k).memberlist.get(l).slot_single[j];
                        }
                        double sum=0.0;
                        for(int u=0;u<t.length;u++)
                        {
                                double prod=1.0;
                                for(int a=0;a<t.length;a++)
                                {
                                        if(u!=a)
                                        {
                                        prod*=(1-t[a]);
                                        }
                                }
                                prod*=t[u];
                                sum+=prod;
                        }
                        sum=temp-sum;
                        grp_sum2+=sum;
                        data_2.write(Double.toString(Round(sum,6)));
                        data_2.write(" ");
                    }
                    all_sum[j]+=temp;
                    grp_sum+=temp; 
                }
            /*    data.write(" Members: ");
                for(int l=0;l<GlobalData.grouplist.get(k).memberlist.get(0).grp_size;l++)
                {
                    data.write(Long.toString(GlobalData.grouplist.get(k).memberlist.get(l).getID()));
                    data.write(", ");
                }

             *
             */
                if(cycle==max_cycles-1)
                {
                    per_grp_1avail=grp_sum/slot_count;
                    avg_grp_1avail+=per_grp_1avail;
                    if(per_grp_1avail>max_grp_1avail)
                        max_grp_1avail=per_grp_1avail;
                    if(per_grp_1avail<min_grp_1avail)
                        min_grp_1avail=per_grp_1avail;
                    data.write(Double.toString(Round(per_grp_1avail,6)));
                    data.write(" ");
                    data.write(Integer.toString(GlobalData.grouplist.get(k).grp_size));
                    data.write("\n");
                    data.close();

                    per_grp_2avail=grp_sum2/slot_count;
                    avg_grp_2avail+=per_grp_2avail;
                    if(per_grp_2avail>max_grp_2avail)
                        max_grp_2avail=per_grp_2avail;
                    if(per_grp_2avail<min_grp_2avail)
                        min_grp_2avail=per_grp_2avail;
                    data_2.write(Double.toString(Round(per_grp_2avail,6)));
                    data_2.write(" ");
                    data_2.write(Integer.toString(GlobalData.grouplist.get(k).grp_size));
                    data_2.write("\n");
                    data_2.close();
                    int sim_slots=max_cycles-1-run_cycles;
                    int up_slots=sim_slots-GlobalData.grouplist.get(k).down_slots;
                    int slot_per=(GlobalData.percentage*sim_slots)/100;
                    if(up_slots>=slot_per)
                        above_percentage++;
                    slot_per=(90*sim_slots)/100;
                    if(up_slots>=slot_per)
                        above_percentage90++;
                    slot_per=(95*sim_slots)/100;
                    if(up_slots>=slot_per)
                        above_percentage95++;
                    slot_per=(75*sim_slots)/100;
                    if(up_slots>=slot_per)
                        above_percentage75++;
                    slot_per=(85*sim_slots)/100;
                    if(up_slots>=slot_per)
                        above_percentage85++;
                    nine = new BufferedWriter(new FileWriter("output\\nine5_"+GlobalData.grp_limit+"_n"+len+"_c"+max_cycles+"_a"+GlobalData.alpha+".txt",true));
                    nine.write(Double.toString(Round(((up_slots*100)/sim_slots),2)));
                    nine.write(" ");
                    nine.write(Double.toString(Round(((GlobalData.grouplist.get(k).down_slots*100)/sim_slots),2)));
                    nine.write(" ");
                    nine.write(Integer.toString(GlobalData.grouplist.get(k).grp_size));
                    nine.write("\n");
                    nine.close();
             //       System.out.print("Group Id: "+GlobalData.grouplist.get(k).grp_id);
             //       System.out.print("Group size: "+GlobalData.grouplist.get(k).grp_size);
             //       System.out.println("Slot available :"+up_slots);
                }
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
            avg_grp_1avail/=grp_count;
            avg_grp_2avail/=grp_count;
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
        int index_per=GlobalData.golayCode.getIndexPerNode();
        System.out.println("NodeCount: "+GlobalData.node_count+" IndexperNode: "+index_per);
        System.out.println("Max "+GlobalData.max_pattern+"Min "+GlobalData.min_pattern+"avg "+GlobalData.avg_pattern1/len);


        try {
        indexcount = new BufferedWriter(new FileWriter("output\\index5_"+GlobalData.grp_limit,true));
        indexcount.write("\n");
        indexcount.write(Integer.toString(len));
        indexcount.write(" ");
        indexcount.write(Integer.toString(index_per));
        indexcount.write(" ");
        indexcount.close();

        output = new BufferedWriter(new FileWriter("output\\write5_"+GlobalData.grp_limit+"_n"+len+"_a"+GlobalData.alpha+".txt",true));
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
        if(cycle!=0 && cycle%GlobalData.slot_cycle==0)
        {
        try {
        output = new BufferedWriter(new FileWriter("output\\write5_"+GlobalData.grp_limit+"_n"+len+"_a"+GlobalData.alpha+".txt",true));
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
         if(cycle>=10)
         {
             GlobalData.prev_grpcount=GlobalData.current_grpcount;
         }
         GlobalData.current_grpcount=grp_count;
         if(GlobalData.current_grpcount!=GlobalData.prev_grpcount && cycle<run_cycles)
         {
             GlobalData.done_cycle=cycle;
         }
        }

        if(cycle>=run_cycles && cycle<max_cycles-1)
        {
            double down_per=((double)GlobalData.down_grps)/(double)all_grp_count;
            double up_per=1-down_per;
            int slot=cycle%slot_count;
            GlobalData.down_per[slot]+=down_per;
            GlobalData.up_per[slot]+=up_per;
        }

        if(cycle==max_cycles-1)
        {
          try {
          up = new BufferedWriter(new FileWriter("output\\up_percentage_"+GlobalData.grp_limit+".txt",true));
          up.write(Double.toString(GlobalData.alpha));
          up.write(" ");
          up.write(Integer.toString(5));
          up.write(" ");
          for(int j=0;j<slot_count;j++)
          {
            GlobalData.up_per[j]=GlobalData.up_per[j]/(((max_cycles-1)-run_cycles)/slot_count);
            String text = Double.toString(Round((GlobalData.up_per[j]*100),2));
            up.write(text);
            up.write(" ");
          }
            up.write("\n");
            up.close();

          down = new BufferedWriter(new FileWriter("output\\down_percentage_"+GlobalData.grp_limit+".txt",true));
          down.write(Double.toString(GlobalData.alpha));
          down.write(" ");
          down.write(Integer.toString(5));
          down.write(" ");
          for(int j=0;j<slot_count;j++)
          {
            GlobalData.down_per[j]=GlobalData.down_per[j]/(((max_cycles-1)-run_cycles)/slot_count);
            String text = Double.toString(Round(GlobalData.down_per[j]*100,2));
            down.write(text);
            down.write(" ");
          }
            down.write("\n");
            down.close();

            req=new BufferedWriter(new FileWriter("output\\req"+GlobalData.grp_limit+".txt",true));
            double ratio=(double)GlobalData.Req_counter/(double)GlobalData.Reply_counter;
            req.write(Double.toString(GlobalData.alpha));
            req.write(" ");
            req.write(Integer.toString(5));
            req.write(" ");
            req.write(Double.toString(Round(avg_grp_size,2)));
            req.write(" ");
            req.write(Integer.toString(grp_count));
            req.write(" ");
            req.write(Integer.toString(above_percentage));
            req.write(" ");
            double above=(double)above_percentage75/(double)grp_count;
            req.write(Double.toString(Round(above*100,2)));
            req.write(" ");
            above=(double)above_percentage/(double)grp_count;
            req.write(Double.toString(Round(above*100,2)));
            req.write(" ");
            above=(double)above_percentage85/(double)grp_count;
            req.write(Double.toString(Round(above*100,2)));
            req.write(" ");
            above=(double)above_percentage90/(double)grp_count;
            req.write(Double.toString(Round(above*100,2)));
            req.write(" ");
            above=(double)above_percentage95/(double)grp_count;
            req.write(Double.toString(Round(above*100,2)));
            req.write(" ");
            req.write(Long.toString(GlobalData.Req_counter));
            req.write(" ");
            req.write(Long.toString(GlobalData.Reply_counter));
            req.write(" ");
            req.write(Double.toString(Round(ratio,2)));
            req.write(" ");
            req.write(Integer.toString(GlobalData.converged_cycle));
            req.write(" ");
            req.write(Double.toString(Round(avg_grp_1avail,6)));
            req.write(" ");
            req.write(Double.toString(Round(min_grp_1avail,6)));
            req.write(" ");
            req.write(Double.toString(Round(max_grp_1avail,6)));
            req.write(" ");
            req.write(Double.toString(Round(avg_grp_2avail,6)));
            req.write(" ");
            req.write(Double.toString(Round(min_grp_2avail,6)));
            req.write(" ");
            req.write(Double.toString(Round(max_grp_2avail,6)));
            req.write(" ");
            req.write(Integer.toString(GlobalData.converged95_cycle));
            req.write(" ");
            req.write(Integer.toString(GlobalData.converged99_cycle));
            req.write(" ");
            req.write(Integer.toString(GlobalData.done_cycle));
            req.write(" ");
            req.write("\n");
            req.close();
          }
          catch(Exception e)
         {
         }
     //     show(all_grp_count);
          System.out.println("Total Req messages "+ GlobalData.Req_counter);
          System.out.println("Total Reply messages "+ GlobalData.Reply_counter);
        }
        }
        return false;
    }
}
