package com.gm.wine.util;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

public class JsonUtil {

	/**
	 * 将对象转换成json字符串
	 * 
	 * @param obj
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String objectToJson(Object obj)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

	/**
	 * 将对象转化成jsong字符串并写到文件中
	 * 
	 * @param fullfilename
	 * @param obj
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void writeJsonToFile(String fullfilename, Object obj)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(fullfilename), obj);
	}

	/**
	 * 
	 * Description:json对象写到文件里
	 * 
	 * @since 2012-1-10
	 * @author qingang
	 * @param path
	 *            目录
	 * @param filename
	 *            文件名
	 * @param obj
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */

	public static void writeJsonToFile(String path, String filename, Object obj)
			throws JsonGenerationException, JsonMappingException, IOException {
		File pathFile = new File(path);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(path, filename), obj);
	}

	/**
	 * 将json字符串转换成对象
	 * 
	 * @param <T>
	 * @param json
	 * @param t
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> Object jsonToObject(String json, Class<T> t)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, t);
	}

	/**
	 * 将json文件转化成对象
	 * 
	 * @param <T>
	 * @param fullname
	 * @param t
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> Object jsonFileToObject(String fullname, Class<T> t)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(new File(fullname), t);
	}

	/**
	 * 将json文件转化成对象
	 * 
	 * @param <T>
	 * @param fullname
	 * @param t
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> Object jsonFileToObject(File file, Class<T> t)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(file, t);
	}

	/**
	 * 
	 * @param <T>
	 * @param file
	 * @param valueType
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> Object jsonFileToObject(File file, JavaType valueType)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(file, valueType);
	}

	/**
	 * String转对象
	 * 
	 * @param <T>
	 * @param string
	 * @param t
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> Object jsonStringToObject(String string, Class<T> t)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(string, t);
	}

	/**
	 * String转list对象
	 * 
	 * @param <T>
	 * @param string
	 * @param valueType
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> Object jsonStringToObject(String string,
			JavaType valueType) throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(string, valueType);
	}

}
