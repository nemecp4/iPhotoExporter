package iPhotoParser;

import static org.junit.Assert.*;

import org.junit.Test;

class ParserTest {
	private  Node genereateDict(){
		def n = new Node(null, "dict")
		n.append(new Node(null,"key", "AlbumId"))
		n.append(new Node(null,"integer","123456"))
		n.append(new Node(null,"key", "AlbumName"))
		n.append(new Node(null,"string","Jmeno jmeno"))
		return n;
	}

	@Test
	public void testDict() {
		def n = genereateDict()

		def parser = new AlbumDataParser();
		def map = parser.parseDict(n);

		assert map!=null
		assert 2==map.keySet().size();
	}
	@Test
	public void testArrayString() {
		def n = new Node(null, "array")
		n.append(new Node(null,"string", "ahoj"))
		n.append(new Node(null,"string","Jmeno jmeno"))

		def parser = new AlbumDataParser();
		def list = parser.parseArray(n);

		assert list!=null
		assert 2==list.size();
	}
	@Test
	public void testArrayDict() {
		def n = new Node(null, "array")
		n.append(genereateDict())
		n.append(genereateDict())

		def parser = new AlbumDataParser();
		def list = parser.parseArray(n);

		assert list!=null
		assert 2==list.size();
		def d1 = list.get(0)
		def d2 = list.get(1)
		assert 2==d1.keySet().size()
		assert 2==d2.keySet().size()
	}
}
