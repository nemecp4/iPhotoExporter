package iPhotoParser

class Album {
	def name
	def type
	def id
	def parent //string
	def timeInterval //event ProjectEarliestDateAsTimerInterval value
	def pictures = new ArrayList<String>()

	public void setPictures (List pics){
		pics.each{
			pictures.add(Integer.parseInt(it))
		}
	}

	public String toString(){
		String result = String.format("Album(%25s) - %10s - with %5d pictures", name, type, pictures.size());
		return result
	}

	public String getPath(){
		if(parent==null){
			return "${name}/"
		}else{
			return parent.getParentPath()+"${name}/"
		}
	}
	
	public String getTimeInterval(){
		return timeInterval;
	}

	public String getParentPath(){
		if(type!="Folder"){
			return ""
		}else if(parent==null){
			return "${name}/"
		}else{
			return parent.getParentPath()+"${name}/"
		}
	}
}
