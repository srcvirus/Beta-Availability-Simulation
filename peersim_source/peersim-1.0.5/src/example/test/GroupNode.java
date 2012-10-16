/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package example.test;
import java.util.ArrayList;
/**
 *
 * @author Admin
 */
public class GroupNode {

    protected double groupslot[];
    protected int grp_size;
    protected int grp_id;
    protected boolean isvalid;
    protected int down_slots;
    protected boolean isdown;
    protected ArrayList<SingleNode> memberlist;
    public SingleNode leader;
    GroupNode()
    {
       groupslot= new double[GlobalData.slot_count];
       grp_id=++GlobalData.gid;
       memberlist=new ArrayList<SingleNode>();
       isvalid=true;
       isdown=false;
       down_slots=0;
    }

}
