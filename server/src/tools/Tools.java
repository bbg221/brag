package tools;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class Tools {
	public static String byteBufferToString(ByteBuffer buffer) {
		CharBuffer charBuffer = null;
		try {
			Charset charset = Charset.forName("UTF-8");
			CharsetDecoder decoder = charset.newDecoder();
			charBuffer = decoder.decode(buffer);
			buffer.flip();
			return charBuffer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static String covertArrToString(int[] arr) {
		String str = "";
		
		if (arr.length < 1) {
			return str;
		}
		
		str += arr[0];
		
		for (int i = 1; i < arr.length; i++) {
			str += "," + arr[i];
		}
		
		return str;
	}
	
	public static int[] covertStringToArr(String str) {
		String[] strArr = str.split(",");
		int[] result = new int[strArr.length];
		
		for (int i =0 ; i < strArr.length; i++) {
			result[i] = Integer.parseInt(strArr[i]);
		}
		
		return result;
	}
}
