/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example.test;

import peersim.common.*;
import java.util.*;

/**
 *
 * @author Admin
 */
public class gAlgoJP extends gAlgo
{

    /*   public double[] compute_2Ava(SingleNode npeer)
     {
     int slot_count=GlobalData.slot_count;
     double ava_2[]=new double[slot_count];
     if(npeer.grp_flag)
     {
     int k=npeer.grp_id-1;
     for(int j=0;j<slot_count;j++)
     {
     double temp=GlobalData.grouplist.get(k).groupslot[j];
     //1-availability
     //2availibility
     double t[]=new double[GlobalData.grouplist.get(k).grp_size];
     for(int l=0;l<GlobalData.grouplist.get(k).grp_size;l++){
     t[l]=GlobalData.grouplist.get(k).memberlist.get(l).slot_single[j];
     }
     double sum=0.0;
     for(int u=0;u<t.length;u++)
     {
     double prod=1.0;
     for(int a=0;a<t.length;a++)
     {
     if(u!=a)
     {
     prod*=(1-t[a]);
     }
     }
     prod*=t[u];
     sum+=prod;
     }
     sum=temp-sum;
     ava_2[j]=sum;
     }
     }
     else
     {
     for(int j=0;j<slot_count;j++)
     {
     ava_2[j]=0;
     }
     }
     return ava_2;
     }

     public double[] compute_2Ava(SingleNode npeer1, SingleNode npeer2, int new_size)
     {
     int slot_count=GlobalData.slot_count;
     double ava_2[]=new double[slot_count];

     int k1=npeer1.grp_id-1;
     int k2=npeer2.grp_id-1;
     double []pold=npeer1.slot;
     double []pnew=npeer2.slot;
     for(int j=0;j<slot_count;j++)
     {
     double temp1=pold[j];
     double temp2=pnew[j];
     double temp=1-((1-temp1)*(1-temp2));
     //1-availability
     //2availibility
     double t[]=new double[new_size];
     int m=0;
     if(npeer1.grp_flag)
     {
     for(int l=0;l<GlobalData.grouplist.get(k1).grp_size;l++){
     t[l]=GlobalData.grouplist.get(k1).memberlist.get(l).slot_single[j];
     m++;

     }
     }
     else
     {
     t[0]=temp1;
     }
     if(npeer2.grp_flag)
     {
     for(int l=0;l<GlobalData.grouplist.get(k2).grp_size;l++){
     t[m+l]=GlobalData.grouplist.get(k2).memberlist.get(l).slot_single[j];
     }
     }
     else
     {
     t[1]=temp2;
     }
     double sum=0.0;
     for(int u=0;u<t.length;u++)
     {
     double prod=1.0;
     for(int a=0;a<t.length;a++)
     {
     if(u!=a)
     {
     prod*=(1-t[a]);
     }
     }
     prod*=t[u];
     sum+=prod;
     }
     sum=temp-sum;
     ava_2[j]=sum;
     }
     return ava_2;
     }


     public double contributionCalc2(SingleNode old, SingleNode nnew){

     double []pold=old.slot;
     double []pnew=nnew.slot;
     int new_grp_size=old.grp_size+nnew.grp_size;
     //    new_grp_size=new_grp_size-1;
     int slot_count=GlobalData.slot_count;
     double [] grp= new double[slot_count];
     double jp=0.0;
     double _old=0.0;
     double _new=0.0;
     double sum_jp=0.0;
     double sum_old=0.0;
     double sum_new=0.0;
     if(new_grp_size>4)
     jp=0.0;
     if(old.grp_flag==false && nnew.grp_flag==false)
     {
     double contribution=0.0;
     double sum_contribution=0.0;
     for(int i=0;i<slot_count;i++)
     {
     jp=pold[i]*pnew[i];
     if(pold[i]<=pnew[i]){
     contribution=Math.pow(jp,pold[i]/pnew[i])-jp;
     }
     else if (pold[i]>pnew[i]){
     contribution=Math.pow(jp,pnew[i]/pold[i])-jp;
     }
     sum_contribution+=contribution;
     }
     double devide_by=(double)slot_count*new_grp_size;
     double utility=sum_contribution/devide_by;
     return utility;
     }
     else
     {
     double []ava_peer1=compute_2Ava(old);
     double []ava_peer2=compute_2Ava(nnew);
     double []ava_new=compute_2Ava(old,nnew,new_grp_size);
     for(int i=0;i<slot_count;i++)
     {
     sum_old+=ava_peer1[i];
     sum_new+=ava_peer2[i];
     sum_jp+=ava_new[i];
     }
     double devide_by=(double)slot_count;
     double uold=(sum_jp-sum_old)/(devide_by);
     double unew=(sum_jp-sum_new)/(devide_by);
     double joint_utility=(uold+unew)/new_grp_size;
     //     System.out.println("Calculating: "+old.getID()+","+nnew.getID()+" Grpisze "+new_grp_size+" contr "+joint_utility);
     return joint_utility;
     }
     }

     */
    /*
     public double contributionCalc(SingleNode old, SingleNode nnew){

     double []pold=old.slot;
     double []pnew=nnew.slot;
     int new_grp_size=old.grp_size+nnew.grp_size;
     int slot_count=GlobalData.slot_count;
     double [] grp= new double[slot_count];
     double jp=0.0;
     double contribution=0.0;
     double sum_contribution=0.0;

     for(int i=0;i<slot_count;i++)
     {
     jp=pold[i]*pnew[i];
     if(pold[i]<=pnew[i]){
     contribution=Math.pow(jp,pold[i]/pnew[i])-jp;
     }
     else if (pold[i]>pnew[i]){
     contribution=Math.pow(jp,pnew[i]/pold[i])-jp;
     }
     sum_contribution+=contribution;
     }
     double devide_by=(double)slot_count*new_grp_size;
     //        double devide_by=(double)new_grp_size;
     double utility=sum_contribution/devide_by;
     return utility;
     }

     public double contributionCalc(double [] old, double [] nnew, int grp_size ){

     double []pold=old;
     double []pnew=nnew;
     int new_grp_size=grp_size;
     int slot_count=GlobalData.slot_count;
     double [] grp= new double[slot_count];
     double jp=0.0;
     double contribution=0.0;
     double sum_contribution=0.0;

     for(int i=0;i<slot_count;i++)
     {
     jp=pold[i]*pnew[i];
     if(pold[i]<=pnew[i]){
     contribution=Math.pow(jp,pold[i]/pnew[i])-jp;
     }
     else if (pold[i]>pnew[i]){
     contribution=Math.pow(jp,pnew[i]/pold[i])-jp;
     }
     sum_contribution+=contribution;
     }
     double devide_by=(double)slot_count*new_grp_size;
     //        double devide_by=(double)new_grp_size;
     double utility=sum_contribution/devide_by;
     return utility;
     }
     */
    public double contributionCalc(SingleNode old, SingleNode nnew)
    {

        double[] pold = old.slot;
        double[] pnew = nnew.slot;
        int new_grp_size = old.grp_size + nnew.grp_size;
        int old_improve = new_grp_size - old.grp_size;
        int new_improve = new_grp_size - nnew.grp_size;

        //    int new_grp_size=1;
        int slot_count = GlobalData.slot_count;
        double[] grp = new double[slot_count];
        double jp = 0.0;
        double _old = 0.0;
        double _new = 0.0;
        double sum_jp = 0.0;
        double sum_old = 0.0;
        double sum_new = 0.0;
        int count = 0;
        long pattern = old.pattern;
        for (int i = slot_count - 1; i >= 0; i--, pattern = pattern >> 1)
        {
            //    if((pattern & 1)==1)
            //         continue;
            _old = 1 - pold[i];
            _new = 1 - pnew[i];
            jp = 1 - _old * _new;
            grp[i] = jp;
            sum_old += pold[i];
            sum_new += pnew[i];
            sum_jp += jp;
            count++;
        }
        /*
         double devide_by=(double)count*new_grp_size;
         double uold=(sum_jp-sum_old)/(devide_by);
         double unew=(sum_jp-sum_new)/(devide_by);

         *
         */
        double devide_by = (double) slot_count;
        double uold = (sum_jp - sum_old) / (devide_by * old_improve);
        double unew = (sum_jp - sum_new) / (devide_by * new_improve);

        double joint_utility = (uold + unew);
        //     System.out.println("Calculating: "+old.getID()+","+nnew.getID()+" Grpisze "+new_grp_size+" contr "+joint_utility);
        return joint_utility;
    }

