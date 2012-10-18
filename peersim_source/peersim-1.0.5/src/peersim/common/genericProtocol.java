/*
 * genericProtocol.java
 *
 * Created on December 3, 2006, 10:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package peersim.common;

import example.test.*;
import java.util.Iterator;
import java.util.Vector;
import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.Node;
//import util.stat_item;

/**
 *
 * @author Nill
 */
public abstract class genericProtocol implements CDProtocol
{

    protected Vector outReqQueue;
    protected Vector incomingQueue;
    protected Vector incomingReqQueue;
    protected Vector incomingReplyQueue;
    protected Vector incomingDenyQueue;
    protected Vector incomingExploreQueue;
    protected Vector incomingExploreReplyQueue;
    protected Vector incominggroupFormQueue;
    protected Vector seenMsgs;
    protected Vector advDB;
    protected int nQueryMsg = 0;
    protected int nAdvertMsg = 0;
    protected int nJoinMsg = 0;
    protected int wait_count;
    protected int deny_count = -1;

    /**
     * Creates a new instance of genericProtocol
     */
    public genericProtocol(String prefix)
    {
        Global.linkableID = Configuration.getInt(prefix + ".linkable");
        outReqQueue = new Vector(10);
        incomingQueue = new Vector(10);
        incomingReqQueue = new Vector(10);
        incomingReplyQueue = new Vector(10);
        incomingDenyQueue = new Vector(10);
        incomingExploreQueue = new Vector(10);
        incomingExploreReplyQueue = new Vector(10);
        incominggroupFormQueue = new Vector(10);
        seenMsgs = new Vector(100);
        advDB = new Vector(10);
        nQueryMsg = 0;
        nAdvertMsg = 0;
    }

    public void queryCycleReset()
    {
        outReqQueue.clear();
        incomingQueue.clear();
        incomingReqQueue.clear();
        incomingReplyQueue.clear();
        incomingDenyQueue.clear();
        incominggroupFormQueue.clear();
        nQueryMsg = 0;
        seenMsgs.clear();
    }

    public Object clone()
    {
        try
        {
            genericProtocol nw = (genericProtocol) super.clone();
            nw.outReqQueue = new Vector(10);
            nw.incomingQueue = new Vector(10);
            nw.incomingReqQueue = new Vector(10);
            nw.incomingReplyQueue = new Vector(10);
            nw.incomingDenyQueue = new Vector(10);
            nw.incominggroupFormQueue = new Vector(10);
            nw.seenMsgs = new Vector(100);
            nw.advDB = new Vector(10);
            nw.incomingExploreQueue = new Vector(10);
            nw.incomingExploreReplyQueue = new Vector(10);
            nQueryMsg = 0;
            nAdvertMsg = 0;
            return nw;
        }
        catch (Exception e)
        {
        }
        return null;
        // do any data allocation here
    }

    /*  public Vector findMatches(String strQuery) {
     Vector res = new Vector(10);
     for (Iterator i = advDB.iterator(); i.hasNext(); ) {
     String adv = (String)i.next();
     if (NameList.Names().isMatch(adv, strQuery))
     res.add(adv);
     }
     return res;
     }*/
    // is used only for search messages; advertise/join are done asyncronously
    public void sendMessage(genericMessage msg, SingleNode dest)
    {
        if (seenMsgs.contains(new Integer(msg.getId())) || msg.isExpired())
        {
            return;
        }
        incomingQueue.add(msg);
        seenMsgs.add(new Integer(msg.getId()));
//        System.out.println("@"+(chord.crdNode.get(dest).getId()+" "+(chord.crdMessage)msg).toString());
    }

    public void sendMessage(reqMessage msg, SingleNode dest)
    {
        if (seenMsgs.contains(new Integer(msg.getId())) || msg.isExpired())
        {
            return;
        }
        msg.setDest(dest);
        GlobalData.Req_counter++;
        incomingReqQueue.add(msg);
        seenMsgs.add(new Integer(msg.getId()));
    }

    public void sendMessage(replyMessage msg, SingleNode dest)
    {
        if (seenMsgs.contains(new Integer(msg.getId())) || msg.isExpired())
        {
            return;
        }
        //    System.out.println("From Reply");
        GlobalData.Reply_counter++;
        incomingReplyQueue.add(msg);
        seenMsgs.add(new Integer(msg.getId()));
    }

