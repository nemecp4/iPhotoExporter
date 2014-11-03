package iPhotoParser
import java.nio.file.Paths
import java.nio.file.Files
import groovy.util.logging.Slf4j

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.apache.commons.io.FileUtils;

import static java.nio.file.StandardCopyOption.*;

class Exporter {
	def log = LoggerFactory.getLogger("EXPORT")
	def configuration
	def iPhotoAlbum
	
	boolean dryRun=false

	public Exporter setExportConfiguration(ExportConfiguration cfg){
		configuration = cfg;
		return this;
	}
	
	public Exporter setDryRun(dryRun){
		this.dryRun=dryRun
		return this
	}
	public Exporter setAlbum(iPhotoAlbum album){
		this.iPhotoAlbum=album
		return this
	}


	public void export(){
		def albumToExport = new HashSet()
		
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
					log.debug("album ${a.name} date: ${TimeStampReader.convertToDate(a.getTimeInterval())} is older")
				}
			}	
		}
		for (String faceName: configuration.facesList){
			Face f = iPhotoAlbum.getFaceByName(faceName);
			if(f==null){
				log.info("can't find face for name:${faceName}")
			}else{
				log.info("adding face by name ${f.name}")
				albumToExport.add(f);
			}
		}
		log.info("going to export ${albumToExport.size()}: items ${albumToExport}")

		
		File exportDir = new File(configuration.exportPath)
		
			
		if(exportDir.exists() && configuration.clearExport){
			log.debug("going to delete $exportDir")
			if(!dryRun) FileUtils.deleteDirectory(exportDir)
		}
		if(!exportDir.exists()){
			log.debug("going to create export dir: $exportDir")
			if(!dryRun) Files.createDirectories(exportDir.toPath())
		}
		
		
		albumToExport.each{
			def replacer={it}
			if(configuration.pathReplaceFrom != null){
				replacer={it.replace(configuration.pathReplaceFrom, configuration.pathReplaceTo)}
			}
			def albumPath = "${exportDir}/${it.type}/${it.getPath()}"
			log.info("scheduled for export: ${it.name}(${it.type}) to >${albumPath}<")
			
			log.debug("creating directory: ${albumPath}")
			if(!dryRun) Files.createDirectories( Paths.get(albumPath))
			
			it.pictures.each{
				def picName= new File(it.getPath()).getName();
				def picPathFrom = replacer(it.getPath())
				def picPathTo="${albumPath}/${it.key}_${picName}"
				
				log.debug("\tcp ${picPathFrom} -> ${picPathTo}")
				if(!dryRun) Files.copy( Paths.get(picPathFrom), Paths.get(picPathTo), COPY_ATTRIBUTES)
			}	
		}
	}
}