    public double contributionCalc(double[] old, double[] nnew, int grp_size)
    {

        double[] pold = old;
        double[] pnew = nnew;
        int new_grp_size = grp_size;
        int slot_count = GlobalData.slot_count;
        double[] grp = new double[slot_count];
        double jp = 0.0;
        double _old = 0.0;
        double _new = 0.0;
        double sum_jp = 0.0;
        double sum_old = 0.0;
        double sum_new = 0.0;
        int count = 0;
        long pattern = GlobalData.golayCode.ConvertToPatternUsingStaticThesoldForGroup(old, grp_size);
        for (int i = slot_count - 1; i >= 0; i--, pattern = pattern >> 1)
        {
            //     if((pattern & 1)==1)
            //       continue;
            _old = 1 - pold[i];
            _new = 1 - pnew[i];
            jp = 1 - _old * _new;
            grp[i] = jp;
            sum_old += pold[i];
            sum_new += pnew[i];
            sum_jp += jp;
            count++;
        }
        double devide_by = (double) count * new_grp_size;
        double uold = (sum_jp - sum_old) / (devide_by);
        double unew = (sum_jp - sum_new) / (devide_by);
        double joint_utility = (uold + unew);
        return joint_utility;
    }

    public double newContributionCalc(SingleNode old, SingleNode nnew)
    {
        double[] pold = old.slot;
        double[] pnew = nnew.slot;
        int new_grp_size = old.grp_size + nnew.grp_size;
        int old_improve = new_grp_size - old.grp_size;
        int new_improve = new_grp_size - nnew.grp_size;

        //    int new_grp_size=1;
        int slot_count = GlobalData.slot_count;
        double[] grp = new double[slot_count];
        double jp = 0.0;
        double _old = 0.0;
        double _new = 0.0;
        double sum_jp = 0.0;
        double sum_old = 0.0;
        double sum_new = 0.0;
        int count = 0;
        long pattern = old.pattern;
        for (int i = slot_count - 1; i >= 0; i--, pattern = pattern >> 1)
        {
            //    if((pattern & 1)==1)
            //         continue;
            _old = 1 - pold[i];
            _new = 1 - pnew[i];
            jp = 1 - _old * _new;
            grp[i] = jp;
            sum_old += pold[i];
            sum_new += pnew[i];
            sum_jp += jp;
            count++;
        }
        /*
         double devide_by=(double)count*new_grp_size;
         double uold=(sum_jp-sum_old)/(devide_by);
         double unew=(sum_jp-sum_new)/(devide_by);

         *
         */
        double devide_by = (double) slot_count; 
        double uold = (sum_jp - sum_old) / (devide_by * old_improve);
        double unew = (sum_jp - sum_new) / (devide_by * new_improve);

        double joint_utility = (uold + unew);
        //     System.out.println("Calculating: "+old.getID()+","+nnew.getID()+" Grpisze "+new_grp_size+" contr "+joint_utility);
        return joint_utility;
    }

