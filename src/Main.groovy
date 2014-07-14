@Grapes([
	@Grab(group='org.slf4j', module='slf4j-api', version='1.6.1'),
	@Grab(group='ch.qos.logback', module='logback-classic', version='0.9.28')
	])

import groovy.util.logging.Slf4j
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iPhotoParser.*



def log = LoggerFactory.getLogger("MAIN")

def ALBUM_PATH="/home/backup/pavel/Shared_Pictures/hlavni/"


log.info("Staarting")

def parser = new AlbumDataParser(ALBUM_PATH)
parser.parse()
