package iPhotoParser;

import static org.junit.Assert.*;

import org.junit.Test;

class TimeStampReaderTest {

	@Test
	public void testConvertZero() {
		long unixTime = TimeStampReader.convertToLinuxDate(0)
		String date = TimeStampReader.convertToDate(0)
		assert unixTime==978303660000l
		assert "2001-01-01".equals(date)
	}
	@Test
	public void testConvertMyExample() {
		long unixTime = TimeStampReader.convertToLinuxDate(414600768.00)
		String date = TimeStampReader.convertToDate(414600768.00)
		assert unixTime==1392904428000
		assert "2014-02-20".equals(date)
	}
	
	@Test 
	public void testNewer(){
		assert TimeStampReader.isNewerOrEqual(414600768.00, "2014-01-20")
		assert TimeStampReader.isNewerOrEqual(414600768.00, "2014-02-19")
		assert TimeStampReader.isNewerOrEqual(414600768.00, "2013-03-19")
		assert TimeStampReader.isNewerOrEqual(414600768.00, "2014-02-20")
		assert !TimeStampReader.isNewerOrEqual(414600768.00, "2014-02-21")
	}
}
