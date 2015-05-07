package processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class SparseMatrix<T> {

	class Entry<K> {
		public final K value;
		public final int row;
		public final int col;

		public Entry(int row, int col, K val) {
			this.col = col;
			this.row = row;
			this.value = val;
		}
	}

	private Map<Integer, Map<Integer, T>> rows = new HashMap<Integer, Map<Integer, T>>();

	public T put(int rowNum, int colNum, T val) {

//		System.out.println("Before put");
//		Iterator<Entry<T>> it = this.iterator();

//		while (it.hasNext()) {
//			Entry<T> ent = it.next();
//			System.out.println("Row : " + ent.row + ", Col : " + ent.col
//					+ ", Val : " + ent.value.toString());
//		}
		Map<Integer, T> row = rows.get((Integer) rowNum);

		if (row == null) {
			row = new HashMap<Integer, T>();
			rows.put(Integer.valueOf(rowNum), row);
		}

		T old = row.get((Integer) colNum);
		row.put((Integer) colNum, val);

//		System.out.println("Before put");
//		it = this.iterator();
//
//		while (it.hasNext()) {
//			Entry<T> ent = it.next();
//			System.out.println("Row : " + ent.row + ", Col : " + ent.col
//					+ ", Val : " + ent.value.toString());
//		}
		return old;
	}

	public T get(int rowNum, int colNum) {
		Map<Integer, T> row = rows.get(Integer.valueOf(rowNum));
		if (row == null)
			return null;

		return row.get((Integer) colNum);
	}

	public T erase(int rowNum, int colNum) {
//		System.out.println("Before erase");
//		Iterator<Entry<T>> it = this.iterator();
//
//		while (it.hasNext()) {
//			Entry<T> ent = it.next();
//			System.out.println("Row : " + ent.row + ", Col : " + ent.col
//					+ ", Val : " + ent.value.toString());
//		}
		Map<Integer, T> row = rows.get(Integer.valueOf(rowNum));
		if (row == null)
			return null;

		T old = row.remove(Integer.valueOf(colNum));

		if (row.isEmpty()) {
			rows.remove(row);
		}

//		System.out.println("after erase");
//		it = this.iterator();
//
//		while (it.hasNext()) {
//			Entry<T> ent = it.next();
//			System.out.println("Row : " + ent.row + ", Col : " + ent.col
//					+ ", Val : " + ent.value.toString());
//		}

		return old;
	}

	public Iterator<Entry<T>> iterator() {
		List<Entry<T>> entries = new ArrayList<Entry<T>>();
		for (Integer rowNum : rows.keySet()) {
			Map<Integer, T> row = rows.get(rowNum);
			for (Integer colNum : row.keySet()) {
				T nnz = row.get(colNum);
//				System.out.println("New entry : row :" + rowNum + ", col : "
//						+ colNum + ", value :" + nnz.toString());
				entries.add(new Entry<T>(rowNum, colNum, nnz));
			}

		}
		return entries.iterator();

	}

}
