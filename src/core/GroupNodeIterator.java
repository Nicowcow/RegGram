package core;

class GroupNodeIterator extends ParentNodeIterator {
	
	private final Node[] children;
	private final GroupNode node;
	private final int nChildren;
	private int currChild; // Need this?
		
	GroupNodeIterator(GroupNode node, boolean reverse) {
		
		super(node.children[reverse?node.children.length-1:0], reverse);
		this.node = node;
		this.nChildren = node.children.length;
		this.children = node.children;
		this.currChild = reverse?node.children.length-1:0;
	}

	
	@Override
	public boolean hasNext()
	{
		if(childIterator.hasNext())
		{
			return true;
		} else if(!hasReachedFinalChild())
		{
//			moveToNextChild(); // Shouldn't be there
			return this.getNextChildIterator().hasNext();
		}
		return false;
	}
	
	@Override
	public Symbol next()
	{
		if(childIterator.hasNext())
		{
			return childIterator.next();
		} else if(!hasReachedFinalChild())
		{
			moveToNextChild();
			return this.childIterator.next();
		}
		return null;
	}
	
	@Override
	public void reset()
	{
		this.childIterator = ParentNodeIterator.getIteratorFor(children[reverse?node.children.length-1:0], this.reverse);
		this.currChild = reverse?node.children.length-1:0;
	}
	
	private boolean hasReachedFinalChild()
	{
		if(reverse)
			return currChild <= 0;
		return currChild >= nChildren - 1;
	}
	
	private void moveToNextChild()
	{
		if(reverse)
			this.currChild--;
		else
			this.currChild ++;
		
		this.childIterator = ParentNodeIterator.getIteratorFor(children[currChild], this.reverse);
	}
	

	private NodeIterator getNextChildIterator()
	{
		int nextChild;
		if(reverse)
			nextChild = this.currChild - 1;
		else
			nextChild = this.currChild + 1;
		
		return ParentNodeIterator.getIteratorFor(children[nextChild], reverse);
	}
}
