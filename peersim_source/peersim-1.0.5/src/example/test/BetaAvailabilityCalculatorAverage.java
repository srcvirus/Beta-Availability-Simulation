package example.test;

import org.apache.commons.math.util.MathUtils;

public class BetaAvailabilityCalculatorAverage implements BetaAvailabilityCalculator
{

    @Override
    public double calculateBetaAvailability(double[] availability, int beta)
    {
        // TODO Auto-generated method stub
        return betaAvailabilityAverage(availability, beta);
    }
    
    public double calculateBetaAvailability(double[] groupAvailability, int beta, int slot)
    {
        double ret = 0.0;
        
        double exactAv = 0.0;
        
        for (int i = 1; i < beta; i++)
        {
            exactAv += MathUtils.binomialCoefficientDouble(groupAvailability.length, i) * Math.pow(groupAvailability[slot], i)
                    * Math.pow(1 - groupAvailability[slot], groupAvailability.length - i);
        }
        
        double atleastOneAv = groupAvailability[slot];
        
        ret = atleastOneAv - exactAv;
        return ret;
    }
    

    private double betaAvailabilityAverage(double[] avail, int beta)
    {
        /**
         * if number of peers is less than beta, return 0
         */
        if (avail.length < beta)
        {
            return 0.0;
        }
        //System.out.println("Otherwise");
        double avgAvail = 0;
        /**
         * finding the average availability of the peers
         */
        for (int i = 0; i < avail.length; i++)
        {
            avgAvail += avail[i];
        }
        avgAvail /= avail.length;

        double availability = 0;
        /**
         * Calculating the sum of exactly i peers available
         */
        for (int i = 1; i < beta; i++)
        {
            availability += MathUtils.binomialCoefficientDouble(avail.length, i) * Math.pow(avgAvail, i)
                    * Math.pow(1 - avgAvail, avail.length - i);
        }
        /**
         * calculating at-least one peer is available
         */
        double av = 1;
        for (int i = 0; i < avail.length; i++)
        {
            av *= (1 - avail[i]);
        }
        av = 1 - av;

        /**
         * returning the value of probability that at-least "beta" peers are
         * online
         */
        return av - availability;
    }
}
