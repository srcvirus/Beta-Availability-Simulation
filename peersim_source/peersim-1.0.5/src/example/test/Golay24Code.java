/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package golaycodegenaration;
package example.test;

import java.util.ArrayList;
import java.util.Vector;
import org.apache.commons.math.util.MathUtils;

/**
 *
 * @author Sourov
 */
public class Golay24Code 
{
    static long[] G24_base = {
//        0x8007ff, 0x400ee2, 0x200dc5, 0x100b8b, 0x080f16, 0x040e2d,
//	0x020c5b, 0x0108b7, 0x00896e, 0x004adc, 0x002db8, 0x001b71  };
        0x800d55, 0x400eaa, 0x200769, 0x100b96, 
        0x0805da, 0x040ae5, 0x020676, 0x0109b9,
        0x00869d, 0x00496e, 0x0025a7, 0x001a5b };
    
    int K=12;
    static int N=24;
    int totalNumberInsertedIntoBucket=0;
    static Golay24CodeWord[] G24_codewords;
    static int maximumHammingDistance;
    private static Golay24Code Golay24CodeInstance; 
    
    public static synchronized Golay24Code getInstance() 
    {
        if (Golay24CodeInstance==null) 
        {
            Golay24CodeInstance = new Golay24Code();
        }
        return Golay24CodeInstance;
    } 
        
    private Golay24Code()
    {
        G24_codewords=new Golay24CodeWord[4096];
        
        for (int i = 0; i < G24_codewords.length; i++) 
        {
            G24_codewords[i]=new Golay24CodeWord(GenerateCodeWord(i));
        }
        maximumHammingDistance=GlobalData.maxHammingDistance;
    }
       
    long GenerateCodeWord(int idx) 
    {
        if (idx < 0 || idx >= (1 << K))
            return -1;
        long code = 0;
        for (int i = K-1, w = 1; i >= 0; i--, w <<= 1) 
        {
            if ((idx & w) == w) {
                code ^= G24_base[i];
            }
        }
        return code;
    }

    void AddToPlexus(SingleNode SNode)
    {
        //long pattern=ConvertToPatternByCombiningTwo(SNode.slot);
   //     long pattern= ConvertByPreviousMethod(SNode.slot);
        if(SNode.getID()==26)
            System.out.println();
        long pattern=ConvertToPatternUsingStaticThesold(SNode.slot);
        //long pattern = ConvertToPatternByCombiningTwo(SNode.slot);
        SNode.pattern=pattern;
        SNode.pattern_single=pattern;
        PlexusNode p=new PlexusNode(SNode,pattern);
        AddPattern(p);
  //      System.out.println(GetNumberOfOneBitInAPattern(pattern));
    }
//Previous method for 24 slots
/*
    public long ConvertToPatternByCombiningTwo(double []aRowInput)
    {
        double threshold,totalValue,lowestValue;
        totalValue=0;
        lowestValue=1;

        for(int i=0;i<24;i++)
        {
            if(lowestValue>aRowInput[i])
                    lowestValue=aRowInput[i];
            totalValue=totalValue+aRowInput[i];
        }

        threshold=totalValue/24-(totalValue/24-lowestValue)*.3;

        long aCodeWord=0;
        for(int i=0;i<24;i++)
        {
            aCodeWord=aCodeWord<<1;
            if(aRowInput[i]>threshold)
                aCodeWord=aCodeWord | 1;
        }
        return aCodeWord;
    }
*/
//actual by combining two

