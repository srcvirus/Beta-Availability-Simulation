/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package example.test;

import peersim.common.genericMessage;
import peersim.common.genericQuery;
/**
 *
 * @author Admin
 */
public class groupFormMessage extends genericMessage {
    private boolean expired;
    protected SingleNode src;
    /** Creates a new instance of regMessage */
    public groupFormMessage(int type,SingleNode s) {
        super(type);
        this.src=s;
 //       System.out.println("SingleNode "+ s);
        this.expired = false;
    }
    public groupFormMessage(groupFormMessage cm) {
        super(cm);
  //      this.key = cm.key;
        this.expired = cm.expired;
    }

    public Object clone() {
        groupFormMessage cm = (groupFormMessage)new groupFormMessage(this);//new fldMessage((genericMessage)this);//.msg_type, super.baseQuery, ttl-1);
        return cm;
    }
    public boolean isExpired() {
        return expired;
    }
    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}

