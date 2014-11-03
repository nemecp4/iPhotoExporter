package iPhotoParser

import static org.junit.Assert.*;

import org.junit.Test;
public class ExportConfigurationTest{
	
	@Test
	public void arraySplitterTest(){
		def list
		list = ExportConfiguration.parseList("\"Adam\"")
		
		assert 1==list.size()
		assert "Adam"== list.get(0)
		
		
		list = ExportConfiguration.parseList("\"Adam\" , \"Sova\" ")
		assert 2==list.size()
		assert "Adam"== list.get(0)
		assert "Sova"== list.get(1)
		
		list = ExportConfiguration.parseList("Adam   ,  \"Sova \"")
		assert 2==list.size()
		assert "Adam"== list.get(0)
		assert "Sova "== list.get(1)
		
		list = ExportConfiguration.parseList("\"Daniela\",\"Pavel\", \"Adam\"")
		assert 3==list.size()
		assert "Daniela"== list.get(0)
		assert "Pavel"== list.get(1)
		assert "Adam"== list.get(2)
	}
}