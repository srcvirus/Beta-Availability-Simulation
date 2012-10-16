/*
 * crdTopologyInit.java
 *
 * Created on December 10, 2006, 9:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package example.test;


import java.util.Random;
import peersim.core.Network;
import peersim.core.*;
import peersim.core.OverlayGraph;
import peersim.graph.Graph;

/**
 *
 * @author Nill
 */
public class crdTopologyInit implements Control {
    
    /** Creates a new instance of crdTopologyInit */
    public crdTopologyInit(String prefix) {
       System.out.println("Inistializing chord topology");
     //  LoadData.load();
    }
       public boolean execute() {

        return false;
    }
}
/*    private int find(CID[] ids, CID key) {
        if (key.compare(ids[ids.length - 1]) > 0 || key.compare(ids[0]) <= 0) 
            return 0;
        int l = 0;
        int h = ids.length - 1;
        while (h-l > 5) {
            int m = (l+h)>>1;
            if (key.compare(ids[m]) > 0) l = m;
            else if (key.compare(ids[m]) < 0) h = m;
            else return m;
        }
        for (int i = (l==0)?1:l; i <= h; i++) {
            if (ids[i-1].compare(key) < 0 && key.compare(ids[i]) <= 0) return i;
        }
        System.out.println("crdTopologyInit.find: ids["+l+"]:"+ids[l]+",  key:"+key+",  ids["+h+"]:"+ids[h]);
        return -1;
    }

    public Graph ChordRing(Graph g, Random rnd, int pid) {
//        long[] ids_fake = { 0x11f, 0x1f6, 0x200, 0x28f, 0x2dc, 0x48d, 0x4aa, 0x4e6, 0x5c2, 0x75e, 0x81b, 0x8d0, 0xa10, 0xa11, 0xa20, 0xa4d, 0xb7f, 0xc84, 0xd40, 0xf54};
        int size = g.size(); // size of the network
//System.out.println("Chord ring size: "+size);
        // generate a list of random IDS
        CID[] ids = new CID[size];
        ids[0] = CID.getRandom();//.nextInt(0xfff);//nextLong();
//        if (ids[0] < 0) ids[0] = -ids[0];
        for (int i = 1; i < size; i++) {
//            long id = ids_fake[i];//rnd.nextInt(0xfff); //nextLong();
            CID id = CID.getRandom();
//            if (id < 0) id = -id;
            int j;
//System.out.println("Inserting:"+Long.toHexString(id)+" in:");
//for (int k = 0; k < i; k++) System.out.print(" "+Long.toHexString(ids[k]));
//System.out.println();

            for (j = i; j > 0; j--) 
                if (id.compare(ids[j-1]) < 0) ids[j] = ids[j-1];
                else break;
            ids[j] = id;
         
        }
//for (int k = 0; k < size; k++) System.out.print(" "+ids[k]);    
//System.out.println();   

        // assingn random ids in order among the nodes
        for (int i = 0; i < size; i++) {
            crdNode cnp = (crdNode)Network.get(i).getProtocol(pid);
            cnp.setId(ids[i]);
            int pre_idx = (i <= 0) ? size - 1 : i-1;
            int suc_idx = (i >= size-1) ? 0 : i+1;
            g.setEdge(i, suc_idx);
            cnp.setPredecessor(Network.get(pre_idx));
            cnp.setSuccessor(Network.get(suc_idx));
        }
//for (int z = 0; z < 2; z++) {        
//long key = (z==0)?ids[0]-3:ids[size-1]+40;//rnd.nextInt(1000);
//int x = find(ids, key);
//System.out.println("key:"+Long.toHexString(key)+"  nd:"+((crdNode)Network.get(x).getProtocol(Global.linkableID)).getId());
//}
        // setup neighborhood relationship
        for (int i = 0; i < size; i++) {
            crdNode cnp = (crdNode)Network.get(i).getProtocol(pid);
//System.out.print("Node:"+cnp.getId());            
            int k = (i==size-1)?0:i+1;
            for (int p = 1; p < CID.nBits(); p++) {
//                long nid = (ids[i] + w); 
                CID nid = cnp.getId().getNeighborID(p);
                int idx = find(ids, nid);
                cnp.addNeighbor(Network.get(idx));
//System.out.print(" "+nid+"->"+crdNode.get(idx).getId());
            }
//System.out.println();
        }
        System.out.println("Chord ring generation finished!");
        return g;
    }
    public void modify() {
        OverlayGraph ogr = new OverlayGraph(Global.linkableID);
        ChordRing(ogr, util.CommonRandom.r, Global.linkableID);
        
//        Debug2();
//        Debug();
//        System.exit(0);
    }
    public void Debug2() {
        CID key = new CID(0x12345678);
        int[] nhops = new int[1];
        int[] si = { 100, Network.size() - 500, 400, 200, 3434, 2000};
        for (int i = 0; i < si.length; i++) {
            Node src = Network.get(si[i]);
            crdProtocol cn = (crdProtocol)src.getProtocol(Global.protocolID);
            Node dst = cn.routeOffline(src, key, nhops);
            System.out.println("key:"+key
                                +"   src:"+((crdNode)src.getProtocol(Global.linkableID)).getId()
                                +"   dst:"+((crdNode)dst.getProtocol(Global.linkableID)).getId()
                                +"   hops:"+nhops[0]);
        }
       
    }
    public void Debug() {
        for (int i = 0; i < Network.size(); i++) {
            crdNode cn = (crdNode)Network.get(i).getProtocol(Global.linkableID);
            cn.Debug();
        }
        for (int i = 0; i < Network.size(); i++) {
            System.out.print(crdNode.get(i).getId()+"  ");
        }
        for (int i = 0; i < 20; i++) {
            CID key = CID.getRandom();
            Node src = Network.get(util.CommonRandom.r.nextInt(Network.size()));
            crdProtocol crdSrc = (crdProtocol)src.getProtocol(Global.protocolID);
            int[] nhops = new int[1];
            Node dst = crdSrc.routeOffline(src, key, nhops);
            System.out.println(
                    "routeOffline Key:"+key+"  dst:"+((crdNode)dst.getProtocol(Global.linkableID)).getId()+
                    "  hops:"+nhops[0]);
        }
    }
}
*/