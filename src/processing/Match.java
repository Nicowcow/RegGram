package processing;

import core.Strand;

public final class Match {

	public final Strand str1;
	public final Strand str2;
	public final int pos1;
	public final int pos2back;
	public final int length;

	/**
	 * 
	 * 
	 * 
	 * <pre>
	 *         -1-				
	 *                			
	 *      ^         5			
	 *      3         |
	 *      |         o
	 *      o        o
	 *       o      o
	 * 		  o    o
	 * ________oHHo__________
	 * 	       oHHo
	 * 	      o	   o
	 * 	      |     o
	 * 	      5      o
	 * 				 |
	 * 				 3
	 * 				 v
	 * 
	 * 			-2-
	 * 
	 * </pre>
	 * 
	 */

	public Match(Strand str1, Strand str2, int pos1, int pos2back, int length)
			throws Exception {

		if (pos1 < 1 || pos2back < 1) {
			throw new Exception("Index cannot be < 1");
		}

		if (pos1 > str1.getLength() || pos2back > str2.getLength()) {
			throw new Exception("Index cannot be > strand length\n Str1:"
					+ str1.getLength() + ", str2:" + str2.getLength()
					+ ", idx1:" + pos1 + ", idx2:" + pos2back);
		}

		if (pos1 + length - 1 > str1.getLength()
				|| pos2back + length - 1 > str2.getLength()) {
			throw new Exception(
					"Index + match length cannot be > strand length");

		}

		this.str1 = str1;
		this.str2 = str2;
		this.pos1 = pos1;
		this.pos2back = pos2back;
		this.length = length;
	}

	// TODO use flip instead
	public Strand[] getProducts() throws Exception {
		int D = 2;
		Strand s1, s2;
		int start1 = 1, stop1 = pos1 - 1;
		int start2 = str2.getLength() - pos2back + 2, stop2 = str2.getLength();
		int l1 = stop1 - start1 + 1, l2 = stop2 - start2 + 1;
		if (l1 == 0 && l2 == 0)
			s1 = null;
		else if (l1 < 0 || l2 < 0) {
			throw new Exception("Length should not be negative! (1)");
		} else {
			s1 = new Strand(str1, start1, stop1, str2, start2, stop2);
		}

		start2 = 1;
		stop2 = str2.getLength() - pos2back - length - 1 + D;
		start1 = pos1 + length;
		stop1 = str1.getLength();

		l1 = stop1 - start1 + 1;
		l2 = stop2 - start2 + 1;
		if (l1 == 0 && l2 == 0)
			s2 = null;
		else if (l1 < 0 || l2 < 0) {
			throw new Exception("Length should not be negative!(2)" + l1 + ", "
					+ l2);
		} else
			s2 = new Strand(str2, start2, stop2, str1, start1, stop1);

		return new Strand[] { s1, s2 };
	}

	@Override
	public String toString() // Ugly
	{

		// System.out.println("Showing match : " + this.pos1 + ", " +
		// this.pos2back + ", " + this.length);
		int leftPadding1 = 0;
		int leftPadding2 = 0;

		if (this.pos1 > this.pos2back)
			leftPadding2 = this.pos1 - this.pos2back;
		else
			leftPadding1 = this.pos2back - this.pos1;

		int maxPos = Math.max(pos1, pos2back);

		// Str 1
		StringBuilder builder = new StringBuilder();

		// builder.append("First : " + pos1 + "-"+(pos1 + length - 1) + "\n");
		// builder.append("Second : " + pos2back + "-" + (pos2back + length -
		// 1)+ "\n");
		for (int i = 0; i < leftPadding1; i++) // 3* : a,], ,
		{
			builder.append("   ");
		}
		builder.append(str1);
		builder.append('\n');

		// Bridge
		for (int i = 0; i < (maxPos - 1); i++) {
			builder.append("   ");
		}
		for (int i = 0; i < length; i++) {
			builder.append(" | ");
		}

		builder.append('\n');

		// Str 2
		for (int i = 0; i < leftPadding2; i++) {
			builder.append("   ");
		}
		builder.append(str2.toString(true));
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		try {
			Match m = (Match) o;
			Match flipM = m.flip();
			if (m.length == this.length) {

				if(m.pos1 == this.pos1 && m.pos2back == this.pos2back &&
						m.str1.equals(this.str1) && m.str2.equals(this.str2))
					return true;
				
				if(flipM.pos1 == this.pos1 && flipM.pos2back == this.pos2back &&
						flipM.str1.equals(this.str1) && flipM.str2.equals(this.str2))
					return true;
				
			}

		} catch (Exception e) {

		}

		return false;
	}

	@Override
	public int hashCode() {
		int c = 37;
		int result = Integer.valueOf(this.pos1 + this.pos2back).hashCode();
		result = c * Integer.valueOf(length).hashCode() + result;
		result = c * (str1.hashCode() + str2.hashCode()) + result;
		return result;
	}
	/**
	 * Creates a Match object equivalent (and equal) to this one, with
	 * switched strands (str1 becomes flipStr2, and str2 becomes flipStr1)
	 * 
	 * 
	 * In case of match length l we have:
	 * \t pos1 + flipPos2Back + l = l1 + 2
	 * \t pos2back + flipPos1 + l = l2 + 2
	 * 
	 * @return
	 */
	public Match flip() throws Exception
	{
		Strand flipStr1 = this.str2;
		Strand flipStr2 = this.str1;
		int flipPos1 = str2.getLength() + 2 - length - pos2back;
		int flipPos2Back = str1.getLength() + 2 - length - pos1;
		int flipLength = this.length;
		
		
		return new Match(flipStr1, flipStr2, flipPos1, flipPos2Back, flipLength);
	}
}
