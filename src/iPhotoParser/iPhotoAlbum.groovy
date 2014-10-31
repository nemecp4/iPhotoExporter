package iPhotoParser

public class iPhotoAlbum {
	def name
	def type
	def albumMap = [:]
	def facesMap = [:]
	def pictureMap = [:]
	
	public Album getAlbumByName(String albumName){
		
		def predicate={it.value.name.equals(albumName)}
		def albumEntry = albumMap.find(predicate);
		return albumEntry==null?null:albumEntry.value;
	}
	public Face getFaceByName(String faceName){
		
		def predicate={it.value.name.equals(faceName)}
		def faceEntry = facesMap.find(predicate);
		return faceEntry==null?null:faceEntry.value;
	}

}
