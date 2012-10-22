/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example.test;

import java.util.ArrayList;

/**
 *
 * @author Sourov
 */
public class Golay24CodeWord
{
    long codeWord;
    int numberOfBitOne;
    ArrayList<PlexusNode> bucket;
    
    Golay24CodeWord(long aCodeWord)
    {
        codeWord=aCodeWord;
        numberOfBitOne=Long.bitCount(aCodeWord);
        bucket=new ArrayList<PlexusNode>();
    }  
    
    boolean Remove(PlexusNode nodeTobeRemoved)
    {
        for(int i=0;i<bucket.size();i++)
        {
            PlexusNode element=bucket.get(i);
            if(element.availabilityPattern==nodeTobeRemoved.availabilityPattern)
            {
                if(nodeTobeRemoved.nodeId==bucket.get(i).nodeId)
                {
                    bucket.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
    
    boolean IfBucketContains(PlexusNode nodeTobeSearched)
    {
        for(int i=0;i<bucket.size();i++)
        {
            PlexusNode element=bucket.get(i);
            if(element.availabilityPattern==nodeTobeSearched.availabilityPattern)
            {
                if(nodeTobeSearched.nodeId==bucket.get(i).nodeId)
                {
                   // bucket.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
    
    int GetHammingDistance(long newPattern)
    {
        int distance=0;
        long bucketCodeWord=codeWord;
        return GetHammingDistance(newPattern,bucketCodeWord);
    }
    
    int GetHammingDistance(long pattern1,long pattern2)
    {
        int distance=0;
        for(int i=0;i<24;i++)
        {
            if((pattern1 & 1)!= (pattern2 & 1))
            {
                distance++;
            }
            pattern1=pattern1>>1;
            pattern2=pattern2>>1;
        }
        
        return distance;
    }
    
    ArrayList<PlexusNode> GetNodeInMaximamHammingDistace(long searchedPattern,int maximumHammingDistance)
    {
        ArrayList<PlexusNode> nodesInMinimumHammingDistance=new ArrayList<PlexusNode>();
        for(int i=0;i<bucket.size();i++)
        {
            if(GetHammingDistance(searchedPattern, bucket.get(i).availabilityPattern)<=maximumHammingDistance)
                nodesInMinimumHammingDistance.add(bucket.get(i));
        }
        return nodesInMinimumHammingDistance;
    }
    
    void Remove(long removingSingleNodeId,long prevPattern)
    {
        for(int i=0;i<bucket.size();i++)
        {
            PlexusNode aPlexusNode=bucket.get(i);
            if(prevPattern==aPlexusNode.availabilityPattern)
            {
                if(removingSingleNodeId==aPlexusNode.node.getID())
                {
                    bucket.remove(i);
                }
            }
                //nodesInMinimumHammingDistance.add(bucket.get(i));
        }
       // return nodesInMinimumHammingDistance;
    }
}

