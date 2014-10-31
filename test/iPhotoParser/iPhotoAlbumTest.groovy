package iPhotoParser;
import static org.junit.Assert.*;

import org.junit.Test;

class iPhotoAlbumTest {

	@Test
	public void findFaceTest(){
		def album = new iPhotoAlbum();
		def f1 = new Face();
		f1.key="id1"
		f1.name="Test 1"
		album.facesMap = [:]
		album.facesMap.put(f1.name,f1);
		
		def face = album.getFaceByName("Test 1")
		assert face !=null
		assert face.key=="id1"
		 
	}
}