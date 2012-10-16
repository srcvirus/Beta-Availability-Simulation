/*
 * genericMessage.java
 *
 * Created on December 3, 2006, 11:03 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package peersim.common;

import example.test.*;
import peersim.core.Node;
import java.util.Vector;
import java.util.Iterator;
/**
 *
 * @author Nill
 */

public abstract class genericMessage {

    public static final int MSG_REQ = 100;
    public static final int MSG_REPLY = 200;
    public static final int MSG_DENY = 300;
    public static final int MSG_EXPLORE = 400;
    public static final int MSG_EXPLORE_REPLY = 500;
    public static final int MSG_GROUP_FORM = 600;
    protected int msg_type;
    public static int id_gen = 100;
    protected int id;
    protected genericQuery baseQuery;
    protected Vector visitedPeers;
    /** Creates a new instance of genericMessage */
    public genericMessage(int type) {
    //    baseQuery = query;
        visitedPeers = new Vector(100);
        msg_type = type;
        id = id_gen++;
        
    }
    public genericMessage(genericMessage msg) {
        baseQuery = msg.baseQuery;
        visitedPeers = (Vector)msg.visitedPeers.clone();//new Vector(100);
        msg_type = msg.msg_type; 
        id = msg.id;
        
    }
    public abstract Object clone();
    public int getId() {
        return id;
    }
   public int getType() {
        return msg_type;
    }

    public void setId(int id) {
        this.id = id;
    }
    public abstract boolean isExpired();
        // for flood: return TTL == 0
    public void hopUpdate(Node dest) {
        baseQuery.addPeer(dest); //nmsg++;
        visitedPeers.add(dest);
//        seenMsgs.add(new Integer(baseQuery.getId()));
    }
    public boolean hasVisited(Node nd) {
        return visitedPeers.contains(nd);
    }
    public boolean isReq() {
        return msg_type == MSG_REQ;
    }
    public boolean isJoin() {
        return msg_type == MSG_DENY;
    }
    public boolean isReply() {
        return msg_type == MSG_REPLY;
    }
    
}
