/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package example.test;
import peersim.config.Configuration;
import java.util.Scanner;
import java.io.*;



/**
 *
 * @author Admin
 */
public class LoadData {
    public static double peer_slot[][];
    private static final String PAR_SIZE = "network.size";
    private static int len;

   public static void show()
   {
      int per[]=new int[len];
      int slot_count=GlobalData.slot_count;
      int []hist=new int[slot_count];
      for(int i=0;i<len;i++)
      {
          per[i]=0;
      }
      for(int i=0;i<slot_count;i++)
      {
          hist[i]=0;
      }
      for(int count=0;count<len;count++)
      {
      //    for(int i=0;i<slot_count;i++)
          {
            if(peer_slot[count][0]>0.5)
                per[count]++;
          }
      }
      for(int i=0;i<len;i++)
      {
          hist[per[i]]++;
      }
      for(int i=0;i<slot_count;i++)
      {
           System.out.println("Number of peers having slot_availability>0.5 in "+i+" slots is "+hist[i]);
      }

    }

   public static void load()
   {
      int slot_count=GlobalData.slot_count;
      int count=0;
      len = Configuration.getInt(PAR_SIZE);
      peer_slot= new double[len][slot_count];
      System.out.println("Node count is "+ len);
      try{
        //    Scanner inFile = new Scanner(new FileReader("alpah "+GlobalData.alpha+" .txt"));
            Scanner inFile = new Scanner(new FileReader("alpah "+GlobalData.alpha+" .txt"));
            while( inFile.hasNextDouble()) {
                for(int i=0;i<slot_count;i++)
                {
                    double theNumber = inFile.nextDouble();
                    peer_slot[count][i]= theNumber;
                }
                count++;
                if(count==len)
                    break;
            }
        }
      catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    //   show();
   //    System.out.println();
   }
}
