package zupkeyvault.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertUtils {
	private ConvertUtils(){}
	
	
	public static <T> T byteToObject(byte[] myBytes, Class<T> clazz) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bis = new ByteArrayInputStream(myBytes);
		ObjectInput in = null;
		try {
		  in = new ObjectInputStream(bis);
		  T o = clazz.cast(in.readObject()); 
		  return o;
		} finally {
		  try {
		    if (in != null) {
		      in.close();
		    }
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		}
	}
	
	public static ByteArrayOutputStream objectToByteArrayOutputStream(Object object) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(object);
		  out.flush();
		} finally {
		  try {
		    bos.close();
		  } catch (IOException ex) {
		  }
		}
		return bos;
	}
	
	public static InputStream objectToInputStream(Object obj) throws IOException{
	    return new ByteArrayInputStream(objectToByteArrayOutputStream(obj).toByteArray());
	}

	public static String objectToJsonString(Object obj) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}
}
