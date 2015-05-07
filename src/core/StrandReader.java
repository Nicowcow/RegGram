package core;

import java.util.Iterator;

public class StrandReader implements Iterable<Symbol> {
	
	public static <T> Iterator<T> iterate(Iterator<T> it, int n)
	{
		for(int i = 0; i <= n; i ++)
		{
			it.next();
		}
		
		return it;
	}
	
	private final Strand str;
	
	public StrandReader(Strand str)
	{
		this.str = str;
	}
	
	public Iterator<Symbol> iterator(int n)
	{
		return StrandReader.iterate(this.iterator(), n);
	}

	public Iterator<Symbol> reverseIterator(int n)
	{
		return StrandReader.iterate(this.reverseIterator(), n);
	}
	
	@Override
	public Iterator<Symbol> iterator() {
		return ParentNodeIterator.getIteratorFor(str.getSymbols(), false);
	}
	
	public Iterator<Symbol> reverseIterator()
	{
		return ParentNodeIterator.getIteratorFor(str.getSymbols(), true);
	}
}
