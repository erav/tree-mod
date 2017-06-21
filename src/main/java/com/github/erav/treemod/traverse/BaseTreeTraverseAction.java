package com.github.erav.treemod.traverse;

import java.util.List;
import java.util.Map;

/**
 * @author eitanraviv
 * @since 7/28/16
 */
public class BaseTreeTraverseAction<M extends Map<String, Object>, L extends List<Object>> implements TreeTraverseAction<M, L>
{
	@Override
	public boolean start(M object)
	{
		return true;
	}

	@Override
	public boolean traverseEntry(String fullPathToEntry, Map.Entry<String, Object> entry)
	{
		return true;
	}

	@Override
	public boolean removeEntry(String fullPathToEntry, Map.Entry<String, Object> entry)
	{
		return false;
	}

	@Override
	public boolean recurInto(String fullPathToSubtree, M entryValue)
	{
		return true;
	}

	@Override
	public boolean recurInto(String fullPathToContainingList, L entryValue)
	{
		return true;
	}

	@Override
	public void handleLeaf(String fullPathToEntry, Map.Entry<String, Object> entry)
	{

	}

	@Override
	public void handleLeaf(String fullPathToContainingList, int listIndex, Object listItem)
	{

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
	public void backToParent(String fullPath, M map)
	{

	}

	@Override
	public void backToParent(String fullPath, L list)
	{

	}
}
