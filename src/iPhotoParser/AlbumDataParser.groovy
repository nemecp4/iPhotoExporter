package iPhotoParser

import groovy.util.logging.Slf4j;
@Slf4j
class AlbumDataParser {
	def FACES = "List of Faces"
	def ALBUMS= "List of Albums"
	def MIL = "Master Image List"
	def albumPath;

	public AlbumDataParser(){
	}


	public AlbumDataParser(albumPath){
		this.albumPath=albumPath
	}

	public void parse(){
		log.info("Parsing {} started", albumPath)
		File xmlPath = new File(albumPath+"/"+"AlbumData.xml")

		if (!xmlPath.exists() || !xmlPath.isFile()){
			log.error ("can't read file ${xmlPath}")
		}
		def records = new XmlParser().parse(xmlPath)

		log.info("found ${records.name()} items")
		def iPhotoArchive = new iPhotoAlbum();

		// U - as unproccessed, it is map of arrasy of list of arrays of primitives (string, ints..
		Map<String,Object> recordsMapU = new HashMap<String,Object>()
		def rootDict = records.dict.get(0)
		log.info (" got root: ${rootDict.name()}")
		recordsMapU =  parseDict(rootDict);
		
		def pictureMap = processImages(recordsMapU.get(MIL));
		def albumMap = processAlbums(recordsMapU.get(ALBUMS), pictureMap)
		

		log.info("succesfully parsed ${pictureMap.size()} images")
		log.info("succesfully parsed ${albumMap.size()} album")
	}
	
	
	def static processAlbums(albumsList, pictureMap){
		def albumMap = [:]
		albumsList.each {
			Album a = new Album()
			a.id=it["AlbumId"]
			a.name=it["AlbumName"]
			a.type=it["Album Tupe"]
			it["KeyList"].each{
				a.pictures.add(pictureMap[it])
			}
			
			assert it["PhotoCount"] == a.pictures.size();
			albumMap.put(a.id, a)
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
	
	
/*
	private static void parseAlbums(Map rootNode){
		values.each(){
			List children = it.children();
			Iterator myIterator = children.iterator();
			Album album = new Album();
			while(myIterator.hasNext()){
				Node key = myIterator.next();
				Node value = myIterator.next();
				String kS = key.text();
				String vS = value.value();

				if(kS.equals("AlbumName")){
					album.setName(vS);
				}
				if(kS.equals("Album Type")){
					album.setType(vS)
				}
				if(kS.equals("AlbumId")){
					album.setId(vS)
				}
				if(kS.equals("KeyList")){
					//log.info ("have list of pictures in album ")
					def pics = []
					value.value().each{
						pics.add(it.text());
					}
					album.setPictures(pics)
				}
			}
			archive.addAlbum(album)
			log.info("adding album $album")
		}
	}*/
	//TODO try to lookup items by name rather then by silly parsing
	private static void parseFaces(archive, values){
		log.info ("X $values")
		String key = values.get("key")
		Node dict = values.get("dict");
		log.info ("XXX $key $dict ")
		/*values.each(){
		 List children = it.children();
		 Iterator myIterator = children.iterator();
		 Face face = new Face();
		 while(myIterator.hasNext()){
		 Node key = myIterator.next();
		 Node value = myIterator.next();
		 String kS = key.text();
		 String vS = value.value();
		 if(kS.equals("key")){
		 face.setKey(vS);
		 }
		 if(kS.equals("name")){
		 face.setName(vS)
		 }
		 }
		 archive.addFace(face)
		 log.info("adding face $face")
		 }*/
	}
	private static void parseImages(archive, values){
		log.info("parsing images: ")
	}
}
