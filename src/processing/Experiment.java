package processing;

import processing.soups.Soup;

public class Experiment implements Runnable{
	
	private final Soup soup;

	public Experiment(Soup soup)
	{
		this.soup = soup;
	}

	@Override
	public void run() {
		while(!soup.isDone())
		{
			try {
				soup.step();
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	
}
