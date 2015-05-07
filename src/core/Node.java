package core;


abstract class Node {
	
	
	@SuppressWarnings("unchecked")
	public <T> T as(Class<T> clazz)
	{
		if(clazz.isAssignableFrom(this.getClass()))
			return (T) this;
		
		return null;
	}
	
	public abstract int getLength();
	
}
