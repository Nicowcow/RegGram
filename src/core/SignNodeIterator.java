package core;

class SignNodeIterator extends ParentNodeIterator {

	SignNodeIterator(SignNode node, boolean reverse) {
		super(node.child, reverse);
	}

	@Override
	public Symbol next() {
		Symbol result = childIterator.next();
		return new Symbol(result.symbol, !result.isNegative);
	}

}
