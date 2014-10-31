@Grapes([
	@Grab(group='org.slf4j', module='slf4j-api', version='1.6.1'),
	@Grab(group='ch.qos.logback', module='logback-classic', version='0.9.28')
	])

import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import iPhotoParser.*



def log = LoggerFactory.getLogger("MAIN")

def ALBUM_PATH="/home/backup/pavel/Shared_Pictures/hlavni/"


log.info("Staarting")

def parser = new AlbumDataParser(ALBUM_PATH)
def iArchive = parser.parse()

StringBuilder builder = new StringBuilder()
builder.append("Succesfully parsed iPhoto Archive:")
builder.append(iArchive.name)
builder.append("\n")
builder.append("Albums:\n")
iArchive.albumMap.each{k,album ->
	if(album.type=="Regular")
	builder.append(String.format("%-40s of type %8s  with %4d pictures\n", album.getPath(), album.type, album.pictures.size()))
}
log.info(builder.toString())

builder = new StringBuilder()
builder.append("Events:\n")
iArchive.albumMap.each{k,album ->
	if(album.type=="Event")
	builder.append(String.format("%-40s of type %8s  with %4d pictures\n", album.name, album.type, album.pictures.size()))
}
log.info(builder.toString())


builder = new StringBuilder()

builder.append("Faces: \n")
iArchive.facesMap.each{k,face ->
	builder.append(String.format("%-30s with %-4d pictures\n",face.name, face.pictures.size()))
}
log.info(builder.toString())

def exportConfiguration = new ExportConfiguration()
	.setExportPath("/data/tmp/test")
	.setClearExport(true)
	.addAlbum("Lucka si hraje s klukama").addAlbum("Svatava Hellerová")
	.setNewerThanDate("2014-01-01")
	.addFace("Daniela Donatova")
	.addPathReplace("/Users/pavel/Shared_Pictures/hlavni/", "/data/export_iphoto/hlaveni/")

def exporter = new Exporter().setExportConfiguration(exportConfiguration).setAlbum(iArchive)
exporter.export()
