package iPhotoParser

class Picture {
	def faces = []
	def path
	def key
	
	@Override
	String toString() { 
		return "$key($path)"
	}
}
