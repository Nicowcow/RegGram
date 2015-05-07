package core;

class RepeatNodeIterator extends ParentNodeIterator {
	
	private final int nTotalReads;
	private int nCompleteReads = 0;
	
	RepeatNodeIterator(RepeatNode node, boolean reverse)
	{
		super(node.child, reverse);
		this.nTotalReads = node.n;
	}

	@Override
	public boolean hasNext() {
		if(childIterator.hasNext())
			return true;
		else if(nCompleteReads < nTotalReads - 1)
		{
			this.nCompleteReads ++;
			childIterator.reset();
			return childIterator.hasNext();
		}
		
		return false;
	}

	@Override
	public void reset() {
		super.reset();
		this.nCompleteReads = 0;
	}

}
