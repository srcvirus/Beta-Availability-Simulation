/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package example.test;


import peersim.config.Configuration;
import peersim.core.*;
import peersim.cdsim.CDState;
import peersim.common.*;
import java.util.Iterator;
import java.util.*;
/**
 *
 *
 * @author Admin
 */
public class PeerDynamics_old implements Control {

     Random randomGenerator;
    public PeerDynamics_old(String name) {
        randomGenerator = new Random(CommonState.r.getLastSeed());
    }
    public SingleNode changeLeader(GroupNode g)
    {
        long leaderid=-1;
        boolean found=false;
        for(int i=0;i<g.memberlist.size();i++)
        {
            if(g.memberlist.get(i).isUp())
            {
                if((g.memberlist.get(i).getID()>leaderid))
                {
                   leaderid=g.memberlist.get(i).getID();
                   g.leader= g.memberlist.get(i);
                   found=true;
                }  
            }
        }
        if(found==true)
            return g.leader;
        else
            return null;
        
    }

    public void handleMessages()
    {
         SingleNode peer;
         SingleNode peer_to_grp;
         double contribution;
         double max_contribution;
         gAlgoJP algo=new gAlgoJP();
         int protocolID=1;
         Vector incomingReqQueue=new Vector();
         Vector outReqQueue=new Vector();
         for (int j = 0; j < Network.size(); j++) {
             boolean should_wait=false;
             peer_to_grp=null;
             max_contribution=0.0;
             SingleNode self = (SingleNode) Network.get(j);
             genericProtocol gp=(genericProtocol)self.getProtocol(protocolID);
             TestProtocol tp=(TestProtocol) gp;
             int cycle=CDState.getCycle();
             incomingReqQueue =gp.getReqMsg();
             if(self.getID()==11 && cycle==13)
              {
          //         System.out.println();
              }
             if(self.group_processing==true)
             {
                 tp.sendReqDenile(protocolID, self);
                 incomingReqQueue.clear();
                 continue;
             }
             outReqQueue=gp.getoutReqMsg();
             reqMessage sentrm=null;
             if (!outReqQueue.isEmpty())
             {
                sentrm=(reqMessage)  outReqQueue.get(0);
             }
             if (!incomingReqQueue.isEmpty())
             {
              genericMessage g;
              for (Iterator i = incomingReqQueue.iterator(); i.hasNext(); ) {
                     g= (genericMessage) i.next();
                     reqMessage gm = (reqMessage)g;
                     peer=gm.src;
                //     System.out.println("REQ is: "+ gm.getId()+"from "+gm.src.getID());
                     int new_size=peer.grp_size+self.grp_size;
                     contribution= algo.contributionCalc(self.slot,gm.src_slot,new_size);
                     if(sentrm!=null&&sentrm.contribution>contribution)
                     {
                          should_wait=true;
                          continue;
                      }
                     should_wait=false;
                     if(new_size>GlobalData.grp_limit)
                         continue;
                     if(gm.contribution>contribution)
                         continue;//nneed to send deny message
                     if((contribution-max_contribution)>0.001)
                     {
                        max_contribution=contribution;
                        peer_to_grp=peer;
                     }
                 }
            }
        if(peer_to_grp!=null)
        {
             genericProtocol greply;
             SingleNode leader=peer_to_grp;
             if(peer_to_grp.grp_flag)
             {
                leader = GlobalData.grouplist.get(peer_to_grp.grp_id - 1).leader;
             }
             if(leader.isUp()==true)
             {
                 leader.group_processing=true;
                 self.group_processing=true;
                 greply=(genericProtocol)leader.getProtocol(protocolID);
                 replyMessage fm = new replyMessage(genericMessage.MSG_REPLY, self);
                 greply.sendMessage(fm, peer_to_grp);
          //       System.out.println("Sending mes To"+ peer_to_grp.getID()+" from "+self.getID()+" "+fm.getId()+" Type "+fm.toString());
                 tp.sendReqDenile(protocolID, self,peer_to_grp);
                 incomingReqQueue.clear();
             }
             else
             {
                 tp.sendReqDenile(protocolID, self);
                 incomingReqQueue.clear();
             }
        }
        else if(should_wait==false)
        {
             tp.sendReqDenile(protocolID, self);
             incomingReqQueue.clear();
        }
    }
    }

    public boolean execute()
    {
        int cycle=CDState.getCycle();
        int run_cycles = Configuration.getInt("run.cycles");
        int max_cycles = Configuration.getInt("simulation.cycles");
        //DHT or oracle will handle incoming messages to it
        if((cycle%GlobalData.slot_cycle>2)&&(cycle<run_cycles))
             handleMessages();
        if((cycle%GlobalData.slot_cycle==0) || cycle==0)
        {
    //    GlobalData.msgList.clear();
        GlobalData.knownList.clear();
        int slot;
        int active=0;
        int down=0;
        int down_grps=0;
        int protocolID=1;// hard code
        for (int i = 0; i < Network.size(); i++) {
            SingleNode s = (SingleNode) Network.get(i);
            s.grouping_done=false;
            s.group_processing=false;
            if(cycle==13&&s.getID()==0 )
             {
          //         System.out.println();
             }
             slot=cycle/GlobalData.slot_cycle;//
             slot=slot%GlobalData.slot_count;
             double avail_probability=randomGenerator.nextDouble();
             if(s.slot_single[slot]<avail_probability)
             {
                 if(s.grp_flag==false)
                 {
                     down_grps++;
                 }
                 TestProtocol gself=(TestProtocol)s.getProtocol(protocolID);
                 gself.getoutReqMsg().clear();
                 s.setFailState(Fallible.DOWN);
                 down++;
             }
             else
             {
                s.setFailState(Fallible.OK);
                active++;
                s.explore_cycle=0;
                s.explored=false;
                s.alive_once=true;

                KnownNode k= new KnownNode();
                k.slot= new double[GlobalData.slot_count];
                k.node=s;
                System.arraycopy(s.slot, 0,k.slot, 0, s.slot.length);
                k.grp_size=s.grp_size;
                GlobalData.knownList.add(k);
             }
        }
   //     System.out.println();
        for(int k=0;k<GlobalData.grouplist.size();k++){
          if(GlobalData.grouplist.get(k).isvalid){
           int up_member=0;
           SingleNode leader=changeLeader(GlobalData.grouplist.get(k));
      //     System.out.println("Group Id: "+GlobalData.grouplist.get(k).grp_id+" Group Size: "+GlobalData.grouplist.get(k).grp_size);
           for(int l=0;l<GlobalData.grouplist.get(k).grp_size;l++)
           {
               SingleNode self= GlobalData.grouplist.get(k).memberlist.get(l);
               TestProtocol gself=(TestProtocol)self.getProtocol(protocolID);
               if(leader==null)
               {
             //        gself.deleteMesseges(self, protocolID);
               }
               else if(leader.getID() != GlobalData.grouplist.get(k).memberlist.get(l).getID())
               {
                     gself.forwardTOLeader(self, protocolID);
               }

               if(self.isUp())
               {
                   up_member++;
               }
     //        System.out.print(GlobalData.grouplist.get(k).memberlist.get(l).getID()+", ");
           }
           if(up_member==0)
           {
               down_grps++;
           }
     //      System.out.println();
          }
        }
        GlobalData.down_grps=down_grps;
        System.out.println("Totally Down groups: "+down_grps);
        System.out.println("Down peers: "+down);
        }
	return false;
    }
}
