/**
 * JSON转换工具类
 */
package zk.console.client;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * @author liutp
 * 
 */
public class GsonUtil {

	private static Gson gson;

	public static Gson createGson() {
		if (null == gson) {
			gson = new GsonBuilder().registerTypeAdapter(Date.class, new UtilDateSerializer())
					.registerTypeAdapter(Calendar.class, new UtilCalendarSerializer())
					.registerTypeAdapter(GregorianCalendar.class, new UtilCalendarSerializer())
					.setDateFormat(DateFormat.LONG).create();
		}
		return gson;
	}

	private static class UtilDateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {

		public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
			return new JsonPrimitive(date.getTime());
		}

		public Date deserialize(JsonElement element, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			return new Date(element.getAsJsonPrimitive().getAsLong());
		}

	}

	private static class UtilCalendarSerializer implements JsonSerializer<Calendar>,
			JsonDeserializer<Calendar> {

		public JsonElement serialize(Calendar cal, Type type, JsonSerializationContext context) {
			return new JsonPrimitive(Long.valueOf(cal.getTimeInMillis()));
		}

		public Calendar deserialize(JsonElement element, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(element.getAsJsonPrimitive().getAsLong());
			return cal;
		}
	}

	/**
	 * 根据json返回对象
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static Object fromJson(String json, Class<?> clazz) {
		return createGson().fromJson(json, clazz);
	}

	/**
	 * 获得对象json值
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		return createGson().toJson(obj);
	}

	public static <T> String toJson(Collection<T> list) {
		return createGson().toJson(list, new TypeToken<Collection<T>>() {
		}.getType());
	}

}
