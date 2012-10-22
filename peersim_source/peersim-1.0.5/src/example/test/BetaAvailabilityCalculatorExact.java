package example.test;


public class BetaAvailabilityCalculatorExact implements BetaAvailabilityCalculator {

	@Override
	public double calculateBetaAvailability(double[] availability, int beta) {
		// TODO Auto-generated method stub
		if( availability.length < beta ) return 0.0 ;
		double[] multiplierVector = new double[availability.length];

		for( int  i = 0 ; i < multiplierVector.length ; i++ )
		{
			multiplierVector[i] = availability[i]/(1-availability[i]);
		}

		double root = 1 ;
		for( int i = 0 ; i < availability.length ; i++  ) root *= (1-availability[i]);
		double sum = 0;
		for( int i = 1 ; i < beta ; i++ )
		{
			//System.out.println("DEBUUG: "+availability.length+" "+i);
			CombinationGenerator generator = new CombinationGenerator(availability.length, i);

			while( generator.hasMore() )
			{
				int[] combination = generator.getNext();

				double val = root ;

				for( int j = 0 ; j < combination.length ; j ++  )
				{
					val *= multiplierVector[combination[j]];
				}
				sum += val ;

			}
		}

		double av = 1 ;
		for( int i = 0 ; i < availability.length ; i++)
		{
			av *= (1 - availability[i]);
		}
		av = 1 - av ;

		return av - sum ;
	}

}
