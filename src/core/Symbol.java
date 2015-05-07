package core;

public final class Symbol {
	
	public final String symbol;	// TODO not a good name, 
								// should differentiate symbol/base
	public final boolean isNegative;
	
	public Symbol(String symbol, boolean isNegative)
	{
		this.symbol = symbol;
		this.isNegative = isNegative;
		
	}
	
	@Override
	public String toString()
	{
		if(isNegative)
		{
			return "[" + symbol + "]";
		}
		return " " + symbol + " ";
	}
	
	@Override
	public int hashCode()
	{
		int result = 42;
		int c = this.symbol.hashCode();
		result = 31*result + c;
		c = isNegative?1:0;
		result = 31*result + c;
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Symbol sym = null;
		try
		{
			sym = (Symbol) obj;
		} catch(ClassCastException e)
		{
			return false;
		}
		
		if(sym.symbol.equals(this.symbol) && 
				sym.isNegative == this.isNegative)
			return true;
		
		return false;
	}
	
	public boolean binds(Symbol s)
	{
		if(this.symbol.equals(s.symbol) &&
				this.isNegative != s.isNegative)
		{
			return true;
		}
		
		return false;
	}

}
