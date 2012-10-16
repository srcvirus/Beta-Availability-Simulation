/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Admin
 */


package example.test;

import peersim.common.genericMessage;
import peersim.common.genericQuery;

/**
 *
 * @author Nill
 */
public class exploreMessage extends genericMessage {
    private boolean expired;
    protected SingleNode src;
    /** Creates a new instance of regMessage */
    public exploreMessage(int type,SingleNode s) {
        super(type);
        this.src=s;
 //       System.out.println("SingleNode "+ s);
        this.expired = false;
    }
    public exploreMessage(exploreMessage cm) {
        super(cm);
  //      this.key = cm.key;
        this.expired = cm.expired;
    }

    public Object clone() {
        exploreMessage cm = (exploreMessage)new exploreMessage(this);//new fldMessage((genericMessage)this);//.msg_type, super.baseQuery, ttl-1);
        return cm;
    }
    public boolean isExpired() {
        return expired;
    }


    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
