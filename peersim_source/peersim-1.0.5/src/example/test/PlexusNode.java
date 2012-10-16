/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example.test;

import java.util.Random;

/**
 *
 * @author Sourov
 */
public class PlexusNode
{
    long nodeId;
    SingleNode node;
    long availabilityPattern;
    boolean taken;
    PlexusNode(long nodeId, long availabilityPattern)
    {
        this.nodeId=nodeId;
        this.availabilityPattern=availabilityPattern;
        taken=false;
    }
    PlexusNode(SingleNode node, long availabilityPattern)
    {
        this.node=node;
        this.availabilityPattern=availabilityPattern;
        this.nodeId=node.getID();
        taken=false;
    }

    double GetCost(int maximumHammingDistance)
    {
        Random random=new Random();
        
        int totalNodeNumber=0;
        int totalcost=0;
        int numberOfBitOneInPattern=Long.bitCount(availabilityPattern);
        
        for(int i=0;i<4096;i++)
        {
            if(Math.abs(Golay24Code.G24_codewords[i].numberOfBitOne-numberOfBitOneInPattern)<=maximumHammingDistance)
                if(Golay24Code.G24_codewords[i].GetHammingDistance(availabilityPattern)<=maximumHammingDistance)
                {
                    long randomCodeWord=random.nextLong();
                    long codeWord1=randomCodeWord & 0xfff000;
                    long codeWord2=Golay24Code.G24_codewords[i].codeWord & 0xfff000;
                    int indivdualCost=Long.bitCount(codeWord1 ^ codeWord2);
                    if(indivdualCost<6)
                        indivdualCost=12-indivdualCost;
                    totalcost=totalcost+ indivdualCost;
                    totalNodeNumber++;
                }
        }
        return (double)totalcost/(double)totalNodeNumber;
    }
}