    double[] new_group(double[] pold, double[] pnew)
    {
        double jp = 0.0;
        double _old = 0.0;
        double _new = 0.0;
        int slot_count = GlobalData.slot_count;
        double[] grp = new double[slot_count];

        for (int i = 0; i < slot_count; i++)
        {
            _old = 1 - pold[i];
            _new = 1 - pnew[i];
            jp = 1 - _old * _new;
            grp[i] = jp;
        }
        return grp;
    }

    public ArrayList<KnownNode> updateKnownList(SingleNode a, SingleNode b)
    {

        SingleNode peer;
        double contribution;
        boolean already_member = false;
        int nsize;

        for (int i = 0; i < a.knownList.size(); ++i)
        {
            peer = a.knownList.get(i).node;
            if (a.grp_flag == true)
            {
                GroupNode ga = GlobalData.grouplist.get(a.grp_id - 1);
                for (int l = 0; l < ga.memberlist.size(); l++)
                {
                    if (peer.getID() == ga.memberlist.get(l).getID())
                    {
                        already_member = true;
                        break;
                    }
                }
            }
            else if (peer.getID() == a.getID())
            {
                already_member = true;
            }

            if (already_member == true)
            {
                a.knownList.remove(i);
                --i;
                already_member = false;
                continue;
            }

            if (b.grp_flag == true)
            {
                GroupNode gb = GlobalData.grouplist.get(b.grp_id - 1);
                for (int l = 0; l < gb.memberlist.size(); l++)
                {
                    if (peer.getID() == gb.memberlist.get(l).getID())
                    {
                        already_member = true;
                        break;
                    }
                }
            }
            else if (peer.getID() == b.getID())
            {
                already_member = true;
            }

            if (already_member == true)
            {
                a.knownList.remove(i);
                --i;
                already_member = false;
                continue;
            }

            a.knownList.get(i).deny_flag = false;
            nsize = a.grp_size + a.knownList.get(i).grp_size;
            contribution = contributionCalc(a.slot, a.knownList.get(i).slot, nsize);
            a.knownList.get(i).contribution = contribution;
        }
        Collections.sort(a.knownList, new ConComparator());
        already_member = false;
        for (int i = 0; i < b.knownList.size(); ++i)
        {
            peer = b.knownList.get(i).node;
            if (a.grp_flag == true)
            {
                GroupNode ga = GlobalData.grouplist.get(a.grp_id - 1);
                for (int l = 0; l < ga.memberlist.size(); l++)
                {
                    if (peer.getID() == ga.memberlist.get(l).getID())
                    {
                        already_member = true;
                        break;
                    }
                }
            }
            else if (peer.getID() == a.getID())
            {
                already_member = true;
            }

            if (b.grp_flag == true)
            {
                GroupNode gb = GlobalData.grouplist.get(b.grp_id - 1);
                for (int l = 0; l < gb.memberlist.size(); l++)
                {
                    if (peer.getID() == gb.memberlist.get(l).getID())
                    {
                        already_member = true;
                        break;
                    }
                }
            }
            else if (peer.getID() == b.getID())
            {
                already_member = true;
            }

            if (already_member == true)
            {
                already_member = false;
                continue;
            }
            KnownNode k = new KnownNode();
            k.slot = new double[GlobalData.slot_count];
            nsize = a.grp_size + b.knownList.get(i).grp_size;
            contribution = contributionCalc(a.slot, b.knownList.get(i).slot, nsize);
            k.node = peer;
            k.deny_flag = false;
            System.arraycopy(b.knownList.get(i).slot, 0, k.slot, 0, b.knownList.get(i).slot.length);
            k.grp_size = b.knownList.get(i).grp_size;
            k.contribution = contribution;
            int position = binsearch(a.knownList, k);
            if (position > 0 && a.knownList.size() == GlobalData.known_count)
            {
                a.knownList.remove(0);
                a.knownList.add(position - 1, k);
            }
            else if (position > 0 && a.knownList.size() < GlobalData.known_count)
            {
                a.knownList.add(position, k);
            }
            else if (position == 0 && a.knownList.size() < GlobalData.known_count)
            {
                a.knownList.add(position, k);
            }
        }
        return a.knownList;
    }