    public long ConvertToPatternByCombiningTwo(double []aRowInput )
    {
        double totalValue,threshold;
        totalValue=0;
        double[] aRowInputCopy = new double[aRowInput.length];
        System.arraycopy(aRowInput, 0, aRowInputCopy, 0, aRowInput.length);
        
        double aRowInputAvg = 0.0, aRowInputSum = 0.0;
        for(int i = 0; i < aRowInput.length; i++)
            aRowInputSum += aRowInput[i];
        
        aRowInputAvg = aRowInputSum / (double)aRowInput.length;
        
        int beta = GlobalData.BETA;
        int slotCount = GlobalData.slot_count;
        for(int i = 0; i < aRowInputCopy.length; i++)
        {
            double exactAv = 0.0;
            for(int j = 1; j < beta; j++)
            {
                exactAv += MathUtils.binomialCoefficientDouble(slotCount, j) * Math.pow(aRowInputAvg, j)
                        * Math.pow(1 - aRowInputAvg, slotCount - j);
            }
            aRowInputCopy[i] -= exactAv;
        }
        
        for(int i=0;i<24;i++)
        {
            totalValue += aRowInputCopy[i];
        }
        threshold=totalValue/24;//(totalValue/24-lowestValue)*.1;
        
        long aCodeWord=0;
        for(int i=0;i<24;i=i+2)
        {
            aCodeWord=aCodeWord<<2;
            if(aRowInputCopy[i]+aRowInputCopy[i+1]>=threshold*2)
                aCodeWord=aCodeWord | 3;
            else if(aRowInputCopy[i]+aRowInputCopy[i+1]>=threshold)
            {
                aCodeWord=aCodeWord | 2;
            }
        }
        return aCodeWord;
    }
    
    
    public long ConvertToPatternUsingStaticThesold(double []aRowInput )
    {
        long aCodeWord=0;
    /*    for(int i=0;i<24;i=i+1)
        {
              aCodeWord=aCodeWord<<1;
              if(aRowInput[i]>=.5)
                  aCodeWord=aCodeWord | 1;
          //  aCodeWord=aCodeWord<<2;
        //    if((aRowInput[i]+aRowInput[i+1])/2>=.5)
        //        aCodeWord=aCodeWord | 3;
         //   else if((aRowInput[i]+aRowInput[i+1])/2>=.2)
            {
         //       aCodeWord=aCodeWord | 2;
            }
        }
     *
     */
        double[] aRowInputCopy = new double[aRowInput.length];
        System.arraycopy(aRowInput, 0, aRowInputCopy, 0, aRowInput.length);
        
        double aRowInputAvg = 0.0, aRowInputSum = 0.0;
        for(int i = 0; i < aRowInput.length; i++)
            aRowInputSum += aRowInput[i];
        
        aRowInputAvg = aRowInputSum / (double)aRowInput.length;
        
        int beta = GlobalData.BETA;
        int slotCount = GlobalData.slot_count;
        for(int i = 0; i < aRowInputCopy.length; i++)
        {
            double exactAv = 0.0;
            for(int j = 1; j < beta; j++)
            {
                exactAv += MathUtils.binomialCoefficientDouble(slotCount, j) * Math.pow(aRowInputAvg, j)
                        * Math.pow(1 - aRowInputAvg, slotCount - j);
            }
            aRowInputCopy[i] -= exactAv;
        }
        
        for(int i=0;i<24;i=i+2)
        {
              aCodeWord=aCodeWord<<2;
              if((aRowInputCopy[i]+aRowInputCopy[i+1])/2>=.5)
                aCodeWord=aCodeWord | 3;
             else if((aRowInputCopy[i]+aRowInputCopy[i+1])/2>=.2)
            {
                 aCodeWord=aCodeWord | 2;
            }
        }
    
   
        return aCodeWord;
    }

