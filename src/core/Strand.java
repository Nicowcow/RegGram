package core;

import java.util.ArrayList;
import java.util.Iterator;

public final class Strand {

	private static ArrayList<Node> createNodesFromIndices(Strand str,
			int start, int stop) throws Exception {
		Iterator<Symbol> it = new StrandReader(str).iterator();
		ArrayList<Node> nodes = new ArrayList<Node>();

		for (int index = 1; index <= stop; index++) {
			Symbol s = it.next();
			if(s == null)
			{
				throw new Exception("Symbol should not be null!");
			}
			if (!(index >= start && index <= stop))// Lapalissade
				continue;

			if (s.isNegative) {
				nodes.add(new SignNode(new SymbolNode(s.symbol)));
			} else {
				nodes.add(new SymbolNode(s.symbol));
			}
		}

		return nodes;
	}

	private final Node symbols;

	/**
	 * Creates a new strand from a <tt>String</tt>.
	 * <p>
	 * 
	 * A Strand is represented by a symbol sequence. Each character represents a
	 * different base. Each and every sequence should be surrounded by
	 * parentheses. Parentheses are delimiters for groups of symbols, and thus
	 * every symbol sequence is a group. Examples 1) and 2) are correct, 3)
	 * isn't.
	 * <ul>
	 * <li>1) <tt>(aaaaa)</tt></li>
	 * <li>2) <tt>(aaa(aa))</tt></li>
	 * <li>3) <tt>aaaaa</tt></li>
	 * </ul> <p>
	 * 
	 * Number in brackets act as repetitions. A repetition will act on the next
	 * symbol or group of symbols. Brackets and parentheses should nested.
	 * <ul>
	 * <li>1) <tt>(aa[3]baa) == (aabbbaa)</tt></li>
	 * <li>2) <tt>(aaa[2](bc)) == (aaa(bc)(bc))</tt></li>
	 * <li>3) <tt>([2](abc)) == ((abc)(abc)) == (abcabc)</tt></li>
	 * </ul> <p>
	 * 
	 * A dash sign can be used to invert sybols. The inversion will act on the next
	 * symbol or group of symbols. If <tt>[a]</tt> is the inversion of symbol <tt>a</tt>,
	 * <tt>a</tt> and <tt>[a]</tt> will bind. Note that in this case the brackets <tt>[]</tt>
	 * are used to represent an inversion and not as to specify anything in a strand, unlike repetitions.
	 * 
	 * <ul>
	 * <li>1) <tt>(aa-aaa) : a a [a] a a </tt></li>
	 * <li>2) <tt>(aaa-(aa)) : a a a [a] [a]</tt></li>
	 * <li>3) <tt>(aa-[2](bc)a) : a a [b] [c] [b] [c] a</tt></li>
	 * </ul> <p>
	 * 
	 * @param str	the formated String
	 * @throws IllegalArgumentException
	 */
	public Strand(String str) {
		NodeBuilder builder = new NodeBuilder(str);
		try {
			builder.prepare();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}

		this.symbols = builder.getNode();
	}

	
	/**
	 * Creates a new Strand object from two Strands. The elements will be 
	 * read from str1, from index <tt>start1</tt> to <tt>stop1</tt>, and then 
	 * from str2, from index <tt>start2</tt> to <tt>stop2</tt>. Note that the first
	 * element of a Strand of <tt>n</tt> elements has index 1, and the last one
	 * <tt>n</tt>.
	 * @param str1	The first strand
	 * @param start1	The start index for the first strand
	 * @param stop1 The stop index for the first strand
	 * @param str2 The second strand
	 * @param start2 The start index for the second strand
	 * @param stop2 The stop index for the second strand
	 * @throws Exception 
	 */
	public Strand(Strand str1, int start1, int stop1, Strand str2, int start2,
			int stop2) throws Exception {
		if (start1 > stop1 && start2 > stop2) 
			throw new IllegalStateException("The resulting Strand should not be empty\n"
					
					+ "Start1="+start1+", Stop1="+stop1
					+ "Start2="+start2+", Stop2="+stop2);

		ArrayList<Node> nodes = Strand.createNodesFromIndices(str1, start1,
				stop1);
		nodes.addAll(Strand.createNodesFromIndices(str2, start2, stop2));
		Node[] nodesArr = new Node[nodes.size()];
		this.symbols = new GroupNode(nodes.toArray(nodesArr));
	}

	@Override
	public int hashCode() {
		// TODO
		// Quick and dirty, could be heavily optimized.
		// 1) Compute once, store
		// 2) Compute with NodeBuilder on construction (no need to iterate)
		Iterator<Symbol> it = new StrandReader(this).iterator();
		int result = 5;
		while (it.hasNext()) {
			result = 31 * result + it.next().hashCode();
		}

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Strand str = null;
		try {
			str = (Strand) obj;
		} catch (ClassCastException e) {
			return false;
		}

		StrandReader reader1 = new StrandReader(this);
		StrandReader reader2 = new StrandReader(str);

		Iterator<Symbol> it1 = reader1.iterator();
		Iterator<Symbol> it2 = reader2.iterator();

		while (it1.hasNext() || it2.hasNext()) {
			if (!(it1.hasNext() && it2.hasNext()))
				return false;

			if (!it1.next().equals(it2.next()))
				return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return this.toString(false);
	}

	public String toString(boolean reverse) {
		StrandReader reader = new StrandReader(this);
		Iterator<Symbol> it;
		if (reverse)
			it = reader.reverseIterator();
		else
			it = reader.iterator();
		StringBuilder builder = new StringBuilder();
		while (it.hasNext()) {
			builder.append(it.next());
		}
		return builder.toString();
	}

	
	
	Node getSymbols() {
		return this.symbols;
	}

	public int getLength() {
		return this.symbols.getLength();
	}

}
