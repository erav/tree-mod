package com.github.erav.treemod.navigate;

import com.github.erav.treemod.path.TreePath;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.Collection;

/**
 * @author adoneitan@gmail.com
 * @since  5/24/16.
 */
public class PrintNavigatedPathAction implements JSONNavigateAction
{
	private int depth = 0;

	@Override
	public boolean pathStart(String path) {
		System.out.println("starting path\t\t" + depth + "\t\t" + path);
		return true;
	}

	@Override
	public boolean start(JSONObject objectToNavigate, Collection<String> pathsToNavigate)
	{
		System.out.println("action\t\t\t\tdepth\tpath");
		return true;
	}

	@Override
	public void pathTailNotFound(TreePath tp, Object source) {
		System.out.println("tail not found\t\t" + depth + "\t\t" + tp.curr());
	}

	@Override
	public void pathEnd(String path) {
		System.out.println("path end\t\t\t" + depth + "\t\t" + path);
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
	public boolean recurInto(TreePath tp, JSONObject sourceNode)
	{
		System.out.println("recur into\t\t\t" + depth + "\t\t" + tp.curr());
		return true;
	}

	@Override
	public boolean recurInto(TreePath tp, JSONArray sourceNode) {
		System.out.println("recur into\t\t\t\t" + depth + "\t\t" + tp.curr());
		return true;
	}

	@Override
	public void foundLeafBeforePathEnd(TreePath tp, Object obj) {
		System.out.println("found leaf before path end\t\t" + depth + "\t\t" + tp.curr());
	}

	@Override
	public void handleLeaf(TreePath tp, Object value) {
		System.out.println("handle leaf\t\t\t" + depth + "\t\t" + tp.curr());
	}

	@Override
	public void handleLeaf(TreePath tp, int arrIndex, Object arrItem) {
		System.out.println("handle array leaf\t\t" + depth + "\t\t" + tp.curr() + "\t\t" + arrItem);
	}

	@Override
	public void recurEnd(TreePath tp, JSONObject jsonObject) {
		System.out.println("recur end\t\t\t" + depth + "\t\t" + tp.curr());
	}

	@Override
	public void recurEnd(TreePath tp, JSONArray objects) {
		System.out.println("recur array end\t\t" + depth + "\t\t" + tp.curr());
	}

	@Override
	public void end()
	{
		System.out.println("end\t\t\t\t\t" + depth);
	}

	@Override
	public Object result()
	{
		return null;
	}
}