   public long ConvertToPatternUsingStaticThesoldForGroup(double []aRowInput, int group_size )
    {
       long aCodeWord=0;
       double totalValue=0,threshold;
    /*   for(int i=0;i<24;i=i+2)
       {
            aCodeWord=aCodeWord<<2;
            if((aRowInput[i]+aRowInput[i+1])/2>=threshold1)
                aCodeWord=aCodeWord | 3;
            else if((aRowInput[i]+aRowInput[i+1])/2>=threshold2)
            {
                aCodeWord=aCodeWord | 2;
            }
       }
     *
     */ double[] aRowInputCopy = new double[aRowInput.length];
        System.arraycopy(aRowInput, 0, aRowInputCopy, 0, aRowInput.length);
        
        double aRowInputAvg = 0.0, aRowInputSum = 0.0;
        for(int i = 0; i < aRowInput.length; i++)
            aRowInputSum += aRowInput[i];
        
        aRowInputAvg = aRowInputSum / (double)aRowInput.length;
        
        int beta = GlobalData.BETA;
        int slotCount = GlobalData.slot_count;
        for(int i = 0; i < aRowInputCopy.length; i++)
        {
            double exactAv = 0.0;
            for(int j = 1; j < beta; j++)
            {
                exactAv += MathUtils.binomialCoefficientDouble(slotCount, j) * Math.pow(aRowInputAvg, j)
                        * Math.pow(1 - aRowInputAvg, slotCount - j);
            }
            aRowInputCopy[i] -= exactAv;
        }
        for(int i=0;i<24;i++)
        {
            totalValue=totalValue+aRowInputCopy[i];
        }
        threshold=totalValue/24;
        double threshold1=threshold*0.90;
        double threshold2=threshold*0.50;

    //   if(Long.bitCount(aCodeWord)==24) //if already 24 bit then need to reconsider the pattern
       /*
            if (group_size == 2)
            {
                threshold1=0.70;
                threshold2=0.50;
            }
            else if(group_size==3)
            {
                threshold1=0.80;
                threshold2=0.60;
            }
            else if(group_size>=4)
            {
                threshold1=0.90;
                threshold2=0.70;
            }
        *
        */
            aCodeWord=0;
            for(int i=0;i<24;i=i+2)
            {
                aCodeWord=aCodeWord<<2;
                if((aRowInputCopy[i]+aRowInputCopy[i+1])/2>=threshold1)
                    aCodeWord=aCodeWord | 3;
          //      else if((aRowInput[i]+aRowInput[i+1])/2>=threshold2)
                {
           //         aCodeWord=aCodeWord | 2;
                }
            }
        

          /*     else if(group_size>=6)
        {
            threshold1=0.95;
            threshold2=0.80;
        }

     /*   else if(group_size==5)
        {
            threshold1=0.85;
            threshold2=0.65;
        }
        else if(group_size==6)
        {
            threshold1=0.90;
            threshold2=0.70;
        }
        else if(group_size==7)
        {
            threshold1=0.95;
            threshold2=0.75;
        }
        */

        return aCodeWord;
    }

    void AddPattern(PlexusNode newNode)
    {        
        
        int totalNodeNumber=0;
        int numberOfBitOneInPattern=Long.bitCount(newNode.availabilityPattern);

        if(numberOfBitOneInPattern>GlobalData.max_pattern)
        {
                GlobalData.max_pattern=numberOfBitOneInPattern;
        }
        if(numberOfBitOneInPattern<GlobalData.min_pattern)
        {
                GlobalData.min_pattern=numberOfBitOneInPattern;
       }
        GlobalData.avg_pattern1+=numberOfBitOneInPattern;
        
        for(int i=0;i<4096;i++)
        {
            if(Math.abs(G24_codewords[i].numberOfBitOne-numberOfBitOneInPattern)<=maximumHammingDistance)
                if(G24_codewords[i].GetHammingDistance(newNode.availabilityPattern)<=maximumHammingDistance)
                {
                    G24_codewords[i].bucket.add(newNode);
                    totalNodeNumber++;
                }
        }
        totalNumberInsertedIntoBucket=totalNumberInsertedIntoBucket+totalNodeNumber;
  //      System.out.println("Generated pattern:"+newNode.availabilityPattern+" total number of close PlexusNode:"+totalNodeNumber+" average cost: "+newNode.GetCost(maximumHammingDistance)+" number of one in new bit pattern: "+numberOfBitOneInPattern);
    }
    
    void RemovePattern(PlexusNode nodeTobeRemoved)
    {
       int numberOfBitOneInPattern=Long.bitCount(nodeTobeRemoved.availabilityPattern);
        
        for(int i=0;i<4096;i++)
        {
            if(Math.abs(G24_codewords[i].numberOfBitOne-numberOfBitOneInPattern)<=maximumHammingDistance)
                if(G24_codewords[i].GetHammingDistance(nodeTobeRemoved.availabilityPattern)<=maximumHammingDistance)
                {
                    G24_codewords[i].Remove(nodeTobeRemoved);
                }
        } 
    }
    
