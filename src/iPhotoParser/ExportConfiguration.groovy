package iPhotoParser

class ExportConfiguration {
	def albumList = []
	def facesList = []
	def newerThanDate=null;
	def exportPath = "/tmp/"
	def clearExport=false
	
	def pathReplaceFrom
	def pathReplaceTo
	
	public ExportConfiguration setClearExport(clear){
		this.clearExport=clear
		return this
	}
	
	public ExportConfiguration setExportPath(path){
		this.exportPath=path;
		return this
	}
	
	public ExportConfiguration addAlbum(albumName){
		albumList.add(albumName)
		return this
	}
	public ExportConfiguration setNewerThanDate(date){
		this.newerThanDate=date;
		return this;
	}
	public ExportConfiguration addFace(face){
		this.facesList.add(face);
		return this
	}
	public ExportConfiguration addPathReplace(from, to){
		this.pathReplaceFrom=from
		this.pathReplaceTo=to;
		return this
	}
	

}
