package iPhotoParser

class Picture {
	def faces = []
	def path
	def key
	def timeStamp
	
	@Override
	String toString() { 
		return "$key($path)"
	}
	
	String getPath(){
		return path;
	}
}
