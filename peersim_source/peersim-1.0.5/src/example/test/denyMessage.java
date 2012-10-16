/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package example.test;

import peersim.common.genericMessage;
import peersim.common.genericQuery;

/**
 *
 * @author Nill
 */
public class denyMessage extends genericMessage {
    private boolean expired;
    protected SingleNode src;
    double []src_slot;
    int src_grp_size;
    /** Creates a new instance of regMessage */
    public denyMessage(int type,SingleNode s,double [] slot, int size) {
        super(type);
        this.src=s;
        this.expired = false;
        src_slot=new double[GlobalData.slot_count];
        System.arraycopy(slot, 0, src_slot, 0, slot.length);
        src_grp_size=size;
    }
    public denyMessage(denyMessage cm) {
        super(cm);
  //      this.key = cm.key;
        this.expired = cm.expired;
    }

    public Object clone() {
        denyMessage cm = (denyMessage)new denyMessage(this);
        return cm;
    }
    public boolean isExpired() {
        return expired;
    }
    public void setExpired(boolean expired) {
        this.expired = expired;
    }
   public SingleNode getSource() {
        return src;
    }

}
