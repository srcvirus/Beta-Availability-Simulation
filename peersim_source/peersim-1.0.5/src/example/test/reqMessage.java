/*
 * crdMessage.java
 *
 * Created on December 10, 2006, 8:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package example.test;

import peersim.common.genericMessage;
import peersim.common.genericQuery;

/**
 *
 * @author Nill
 */
public class reqMessage extends genericMessage {
    private boolean expired;
    protected SingleNode src;
    protected SingleNode dest;
    protected double contribution;
    double []src_slot;
    int src_grp_size;
    /** Creates a new instance of regMessage */
    public reqMessage(int type,SingleNode s,double c,double [] slot, int size) {
        super(type);
        this.src=s;
        contribution=c;
        src_slot=new double[GlobalData.slot_count];
        System.arraycopy(slot, 0, src_slot, 0, slot.length);
        src_grp_size=size;
        this.expired = false;
    }
    public reqMessage(reqMessage cm) {
        super(cm);
  //      this.key = cm.key;
        this.expired = cm.expired;
    }

    public Object clone() {
        reqMessage cm = (reqMessage)new reqMessage(this);//new fldMessage((genericMessage)this);//.msg_type, super.baseQuery, ttl-1);
        return cm;
    }
    public boolean isExpired() {
        return expired;
    }


    public void setExpired(boolean expired) {
        this.expired = expired;
    }

   public void setDest(SingleNode dest) {
        this.dest = dest;
    }

    public SingleNode getSource() {
        return src;
    }
}



