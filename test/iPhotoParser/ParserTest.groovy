package iPhotoParser;

import static org.junit.Assert.*;

import org.junit.Test;

class ParserTest {

	@Test
	public void test() {
		def n = new Node(null, "dict")
		n.append(new Node(null,"key", "AlbumId"))
		n.append(new Node(null,"integer","123456"))
		n.append(new Node(null,"key", "AlbumName"))
		n.append(new Node(null,"string","Jmeno jmeno"))
			
		def parser = new AlbumDataParser();
		def map = parser.parseDict(n);
		
		assert map!=null
		assert 2==map.keySet().size();
	}

}
