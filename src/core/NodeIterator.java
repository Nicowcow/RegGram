package core;

import java.util.Iterator;

abstract class NodeIterator implements Iterator<Symbol> {
	
	protected boolean reverse = false;
	
	NodeIterator(boolean reverse)
	{
		this.reverse = reverse;
	}
	
	public abstract void reset();

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public Symbol next() {
		return null;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
