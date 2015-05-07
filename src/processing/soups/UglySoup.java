package processing.soups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.springframework.util.Assert;

import processing.Match;
import processing.Matcher;

import com.google.common.collect.Sets;

import core.Strand;

public class UglySoup extends Soup {

	private static class State extends HashMap<Strand, Integer> {
		private static final long serialVersionUID = -2849521057395628031L;
		private static int indexGen = 0;
		private int index = -42;
		private boolean visited = false;

		public State identify() {
			this.index = indexGen++;
			return this;
		}

		public int getNumberOfStrands(Strand s) {
			Integer n = this.get(s);
			try {
				Assert.isTrue(n == null || n > 0);
			} catch (Exception e) {
				throw new IllegalStateException("Invalid number of strands : "
						+ n);
			}
			if (n == null)
				return 0;
			else
				return n;
		}

		public void addStrands(Strand s, int n) {
			if (n < 0)
				throw new IllegalArgumentException(
						"Number of strands to add should be positive, not " + n);
			Integer current = this.get(s);
			if (current == null)
				current = 0;
			current += n;
			this.put(s, current);
		}

		public void setStrands(Strand s, int n) {
			if (n <= 0)
				throw new IllegalArgumentException(
						"Number of strands should be strictly positive, not "
								+ n);
			this.put(s, n);
		}

		@Override
		public Integer put(Strand key, Integer value) {
			Assert.isTrue(value >= 0);
			return super.put(key, value);
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();

			builder.append("\nState " + index);
			builder.append("\n\nContains : ");
			for (Strand str : keySet()) {
				builder.append("\n" + getNumberOfStrands(str) + " x " + str);
			}

			return builder.toString();
		}

		boolean hasEnoughForMatch(Strand str1, Strand str2) {

			// If state has 0 strands of type x, x should not be listed
			Assert.isTrue(getNumberOfStrands(str1) > 0);
			Assert.isTrue(getNumberOfStrands(str2) > 0);

			if (str1.equals(str2))
				return getNumberOfStrands(str1) >= 2;
			else
				return getNumberOfStrands(str1) >= 1
						&& getNumberOfStrands(str2) >= 1;

		}
	}

	private State currentState = new State().identify();
	private DirectedGraph<State, Match> stateSet = new DirectedAcyclicGraph<State, Match>(
			Match.class);

	public UglySoup() {
		this(null, 0);
	}

	public UglySoup(Matcher m, int n_iter) {
		super(m, n_iter);
		this.stateSet.addVertex(currentState);

	}

	public void add(String s, int toAdd) {
		currentState.addStrands(new Strand(s), toAdd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void stepImpl() throws Exception {
//		System.out.println("New step");
		boolean changedState = false;
		for (State s : stateSet.vertexSet()) {
//			System.out.println("Found state " + s.index);
			if (!s.visited) {
//				System.out.println("\tVisiting " + s.index);
				this.currentState = s;
				changedState = true;
				break;
			}
//			System.out.println("\tNot visiting " + s.index);

		}
		if (!changedState) {
			finish();
			return;
		}

		// Could assert state is valid

		stateSet.addVertex(currentState);
		Assert.isTrue(stateSet.outgoingEdgesOf(currentState).isEmpty());

		Set<Strand> strands = this.currentState.keySet();
		Set<List<Strand>> pairs = Sets.cartesianProduct(strands, strands);
		for (List<Strand> pair : pairs) {
			Assert.isTrue(pair.size() == 2);
			Strand str1 = pair.get(0), str2 = pair.get(1);

			if (!currentState.hasEnoughForMatch(str1, str2))
				continue;

			matcher.setStrands(str1, str2);
			matcher.run();
			for (Match m : matcher.retrieveMatches()) {
//				System.out.println("Doing : ");
//				System.out.println("\n" + m.toString() + "\n");
				stateFromMatch(m);
			}

		}

		currentState.visited = true;
	}

	private void stateFromMatch(Match m) throws Exception {

		Assert.isTrue(currentState.get(m.str1) >= 0
				&& currentState.get(m.str2) >= 0);

		Map<Strand, Integer> deltas = new HashMap<Strand, Integer>();

		// Makes new state copy of currentState
		for (Strand str : currentState.keySet()) {
			deltas.put(str, 0);
		}

		// Remove strands used for match
		if (m.str1.equals(m.str2)) {
			deltas.put(m.str1, -2);
		} else {
			deltas.put(m.str1, -1);
			deltas.put(m.str2, -1);
		}

		// Add product strands
		for (Strand p : m.getProducts()) {

			if (p == null)
				continue;

			Integer delta = deltas.get(p);
			if (delta == null)
				delta = 0;
			deltas.put(p, delta + 1);
		}

		State s = new State();
		for (Strand str : deltas.keySet()) {

			int delta = deltas.get(str);
			int n = currentState.getNumberOfStrands(str) + delta;

			if (n > 0)
				s.setStrands(str, n);
		}

//		System.out.println("Created state : ");
//		System.out.println(s.toString());

		if (stateSet.addVertex(s)) {
//			System.out.println("Confirmed state is new");
			s.identify();
		} else {
			boolean foundMatch = false;
			for (State existing : stateSet.vertexSet()) {
				if (s.equals(existing)) {
					s = existing;
					foundMatch = true;
					break;
				}

			}
			Assert.isTrue(foundMatch);

		}
		stateSet.addEdge(currentState, s, m);

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (State s : stateSet.vertexSet()) {
			builder.append("\n\n");
			builder.append(s.toString());

			builder.append("\n\nTransitions : ");
			for (Match m : stateSet.outgoingEdgesOf(s)) {
				builder.append("\n\n" + m.str1 + " + " + m.str2 + " -> State "
						+ stateSet.getEdgeTarget(m).index);
			}
		}

		return builder.toString();
	}
}