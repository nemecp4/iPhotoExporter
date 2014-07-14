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
		//spike collections with lookahead stuff
		Collection.metaClass.eachWithPeek = { closure ->
			def last = null
			delegate?.each { current ->
				if (last) closure(last, current)
				last = current
			}
			if (last) closure(last, null)
		}

		def myPeek = { closure ->
			def last = null
			delegate?.each { current ->
				if (last) closure(last, current)
				last = current
			}
			if (last) closure(last, null)
		}

		log.info("Parsing {} started", albumPath)
		File xmlPath = new File(albumPath+"/"+"AlbumData.xml")

		if (!xmlPath.exists() || !xmlPath.isFile()){
			log.error ("can't read file ${xmlPath}")
		}
		def records = new XmlParser().parse(xmlPath)

		log.info("found ${records.name()} items")
		def iPhotoArchive = new iPhotoAlbum();

		Map<String,Object> recordsMap = new HashMap<String,Object>()
		records.dict.each{
			log.info (" got guy: ${it.name()} ")
			List children = it.children();

			Iterator myIterator = children.iterator();
			while(myIterator.hasNext()){
				Node key = myIterator.next();
				Node value = myIterator.next();
				if(key.name()!="key"){
					throw new RuntimeException("unexpected key, ${key.name()}")
				}
				def valueO;
				if(value.name()=="dict") recordsMap.put(key.value(), parseDict(value))

				log.info("tuupple: ${key.name()} ${value.name()}")
			}
		}

		/*records.dict.each{
		 log.info (" got guy: ${it.name()} ")
		 List children = it.children();
		 Iterator myIterator = children.iterator();
		 while(myIterator.hasNext()){
		 Node key = myIterator.next();
		 Node value = myIterator.next();
		 //log.info ("key(${key.text()})  value (${value.text()})")
		 if (key.name()=="key" && key.text()== ALBUMS) {
		 parseAlbums(iPhotoArchive, value.value())
		 }
		 if (key.name()=="key" && key.text()== MIL){
		 parseImages(iPhotoArchive, value.value())
		 }
		 if (key.name()=="key" && key.text()== FACES){
		 parseFaces(iPhotoArchive, value)
		 }
		 }
		 }*/
	}

	public static Map<String,Object> parseDict(Node n){
		log.info("got Node ${n.name()}")
		n.children().each{
			log.info("${it}")
		}

		Iterator myIterator = n.iterator();
		while(myIterator.hasNext()){
			Node key = myIterator.next();
			Node value = myIterator.next();
			if(key.name()!="key"){
				throw new RuntimeException("unexpected key, ${key.name()}")
			}
			log.info("tuupple: ${key.name()} ${value.name()}")
		}
	}

	private static void parseAlbums(archive, values){
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