    public void mergeGroup(SingleNode a, SingleNode b)
    {
        double[] grp = new_group(a.slot, b.slot);
        System.arraycopy(grp, 0, a.slot, 0, grp.length);
        System.arraycopy(grp, 0, b.slot, 0, grp.length);
        int old_agrp_size = a.grp_size;
        int old_bgrp_size = b.grp_size;
        int old_bgrp_id = b.grp_id;
        int new_grp_size = a.grp_size + b.grp_size;
        a.grp_size = new_grp_size;
        b.grp_size = new_grp_size;
        GlobalData.golayCode.ModifyPatternOfANode(a, b);
        GlobalData.golayCode.ModifyPatternOfANode(b, a);
        ArrayList<KnownNode> klist = new ArrayList<KnownNode>();
        klist = updateKnownList(a, b);

        if (old_agrp_size > 1)
        {
            GroupNode g = GlobalData.grouplist.get(a.grp_id - 1);
            g.grp_size = new_grp_size;
            System.arraycopy(grp, 0, g.groupslot, 0, grp.length);
            for (int i = 0; i < g.memberlist.size(); i++)
            {
                if (g.memberlist.get(i).getID() != a.getID())
                {
                    g.memberlist.get(i).grp_size = new_grp_size;
                    g.memberlist.get(i).knownList.clear();
                    g.memberlist.get(i).knownList.addAll(klist);
                    System.arraycopy(grp, 0, g.memberlist.get(i).slot, 0, grp.length);
                    GlobalData.golayCode.ModifyPatternOfANode(g.memberlist.get(i), a.pattern);
                }
            }
            if (old_bgrp_size > 1)
            {
                GroupNode gb = GlobalData.grouplist.get(b.grp_id - 1);
                for (int i = 0; i < gb.memberlist.size(); i++)
                {
                    gb.memberlist.get(i).grp_size = new_grp_size;
                    gb.memberlist.get(i).grp_id = g.grp_id;
                    gb.memberlist.get(i).knownList.clear();
                    gb.memberlist.get(i).knownList.addAll(klist);
                    System.arraycopy(grp, 0, gb.memberlist.get(i).slot, 0, grp.length);
                    GlobalData.golayCode.ModifyPatternOfANode(gb.memberlist.get(i), b.pattern);
                    //  GlobalData.grouplist.get(a.grp_id-1).memberlist.add(gb.memberlist.get(i));
                    g.memberlist.add(gb.memberlist.get(i));
                    if (gb.memberlist.get(i).getID() > g.leader.getID())
                    {
                        g.leader = gb.memberlist.get(i);
                    }
                }
                GlobalData.grouplist.get(old_bgrp_id - 1).isvalid = false;
            }
            else
            {
                b.grp_id = g.grp_id;
                b.knownList.clear();
                b.knownList.addAll(klist);
                b.grp_flag = true;
                g.memberlist.add(b);
                if (b.getID() > g.leader.getID())
                {
                    g.leader = b;
                }
            }
            //      System.out.println("Modifying groups :"+a.getID()+" and "+b.getID());
        }
        else if (old_bgrp_size > 1)
        {
            GroupNode g = GlobalData.grouplist.get(b.grp_id - 1);
            g.grp_size = new_grp_size;
            System.arraycopy(grp, 0, g.groupslot, 0, grp.length);
            for (int i = 0; i < g.memberlist.size(); i++)
            {
                g.memberlist.get(i).grp_size = new_grp_size;
                g.memberlist.get(i).knownList.clear();
                g.memberlist.get(i).knownList.addAll(klist);
                System.arraycopy(grp, 0, g.memberlist.get(i).slot, 0, grp.length);
                GlobalData.golayCode.ModifyPatternOfANode(g.memberlist.get(i), b.pattern);
            }
            a.grp_id = g.grp_id;
            a.grp_flag = true;
            g.memberlist.add(a);
            if (a.getID() > g.leader.getID())
            {
                g.leader = a;
            }
            //     System.out.println("Modifying groups :"+a.getID()+" and "+b.getID());
        }
        else
        {
            GroupNode g = new GroupNode();
            a.grp_id = g.grp_id;
            b.grp_id = g.grp_id;
            g.memberlist.add(a);
            g.memberlist.add(b);
            g.grp_size = new_grp_size;
            b.knownList.clear();
            b.knownList.addAll(klist);
            a.grp_flag = true;
            b.grp_flag = true;
            if (a.getID() > b.getID())
            {
                g.leader = a;
            }
            else
            {
                g.leader = b;
            }
            System.arraycopy(grp, 0, g.groupslot, 0, grp.length);
            GlobalData.grouplist.add(g);
            //     System.out.println("Forming groups :"+a.getID()+" and "+b.getID());
        }
    }

