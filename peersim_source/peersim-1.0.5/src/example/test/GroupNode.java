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
    protected int down_fail_slots[];
    protected boolean isdown;
    protected ArrayList<SingleNode> memberlist;
    public SingleNode leader;
    GroupNode()
    {
       groupslot= new double[GlobalData.slot_count];
       down_fail_slots = new int[20];
       for(int i = 0; i < 20; i++) down_fail_slots[i] = 0;
       grp_id=++GlobalData.gid;
       memberlist=new ArrayList<SingleNode>();
       isvalid=true;
       isdown=false;
       down_slots=0;
    }

}