    public void sendMessage(denyMessage msg, SingleNode dest)
    {
        if (seenMsgs.contains(new Integer(msg.getId())) || msg.isExpired())
        {
            return;
        }
        //    System.out.println("From Reply");
        incomingDenyQueue.add(msg);
        seenMsgs.add(new Integer(msg.getId()));
    }

    public void sendMessage(exploreMessage msg, SingleNode dest)
    {
        if (seenMsgs.contains(new Integer(msg.getId())) || msg.isExpired())
        {
            return;
        }
        //    System.out.println("From Reply");
        incomingExploreQueue.add(msg);
        seenMsgs.add(new Integer(msg.getId()));
    }

    public void sendMessage(exploreReplyMessage msg, SingleNode dest)
    {
        if (seenMsgs.contains(new Integer(msg.getId())) || msg.isExpired())
        {
            return;
        }
        //    System.out.println("From Reply");
        incomingExploreReplyQueue.add(msg);
        seenMsgs.add(new Integer(msg.getId()));
    }

    public void sendMessage(groupFormMessage msg, SingleNode dest)
    {
        if (seenMsgs.contains(new Integer(msg.getId())) || msg.isExpired())
        {
            return;
        }
        //    System.out.println("From Reply");
        incominggroupFormQueue.add(msg);
        seenMsgs.add(new Integer(msg.getId()));
    }

    public denyMessage containDenyMessage(SingleNode s)
    {
        Vector queue = new Vector(10);
        queue = incomingDenyQueue;
        genericMessage g;
        SingleNode peer;
        SingleNode leader = s;
        if (s.getGrpFlag())
        {
            leader = GlobalData.grouplist.get(s.getGrpID() - 1).leader;
        }
        if (!incomingDenyQueue.isEmpty())
        {
            for (Iterator i = queue.iterator(); i.hasNext();)
            {
                g = (genericMessage) i.next();
                denyMessage gm = (denyMessage) g;
                peer = gm.getSource();
                if (s.getID() == peer.getID() || leader.getID() == peer.getID())
                {
                    return gm;
                }
            }
        }
        return null;
    }

    public reqMessage containReqMessage(SingleNode s)
    {
        Vector queue = new Vector(10);
        queue = incomingReqQueue;
        genericMessage g;
        SingleNode peer;
        SingleNode leader = s;
        if (s.getGrpFlag())
        {
            leader = GlobalData.grouplist.get(s.getGrpID() - 1).leader;
        }
        if (!incomingReqQueue.isEmpty())
        {
            for (Iterator i = queue.iterator(); i.hasNext();)
            {
                g = (genericMessage) i.next();
                reqMessage gm = (reqMessage) g;
                peer = gm.getSource();
                if (s.getID() == peer.getID() || leader.getID() == peer.getID())
                {
                    return gm;
                }
            }
        }
        return null;
    }

    public abstract boolean initiateQuery(genericQuery gq);

    public abstract void processMessages(Node node);
    //   public abstract void Advertise(Node nd, String advStr, stat_item si_visited_peer, stat_item si_replica);
    //   public abstract void Join(Node nd, stat_item si_join_overhead);

    public void nextCycle(Node node, int protocolID)
    {
        try
        {
            if (!incomingQueue.isEmpty())
            {
                for (Iterator i = incomingQueue.iterator(); i.hasNext();)
                {
                    genericMessage gm = (genericMessage) i.next();
                    processMessages(node);
                }
                incomingQueue.clear();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public Vector getAdvDB()
    {
        return advDB;
    }

    public Vector getReqMsg()
    {
        return incomingReqQueue;
    }

    public Vector getoutReqMsg()
    {
        return outReqQueue;
    }

    public boolean addAdvert(String advStr)
    {
        if (!advDB.contains(advStr))
        {
            advDB.add(advStr);
            return true;
        }
        return false;
    }
//    public boolean addAdvertisement(String advert) {
//        advDB.add(advert);                    
//        nAdvertMsg++;
//    }
//    public void routeAdvert(String advert) {
//        nAdvertMsg++;
//    }

    public int getNQueryMsg()
    {
        return nQueryMsg;
    }

    public int getNAdvertMsg()
    {
        return nAdvertMsg;
    }

    public int getNJoinMsg()
    {
        return nJoinMsg;
    }
//    public abstract void printParams(PrintStream ps);
}
