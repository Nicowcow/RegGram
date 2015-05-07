package core;

class RepeatNode extends SingleNode {

	final int n;

	RepeatNode(int n, Node child) {
		super(child);
		this.n = n;
	}
	
	@Override
	public int getLength()
	{
		return n*super.getLength();
	}
}
