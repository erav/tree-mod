package com.github.erav.treemod.path;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * {@link TreePath} represents an n-gram formatted path
 * corresponding to a branch in a tree of {@link Map}s
 * and {@link List}s
 * <p>
 * See package-info for more details
 *
 * @author adoneitan@gmail.com
 */
public class TreePath
{
	private static final String ROOT = "ROOT";

	protected final String path;
	protected final List<String> pathPointers;
	protected final PathDelimiter delim;
	protected final String doubleDelimStr;
	protected int curr;
	protected StringBuilder origin;
	protected StringBuilder remainder;

	public TreePath(String path, PathDelimiter delim)
	{
		this.delim = delim;
		this.doubleDelimStr = delim.str() + delim.str();
		checkPath(path);
		this.path = path;
		this.pathPointers = Arrays.asList((ROOT + delim.str() + path).split(delim.regex()));
		reset();
	}

	public void reset()
	{
		curr = 0;
		origin = new StringBuilder("");
		remainder = new StringBuilder(path);
	}

	public boolean hasNext() {
		return curr < pathPointers.size() - 1;
	}

	public boolean hasPrev() {
		return curr > 0;
	}

	public String next()
	{
		if (!hasNext()) {
			throw new NoSuchElementException("there is no next key");
		}
		originIncrement();
		curr += 1;
		remainderDecrement();
		return pathPointers.get(curr);
	}

	public String prev()
	{
		if (!hasPrev()) {
			throw new NoSuchElementException("there is no previous key");
		}
		remainderIncrement();
		curr -= 1;
		originDecrement();
		return curr();
	}

	private void originDecrement()
	{
		if (length() == 1)
			origin = new StringBuilder("");
		else if (origin.indexOf(delim.str()) < 0)
			origin = new StringBuilder("");
		else
			origin.delete(origin.lastIndexOf(delim.str()), origin.length());
	}

	private void originIncrement()
	{
		if (origin.length() != 0) {
			origin.append(delim.chr());
		}
		origin.append(curr());
	}

	private void remainderDecrement()
	{
		if (length() == 1)
			remainder = new StringBuilder("");
		else if (remainder.indexOf(delim.str()) < 0)
			remainder = new StringBuilder("");
		else
			remainder.delete(0, remainder.indexOf(delim.str()) + 1);
	}

	private void remainderIncrement()
	{
		if (remainder.length() == 0)
			remainder = new StringBuilder(pathPointers.get(curr));
		else
			remainder = new StringBuilder(pathPointers.get(curr)).append(delim.chr()).append(remainder);
	}

	/**
	 * @return An n-gram path from the first key to the current key (inclusive)
	 */
	public String path() {
		return path;
	}

	/**
	 * @return An n-gram path from the first key to the current key (inclusive)
	 */
	public String origin() {
		return root() ? null : origin.toString();
	}

	public boolean root() {
		return curr == 0;
	}

	/**
	 * @return An n-gram path from the current key to the last key (inclusive)
	 */
	public String remainder() {
		return remainder.toString();
	}

	/**
	 * @return first element in the JSONPath
	 */
	public String first() {
		return pathPointers.get(1);
	}

	/**
	 * @return last element in the TreePath
	 */
	public String last() {
		return pathPointers.get(pathPointers.size() - 1);
	}

	/**
	 * @return current element pointed to by the tree path
	 */
	public String curr()
	{
		return root() ? "" : pathPointers.get(curr);
	}

	public int length() {
		return pathPointers.size();
	}

	public String subPath(int firstIndex, int lastIndex)
	{
		if (lastIndex < firstIndex) {
			throw new IllegalArgumentException("bad call to subPath");
		}
		StringBuilder sb = new StringBuilder(path.length());
		for (int i = firstIndex; i <= lastIndex; i++)
		{
			sb.append(pathPointers.get(i));
			if (i < lastIndex) {
				sb.append(delim.chr());
			}
		}
		sb.trimToSize();
		return sb.toString();
	}

	private void checkPath(String path)
	{
		if (path == null || path.equals(""))
			throw new IllegalArgumentException("path cannot be null or empty");
		if (path.startsWith(delim.str()) || path.endsWith(delim.str()) || path.contains(doubleDelimStr))
			throw new IllegalArgumentException(String.format("path cannot start or end with %s or contain '%s%s'", delim.str(), delim.str(), delim.str()));
	}

	@Override
	public TreePath clone() throws CloneNotSupportedException
	{
		TreePath cloned = new TreePath(this.path, this.delim);
		cloned.curr = this.curr;
		cloned.origin = new StringBuilder(this.origin);
		cloned.remainder = new StringBuilder(this.remainder);
		return cloned;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TreePath treePath = (TreePath) o;

		return path().equals(treePath.path()) &&
				hasNext() == treePath.hasNext() &&
				hasPrev() == treePath.hasPrev() &&
				curr().equals(treePath.curr()) &&
				(origin() == null ? treePath.origin() == null : origin().equals(treePath.origin())) &&
				remainder().equals(treePath.remainder()) &&
				delim.equals(treePath.delim);

	}

	@Override
	public int hashCode() {
		int result = path.hashCode();
		result = 31 * result + curr;
		result = 31 * result + pathPointers.hashCode();
		result = 31 * result + origin.hashCode();
		result = 31 * result + remainder.hashCode();
		result = 31 * result + delim.hashCode();
		return result;
	}
}


