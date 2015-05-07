package core;

class SingleNode extends Node {

	public final Node child;

	SingleNode(Node child) {
		this.child = child;
	}

	@Override
	public int getLength() {
		return child.getLength();
	}

}