    Vector SearchANode(PlexusNode nodeTobSearched)
    {
        Vector <Golay24CodeWord>patternHolders=new Vector<Golay24CodeWord>();
        
        int numberOfBitOneInPattern=Long.bitCount(nodeTobSearched.availabilityPattern);
        
        for(int i=0;i<4096;i++)
        {
            if(Math.abs(G24_codewords[i].numberOfBitOne-numberOfBitOneInPattern)<=maximumHammingDistance*2)
                if(G24_codewords[i].GetHammingDistance(nodeTobSearched.availabilityPattern)<=maximumHammingDistance*2)
                {
                    if(G24_codewords[i].IfBucketContains(nodeTobSearched))
                        patternHolders.add(G24_codewords[i]);
                }
        }
        return patternHolders;
    }
    
    
    ArrayList<PlexusNode> SearchAPattern(long patternToBeSearched, int HammingDistance)
    {
         ArrayList <PlexusNode>nodesInMinimumHammingDistanc=new ArrayList<PlexusNode>();
         int numberOfBitOneInPattern=Long.bitCount(patternToBeSearched);
         int maxHammingDistance=HammingDistance;
        
         for(int i=0;i<4096;i++)
         {
            if(Math.abs(G24_codewords[i].numberOfBitOne-numberOfBitOneInPattern)<=maxHammingDistance*2)
                if(G24_codewords[i].GetHammingDistance(patternToBeSearched)<=maxHammingDistance*2)
                {
                    ArrayList<PlexusNode> nodesInBucket=G24_codewords[i].GetNodeInMaximamHammingDistace(patternToBeSearched, maxHammingDistance);
                    
                    for(int j=0;j<nodesInBucket.size();j++)
                    {
                        /*boolean taken=true;
                        for(int k=0;k<nodesInMinimumHammingDistanc.size();k++)
                        {
                            if(nodesInBucket.get(j).nodeId==nodesInMinimumHammingDistanc.get(k).nodeId)
                            {
                                taken=false;
                                break;
                            }
                        }
                        if(taken)
                            nodesInMinimumHammingDistanc.add(nodesInBucket.get(j));*/
                        if(!nodesInBucket.get(j).taken)
                        {
                            nodesInMinimumHammingDistanc.add(nodesInBucket.get(j));
                            nodesInBucket.get(j).taken=true;
                        }
                    }
                }
         }
         
         for(int i=0;i<nodesInMinimumHammingDistanc.size();i++)
             nodesInMinimumHammingDistanc.get(i).taken=false;
         return nodesInMinimumHammingDistanc;
    }
    
    
  /*  void ModifyPatternOfANode(PlexusNode aNode,long modifiedPattern)
    {
        long nodeID=aNode.nodeId;
        RemovePattern(aNode);
        PlexusNode modifiedNode=new PlexusNode(nodeID,modifiedPattern);
        modifiedNode.nodeId=nodeID;
        AddPattern(aNode);
    }*/
    
    void ModifyPatternOfANode(SingleNode aSingleNode,SingleNode bSingleNode)
    {
        long previousPattern=aSingleNode.pattern;
        int numberOfBitOnePreviuosPattern=Long.bitCount(previousPattern);
        for(int i=0;i<4096;i++)
        {
            if(Math.abs(G24_codewords[i].numberOfBitOne-numberOfBitOnePreviuosPattern)<=maximumHammingDistance)
                if(G24_codewords[i].GetHammingDistance(previousPattern)<=maximumHammingDistance)
                {
                    G24_codewords[i].Remove(aSingleNode.getID(), previousPattern);
                }
        }
        //long modifiedPattern=ConvertToPatternByCombiningTwo(modifiedSlot);
        if(aSingleNode.getID()==39)
            System.out.println();
        long modifiedPattern=aSingleNode.pattern|bSingleNode.pattern;
    //    long modifiedPattern=ConvertToPatternUsingStaticThesoldForGroup(modifiedSlot, aSingleNode.grp_size);
        aSingleNode.pattern=modifiedPattern;
        PlexusNode newPlexusNode=new PlexusNode(aSingleNode,modifiedPattern);
        AddPattern(newPlexusNode);
    }

