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
	public void testProcessAlbums(){
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
		assert "event" == album.type
	}
	
	@Test
	public void testProcessAlbumsWithParent(){
		def list =[]
		
		def a1 = [:]
		a1["AlbumId"]="uniq1"
		a1["AlbumName"]="name1"
		a1["Album Type"]="Regular"
		a1["PhotoCount"]=1
		a1["KeyList"]=["1234"]
		a1["Parent"]="folder1"
		list.add(a1)
		
		def a2 = [:]
		a2["AlbumId"]="folder1"
		a2["AlbumName"]="Folder 1"
		a2["Album Type"]="Folder"
		a2["PhotoCount"]=1
		a2["KeyList"]=["1234"]
		list.add(a2)
		
		def pictureMap = ["1234":new Picture()]
		
		def albumMap = AlbumDataParser.processAlbums(list, pictureMap);
		println "have list: $albumMap"
		assert 2==albumMap.size();
		
		
		def album = albumMap["uniq1"]
		assert 1 == album.pictures.size()
		assert "event" == album.type
		assert "Folder 1" == album.parent.name
		assert "Folder 1/name1/" == album.getPath()
	}
	
	
	@Test
	public void testProcessFaces(){
		def picture1 = new Picture();
		picture1.faces.add("face1") 
		def picture2 = new Picture();
		picture2.faces.add("face1")
		
		def picture3 = new Picture();
		picture3.faces.add("face2")
		picture3.faces.add("face1")
		def pictureMap = ["1234": picture1, "2":picture2, "3":picture3]
		
		def faces=["face1": ["name":"faceName1"], 
				"face2":["name":"faceName1"]]
				
		def faceMap = AlbumDataParser.processFaces(faces, pictureMap)
		assert 2 == faceMap.size();
		
		println "got map back: $faceMap"
		
		def f1 = faceMap["face1"]
		assert 3 == f1.pictures.size()
		def f2 = faceMap["face2"]
		assert 1 == f2.pictures.size()
		
		
		
		
		
		
		
	}
}
