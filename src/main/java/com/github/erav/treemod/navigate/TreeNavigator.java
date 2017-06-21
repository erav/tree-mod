package com.github.erav.treemod.navigate;

import net.minidev.json.JSONObject;
import com.github.erav.treemod.path.DotDelimiter;
import com.github.erav.treemod.path.TreePath;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <b>Navigates only the branches of a {@link JSONObject} corresponding to the paths specified.</b>
 * <p>
 * For each specified path to navigate, the {@link TreeNavigator} only traverses the matching
 * branch.
 * <p>
 * The navigator accepts an action and provides callback hooks for it to act on the traversed
 * nodes at each significant step. See {@link NavigateAction}.
 * <p>
 * See package-info for more details
 * <p>
 * <b>Example:</b>
 * <p>
 * To navigate the branch k1.k2 of the object {"k1":{"k2":"v1"}, "k3":{"k4":"v2"}} instantiate
 * the navigator like so: new JSONNavigator("k1.k2")
 *
 * @author adoneitan@gmail.com
 * @since 15 June 2016.
 *
 */
public class TreeNavigator<M extends Map<String, Object>, L extends List<Object>>
{
	protected List<String> pathsToNavigate;
	protected NavigateAction action;
	protected String pathPrefix = "";

	public TreeNavigator(NavigateAction action, List<String> pathsToNavigate)
	{
		if (action == null) {
			throw new IllegalArgumentException("NavigateAction cannot be null");
		}
		this.action = action;
		this.pathsToNavigate = pathsToNavigate;
	}

	public TreeNavigator with(String pathPrefix) {
		this.pathPrefix = pathPrefix;
		return this;
	}

	public TreeNavigator(NavigateAction action, String... pathsToNavigate)
	{
		this(action, Arrays.asList(pathsToNavigate));
	}

	public void nav(M object) throws Exception
	{
		if (action.start(object, pathsToNavigate))
		{
			for (String path: pathsToNavigate)
			{
				try
				{
					if (path != null && !path.equals("") && action.pathStart(path))
					{
						TreePath jp = new TreePath(path, new DotDelimiter().withAcceptDelimiterInNodeName(true));
						nav(jp, object);
						action.pathEnd(path);
					}
				}
				catch (Exception e)
				{
					if (action.failSilently(path ,e)) {
						break;
					}
					else if (action.failFast(path, e)) {
						throw e;
					}
				}
			}
		}
		action.end();
	}

	public void nav(TreePath tp, M map)
	{
		if (map == null || !action.recurInto(tp, map))
		{
			//source is null - navigation impossible
			return;
		}

		if (tp.hasNext())
		{
			String key = tp.next();
			if (!map.containsKey(key))
			{
				// cannot find next element of path in the source -
				// the specified path does not exist in the source
				action.pathTailNotFound(tp, map);
			}
			else if (map.get(key) instanceof Map)
			{
				//reached Map type node - handle it and recur into it
				nav(tp, (M) map.get(key));
			}
			else if (map.get(key) instanceof List)
			{
				//reached List type node - handle it and recur into it
				nav(tp, (L) map.get(key));
			}
			else if (tp.hasNext())
			{
				// reached leaf node (not a container) in source but specified path expects children -
				// the specified path is illegal because it does not exist in the source.
				action.foundLeafBeforePathEnd(tp, map.get(key));
			}
			else if (!tp.hasNext())
			{
				//reached leaf in source and specified path is also at leaf -> handle it
				action.handleLeaf(tp, map.get(key));
			}
			else
			{
				throw new IllegalStateException("fatal: unreachable code reached at '" + tp.curr() + "'");
			}
		}
		if (tp.hasPrev()) {
			tp.prev();
		}
		action.recurEnd(tp, (M) map);
	}

	public void nav(TreePath tp, L list)
	{
		if (list == null || !action.recurInto(tp, (L) list))
		{
			//list is null - navigation impossible
			return;
		}
		int arrIndex = 0;
		for (Object arrItem : list.toArray())
		{
			if (arrItem instanceof Map)
			{
				// clone the path so that for each object in the array,
				// the iterator continues from the same position in the path
				TreePath tpClone = getClone(tp);
				nav(tpClone, (M) arrItem);
			}
			else if (arrItem instanceof List)
			{
				nav(tp, (L) arrItem);
			}
			else if (!tp.hasNext())
			{
				//reached leaf - handle it
				action.handleLeaf(tp, arrIndex, arrItem);
			}
			arrIndex++;
		}
		action.recurEnd(tp, (L) list);
	}

	private TreePath getClone(TreePath jp)
	{
		try
		{
			return jp.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new RuntimeException("failed to clone path", e);
		}
	}
}
