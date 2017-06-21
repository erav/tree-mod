package com.github.erav.treemod.traverse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.Map.Entry;

/**
 * @author adoneitan@gmail.com
 * @since  5/24/16.
 */
public class KeysPrintAction implements JSONTraverseAction
{
	private int depth = 0;

	@Override
	public boolean start(JSONObject object)
	{
		System.out.println("action\t\t\t\tdepth\tpath");
		return true;
	}

	@Override
	public boolean traverseEntry(String fullPathToEntry, Entry<String, Object> entry)
	{
		System.out.println("traverseEntry\t\t" + depth + "\t\t" + fullPathToEntry);
		return true;
	}

	@Override
	public boolean recurInto(String pathToEntry, JSONObject entryValue) {
		depth++;
		return true;
	}

	@Override
	public boolean recurInto(String pathToEntry, JSONArray entryValue) {
		depth++;
		return true;
	}

	@Override
	public void handleLeaf(String pathToEntry,  Entry<String, Object> entry)
	{
		System.out.println("handleLeaf   \t\t" + depth + "\t\t" + pathToEntry);
	}

	@Override
	public void handleLeaf(String fullPathToContainingList, int listIndex, Object listItem)
	{

	}

	@Override
	public boolean removeEntry(String fullPathToEntry, Entry<String, Object> entry)
	{
		return false;
	}

	@Override
	public void end()
	{

	}

	@Override
	public Object result()
	{
		return null;
	}

	@Override
	public void backToParent(String fullPath, JSONObject map)
	{
		depth--;
		System.out.println("backToParent\t\t" + depth + "\t\t" + fullPath);
	}

	@Override
	public void backToParent(String fullPath, JSONArray list)
	{
		depth--;
		System.out.println("backToParent\t\t" + depth + "\t\t" + fullPath);
	}

}
