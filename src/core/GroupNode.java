package core;

class GroupNode extends Node{
	
	final Node[] children;
	
	GroupNode(Node[] children)
	{
		this.children = children;
	}

	@Override
	public int getLength() {
		int length = 0;
		for(Node node : children)
		{
			length += node.getLength();
		}
		return length;
	}
	
	

}
