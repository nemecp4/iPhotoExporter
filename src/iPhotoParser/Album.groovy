package iPhotoParser

class Album {
	def name
	def type
	def id
	def pictures = new ArrayList<Integer>()
	
	public void setPictures (List pics){
		pics.each{
			pictures.add(Integer.parseInt(it))
		}
	}
	
	public String toString(){
		String result = String.format("Album(%25s) - %10s - with %5d pictures", name, type, pictures.size());
		return result
	}
	
	
}
