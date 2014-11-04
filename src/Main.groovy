@Grapes([
	@Grab(group='org.slf4j', module='slf4j-api', version='1.6.1'),
	@Grab(group='ch.qos.logback', module='logback-classic', version='0.9.28'),
	@Grab(group='commons-cli', module='commons-cli', version='1.1'),
	@Grab(group='org.apache.commons', module='commons-io', version='1.3.2')
	])

import groovy.util.logging.Slf4j

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level;

import iPhotoParser.*



def log = LoggerFactory.getLogger("MAIN")

def cli = new CliBuilder(usage: 'groovy Main')
cli.f("list all faces")
cli.a("list all albums (except directories)")
cli.e("list all events")
cli.v("be little bit more verbose")
cli.h(longOpt: 'help', "print help")

cli.generateDefault("create example of property file")
cli.export("export accoarding to configuration")
cli.dryRun("do not actualy copy files, just execute and log")
cli.cfg(args:1, argName:'file', 'use given file as configuration')
def options = cli.parse(args)

if(options==null){
		log.error("fail to parse ${args}")
		System.exit(1)
}

if(options.h || options.help){
	cli.usage();
	System.exit(0)
}
if(options.generateDefault){
	log.info("generating default configuration, be sure to provide proper values!")
	def content = ExportConfiguration.generateDefault();
	new File("default.properties").write(content);
	log.info("file stored: default.properties")
	System.exit(0)
}

if(!options.cfg){
	log.error("--cfg <file> is mandatory")
	cli.usage()
	System.exit(1)
}

Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
if(options.v){
	root.setLevel(Level.DEBUG);
}else{
	root.setLevel(Level.INFO);
}

def configuration = new ExportConfiguration(options.cfg) 

//def ALBUM_PATH="/home/backup/pavel/Shared_Pictures/hlavni/"
def ALBUM_PATH=configuration.albumPath


log.info("Starting with configuration: "+configuration)
def parser = new AlbumDataParser(ALBUM_PATH)
def iArchive = parser.parse()

log.info("Succesfully parsed iPhoto Archive(${iArchive.name}): Albums(${iArchive.albumMap.size()}) Faces(${iArchive.facesMap.size()}) Pictures(${iArchive.pictureMap.size()})")


if(options.f){
	StringBuilder builder = new StringBuilder()
	builder.append("Faces: ")
	iArchive.facesMap.each{k,face ->
		builder.append("\"${face.name}\"")
		if(options.v) builder.append("[${face.pictures.size()}]")
		builder.append(", ")
	}
	log.info(builder.toString())
}
if(options.a){
	StringBuilder builder = new StringBuilder()
	builder.append("Albums:")
	iArchive.albumMap.each{k,album ->
		if(album.type=="Regular"){
			builder.append("\"${album.name}\"")
			if(options.v) builder.append("[${album.pictures.size()}]")
			builder.append(", ")
		}
	}
	log.info(builder.toString())
}
if(options.e){
	StringBuilder builder = new StringBuilder()
	builder.append("Events:")
	iArchive.albumMap.each{k,event ->
		if(event.type=="Event"){
			builder.append("\"${event.name}\"")
			if(options.v) builder.append("[${event.pictures.size()}]")
			builder.append(", ")
		}
	}
	log.info(builder.toString())
}

//def exportConfiguration = new ExportConfiguration()
//	.setExportPath("/data/tmp/test")
//	.setClearExport(true)
//	.addAlbum("Lucka si hraje s klukama").addAlbum("Svatava Hellerová")
//	.setNewerThanDate("2014-01-01")
//	.addFace("Daniela Donatova")
//	.addPathReplace("/Users/pavel/Shared_Pictures/hlavni/", "/data/export_iphoto/hlavni/")
if(options.export){
	log.info("Exporting ...")
	def exporter = new Exporter().setExportConfiguration(configuration).setAlbum(iArchive).setDryRun(options.dryRun)
	exporter.export()
}else{
	log.info("skipping export, --export was not provided")
}
log.info("I am done")

