package iPhotoParser

import groovy.util.logging.Slf4j;
@Slf4j
public class AlbumDataParser {
	def FACES = "List of Faces"
	def ALBUMS= "List of Albums"
	def MIL = "Master Image List"
	def albumPath;
	
	public AlbumDataParser(albumPath){
		this.albumPath=albumPath
	}
	def  parse(){
		log.info("Parsing {} started", albumPath)
		File xmlPath = new File(albumPath+"/"+"AlbumData.xml")

		if (!xmlPath.exists() || !xmlPath.isFile()){
			log.error ("can't read file ${xmlPath}")
		}
		def records = new XmlParser().parse(xmlPath)

	
		def iPhotoArchive = new iPhotoAlbum();

		// U - as unproccessed, it is map of arrasy of list of arrays of primitives (string, ints..
		Map<String,Object> recordsMapU = new HashMap<String,Object>()
		def rootDict = records.dict.get(0)
		recordsMapU =  parseDict(rootDict);

		def pictureMap = processImages(recordsMapU.get(MIL));
		def albumMap = processAlbums(recordsMapU.get(ALBUMS), pictureMap)
		def facesMap = processFaces(recordsMapU.get(FACES), pictureMap)

		iPhotoArchive.albumMap=albumMap
		iPhotoArchive.facesMap=facesMap
		iPhotoArchive.pictureMap=pictureMap
		return iPhotoArchive
	}

	def static processFaces(faceMapU, pictureMap){
		def faceMap=[:]
		def picToFace = [:]
		pictureMap.each{k,v->
			v.faces.each{face->
				if(picToFace[face]==null){
					picToFace[face]=[]
				}
				picToFace[face].add(v)
			}
		}
		faceMapU.each{k,v ->
			def f = new Face();
			f.key=k
			f.name=v["name"]

			if (picToFace.containsKey(k)){
				f.pictures.addAll(picToFace[k])
			}
			faceMap.put(k,f)
		}
		return faceMap
	}

	def static processAlbums(albumsList, pictureMap){
		def albumMap = [:]
		albumsList.each {
			Album a = new Album()
			a.id=it["AlbumId"]
			a.name=it["AlbumName"]
			a.type=it["Album Type"]
			it["KeyList"].each{
				a.pictures.add(pictureMap[it])
			}

			assert it["PhotoCount"] == a.pictures.size();
			albumMap.put(a.id, a)
		}
		//itterate again to eval parrents
		albumsList.each {
			if(it.containsKey("Parent")){
				albumMap[it["AlbumId"]].parent=albumMap[it["Parent"]]
			}
		}
		return albumMap
	}

	def static processImages(map){
		def imageMap = [:]
		map.each{k,v ->
			def p = new Picture();
			p.path=v["ImagePath"]
			p.key=k
			if(v.containsKey("Faces")){
				v["Faces"].each{
					p.faces.add(it["face key"])
				}
			}
			imageMap.put(k,p)
		}
		return imageMap;
	}

	public static Object parseValue(Node n){
		String type = n.name();

		if(type=="dict"){
			return parseDict(n)
		}
		if(type=="array"){
			return parseArray(n)
		}
		if(type=="string" || type=="real"){
			return n.text()
		}
		if (type=="integer"){
			return Integer.parseInt(n.text())
		}

		if (type=="true"){
			return Boolean.TRUE
		}
		if(type =="false"){
			return Boolean.FALSE
		}
		throw new RuntimeException("unkown type $type when parsing node: $n")
	}

	public static Map<String,Object> parseDict(Node n){
		def map = [:]
		Iterator myIterator = n.iterator();
		while(myIterator.hasNext()){
			Node key = myIterator.next();
			Node value = myIterator.next();
			if(key.name()!="key"){
				throw new RuntimeException("unexpected key, ${key.name()}")
			}
			//log.info("parsing dict: ${key.name()} X ${value.name()}")
			map.put(key.text(), parseValue(value))
		}
		return map
	}

	public static List parseArray(Node n){
		def list = []
		Iterator myIterator = n.iterator();
		while(myIterator.hasNext()){
			Node type = myIterator.next()
			list.add(parseValue(type))
		}
		return list
	}
}
