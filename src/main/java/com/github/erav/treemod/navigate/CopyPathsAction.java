package com.github.erav.treemod.navigate;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import com.github.erav.treemod.path.TreePath;

import java.util.Collection;
import java.util.Stack;

/**
 * <b>Creates a copy of a {@link JSONObject} containing just the nodes on the paths specified.</b>
 * <p>
 * Specified paths that do not exist in the source object are ignored silently.
 * Specifying an empty list of paths to navigate or only non-existent paths will result in an empty
 * object being returned.
 * <p>
 * See package-info for more details
 * <p>
 * <b>Example:</b>
 * <p>
 * To copy the branch k1.k2 from {k1:{k2:v1}, k3:{k4:v2}} instantiate the copier like so:
 * new JSONObjectCopier("k1.k2")
 * The resulting copy would be {k1:{k2:v1}}
 * <p>
 * See unit tests for more examples
 *
 * @author adoneitan@gmail.com
 * @since 15 March 2016.
 *
 */
public class CopyPathsAction implements JSONNavigateAction
{
	protected JSONObject destTree;
	protected JSONObject destBranch;
	protected Stack<Object> destNodeStack;

	@Override
	public boolean start(JSONObject source, Collection<String> pathsToCopy)
	{
		if (source == null)
		{
			destTree = null;
			return false;
		}
		initDestTree();
		if (pathsToCopy == null || pathsToCopy.size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean recurInto(TreePath tp, JSONObject o)
	{
		if (!tp.root()) {
			//reached JSONObject node - instantiate it and recur
			handleNewNode(tp, new JSONObject());
		}
		return true;
	}


	private void initDestTree() {
		destTree = new JSONObject();
	}

	private void handleNewNode(TreePath tp, Object node)
	{
		if (destNodeStack.peek() instanceof JSONObject) {
			((JSONObject) destNodeStack.peek()).put(tp.curr(), node);
		}
		else if (destNodeStack.peek() instanceof JSONArray) {
			((JSONArray) destNodeStack.peek()).add(node);
		}
		destNodeStack.push(node);
	}

	@Override
	public boolean recurInto(TreePath tp, JSONArray o)
	{
		//reached JSONArray node - instantiate it and recur
		handleNewNode(tp, new JSONArray());
		return true;
	}

	@Override
	public void foundLeafBeforePathEnd(TreePath tp, Object obj) {
		throw new IllegalArgumentException("branch is shorter than path - path not found in source: '" + tp.origin() + "'");
	}

	@Override
	public void pathTailNotFound(TreePath tp, Object source) {
		throw new IllegalArgumentException("cannot find next element of path - path not found in source: '" + tp.origin() + "'");
	}

	@Override
	public void handleLeaf(TreePath tp, Object o) {
		((JSONObject) destNodeStack.peek()).put(tp.curr(), o);
	}

	@Override
	public void handleLeaf(TreePath tp, int arrIndex, Object o) {
		((JSONArray) destNodeStack.peek()).add(o);
	}

	@Override
	public void recurEnd(TreePath tp, JSONObject jo) {
		destNodeStack.pop();
	}

	@Override
	public void recurEnd(TreePath tp, JSONArray ja) {
		destNodeStack.pop();
	}

	@Override
	public boolean pathStart(String path)
	{
		destBranch = new JSONObject();
		destNodeStack = new Stack<Object>();
		destNodeStack.push(destBranch);
		return true;
	}

	@Override
	public void pathEnd(String path) {
		destTree.merge(destBranch);
	}

	@Override
	public boolean failSilently(String path, Exception e) {
		return false;
	}

	@Override
	public boolean failFast(String path, Exception e) {
		return false;
	}

	@Override
	public void end() {

	}

	@Override
	public Object result() {
		return destTree;
	}
}
