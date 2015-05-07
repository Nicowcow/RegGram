package core;

abstract class ParentNodeIterator extends NodeIterator {
	
	
	public static NodeIterator getIteratorFor(Node node, boolean reverse)
	{
		NodeIterator it = null;
		SignNode sNode = node.as(SignNode.class);
		if(sNode != null)
		{
			it = new SignNodeIterator(sNode, reverse);
		}
		
		RepeatNode rNode = node.as(RepeatNode.class);
		if(rNode != null)
		{
			it = new RepeatNodeIterator(rNode, reverse);
		}
		
		GroupNode gNode = node.as(GroupNode.class);
		if(gNode != null)
		{
			it = new GroupNodeIterator(gNode, reverse);
		}
		
		SymbolNode symNode = node.as(SymbolNode.class);
		if(symNode != null)
		{
			it =  new SymbolNodeIterator(symNode, reverse);
		}
		return it; 
	}

	protected NodeIterator childIterator;

	
	ParentNodeIterator(Node node, boolean reverse)
	{
		super(reverse);
		this.childIterator = ParentNodeIterator.getIteratorFor(node, 
				this.reverse);
	}
	
	@Override
	public boolean hasNext()
	{
		return childIterator.hasNext();
	}

	@Override
	public Symbol next()
	{
		return childIterator.next();
	}

	@Override
	public void reset() {
		childIterator.reset();
	}

}
