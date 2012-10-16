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
import java.util.ArrayList;

/**
 *
 * @author Nill
 */
public class exploreReplyMessage extends genericMessage {
    private boolean expired;
    protected SingleNode src;
    protected ArrayList<SingleNode> knownList;
    /** Creates a new instance of regMessage */
    public exploreReplyMessage(int type,SingleNode s) {
        super(type);
        knownList=new ArrayList<SingleNode>();
        this.src=s;
 //       System.out.println("SingleNode "+ s);
        this.expired = false;
    }
    public exploreReplyMessage(exploreReplyMessage cm) {
        super(cm);
  //      this.key = cm.key;
        this.expired = cm.expired;
    }

    public Object clone() {
        exploreReplyMessage cm = (exploreReplyMessage)new exploreReplyMessage(this);//new fldMessage((genericMessage)this);//.msg_type, super.baseQuery, ttl-1);
        return cm;
    }
    public boolean isExpired() {
        return expired;
    }


    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}

