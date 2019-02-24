package de.sprachfaul.evemanager.server.logic;

public class ExtractorYieldCalculator {

	/**
	 * Quelle: https://wiki.eveonline.com/en/wiki/Equations_Industry Dogma
	 * Attribute: SELECT * FROM eve.dgmAttributeTypes where attributeID
	 * IN(1683,1687)
	 */
	public static int[] calculateExtractorYield(int cycleTime, int quantityPerCycle, int numIterations) {
		// These constants are the defaults in dgmAttributeTypes. They may
		// change. SELECT * FROM eve.dgmAttributeTypes where attributeID
		// IN(1683,1687)
		final float decayFactor = 0.012f; // Dogma attribute 1683 for this pin typeID
		final float noiseFactor = 0.8f; // Dogma attribute 1687 for this pin typeID

		float barWidth = cycleTime / 900f;
		int[] values = new int[numIterations];

		for (int i = 0; i < numIterations; i++) {
			float t = (i + 0.5f) * barWidth;
			float decayValue = quantityPerCycle / (1 + t * decayFactor);
			double phaseShift = Math.pow(quantityPerCycle, 0.7f);

			double sinA = Math.cos(phaseShift + t * (1 / 12f));
			double sinB = Math.cos(phaseShift / 2 + t * 0.2f);
			double sinC = Math.cos(t * 0.5f);

			double sinStuff = (sinA + sinB + sinC) / 3;
			sinStuff = Math.max(sinStuff, 0);

			double barHeight = decayValue * (1 + noiseFactor * sinStuff);

			values[i] = (int) (barWidth * barHeight);
		}
		return values;
	}
	
}
