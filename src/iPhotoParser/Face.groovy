package iPhotoParser

class Face {
	def key
	def name
	
	public String toString(){
		String result = String.format("Face(%15s)", name);
		return result
	}
}
