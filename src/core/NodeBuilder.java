package core;

import java.util.ArrayList;
import java.util.List;


final class NodeBuilder
{
	private final String str;
	private int index;
	private Node node;
	public NodeBuilder(String str)
	{
		this(str, 0);
	}
	
	public NodeBuilder(String str, int startIndex)
	{
		this.str = str;
		this.index = startIndex;
	}
	
	public void prepare()
	{
		char c = str.charAt(index);
		switch(c)
		{
		case '[':
			prepareRepeat();
			break;
			
		case '(':
			prepareGroup();
			break;
			
		case '-':
			prepareSign();
			break;
			
		case ' ':
			break;
			
		default:
			prepareSymbol();
		}
	}
	
	private void prepareSign()
	{
		index++;  //current char is '-'
		NodeBuilder builder = new NodeBuilder(str, index);
		builder.prepare();
		
		index = builder.getLastIndex();
		this.node = new SignNode(builder.node);
	}
	
	private void prepareSymbol()
	{
		// TODO 'symbol' as string (maybe)
		String symbol = str.charAt(index) + "";
		this.node = new SymbolNode(symbol);
	}
	
	private void prepareRepeat()
	{
		index++; // current char is '['
		StringBuilder sBuilder = new StringBuilder();
		while(true)
		{
			char c = str.charAt(index);
			if(c == ' ')
			{
				index ++ ;
				continue;
			}
			if(c == ']')
				break;
			
			sBuilder.append(c);
			index ++ ;
		}
		
		int n = Integer.parseInt(sBuilder.toString());
		NodeBuilder nBuilder = new NodeBuilder(str, index + 1); 
		nBuilder.prepare();
		this.index = nBuilder.getLastIndex();
		this.node = new RepeatNode(n, nBuilder.getNode());
		
	}
	
	private void prepareGroup()
	{
		index++; // current char is '('
		List<Node> children = new ArrayList<Node>();
		while(true)
		{
			char c = str.charAt(index);
			if(c == ' ')
			{
				index ++ ;
				continue;
			}
			if(c == ')')
				break;
			
			NodeBuilder builder = new NodeBuilder(str, index);
			builder.prepare();
			children.add(builder.getNode());
			index = builder.getLastIndex() + 1;
		}
		
		Node[] childrenArr = new Node[children.size()];
		children.toArray(childrenArr);
		this.node = new GroupNode(childrenArr);
	}
	
	public int getLastIndex()
	{
		return this.index;
	}
	
	public Node getNode()
	{
		return this.node;
	}
	
	
}
