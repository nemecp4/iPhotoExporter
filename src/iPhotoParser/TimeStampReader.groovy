package iPhotoParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampReader {
	private static final long TIMERINTERVAL_EPOCH = 978303660000l;
	private static final long APPLE_TIME_INTERVAL_MAGIC_CONSTANT = 1000l;
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public static long convertToLinuxDate(String doubleAsString){
		return convertToLinuxDate(Double.parseDouble(doubleAsString))
	}
	
	public static long convertToLinuxDate(double appleInterval){
		return TIMERINTERVAL_EPOCH + (long)(APPLE_TIME_INTERVAL_MAGIC_CONSTANT * appleInterval)
	}
	
	public static String convertToDate(appleInterval){
		if(appleInterval == null){
			return "NO DATE RECORDED"
		}
		return formatter.format(convertToLinuxDate(appleInterval))
	}
	public static boolean isNewerOrEqual(appleInterval, isoDateAsString){
		if(appleInterval==null || isoDateAsString==null){
			return false;
		}
		long date = formatter.parse(isoDateAsString).getTime();
		long unixDate = convertToLinuxDate(appleInterval);
		return unixDate>=date;
	}
	
	public static void main(String[] argc) {
		//our $TIMERINTERVAL_EPOCH = 978307200; # Epoch of TimeInterval zero point: 2001.01.01
		long TIMERINTERVAL_EPOCH = 978303660000l;
		String EPOCH="2001-01-01";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date epochDate=null;
		try {
			epochDate = formatter.parse(EPOCH);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(EPOCH+"="+epochDate.getTime());
		
		//double realTS =330769419.000000;
		//double realTS =330769419000l;
		double realTS=414600768.00;
				
		long ts = (long) (realTS*1000);

		String output;
		

		output = formatter.format(TIMERINTERVAL_EPOCH+ts);
		System.out.println(TIMERINTERVAL_EPOCH+ts + "=" + output);
		
		long current = System.currentTimeMillis();
		output = formatter.format(TIMERINTERVAL_EPOCH);
		System.out.println(current + "=" + output);
	}
}
