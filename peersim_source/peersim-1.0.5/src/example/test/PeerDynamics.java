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
public class PeerDynamics implements Control
{

    Random randomGenerator;

    public PeerDynamics(String name)
    {
        randomGenerator = new Random(CommonState.r.getLastSeed());
    }

    public SingleNode changeLeader(GroupNode g)
    {
        long leaderid = -1;
        boolean found = false;
        for (int i = 0; i < g.memberlist.size(); i++)
        {
            if (g.memberlist.get(i).isUp())
            {
                if ((g.memberlist.get(i).getID() > leaderid))
                {
                    leaderid = g.memberlist.get(i).getID();
                    g.leader = g.memberlist.get(i);
                    found = true;
                }
            }
        }
        if (found == true)
        {
            return g.leader;
        }
        else
        {
            return null;
        }

    }

    public void handleMessages()
    {
        SingleNode peer;
        SingleNode peer_to_grp;
        double contribution;
        double max_contribution;
        gAlgoJP algo = new gAlgoJP();
        int protocolID = 1;
        Vector incomingReqQueue = new Vector();
        Vector outReqQueue = new Vector();
        int beta = GlobalData.BETA;

        for (int j = 0; j < Network.size(); j++)
        {
            boolean should_wait = false;
            peer_to_grp = null;

            PriorityQueue<PeerComparator> peersToGroup = new PriorityQueue<PeerComparator>();

            SingleNode self = (SingleNode) Network.get(j);
            max_contribution = 0.0;
            genericProtocol gp = (genericProtocol) self.getProtocol(protocolID);
            TestProtocol tp = (TestProtocol) gp;
            int cycle = CDState.getCycle();
            incomingReqQueue = gp.getReqMsg();

            if (self.group_processing == true)
            {
                tp.sendReqDenile(protocolID, self);
                incomingReqQueue.clear();
                continue;
            }

            reqMessage sentrm = null;
            if (!incomingReqQueue.isEmpty())
            {
                genericMessage g;
                for (Iterator i = incomingReqQueue.iterator(); i.hasNext();)
                {
                    g = (genericMessage) i.next();
                    reqMessage gm = (reqMessage) g;
                    peer = gm.src;
                    int new_size = peer.grp_size + self.grp_size;
                    contribution = algo.contributionCalc(self, gm.src);
                    for (int l = 0; l < GlobalData.BETA; l++)
                    {
                        outReqQueue = gp.getoutReqMsg()[l];
                        sentrm = null;
                        if (!outReqQueue.isEmpty())
                        {
                            sentrm = (reqMessage) outReqQueue.get(0);
                        }
                        if (sentrm != null && sentrm.contribution > contribution)
                        {
                            should_wait = true;
                            break;
                        }
                    }

                    if (should_wait)
                    {
                        continue;
                    }

                    should_wait = false;
                    if (new_size > GlobalData.grp_limit)
                    {
                        continue;
                    }
                    if (gm.contribution > contribution)
                    {
                        continue;//nneed to send deny message
                    }

                    peersToGroup.add(new PeerComparator(peer, contribution, -1));
                    if ((contribution - max_contribution) > 0.001)
                    {
                        max_contribution = contribution;
                        peer_to_grp = peer;
                    }
                }
            }

            if (!peersToGroup.isEmpty())
            {
                int peersProcessed = 0;
                Vector <SingleNode> processedPeers = new Vector <SingleNode>();
                while (!peersToGroup.isEmpty() && peersProcessed < beta)
                {
                    PeerComparator pc = peersToGroup.poll();
                    SingleNode peerToGroup = pc.getPeer();
                    genericProtocol greply;
                    SingleNode leader = peerToGroup;
                    
                    if (peerToGroup.grp_flag)
                    {
                        leader = GlobalData.grouplist.get(peerToGroup.grp_id - 1).leader;
                    }
                    if (leader.isUp() == true)
                    {
                        leader.group_processing = true;
                        self.group_processing = true;
                        greply = (genericProtocol) leader.getProtocol(protocolID);
                        replyMessage fm = new replyMessage(genericMessage.MSG_REPLY, self);
                        greply.sendMessage(fm, peerToGroup);
                        //      System.out.println("Sending mes To"+ peer_to_grp.getID()+" from "+self.getID()+" "+fm.getId()+" Type "+fm.toString());
                        processedPeers.add(peerToGroup);
                        peersProcessed++;
                        //tp.sendReqDenile(protocolID, self, peerToGroup);
                        //incomingReqQueue.clear();
                    }
                    else
                    {
                        tp.sendReqDenile(protocolID, self);
                        //incomingReqQueue.clear();
                    }
                }
                tp.sendReqDenile(protocolID, self, processedPeers);
                incomingReqQueue.clear();
            }
            else if (should_wait == false)
            {
                tp.sendReqDenile(protocolID, self);
                incomingReqQueue.clear();
            }
        }
    }

