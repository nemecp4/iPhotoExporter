package iPhotoParser
import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Exporter {
	def log = LoggerFactory.getLogger("EXPORT")
	def configuration
	def iPhotoAlbum
	
	boolean execute = false;

	public Exporter setExportConfiguration(ExportConfiguration cfg){
		configuration = cfg;
		return this;
	}
	public Exporter setAlbum(iPhotoAlbum album){
		this.iPhotoAlbum=album
		return this
	}

	public void export(){
		def albumToExport = []
		
		for(String albumName: configuration.albumList){
			def album = iPhotoAlbum.getAlbumByName(albumName)
			if(album==null){
				log.info("can't find album for name:${albumName}")
			}else{
				log.info("adding album by name ${albumName}")
				albumToExport.add(album);
			}
		}
		
		if(configuration.newerThanDate!=null){
			log.info("adding newer than: ${configuration.newerThanDate}");
			for(Album a: iPhotoAlbum.albumMap.values()){
				if(a.type=="Folder") {
					continue;
				}
				if(TimeStampReader.isNewerOrEqual(a.getTimeInterval(), configuration.newerThanDate)){
					log.info("adding album ${a} by date ${configuration.newerThanDate}")
					albumToExport.add(a);
				}
				else{
					log.info("album ${a.name} date: ${TimeStampReader.convertToDate(a.getTimeInterval())} is older")
				}
			}	
		}
		for (String faceName: configuration.facesList){
			Face f = iPhotoAlbum.getFaceByName(faceName);
			if(f==null){
				log.info("can't find face for name:${f.name}")
			}else{
				log.info("adding face by name ${f.name}")
				albumToExport.add(f);
			}
		}
		log.info("going to export ${albumToExport.size}: items")
		
		File exportDir = new File(configuration.exportPath)
		
			
		if(exportDir.exists() && configuration.clearExport){
			if(execute){
				Files.deleteIfExists(exportDir.toPath())
			}else{
				log.info("going to delete $exportDir")
			}
		}
		if(!exportDir.exists()){
			if(execute){
				Files.createDirectories(exportDir.toPath())
			}else{
				log.info("going to create export dir: $exportDir")
			}
		}
		
		albumToExport.each{
			def replacer={it}
			if(configuration.pathReplaceFrom != null){
				replacer={it.replace(configuration.pathReplaceFrom, configuration.pathReplaceTo)}
			}
			def albumPath = "${exportDir}/${it.type}/${it.getPath()}"
			log.info("scheduled for export: ${it.name}/${it.type}) to ${albumPath}")
			it.pictures.each{
				def picName= new File(it.getPath()).getName();
				def picPath = replacer(it.getPath())
				log.info("\t${picPath} -> ${albumPath}/${picName}")
			}	
		}
	}
}
