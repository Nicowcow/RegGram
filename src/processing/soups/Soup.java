package processing.soups;

import processing.Matcher;

public abstract class Soup {

	protected final Matcher matcher;
	private final int N_ITER_MAX;
	protected int n_iter = 0;
	protected boolean isDone = false;


	public Soup(Matcher matcher, int N_ITER_MAX) {
		if (matcher != null)
			this.matcher = matcher;
		else
			this.matcher = new Matcher();

		this.N_ITER_MAX = N_ITER_MAX;
	}

	public final void step() throws Exception {
		if (this.isDone)
			return;
		if (N_ITER_MAX != 0 && n_iter > N_ITER_MAX) {
			this.finish();
			return;
		}
		this.stepImpl();

		this.n_iter++;

	}

	public abstract void stepImpl() throws Exception;

	public final boolean isDone() {
		return this.isDone;
	}

	protected void finish() {
		this.isDone = true;
	}
}