    public boolean execute()
    {
        int cycle = CDState.getCycle();
        int run_cycles = Configuration.getInt("run.cycles");
        int max_cycles = Configuration.getInt("simulation.cycles");
        //DHT or oracle will handle incoming messages to it

        if ((cycle % GlobalData.slot_cycle > 2) && (cycle < run_cycles))
        {
            handleMessages();
        }
        if ((cycle < run_cycles && cycle % GlobalData.slot_cycle == 0) || (cycle >= run_cycles && cycle < (max_cycles - 1)))
        {
            if (cycle < run_cycles)
            {
                GlobalData.knownList.clear();
            }
            int slot;
            int active = 0;
            int down = 0;
            int down_grps = 0;
            int protocolID = 1;// hard code
            for (int i = 0; i < Network.size(); i++)
            {
                SingleNode s = (SingleNode) Network.get(i);
                s.grouping_done = false;
                s.group_processing = false;
                if (cycle < run_cycles)
                {
                    slot = cycle / GlobalData.slot_cycle;//
                }
                else
                {
                    slot = cycle;
                }
                slot = slot % GlobalData.slot_count;
                double avail_probability = randomGenerator.nextDouble();
                if (s.slot_single[slot] < avail_probability)
                {
                    if (s.grp_flag == false)
                    {
                        down_grps++;
                    }
                    TestProtocol gself = (TestProtocol) s.getProtocol(protocolID);
                    for (int l = 0; l < GlobalData.BETA; l++)
                    {
                        gself.getoutReqMsg()[l].clear();
                    }
                    s.setFailState(Fallible.DOWN);
                    down++;
                }
                else
                {
                    s.setFailState(Fallible.OK);
                    active++;
                    s.explore_cycle = 0;
                    s.explored = false;
                    s.alive_once = true;

                    if (cycle < run_cycles)
                    {
                        KnownNode k = new KnownNode();
                        k.slot = new double[GlobalData.slot_count];
                        k.node = s;
                        System.arraycopy(s.slot, 0, k.slot, 0, s.slot.length);
                        k.grp_size = s.grp_size;
                        GlobalData.knownList.add(k);
                    }
                }
            }
            //     System.out.println();
            for (int k = 0; k < GlobalData.grouplist.size(); k++)
            {
                if (GlobalData.grouplist.get(k).isvalid)
                {
                    int up_member = 0;
                    SingleNode leader = changeLeader(GlobalData.grouplist.get(k));
                    //       System.out.println("Group Id: "+GlobalData.grouplist.get(k).grp_id+" Group Size: "+GlobalData.grouplist.get(k).grp_size);
                    for (int l = 0; l < GlobalData.grouplist.get(k).grp_size; l++)
                    {
                        SingleNode self = GlobalData.grouplist.get(k).memberlist.get(l);
                        TestProtocol gself = (TestProtocol) self.getProtocol(protocolID);
                        if (leader == null)
                        {
                            //        gself.deleteMesseges(self, protocolID);
                        }
                        else if (leader.getID() != GlobalData.grouplist.get(k).memberlist.get(l).getID())
                        {
                            gself.forwardTOLeader(self, protocolID);
                        }

                        if (self.isUp())
                        {
                            up_member++;
                        }
                        //      System.out.print(GlobalData.grouplist.get(k).memberlist.get(l).getID()+", ");
                    }
                    if (up_member == 0)
                    {
                        down_grps++;
                        if (cycle >= run_cycles)
                        {
                            GlobalData.grouplist.get(k).down_slots++;
                        }
                    }
                    //     System.out.println();
                }
            }
            GlobalData.down_grps = down_grps;
            if (cycle % GlobalData.slot_cycle == 0)
            {
                System.out.println("Totally Down groups: " + down_grps);
                System.out.println("Down peers: " + down);
                GlobalData.alive_count = GlobalData.node_count - down;
                GlobalData.contribution_threshold = 0;
                GlobalData.avg_contribution = 0;
            }
        }
        return false;
    }
}
