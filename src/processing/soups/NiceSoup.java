package processing.soups;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.util.Assert;

import processing.Match;
import processing.Matcher;
import core.Strand;

public class NiceSoup extends Soup {

	private List<Set<Strand>> soup = new ArrayList<Set<Strand>>();
	private Set<Strand> pool = new HashSet<Strand>();
	private Set<Strand> products = new HashSet<Strand>();
	private boolean hasStarted = false;
	private boolean verbose = false;

	// FIXME missing strands
	public NiceSoup(Matcher matcher, int i) {
		super(matcher,i);
	}

	public NiceSoup verbose() {
		this.verbose = true;
		return this;
	}

	public void add(Strand s) {
		this.products.add(s);
	}

	public void add(String s) {
		this.add(new Strand(s));
	}

	/**
	 * Moves the latest products to the soup, copies them to the new pool, and
	 * creates a set for the new ones.
	 */
	private boolean moveToNextState() {
		Assert.state(this.pool.isEmpty());

		if (this.products.isEmpty()) {
			this.finish();
			System.out.println(this);
			return false;
		}
		this.soup.add(products);
		this.pool.addAll(this.products);
		this.products = new HashSet<Strand>();

		return true;
	}

	private void initialize() {
		this.moveToNextState();
		this.hasStarted = true;
	}

	/**
	 * Take a strand from the current pool and tests it against all the strands
	 * already existing in the soup, adding the products to the products set
	 * 
	 * @throws Exception
	 */
	public void stepImpl() throws Exception {
		
		if(verbose) System.out.println("\n\n---Iteration " + (n_iter+1)+"---");
		if(verbose) System.out.println("-----------------");

		if (!hasStarted)
			this.initialize();

		if (pool.isEmpty() && !moveToNextState()) {
			System.out.println("Done.");
			this.finish();
			return;
		}

		Iterator<Strand> it = pool.iterator();
		Strand poolStrand = it.next();
		it.remove();
		for (Set<Strand> pastSet : soup) {
			for (Strand s2 : pastSet) {
				if (verbose) {
					System.out.println("----------\n---<Matching> : ---");
					System.out.println(poolStrand);
					System.out.println(s2);
				}
				matcher.setStrands(poolStrand, s2);
				matcher.run();
				Match[] matches = matcher.retrieveMatches();
				for (Match match : matches) {
					Strand[] producedByMatch = match.getProducts();
					if(verbose) System.out.println("\n------<Binding> : ---");
					if(verbose) System.out.println(match);

					for (Strand p : producedByMatch) {
						if (verbose && p!= null)
							System.out.println("Product : " + p);
						if (p != null && !existsInSoup(p)) {
							System.out.println("Added to soup");
							this.products.add(p);
						}
					}
				}
			}
		}
		if(verbose) System.out.println("-----------------");
		if(verbose) System.out.println("-----------------");

	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Growth : \n");
		int total = 0;
		for (Set<Strand> set : soup) {
			total += set.size();
			builder.append(total + " ");
		}

		return builder.toString();

	}

	private boolean existsInSoup(Strand s) {
		for (Set<Strand> pastSet : soup) {
			if (pastSet.contains(s))
				return true;
		}
		return false;
	}

	public Strand[] getCurrentStrands() {
		Set<Strand> currentSoup = new HashSet<Strand>();
		for (Set<Strand> set : soup) {
			currentSoup.addAll(set);
		}
		Strand[] currentSoupArr = new Strand[currentSoup.size()];
		return currentSoup.toArray(currentSoupArr);
	}
}
