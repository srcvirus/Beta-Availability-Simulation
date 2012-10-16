/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Sourov
 */
public class ReadFileAndAddDataToBucket
{
    Scanner fileScanner;
    int totalInputRow;
    Golay24Code golayCode;
    
    ReadFileAndAddDataToBucket(String inputFileName,int totalInputNumber)
    {
        golayCode=Golay24Code.getInstance();
        
        try
        {
            fileScanner=new Scanner(new File(inputFileName));
        } 
        catch (FileNotFoundException ex) 
        {
        }
        totalInputRow=totalInputNumber;
    }
    
    
    double[] ReadARowWithoutIndex()
    {
        double[] aRowInput=new double[24];
        for(int i=0;i<24;i++)
        {
            aRowInput[i]=fileScanner.nextDouble();
        }
        return aRowInput;
    }
    
    double[] ReadARowWithIndex()
    {
        double[] aRowInput=new double[24];
        for(int i=0;i<25;i++)
        {
            if(i==0)
            {
                fileScanner.nextDouble();
                continue;
            }
            aRowInput[i-1]=fileScanner.nextDouble();
        }
        return aRowInput; 
    }
    
    void ReadFromFilePreviousMethod(boolean ifRowHasIndex)
    {
        double totalValue;
        double[] aRowInput=new double[24];
        double averageNumberOoneBit;
        int[] numberOfOneBitInAllPattern=new int[totalInputRow]; 
        int totalNumberOfOne=0;
            
        for(int j=0;j<totalInputRow;j++)
        {
            if(ifRowHasIndex)
                aRowInput=ReadARowWithIndex();
            else
                aRowInput=ReadARowWithoutIndex();
            
            long aCodeWord=ConvertByPreviousMethod(aRowInput);
            golayCode.AddPattern(new PlexusNode(j,aCodeWord));

            numberOfOneBitInAllPattern[j]=Long.bitCount(aCodeWord);
            totalNumberOfOne=totalNumberOfOne+numberOfOneBitInAllPattern[j];
        }
        
        averageNumberOoneBit=(double)totalNumberOfOne/(double)totalInputRow;
        //System.out.println("average number inserted into bucket:" +(float)totalNumberInsertedIntoBucket/(float)4096);
        totalValue=0;
        for(int i=0;i<totalInputRow;i++)
        {
            totalValue=totalValue+(averageNumberOoneBit-numberOfOneBitInAllPattern[i])*(averageNumberOoneBit-numberOfOneBitInAllPattern[i]);
        }
        System.out.println("average value: "+averageNumberOoneBit+" varriance: " +totalValue/totalInputRow);
    }
    
    long ConvertByPreviousMethod(double []aRowInput)
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
    
    
    void ReadFromFileByCombiningTwo(boolean ifRowHasIndex)
    {
        double totalValue;
        double[] aRowInput=new double[24];
        double averageNumberOoneBit;
        int[] numberOfOneBitInAllPattern=new int[totalInputRow]; 
        int totalNumberOfOne=0;
            
        for(int j=0;j<totalInputRow;j++)
        {
            if(ifRowHasIndex)
                aRowInput=ReadARowWithIndex();
            else
                aRowInput=ReadARowWithoutIndex();
            
            long aCodeWord=ConvertToPatternByCombiningTwo(aRowInput);
            golayCode.AddPattern(new PlexusNode(j,aCodeWord));
            
            numberOfOneBitInAllPattern[j]=Long.bitCount(aCodeWord);
            totalNumberOfOne=totalNumberOfOne+numberOfOneBitInAllPattern[j];
        }

        averageNumberOoneBit=(double)totalNumberOfOne/(double)totalInputRow;
        //System.out.println("average number inserted into bucket:" +(float)totalNumberInsertedIntoBucket/(float)4096);
        totalValue=0;
        for(int i=0;i<totalInputRow;i++)
        {
            totalValue=totalValue+(averageNumberOoneBit-numberOfOneBitInAllPattern[i])*(averageNumberOoneBit-numberOfOneBitInAllPattern[i]);
        }
        System.out.println("average value: "+averageNumberOoneBit+" varriance: " +totalValue/totalInputRow);
    }
    
    public long ConvertToPatternByCombiningTwo(double []aRowInput )
    {
        double totalValue,threshold;
        totalValue=0;
        for(int i=0;i<24;i++)
        {
            totalValue=totalValue+aRowInput[i];
        }
        threshold=totalValue/24;//(totalValue/24-lowestValue)*.1;

        long aCodeWord=0;
        for(int i=0;i<24;i=i+2)
        {
            aCodeWord=aCodeWord<<2;
            if(aRowInput[i]+aRowInput[i+1]>=threshold*2)
                aCodeWord=aCodeWord | 3;
            else if(aRowInput[i]+aRowInput[i+1]>=threshold)
            {
                aCodeWord=aCodeWord | 2;
            }
        }
        return aCodeWord;
    }
}


