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
public class replyMessage extends genericMessage {
    private boolean expired;
    protected SingleNode src;
    /** Creates a new instance of regMessage */
    public replyMessage(int type,SingleNode s) {
        super(type);
        this.src=s;
 //       System.out.println("SingleNode "+ s);
        this.expired = false;
    }
    public replyMessage(replyMessage cm) {
        super(cm);
  //      this.key = cm.key;
        this.expired = cm.expired;
    }

    public Object clone() {
        replyMessage cm = (replyMessage)new replyMessage(this);
        return cm;
    }
    public boolean isExpired() {
        return expired;
    }


    public void setExpired(boolean expired) {
        this.expired = expired;
    }

}
