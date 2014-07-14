package iPhotoParser

class iPhotoAlbum {
	def name=null
	def type=null
	def albumMap=new HashMap<Integer,Album>()
	def faceMap = new HashMap<Integer,Face>()
	def pictures=new HashMap<Integer,Picture>()
	
	public void addAlbum(album){
		this.albumMap.put(album.getId(), album);
	}
	public void addFace(face){
		faceMap.putAt(face.getKey(), face);
	}
}
