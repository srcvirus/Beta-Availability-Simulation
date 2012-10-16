/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package peersim.common;
import example.test.*;
/**
 *
 * @author Admin
 */
public abstract class gAlgo {
 public abstract double contributionCalc(SingleNode old, SingleNode nnew);
 public abstract void mergeGroup(SingleNode a, SingleNode b);

}
