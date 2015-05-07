package core;

class SymbolNode extends Node {

	final String symbol;

	public SymbolNode(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public int getLength() {
		return 1;
	}

}
