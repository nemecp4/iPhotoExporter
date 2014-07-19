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
	
	@Test
	public void testParsePictures(){
		def map =[:]
		
		def pic1 = [:]
		pic1["ImagePath"]="c:path"
		map[12345]=pic1
		
		def pic2 = [:]
		pic2["ImagePath"]="c:\\path\\ahoj"
		map[4121]=pic2
		
		
		def faceMap1 =["face key":"123456"]
		def faceMap2 =["face key":"9879"]
		def faceList = [faceMap1,faceMap2]
		pic2["Faces"]=faceList
		
		def imageMap = AlbumDataParser.processImages(map);
		println "have list: $imageMap"
		assert 2==imageMap.size();
		
		assert "c:path" == imageMap[12345].path
		
		def picture2 = imageMap[4121]
		assert 2 == picture2.faces.size()
	}
	
	@Test
	public void testprocessAlbums(){
		def list =[]
		
		def a1 = [:]
		a1["AlbumId"]="uniq1"
		a1["AlbumName"]="name1"
		a1["Album Type"]="event"
		a1["PhotoCount"]=1
		a1["KeyList"]=["1234"]
		list.add(a1)
		
		def pictureMap = ["1234":new Picture()]
		
		def albumMap = AlbumDataParser.processAlbums(list, pictureMap);
		println "have list: $albumMap"
		assert 1==albumMap.size();
		
		
		
		def album = albumMap["uniq1"]
		assert 1 == album.pictures.size()
	}
}
