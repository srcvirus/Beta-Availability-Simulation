/*
 * genericQuery.java
 *
 * Created on December 3, 2006, 10:25 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package peersim.common;

import peersim.core.*;
import peersim.cdsim.*;
import java.util.Vector;
import java.util.Iterator;
import peersim.util.*;

/**
 *
 * @author Nill
 */
public class genericQuery {
    private Node src;
    private String strQuery;
    Vector results;
    Vector visitedPeers;
    int nmsg;
    int nFound;
    int startCycle;
    private int id;
    int lastSeen;
    static int id_gen = 100;
    
    /** Creates a new instance of genericQuery */
    public genericQuery(int cycle, String strAdv, int pctNGram) {
    /*    src = Network.get(CommonRandom.r.nextInt(Network.size()));
        genericProtocol fdst;
        do {
            fdst = (genericProtocol)(Network.get(
                    CommonRandom.r.nextInt(Network.size())).getProtocol(Global.protocolID));
        } while (fdst.getAdvDB().size() <= 0);
        strQuery = NameList.Names().makeStrQuery(strAdv, pctNGram);

        startCycle = cycle; //baseCycle+CommonRandom.r.nextInt(Period);
        id = id_gen++;
        results = new Vector(20);
        visitedPeers = new Vector(200);
        lastSeen = startCycle; //CommonState.getCycle();
        nmsg = 0;
        nFound = 0;*/
    }

    public int getId() {
        return id;
    }
    public String toString() {
//        return "c"+startCycle+":"+id+":"+strQuery;
        return "c"+startCycle+":"+id+":"+strQuery+" np:"+visitedPeers.size()+" nr:"+results.size();
    }

    public boolean addPeer(Node peer) {
    /*    Integer id = new Integer(peer.getIndex());
        if (visitedPeers.contains(id))
            return false;
        visitedPeers.add(id);
        lastSeen = CommonState.getCycle();   
        nmsg++;
        addResults(((genericProtocol)peer.getProtocol(Global.protocolID)).getAdvDB());*/
        return true;
    }
    private boolean addResults(Vector txtList) { // txtList is basically the advertDB of a node
     /*   if (txtList == null || txtList.size() <= 0) return false;
        for (Iterator i = txtList.iterator(); i.hasNext(); ) {
            String txt = (String)i.next();
            if (!NameList.isMatch(txt, strQuery)) continue;
            if (!results.contains(txt)) 
                results.add(txt);
            nFound++;
        }
//        nFound += txtList.size();
        lastSeen = CommonState.getCycle();*/
        return true;
    }

    public int findUniqueMatchCount() {
        int count = 0;
//System.out.println("********** mxAI:"+Global.maxAdvIndx+"  sz:"+NameList.Names().getNameCount());        
    /*    for (int i = 0; i < Global.maxAdvIndx; i++) {
            String adv = NameList.Names().getName(i);
//if (adv == null) System.out.println("????????????? i:"+i);            
            if (NameList.isMatch(adv, strQuery)) {
                count++;
            }
        }*/
        return count;
    }
    
    public int findTotalMatchCount(int pid) {
        int count = 0;
    /*    for (int i = 0; i < Network.size(); i++) {
            genericProtocol fnd = (genericProtocol)Network.get(i).getProtocol(pid);
            for (Iterator ai = fnd.getAdvDB().iterator(); ai.hasNext(); ) {
                String adv = (String)ai.next();
                if (NameList.isMatch(adv, strQuery)) count++;
            }
        }*/
        return count;
    }
    
    public Node getSrc() {
        return src;
    }

    public String getStrQuery() {
        return strQuery;
    }
}