    void ModifyPatternOfANode(SingleNode aSingleNode,long modifiedPattern)
    {
        long previousPattern=aSingleNode.pattern;
        int numberOfBitOnePreviuosPattern=Long.bitCount(previousPattern);
        for(int i=0;i<4096;i++)
        {
            if(Math.abs(G24_codewords[i].numberOfBitOne-numberOfBitOnePreviuosPattern)<=maximumHammingDistance)
                if(G24_codewords[i].GetHammingDistance(previousPattern)<=maximumHammingDistance)
                {
                    G24_codewords[i].Remove(aSingleNode.getID(), previousPattern);
                }
        }
        //long modifiedPattern=ConvertToPatternByCombiningTwo(modifiedSlot);
  //      long modifiedPattern=ConvertToPatternUsingStaticThesold(modifiedSlot);
        aSingleNode.pattern=modifiedPattern;
        PlexusNode newPlexusNode=new PlexusNode(aSingleNode,modifiedPattern);
        AddPattern(newPlexusNode);
    }
    
    int GetNumberOfOneBitInAPattern(long aPattern)
    {
        return Long.bitCount(aPattern);
    }
    
    void CheckNumberOfOneBitInGolay24Code(int checkNumber)
    {
        int total=0;
        for(int i=0;i<4096;i++)
        {
            if(Long.bitCount(G24_codewords[i].codeWord)==checkNumber)
                total++;
        }
        System.out.println("number of 1 bit with "+checkNumber+" number is:"+total);
            
    }
    
    public PlexusNode GetComplementNode(PlexusNode rootNode,ArrayList<PlexusNode> nodeList)
    {
        long maximumXorValue=0;
        PlexusNode mirrorNode=null;
        for(int i=0;i<nodeList.size();i++)
        {
            long xorValue=rootNode.availabilityPattern ^ nodeList.get(i).availabilityPattern;
            if(Long.bitCount(xorValue)>maximumXorValue)
            {
                maximumXorValue=Long.bitCount(xorValue);
                mirrorNode=nodeList.get(i);
            }
        }
        return mirrorNode;
    }
    
    
    long GetComplementPattern(long pattern)
    {
        return (pattern^0xffffff);
    }
    void PrintPattern(long k)
    {
        System.out.println(Long.toBinaryString(k));
    }

    public long GetPartialComplementPattern(long pattern)
    {
        long partialComplemnt=0;
        int lastIndex;
        boolean isSpecialCase=false;
        int uptime=GlobalData.varied_uptime;

        if(Long.bitCount(pattern)>20)
            return pattern ^ 0xffffff;

        for(lastIndex=0;lastIndex<24 && (pattern & 1)!=1;lastIndex++)
        {
            pattern=pattern>>1;
            //partialComplemnt=partialComplemnt|(1<<lastIndex);
        }

        if(lastIndex>=uptime)
            isSpecialCase=false;
        else
        {
            if(pattern>=1<<(24-uptime))
                isSpecialCase=true;
            else
                isSpecialCase=false;
        }

        if(!isSpecialCase)
        {
            for(int i=lastIndex-1,j=0;j<uptime && i>=0;j++,i--)
            {
                partialComplemnt=partialComplemnt|(1<<i);
            }

            for(int i=23,j=0;j<uptime-lastIndex;i--,j++)
            {
                partialComplemnt=partialComplemnt | 1<<i;
            }
        }
        else
        {
            boolean bitOneFound=false;
            //int contigiousOneEndIndex;
            pattern=pattern<<lastIndex;
            int maxZeroNum=0,MaxZeroIndex=0;
            int index,lastOneBitIndex=0;
            for(index=1;index<=24;index++)
            {
                long result=((1<<(24-index)) & pattern)>>(24-index);

                if(result==1)
                    lastOneBitIndex=index;
                else
                {
                    if(index-lastOneBitIndex>maxZeroNum)
                    {
                        MaxZeroIndex=lastOneBitIndex;
                        maxZeroNum=index-lastOneBitIndex;
                    }
                    if(index-lastOneBitIndex>=uptime)
                    {
                        break;
                    }
                }
            }

            if(index<=24)
            {
                for(int i=lastOneBitIndex+1,j=0;j<uptime;j++,i++)
                {
                    partialComplemnt=partialComplemnt|(1<<(24-i));
                }
            }
            else
            {
                for(int i=MaxZeroIndex,j=0;j<uptime && i<=24;i++,j++)
                {
                    partialComplemnt=partialComplemnt|(1<<(24-i));
                }


                for(int i=23,j=0;j<MaxZeroIndex-24+uptime;i--,j++)
                {
                    partialComplemnt=partialComplemnt|(1<<i);
                }
            }

        }
        return partialComplemnt;
    }


