package core;

class SymbolNodeIterator extends NodeIterator {

	private boolean wasRead = false;
	private final Symbol symbol;
	
	SymbolNodeIterator(SymbolNode node, boolean reverse)
	{
		super(reverse);
		this.symbol = new Symbol(node.symbol, false);
	}
	
	@Override
	public boolean hasNext() {
		return !wasRead;
	}

	@Override
	public Symbol next() {
		if(wasRead)
			return null;
		
		wasRead = true;
		
		return symbol;
	}

	@Override
	public void reset() {
		this.wasRead = false;
	}

}
