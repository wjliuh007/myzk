/**
 * className:PropertiesUtil.java<br/>
 * @since 2017年3月14日 下午6:41:57
 * @aouthor eagle
 */
package zk.console.utils;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtil {

	private static ConcurrentHashMap<String, Properties> propsMap = new ConcurrentHashMap<String, Properties>();

	private static Logger logger = Logger.getLogger(PropertiesUtil.class);

	/**
	 * 基于Spring读取Properties的文件，返回属性值
	 *
	 * @param propertyKey
	 *            - 要获取属性值对应的键名
	 * @param propertyFilePath
	 *            - 属性文件路径，必须在类路径中
	 * @return 该属性文件中对应propertyKey的值，如果文件未找到或key不存在，返回null
	 * @author zhupengren
	 * @date 2014年4月17日 下午5:55:13
	 */
	public static String getProperty(String propertyKey, String propertyFilePath) {
		Properties props = getPropertys(propertyFilePath);
		return props == null ? null : props.getProperty(propertyKey);
	}

	/**
	 * 基于Spring读取Properties的文件，返回属性值
	 *
	 * @param propertyKey
	 *            - 要获取属性值对应的键名
	 * @param propertyFileFolder
	 *            - 属性文件所在文件夹
	 * @param propertyFileName
	 *            - 属性文件名
	 * @return 该属性文件中对应propertyKey的值，如果文件未找到或key不存在，返回null
	 * @author zhupengren
	 * @date 2014年4月17日 下午5:55:13
	 */
	public static String getProperty(String propertyKey, String propertyFileFolder, String propertyFileName) {
		return getProperty(propertyKey, String.format("%s/%s", propertyFileFolder, propertyFileName));
	}

	/**
	 * 基于Spring的属性查询器来读取Properties的文件，返回读取完的属性列表
	 * <p/>
	 * 首先会以全路径文件名称作为key，先去缓存中查找，如果未找到去类路径中查找加载，并放置在缓存中
	 *
	 * @param propertyFilePath
	 *            属性文件路径，必须在类路径中
	 * @return 如果文件不在类路径下或发生其他异常，返回null而非抛出异常
	 */
	public static Properties getPropertys(String propertyFilePath) {
		Properties properties = propsMap.get(propertyFilePath);
		try {
			if (properties == null) {
				properties = PropertiesLoaderUtils.loadAllProperties(propertyFilePath);
				propsMap.put(propertyFilePath, properties);
			}
		} catch (IOException e) {
			logger.info(propertyFilePath + "文件未找到!=========================");
			logger.error(e.getMessage(), e);
		}
		return properties;
	}

	/**
	 *
	 * @param propertyFileFolder
	 *            - 属性文件所在文件夹
	 * @param propertyFileName
	 *            - 属性文件名
	 * @return 读取到的属性列表
	 */
	public static Properties getProperties(String propertyFileFolder, String propertyFileName) {
		return getPropertys(String.format("%s/%s", propertyFileFolder, propertyFileName));
	}

}
