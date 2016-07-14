package com.github.erav.treemod;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import com.github.erav.treemod.traverse.JSONTraverser;
import com.github.erav.treemod.traverse.KeysPrintAction;
import net.minidev.json.parser.ParseException;
import org.junit.Test;

/**
 * @author adoneitan@gmail.com
 * @since 30 May 2016
 */
public class KeysPrintActionTest
{
	@Test
	public void test() throws ParseException
	{
		KeysPrintAction p = new KeysPrintAction();
		JSONTraverser t = new JSONTraverser(p);
		JSONObject jo = (JSONObject) JSONValue.parseWithException(
				"{" +
					"\"k0\":{" +
						"\"k01\":{" +
							"\"k011\":\"v2\"" +
						"}" +
					"}," +
					"\"k1\":{" +
						"\"k11\":{" +
							"\"k111\":\"v5\"" +
						"}," +
						"\"k12\":{" +
							"\"k121\":\"v5\"" +
						"}" +
					"}," +
					"\"k3\":{" +
						"\"k31\":{" +
							"\"k311\":\"v5\"" +
						"}" +
					"}" +
				"}"
		);
		t.traverse(jo);
	}
}