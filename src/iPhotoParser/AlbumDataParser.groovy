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

		Map<String,Object> recordsMap = new HashMap<String,Object>()
		def rootDict = records.dict.get(0)
		log.info (" got root: ${rootDict.name()}")
		recordsMap=  parseDict(rootDict);
	}
	
	public static Object parseValue(Node n){
		String type = n.name();
		
		if(type=="dict"){
			return parseDict(n)
		}
		if(type=="array"){
			return parseArray(n)
		}
		if(type=="string"){
			return n.value()
		}
		if (type=="integer"){
			return Integer.parseInt(n.value())
		}
		throw new RuntimeException("unkown type $type")
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
			map.put(key.value(), parseValue(value))
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
	}
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
