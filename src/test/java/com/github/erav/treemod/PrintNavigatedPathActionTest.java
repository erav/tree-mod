package com.github.erav.treemod;

import com.github.erav.treemod.navigate.JSONNavigator;
import com.github.erav.treemod.navigate.PrintNavigatedPathAction;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.Test;

/**
 * @author adoneitan@gmail.com
 * @since 30 May 2016
 */
public class PrintNavigatedPathActionTest
{
	@Test
	public void testNavigate() throws Exception
	{
		PrintNavigatedPathAction p = new PrintNavigatedPathAction();
		JSONNavigator n = new JSONNavigator(p, "k0.k01.k011", "k1.k11.k111");
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
							"\"k311\":\"v5\"," +
							"\"k312\":\"v6\"," +
							"\"k313\":\"v7\"" +
						"}" +
					"}" +
				"}"
		);
		n.nav(jo);
	}
}