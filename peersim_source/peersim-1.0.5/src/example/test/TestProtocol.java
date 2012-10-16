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

import java.util.*;
import peersim.cdsim.CDState;
import peersim.common.*;
import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.*;
import peersim.vector.SingleValueHolder;

/**
 * <p>
 * This class implements a (simple) load balancing strategy: each node selects
 * its most "distant" neighbor in terms of load difference and exchanges with it
 * an amount of load not exceeding the {@link #PAR_QUOTA} parameter.
 * </p>
 * <p>
 * The class subclasses {@link peersim.vector.SingleValueHolder} in order to be
 * type compatible with its observers and initializers object companions.
 * </p>
 * 
 */
class ConComparator implements Comparator
{
   public int compare(Object no1,Object no2){

   KnownNode n1,n2;
   n1=(KnownNode)no1;
   n2=(KnownNode)no2;
   if(n1.contribution==n2.contribution)
       return 0;
   else if (n1.contribution>n2.contribution)
       return 1;
   else
       return -1;
}
}

public class TestProtocol extends genericProtocol {

    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * Initial quota. Defaults to 1.
     * 
     * @config
     */
    protected static final String PAR_QUOTA = "quota";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** Quota amount. Obtained from config property {@link #PAR_QUOTA}. */
    private final double quota_value;

    protected double quota; // current cycle quota
     Random randomGenerator;


    // ------------------------------------------------------------------------
    // Initialization
    // ------------------------------------------------------------------------
    /**
     * Standard constructor that reads the configuration parameters. Invoked by
     * the simulation engine.
     * 
     * @param prefix
     *            the configuration prefix for this class.
     */
    public TestProtocol(String prefix) {
        super(prefix);
        // get quota value from the config file. Default 1.
        quota_value = (Configuration.getInt(prefix + "." + PAR_QUOTA, 1));
        quota = quota_value;
        randomGenerator = new Random(CommonState.r.getLastSeed());
    }

    // The clone() method is inherited.

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------

    /** Resets the current node quota value. */
    protected void resetQuota() {
        this.quota = quota_value;
    }

   public void sendReqDenile(int protocolID, SingleNode self, SingleNode peer_to_grp)
   {
       SingleNode peer;
       genericProtocol gp;
       if(!incomingReqQueue.isEmpty())
              {
                genericMessage g;
                for (Iterator i = incomingReqQueue.iterator(); i.hasNext(); ) {
                    g= (genericMessage) i.next();
                    reqMessage gm = (reqMessage)g;
                    peer=gm.src;
                    if( peer_to_grp.getID()!=peer.getID())
                    {
                        gp=(genericProtocol)peer.getProtocol(protocolID);
                        denyMessage dm = new denyMessage(genericMessage.MSG_DENY, gm.dest, self.slot,self.grp_size);
                        gp.sendMessage(dm, peer);
                //        System.out.println("Sending mes "+ dm.getId()+"To "+peer.getID()+" Type "+dm.toString());
                    }
                    }
              }
         //      incomingDenyQueue.clear();
   }

  public void sendReqDenile(int protocolID, SingleNode self)
   {
       SingleNode peer;
       genericProtocol gp;
       if(!incomingReqQueue.isEmpty())
              {
                genericMessage g;
                for (Iterator i = incomingReqQueue.iterator(); i.hasNext(); ) {
                    g= (genericMessage) i.next();
                    reqMessage gm = (reqMessage)g;
                    peer=gm.src;
                    gp=(genericProtocol)peer.getProtocol(protocolID);
                    denyMessage dm = new denyMessage(genericMessage.MSG_DENY, gm.dest, self.slot,self.grp_size);
                    gp.sendMessage(dm, peer);
              //      System.out.println("Sending mes "+ dm.getId()+"To "+peer.getID()+" Type "+dm.toString());
                    }
              }
    //    incomingDenyQueue.clear();
   }

   public void deleteMesseges(SingleNode self,int protocolID)
   {
        incomingExploreQueue.clear();
        incomingReplyQueue.clear();
        incomingDenyQueue.clear();
        incomingReqQueue.clear();
    }


   public void forwardTOLeader(SingleNode self,int protocolID)
   {
            SingleNode leader=GlobalData.grouplist.get(self.grp_id-1).leader;
            if(leader.isUp())
            {
            if (!incomingReplyQueue.isEmpty()) {
                 for (Iterator i = incomingReplyQueue.iterator(); i.hasNext(); ) {
                  genericMessage g= (genericMessage) i.next();
                  replyMessage gm = (replyMessage)g;
                  genericProtocol gpeer=(genericProtocol)leader.getProtocol(protocolID);
                  gpeer.sendMessage(gm, leader);
                 }
             }
            if (!incomingReqQueue.isEmpty()) {
                 for (Iterator i = incomingReqQueue.iterator(); i.hasNext(); ) {
                  genericMessage g= (genericMessage) i.next();
                  reqMessage gm = (reqMessage)g;
                  genericProtocol gpeer=(genericProtocol)leader.getProtocol(protocolID);
                  gpeer.sendMessage(gm, gm.dest);
                 }
             }
            if (!incomingDenyQueue.isEmpty()) {
                 for (Iterator i = incomingDenyQueue.iterator(); i.hasNext(); ) {
                  genericMessage g= (genericMessage) i.next();
                  denyMessage gm = (denyMessage)g;
                  genericProtocol gpeer=(genericProtocol)leader.getProtocol(protocolID);
                  gpeer.sendMessage(gm, leader);
                 }
             }
            if (!incomingExploreQueue.isEmpty()) {
                for (Iterator i = incomingExploreQueue.iterator(); i.hasNext(); ) {
                  genericMessage g= (genericMessage) i.next();
                  exploreMessage gm = (exploreMessage)g;
                  genericProtocol gpeer=(genericProtocol)leader.getProtocol(protocolID);
                  gpeer.sendMessage(gm, leader);
                 }
             }
        incomingReplyQueue.clear();
        incomingDenyQueue.clear();
        incomingReqQueue.clear();
       }
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

 public void exploration(SingleNode self, int linkableID,int protocolID)
 {
       int cycle=CDState.getCycle();
       SingleNode peer;
       double contribution=0;
       genericProtocol gpeer;
       gAlgoJP algo=new gAlgoJP();
       Linkable linkable;
       int p=0;
       ArrayList<SingleNode> members=new ArrayList<SingleNode>();
       ArrayList <PlexusNode>complement_nodes=new ArrayList<PlexusNode>();
       SingleNode s;
       if(self.grp_flag)
       {
            GroupNode group=GlobalData.grouplist.get(self.grp_id-1);
            for(int h=0;h<group.memberlist.size();h++)
            {
                members.add(group.memberlist.get(h));
            }
        }
        else
        {
            members.add(self);
        }
/*
        SingleNode r;
        KnownNode v;
        for (int i = (self.knownList.size()-1); i >= 0; i--) {
            v= self.knownList.get(i);
            r = (SingleNode)v.node;
            v.slot= new double[GlobalData.slot_count];
            System.arraycopy(r.slot, 0,v.slot, 0, r.slot.length);
            v.grp_size=r.grp_size;
            contribution= algo.contributionCalc(self,r);
            v.contribution=contribution;
        }
        Collections.sort(self.knownList, new ConComparator());
 *
 */
       self.knownList.clear();
        if(self.getID()==12 && cycle==2)
            System.out.println();
     //   complement_nodes=GlobalData.golayCode.SearchAPattern(GlobalData.golayCode.GetComplementPattern(self.pattern));
        long comp_pattern=GlobalData.golayCode.GetPartialComplementPattern(self.pattern);

     //   if(self.grp_flag==true && GlobalData.grouplist.get(self.grp_id - 1).leader.getID()==self.getID())
     //       System.out.println("Peer is: "+self.getID()+"Grouped: "+self.grp_flag+" "+Get24BitsString(self.pattern)+ " "+"Searching :"+ Get24BitsString(GlobalData.golayCode.GetPartialComplementPattern(self.pattern)));
        int searchdistance=GlobalData.maxHammingDistance;
   //     int searchdistance=GlobalData.maxHammingDistance*2;
        complement_nodes=GlobalData.golayCode.SearchAPattern(comp_pattern, searchdistance);
        if(Long.bitCount(self.pattern)<24 && complement_nodes.isEmpty())
        {
           searchdistance=GlobalData.maxHammingDistance*2;
           complement_nodes=GlobalData.golayCode.SearchAPattern(comp_pattern, searchdistance);
        }

        if(Long.bitCount(self.pattern)<24 && complement_nodes.isEmpty())
     //    if(self.grp_flag==false && complement_nodes.isEmpty())
        {
           searchdistance=GlobalData.maxHammingDistance*4;
           complement_nodes=GlobalData.golayCode.SearchAPattern(comp_pattern, searchdistance);
        }

  /*     if(Long.bitCount(self.pattern)<24 && complement_nodes.isEmpty())
        {
           searchdistance=GlobalData.maxHammingDistance*6;
           complement_nodes=GlobalData.golayCode.SearchAPattern(comp_pattern, searchdistance);
        }

   //     PlexusNode px=new PlexusNode(self,self.pattern);
   //     PlexusNode py = GlobalData.golayCode.GetComplementNode(px, complement_nodes);
 //       System.out.println("Peer is: "+self.getID()+" "+Long.toBinaryString(self.pattern)+ " Best Complement is:"+py.node.getID()+" "+Long.toBinaryString(py.node.pattern)+" Xor "+Long.bitCount(self.pattern^py.node.pattern));
   /*     if(self.getID()==72 || self.getID()==253 || self.getID()==866){
        System.out.println("Peer is: "+self.getID()+"Grouped: "+self.grp_flag+" "+Get24BitsString(self.pattern)+ " "+"Searching :"+ Get24BitsString(GlobalData.golayCode.GetPartialComplementPattern(self.pattern)));
        for (int i = 0; i < complement_nodes.size(); i++)
        {
           peer=complement_nodes.get(i).node;
           System.out.println("Complement Peer "+peer.getID()+" XOR :"+ Get24BitsString(peer.pattern)+" XOR Bitcount: "+ " Xor bit count :"+Long.bitCount(self.pattern^peer.pattern));
        }
     }
    *
    *
    */

    //     for(int i=0;i<GlobalData.knownList.size();i++)
     //   for (int i = 0; i < Network.size(); i++)
         for (int i = 0; i < complement_nodes.size(); i++)
         {
           //      peer = (SingleNode)GlobalData.knownList.get(i).node;
           //     peer = (SingleNode)Network.get(i);
                 peer = (SingleNode)complement_nodes.get(i).node;
                 boolean already_member=false;
                 for(int q=0;q<members.size();q++)
                 {
                     if(members.get(q).getID()==peer.getID())
                     {
                         already_member=true;  //if the peer already in the memberlist ignore that
                         break;
                     }
                 }
                 if(already_member==true)
                     continue;
                 KnownNode k= new KnownNode();
                 contribution= algo.contributionCalc(self,peer);
                 k.slot= new double[GlobalData.slot_count];
                 System.arraycopy(peer.slot, 0,k.slot, 0, peer.slot.length);
                 k.grp_size=peer.grp_size;
                 k.node=peer;
                 k.contribution=contribution;
                 int position=algo.binsearch(self.knownList,k);
                 if(position>0 && self.knownList.size()==GlobalData.known_count)
                 {
                     self.knownList.remove(0);
                     self.knownList.add(position-1, k);
                 }
                 else if(position > 0 && self.knownList.size() < GlobalData.known_count)
                  {
                     self.knownList.add(position, k);
                  }
                 else if(position == 0 && self.knownList.size() < GlobalData.known_count)
                  {
                     self.knownList.add(position, k);
                  }
           }
   //     if(self.knownList.size()>0)
   //             GlobalData.avg_contribution+=self.knownList.get(self.knownList.size()-1).contribution;

       for(int h=0;h<members.size();h++)
       {
             if(self.getID()!=members.get(h).getID())
             {
                 members.get(h).knownList.clear();
                 members.get(h).knownList.addAll(self.knownList);
             }
       }
 }

public void nextCycle(Node node, int protocolID) {
        int cycle=CDState.getCycle();
        int run_cycles = Configuration.getInt("run.cycles");
        if(cycle<run_cycles)
        {
        int linkableID = FastConfig.getLinkable(protocolID);
        Linkable linkable = (Linkable) node.getProtocol(linkableID);
        SingleNode peer_to_grp=null;
        double contribution;
        double max_contribution=0.0;
        SingleNode self= (SingleNode)node;
        SingleNode peer;
        gAlgoJP algo=new gAlgoJP();
        int cycles = Configuration.getInt("simulation.cycles");
        boolean isrecv=true;
        boolean isreplygot=false;
        boolean reqCheck=false;
        genericProtocol gself=(genericProtocol)self.getProtocol(protocolID);
        long noreply=-1;
   //     if(node.getID()==8)
        {
 //         System.out.println();
   //     System.out.println("**********Cycle is: "+cycle);
   //     System.out.println("Peer is: "+node.getID());
        }
        
        if(self.getID()==720 && cycle==12)
        {
                   System.out.println();
        }

        if(self.getID()==18 && cycle==34)
            System.out.println();

        if(self.explored==false &&(self.grp_flag==false||GlobalData.grouplist.get(self.grp_id-1).leader.getID()==self.getID()))
        {
            if(self.explore_cycle==0)
            {
          //      exploration(self, linkableID, protocolID );
                self.explore_cycle++;
            }
            else if(self.explore_cycle==1)
            {
          //      exploreReply(self,linkable, protocolID);
                self.explore_cycle++;
            }
            else if(self.explore_cycle==2 && self.grp_size<=GlobalData.grp_limit)
            {
               if(node.getID()==8)
               {
                    System.out.println();
               }
                exploration(self, linkableID, protocolID);
                self.explore_cycle++;
                self.explored=true;
            }
        }
        else if(self.grp_flag && GlobalData.grouplist.get(self.grp_id-1).leader.getID()!=self.getID())
        {
            forwardTOLeader(self,protocolID);   //if not a leader forward all message to leader
            incomingReplyQueue.clear();
            incomingDenyQueue.clear();
        }
        else if(!incominggroupFormQueue.isEmpty()||self.grouping_done==true)
        {
           sendReqDenile(protocolID,self);
           incominggroupFormQueue.clear();
           outReqQueue.clear();
           incomingReqQueue.clear();
           incomingReplyQueue.clear();
           incomingDenyQueue.clear();
        }
        else
        {
        try
        {
   //     exploreReply(self,linkable, protocolID);
        if (!incomingReplyQueue.isEmpty()) {
             for (Iterator i = incomingReplyQueue.iterator(); i.hasNext(); ) {
                  genericMessage g= (genericMessage) i.next();
                  replyMessage gm = (replyMessage)g;
             //     if((gm.src.grp_flag && GlobalData.grouplist.get(gm.src.grp_id-1).leader==null)||
             //         gm.src.isUp()==false||(self.grp_size+gm.src.grp_size)>GlobalData.grp_limit)
                  if((self.grp_size+gm.src.grp_size)>GlobalData.grp_limit)
                  {
                      continue;
                  }
          //        System.out.println("Reply is: "+ gm.getId()+"from "+gm.src.getID());
                  algo.mergeGroup(self,gm.src);
                  SingleNode leader=GlobalData.grouplist.get(self.grp_id-1).leader;
                  leader.grouping_done=true;
                  if(leader.getID()!=self.getID())
                  {
                      groupFormMessage fm = new groupFormMessage(genericMessage.MSG_GROUP_FORM, self);
                      genericProtocol gpeer=(genericProtocol)leader.getProtocol(protocolID);
                      gpeer.sendMessage(fm,leader);
                 //     System.out.println("Sending mes "+ fm.getId()+"To "+leader.getID()+" Type "+fm.toString());
                  }
                  isreplygot=true;
                  sendReqDenile(protocolID, self,gm.src);
                  deny_count=-1;
                  wait_count=0;
                  break;
            }
             outReqQueue.clear();
             incomingReqQueue.clear();
             incomingReplyQueue.clear();
           }
        else
         {
           if (!outReqQueue.isEmpty())
            {
            reqMessage sentrm=(reqMessage)  outReqQueue.get(0);
            denyMessage gotdm= gself.containDenyMessage(sentrm.dest);
            reqMessage gotrm= gself.containReqMessage(sentrm.dest);
            if(gotdm!=null||gotrm!=null)
            {
                reqCheck=true;
                outReqQueue.clear();
                wait_count=0;
            }
            else
            {
                wait_count--;
                if(wait_count==0)
                {
                    noreply=sentrm.dest.getID();
                    outReqQueue.clear();
                }
            }
            }
            }
     /*       if (!incomingReqQueue.isEmpty())
              {
              genericMessage g;
              for (Iterator i = incomingReqQueue.iterator(); i.hasNext(); ) {
                     g= (genericMessage) i.next();
                     reqMessage gm = (reqMessage)g;
                     peer=gm.src;
               //      if(peer.isUp()==false)
               //          continue;
                     System.out.println("REQ is: "+ gm.getId()+"from "+gm.src.getID());
                     int new_size=self.grp_size+peer.grp_size;
                     if(new_size>GlobalData.grp_limit)
                         continue;
                     contribution= algo.contributionCalc(self.slot,gm.src_slot,new_size);
                     if(contribution<gm.contribution)
                         continue;//nneed to send deny message
                     double toss=randomGenerator.nextDouble();
                     if(peer.grp_flag==false)
                     {
                         if(toss>=(peer.grp_size*0.1))
                         {
                            peer_to_grp=peer;
                            isrecv=false;
                         }
                     }
                    else if(toss >= (new_size * 0.1))
                     {
                        max_contribution=contribution;
                        peer_to_grp=peer;
                        isrecv=false;
                     }
                     if((contribution-max_contribution)>0.001)
                     {
                        max_contribution=contribution;
                        peer_to_grp=peer;
                        isrecv=false;
                     }
                 }
            }*/
        //    }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        if(isreplygot==false)
        {
   //     if(GlobalData.contribution_threshold==0)
   //         GlobalData.contribution_threshold=GlobalData.avg_contribution/(double)GlobalData.alive_count;
   //     max_contribution=(GlobalData.contribution_threshold/(self.grp_size))*0.8;
        
        int known_index=-1;
        if(outReqQueue.isEmpty()||reqCheck==true)
       {
   //     if(isrecv==true)
        boolean all_denied=false;
        boolean one_got=false;
        for (int i = (self.knownList.size()-1); i >= 0; i--) {
            peer = (SingleNode)self.knownList.get(i).node;
            if(peer.getID()==noreply)
                continue;
            denyMessage dm= gself.containDenyMessage(peer);
            if( dm!=null)
            {

                self.knownList.get(i).grp_size=dm.src_grp_size;
                self.knownList.get(i).deny_flag=true;
                self.knownList.get(i).sent_flag=false;
                if(Arrays.equals(self.knownList.get(i).slot,dm.src_slot)==false)
                {
                    System.arraycopy(dm.src_slot, 0,self.knownList.get(i).slot, 0,dm.src_slot.length);
                    int newsize=self.grp_size+dm.src_grp_size;
           //         self.knownList.get(i).contribution=algo.contributionCalc(self.slot, dm.src_slot, newsize);
                    self.knownList.get(i).contribution=algo.contributionCalc(self, dm.src);
                    self.knownList.get(i).deny_flag=false;
                }
                continue;
            }
            if( self.knownList.get(i).deny_flag==true)
            {
                self.knownList.get(i).deny_flag=true;
                if(one_got==false)
                {
                    all_denied=true;
                    if(deny_count<0)
                        deny_count=GlobalData.deny_counter;
                }
                continue;
            }
            if(self.knownList.get(i).sent_flag ==true)
                continue;
            one_got=true;
            all_denied=false;
            reqMessage rm= gself.containReqMessage(peer);
            if(rm!=null)
            {
                self.knownList.get(i).grp_size=rm.src_grp_size;
                if(Arrays.equals(self.knownList.get(i).slot,rm.src_slot)==false)
                {
                    System.arraycopy(rm.src_slot, 0,self.knownList.get(i).slot, 0,rm.src_slot.length);
                    int newsize=self.grp_size+rm.src_grp_size;
                    self.knownList.get(i).contribution=algo.contributionCalc(self, rm.src);
                 //   self.knownList.get(i).contribution=algo.contributionCalc(self.slot, rm.src_slot, newsize);
                }
            }
            double []s=self.knownList.get(i).slot;
            int new_grp_size=self.grp_size+self.knownList.get(i).grp_size;
            if(new_grp_size>GlobalData.grp_limit)
                continue;
      //      contribution= algo.contributionCalc(self.slot,s,new_grp_size);
             contribution= algo.contributionCalc(self,peer);
            if((contribution-max_contribution)>0.001)
            {
		max_contribution=contribution;
		peer_to_grp=peer;
                isrecv=true;
                known_index=i;
	    }
        }
        if( all_denied==true)
        {
            deny_count--;
            if(deny_count==0)
            {
                for (int i = 0; i < self.knownList.size(); ++i) {
                    self.knownList.get(i).deny_flag=false;
                    deny_count=-1;
                }
            }
        }

        if(peer_to_grp!=null)
        {
        genericProtocol gp;
        SingleNode leader=peer_to_grp;
        if(peer_to_grp.grp_flag)
        {
            leader = GlobalData.grouplist.get(peer_to_grp.grp_id - 1).leader;    
        }
          gp=(genericProtocol)leader.getProtocol(protocolID);
        //  System.out.println(" Best peer is: "+ peer_to_grp.getID()+" Contribution is: "+max_contribution);
          self.knownList.get(known_index).sent_flag=true;
          reqMessage gotrm= gself.containReqMessage(leader);
          reqMessage fm;
          if(gotrm!=null)
          {
              fm = new reqMessage(genericMessage.MSG_REQ,gotrm.dest, max_contribution,self.slot,self.grp_size);
          }
          else
          {
             fm = new reqMessage(genericMessage.MSG_REQ, self, max_contribution,self.slot,self.grp_size);
          }
          gp.sendMessage(fm, leader);
          outReqQueue.add(fm);
          wait_count=GlobalData.wait_cycle;
          if(self.getID()==720)
        //     System.out.println("Sending mes To "+leader.getID()+" "+fm.getId()+" Type "+fm.toString());
          sendReqDenile(protocolID, self,leader);
      }
     }
     }
          incomingDenyQueue.clear();
     }

        
   //     if(self.getID()==593|| self.getID()==720|| self.getID()==806)
/*
        if(self.grp_flag==true && GlobalData.grouplist.get(self.grp_id - 1).leader.getID()==self.getID())
   //   if(self.getID()==190 || self.getID()==407 || self.getID()==455|| self.getID()==874|| self.getID()==886|| self.getID()==892)
        {
            System.out.println("**********Cycle is: "+cycle);
            System.out.println("Peer is: "+self.getID()+"Groupflag "+self.grp_flag+"Groupsize "+self.grp_size+" pattern "+Get24BitsString(self.pattern));
            KnownNode p;
            for (int j = 0; j < self.knownList.size(); j++) {
                 p=(KnownNode)self.knownList.get(j);
                 System.out.println("Neighbor :"+p.node.getID()+" Dflag:"+ p.deny_flag+" Size:"+ p.grp_size+" Contribution :"+p.contribution+"  "+Get24BitsString(p.node.pattern)+" Xor bit count :"+Long.bitCount((self.pattern^p.node.pattern)));
        }
      }

     /*   if(self.getID()>=99)
        {
        for(int k=0;k<GlobalData.grouplist.size();k++){
          if(GlobalData.grouplist.get(k).isvalid){
           System.out.println("Group Id: "+GlobalData.grouplist.get(k).grp_id+" Group Size: "+GlobalData.grouplist.get(k).grp_size);
           for(int l=0;l<GlobalData.grouplist.get(k).grp_size;l++)
                {
               System.out.print(GlobalData.grouplist.get(k).memberlist.get(l).getID()+", ");
           }
           System.out.println();
          }
        }
        }*/
    }
    }
    public void processMessages(Node node) {
    /*    SingleNode peer_to_grp=null;
        double contribution,max_contribution=0;
        SingleNode self= (SingleNode)node;
        for (Iterator i = incomingQueue.iterator(); i.hasNext(); ) {
             reqMessage gm = (reqMessage)i.next();
             System.out.println("Incoming is: "+ gm.getId());
             SingleNode peer=gm.src;
             contribution= contributionCalc(self,peer);
             if(contribution>max_contribution)
             {
		max_contribution=contribution;
		peer_to_grp=peer;
	     }
            }
            incomingQueue.clear();*/
    }
     public boolean initiateQuery(genericQuery gq) {
        return true;
    }

    /**
     * Performs the actual load exchange selecting to make a PUSH or PULL
     * approach. It affects the involved nodes quota.
     * 
     * @param neighbor
     *            the selected node to talk with. It is assumed that it is an
     *            instance of the {@link example.loadbalance.BasicBalance}
     *            class.
     */
/*    protected void doTransfer(TestProtocol neighbor) {
        double a1 = this.value;
        double a2 = neighbor.value;
        double maxTrans = Math.abs((a1 - a2) / 2);
        double trans = Math.min(maxTrans, quota);
        trans = Math.min(trans, neighbor.quota);
        if (a1 <= a2) // PULL phase
        {
            a1 += trans;
            a2 -= trans;
        } else // PUSH phase
        {
            a1 -= trans;
            a2 += trans;
        }
        this.value = a1;
        this.quota -= trans;
        neighbor.value = a2;
        neighbor.quota -= trans;
    }*/
/*    public void printParams(PrintStream ps) {

    }*/

}
