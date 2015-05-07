package processing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.Strand;
import core.StrandReader;
import core.Symbol;

/**
 * A Matcher object is a Runnable which can be configured for different types of
 * strand matches
 * 
 * @author cowcow
 *
 */
public class Matcher implements Runnable {
	
	static class MatchData
	{
		int occurences = 0;
		boolean containsUncovered = false;
	}
	
	public static enum LiftRegion
	{
		THREE,
		FIVE,
		THREE_AND_FIVE,
		EVERYWHERE
	}
	
	

	private Strand str1;
	private Strand str2;

	private SparseMatrix<MatchData> matchMatrix;
	private boolean useCovers = false;
	private final List<Symbol> uncovered = new ArrayList<Symbol>();
	private final List<Symbol> threeUncovered = new ArrayList<Symbol>();
	private final List<Symbol> fiveUncovered = new ArrayList<Symbol>();


	public Matcher useCovers() {
		this.useCovers = true;
		return this;
	}

	public Matcher lift(LiftRegion reg, Symbol... symbols) {
		if(reg == LiftRegion.EVERYWHERE)
		for (Symbol sym : symbols) {
			uncovered.add(sym);
		}
		
		if(reg == LiftRegion.FIVE || reg == LiftRegion.THREE_AND_FIVE)
		{
			for(Symbol sym : symbols)
			{
				fiveUncovered.add(sym);
			}
		}
		
		if(reg == LiftRegion.THREE || reg == LiftRegion.THREE_AND_FIVE)
		{
			for(Symbol sym : symbols)
			{
				threeUncovered.add(sym);
			}
		}

		return this;
	}
	
	
	private boolean isCovered(Strand str1, Strand str2, int idx1, int idx2back, Symbol s1, Symbol s2)
	{
		boolean s1IsUncovered = uncovered.contains(s1) ||
				(threeUncovered.contains(s1) && idx1 == str1.getLength()) ||
				(fiveUncovered.contains(s1) && idx1 == 1);
		
		boolean s2IsUncovered = uncovered.contains(s2) ||
				(threeUncovered.contains(s2) && idx2back == 1) ||
				(fiveUncovered.contains(s2) && idx2back == str2.getLength());	
		
		boolean bothUncovered = s1IsUncovered && s2IsUncovered;
		
		return !bothUncovered;
	}

	@Override
	public void run() {
		this.matchMatrix = new SparseMatrix<MatchData>();
		Iterator<Symbol> it1 = new StrandReader(str1).iterator();
		int idx1 = 0;
		while (it1.hasNext()) {

			Symbol s1 = it1.next();
			idx1++;
			Iterator<Symbol> it2back = new StrandReader(str2).reverseIterator();
			int idx2back = 0;

			while (it2back.hasNext()) {
				Symbol s2 = it2back.next();
				idx2back++;

				if (s1.binds(s2)) {
					boolean covered = useCovers
							&& this.isCovered(str1, str2, idx1, idx2back, s1,
									s2);
					
					
					
					registerMatch(idx1, idx2back, covered);
				}
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void registerMatch(int pos1, int pos2back, boolean covered) {
		MatchData val = this.matchMatrix.erase(pos1 - 1, pos2back - 1);
		if(val == null)
			val = new MatchData();
		
		val.occurences++;
		val.containsUncovered |= !covered;
		this.matchMatrix.put(pos1, pos2back, val);
	}

	public Match[] retrieveMatches() throws Exception { 
		Iterator<SparseMatrix<MatchData>.Entry<MatchData>> it = this.matchMatrix
				.iterator();

		List<Match> matches = new ArrayList<Match>();

		while (it.hasNext()) {
			SparseMatrix<MatchData>.Entry<MatchData> e = it.next();
			if(!e.value.containsUncovered)
				continue;
			
			int comboLength = e.value.occurences;
			int comboStartIdxStr1 = e.row - e.value.occurences + 1;
			int comboStartIdxStr2 = e.col - e.value.occurences + 1;

			matches.add(new Match(str1, str2, comboStartIdxStr1,
					comboStartIdxStr2, comboLength));
		}
		Match[] matchesArr = new Match[matches.size()];
		matches.toArray(matchesArr);
		return matchesArr;
	}

	public Matcher setStrands(Strand str1, Strand str2) {
		this.str1 = str1;
		this.str2 = str2;
//		System.out.println("MATCHER STRANDS : " + str1 + ", " + str2);
		return this;
	}
}