    public int binsearch(ArrayList<KnownNode> k_list, KnownNode kn)
    {
        int l, h, m;
        for (l = 0; l < k_list.size(); l++)
        {
            /*     if(k_list.get(l).node.isUp()==false)
             {
             k_list.remove(l);
             l--;
             continue;
             }*/
            if (k_list.get(l).node.getID() == kn.node.getID())
            {
                if (k_list.get(l).contribution == kn.contribution) //same effect no need to insert
                {
                    return -1;
                }
                else
                {
                    if (k_list.get(l).grp_size > kn.grp_size) //list's grp size is larger that info seems to be fresher than kn
                    {
                        k_list.get(l).deny_flag = false;
                        return -1;
                    }
                    else
                    {
                        k_list.remove(l);  //kn is seems to be sresher than klist, so remove that from list and insert kn
                        return l;
                    }
                }
            }
            else
            {
                k_list.get(l).deny_flag = false;
            }
        }

        l = 0;
        h = k_list.size();
        m = (l + h) / 2;
        while (l < h)
        {
            if (k_list.get(m).contribution >= kn.contribution)
            {
                h = m;
            }
            else
            {
                l = m + 1;
            }
            m = (l + h) / 2;
        }
        return m;
    }
}

/*
public int binsearch(ArrayList<KnownNode> k_list,KnownNode kn)
{
    int l,h,m;
    l=0;
    h=k_list.size();
    m=(l+h)/2;
    while(l<h){
        if(k_list.get(m).contribution>=kn.contribution)
            h=m;
        else
            l=m+1;
        m=(l+h)/2;
    }
    return m;
}
*/