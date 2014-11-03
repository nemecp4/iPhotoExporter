package iPhotoParser

class ExportConfiguration {
	def albumList = []
	def facesList = []
	def newerThanDate=null;
	def albumPath
	def exportPath = "/tmp/"
	def clearExport=false
	
	def pathReplaceFrom
	def pathReplaceTo
	
	public ExportConfiguration(){
		//nop
	}
	public ExportConfiguration(propertyFileName){
		Properties props = new Properties()
		File propsFile = new File((String)propertyFileName)
		props.load(propsFile.newDataInputStream())
		
		albumList = parseList(props.albumList)
	
		facesList = parseList(props.faceList)

		newerThanDate=props.newerThanDate
		albumPath=props.albumPath
		exportPath=props.exportPath
		clearExport=props.clearExport
				
		pathReplaceFrom=props.pathReplaceFrom
		pathReplaceTo=props.pathReplaceTo
		
	}
	public static List<String> parseList(String line){
		def list = []
		if(line!=null && line.size()>0){
			def lineCroped = line.trim()
			def parts = lineCroped.split(",")
			parts.each {
				list.add(it.trim().replaceAll("\"", ""))
			}
		}
		return list
	}
	
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
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExportConfiguration [");
		if (albumList != null) {
			builder.append("albumList=");
			builder.append(albumList);
			builder.append(", ");
		}
		if (facesList != null) {
			builder.append("facesList=");
			builder.append(facesList);
			builder.append(", ");
		}
		if (newerThanDate != null) {
			builder.append("newerThanDate=");
			builder.append(newerThanDate);
			builder.append(", ");
		}
		if (albumPath != null) {
			builder.append("albumPath=");
			builder.append(albumPath);
			builder.append(", ");
		}
		if (exportPath != null) {
			builder.append("exportPath=");
			builder.append(exportPath);
			builder.append(", ");
		}
		if (clearExport != null) {
			builder.append("clearExport=");
			builder.append(clearExport);
			builder.append(", ");
		}
		if (pathReplaceFrom != null) {
			builder.append("pathReplaceFrom=");
			builder.append(pathReplaceFrom);
			builder.append(", ");
		}
		if (pathReplaceTo != null) {
			builder.append("pathReplaceTo=");
			builder.append(pathReplaceTo);
		}
		builder.append("]");
		return builder.toString();
	}

	
	

}