   /*
    long GetPartialComplementPattern(long pattern)
    {
        long partialComplemnt=0;
        int lastIndex;
        boolean isSpecialCase=false;
        
        if(Long.bitCount(pattern)>12)
            return pattern ^ 0xffffff;
        
        for(lastIndex=0;lastIndex<24 && (pattern & 1)!=1;lastIndex++)
        {
            pattern=pattern>>1;
            //partialComplemnt=partialComplemnt|(1<<lastIndex);
        }
        
        if(lastIndex>=8)
            isSpecialCase=false;
        else
        {
            if(pattern>=1<<16)
                isSpecialCase=true;
            else
                isSpecialCase=false;
        }
        
        if(!isSpecialCase)
        {
            for(int i=lastIndex-1,j=0;j<8 && i>=0;j++,i--)
            {
                partialComplemnt=partialComplemnt|(1<<i);
            }

            for(int i=23,j=0;j<8-lastIndex;i--,j++)
            {
                partialComplemnt=partialComplemnt | 1<<i;
            }
        }
        else
        {
            boolean bitOneFound=false;
            //int contigiousOneEndIndex;
            pattern=pattern<<lastIndex;
            int maxZeroNum=0,MaxZeroIndex=0;
            int index,lastOneBitIndex=0;
            for(index=1;index<=24;index++)
            {
                long result=((1<<(24-index)) & pattern)>>(24-index);

                if(result==1)
                    lastOneBitIndex=index;
                else
                {
                    if(index-lastOneBitIndex>maxZeroNum)
                    {
                        MaxZeroIndex=lastOneBitIndex;
                        maxZeroNum=index-lastOneBitIndex;
                    }
                    if(index-lastOneBitIndex>=8)
                    {
                        break;
                    }
                }
            }
            
            if(index<=24)
            {
                for(int i=lastOneBitIndex+1,j=0;j<8;j++,i++)
                {
                    partialComplemnt=partialComplemnt|(1<<(24-i));
                }
            }
            else
            {
                for(int i=MaxZeroIndex,j=0;j<8 && i<=24;i++,j++)
                {
                    partialComplemnt=partialComplemnt|(1<<(24-i));
                }
                
                
                for(int i=23,j=0;j<MaxZeroIndex-16;i--,j++)
                {
                    partialComplemnt=partialComplemnt|(1<<i);
                }
            }
            
        }
        return partialComplemnt;
    }

    *
    */

    String Get24BitsString(long aCodeWord)
    {
        
        String str=Long.toBinaryString(aCodeWord);
        if(str.length()<=24)
        {
            char []aSubsStr=new char[24-str.length()];
            for(int i=0;i<24-str.length();i++)
            {
                aSubsStr[i]='0';
            }
            String as=new String(aSubsStr);
            str=as.concat(str);
        }
        return str;
    }

    double getIndexPerNode( )
    {
        int total=0;
        int freq[]= new int[20];
        int maximum=0;
        int minimum=2000;
        int zerocount=0;

        for(int i=0;i<4096;i++)
        {
            total+= G24_codewords[i].bucket.size();

            if(G24_codewords[i].bucket.size()>maximum)
            {
                maximum=G24_codewords[i].bucket.size();
            }
           if(G24_codewords[i].bucket.size()<minimum)
            {
                minimum=G24_codewords[i].bucket.size();
            }
           if(G24_codewords[i].bucket.size()==0)
            {
                zerocount++;
            }

            if(G24_codewords[i].bucket.size()!=0)
            {
              System.out.println();
            }

        }
        System.out.println("MAximum index: "+maximum+ " MAximum index: "+minimum+ " zero: "+zerocount);
        double avg=(double)total/4096;
        return avg;
    }
}



