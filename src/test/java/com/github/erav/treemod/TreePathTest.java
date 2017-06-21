package com.github.erav.treemod;

import com.github.erav.treemod.path.DotDelimiter;
import com.github.erav.treemod.path.PathDelimiter;
import com.github.erav.treemod.path.TreePath;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author adoneitan@gmail.com
 */
public class TreePathTest
{
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	private static final PathDelimiter delim = new DotDelimiter().withAcceptDelimiterInNodeName(true);

	@Test
	public void testEmptyPath()
	{
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("path cannot be null or empty");
		TreePath tp = new TreePath("", delim);
	}

	@Test
	public void testInitialPathPointer()
	{
		TreePath tp = new TreePath("a.b.c", delim);
		assertEquals("", tp.curr());
		assertEquals(null, tp.origin());
		assertEquals("a.b.c", tp.remainder());
	}

	@Test
	public void testNoPrev1()
	{
		TreePath tp = new TreePath("a", delim);
		assertFalse(tp.hasPrev());
		exception.expect(NoSuchElementException.class);
		exception.expectMessage("there is no previous key");
		tp.prev();
	}

	@Test
	public void testNoPrev2()
	{
		TreePath tp = new TreePath("a.b.c", delim);
		assertFalse(tp.hasPrev());
		exception.expect(NoSuchElementException.class);
		exception.expectMessage("there is no previous key");
		tp.prev();
	}

	@Test
	public void testNoNext1()
	{
		TreePath tp = new TreePath("a", delim);
		tp.next();
		assertFalse(tp.hasNext());
		exception.expect(NoSuchElementException.class);
		exception.expectMessage("there is no next key");
		tp.next();
	}

	@Test
	public void testNoNext2()
	{
		TreePath tp = new TreePath("a.b.c", delim);
		tp.next();
		tp.next();
		tp.next();
		assertFalse(tp.hasNext());
		exception.expect(NoSuchElementException.class);
		exception.expectMessage("there is no next key");
		tp.next();
	}

	@Test
	public void testIterator()
	{
		TreePath tp = new TreePath("a.b.c", delim);

		assertEquals("", tp.curr());
		assertTrue(tp.hasNext());
		assertFalse(tp.hasPrev());

		tp.next();
		assertEquals("a", tp.curr());
		assertTrue(tp.hasNext());
		assertTrue(tp.hasPrev());

		tp.next();
		assertEquals("b", tp.curr());
		assertTrue(tp.hasNext());
		assertTrue(tp.hasPrev());

		tp.next();
		assertEquals("c", tp.curr());
		assertFalse(tp.hasNext());
		assertTrue(tp.hasPrev());

		tp.prev();
		assertEquals("b", tp.curr());
		assertTrue(tp.hasNext());
		assertTrue(tp.hasPrev());

		tp.prev();
		assertEquals("a", tp.curr());
		assertTrue(tp.hasNext());
		assertTrue(tp.hasPrev());

		tp.prev();
		assertEquals("", tp.curr());
		assertTrue(tp.hasNext());
		assertFalse(tp.hasPrev());
	}

	@Test
	public void testOriginAndRemainder()
	{
		TreePath tp = new TreePath("a.b.c", delim);

		assertEquals("", tp.curr());
		assertEquals(null, tp.origin());
		assertEquals("a.b.c", tp.remainder());

		tp.next();
		assertEquals("a", tp.curr());
		assertEquals("", tp.origin());
		assertEquals("b.c", tp.remainder());

		tp.next();
		assertEquals("b", tp.curr());
		assertEquals("a", tp.origin());
		assertEquals("c", tp.remainder());

		tp.next();
		assertEquals("c", tp.curr());
		assertEquals("a.b", tp.origin());
		assertEquals("", tp.remainder());

		tp.prev();
		assertEquals("b", tp.curr());
		assertEquals("a", tp.origin());
		assertEquals("c", tp.remainder());

		tp.prev();
		assertEquals("a", tp.curr());
		assertEquals("", tp.origin());
		assertEquals("b.c", tp.remainder());

		tp.prev();
		assertEquals("", tp.curr());
		assertEquals(null, tp.origin());
		assertEquals("a.b.c", tp.remainder());
	}

	@Test
	public void testSubPath()
	{
		TreePath jp = new TreePath("a.b.c", delim);
		assertEquals("a.b", jp.subPath(1,2));
	}

	@Test
	public void testClone() throws CloneNotSupportedException
	{
		TreePath jp1 = new TreePath("a.b.c", delim);
		TreePath jp2 = jp1.clone();
		assertTrue(jp1.equals(jp2));

		jp1.next();
		TreePath jp3 = jp1.clone();
		assertTrue(jp1.equals(jp3));

		jp1.prev();
		TreePath jp4 = jp1.clone();
		assertTrue(jp1.equals(jp4));

	}
}