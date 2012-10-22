/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example.test;

/**
 *
 * @author shihab
 */
public class PeerComparator implements Comparable
{

    private SingleNode peer;
    private double contribution;
    private int knownIndex;

    public PeerComparator()
    {
        super();
        this.peer = null;
        this.contribution = 0.0;
        this.knownIndex = -1;
    }

    public PeerComparator(SingleNode peer, double contribution, int knownIndex)
    {
        super();
        this.peer = peer;
        this.contribution = contribution;
        this.knownIndex = knownIndex;
    }

    SingleNode getPeer()
    {
        return this.peer;
    }

    void setPeer(SingleNode peer)
    {
        this.peer = peer;
    }

    double getContribution()
    {
        return this.contribution;
    }

    void setContribution(double contribution)
    {
        this.contribution = contribution;
    }

    int getKnownIndex()
    {
        return this.knownIndex;
    }

    void setKnownIndex(int index)
    {
        this.knownIndex = index;
    }

    public int compareTo(Object o)
    {
        PeerComparator p2 = (PeerComparator) o;

        if (this.contribution < p2.contribution)
        {
            return 1;
        }
        else if (this.contribution > p2.contribution)
        {
            return -1;
        }
        return 0;
    }
}