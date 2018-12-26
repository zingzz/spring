package com.ssm.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.ssm.commom.util.SerializeUtil;
import com.ssm.commom.util.SpringContextUtil;
import com.ssm.core.shiro.cache.JedisManager;

import redis.clients.jedis.Jedis;
/**
 * 
 * 寮�鍙戝叕鍙革細SOJSON鍦ㄧ嚎宸ュ叿 <p>
 * 鐗堟潈鎵�鏈夛細漏 www.sojson.com<p>
 * 鍗氬鍦板潃锛歨ttp://www.sojson.com/blog/  <p>
 * <p>
 * 鍘熺敓jedis鎿嶄綔
 * 绠�鍗曞皝瑁呯殑Cache
 * 
 * <p>
 * 
 * 鍖哄垎銆�璐ｄ换浜恒��鏃ユ湡銆�銆�銆�銆�璇存槑<br/>
 * 鍒涘缓銆�鍛ㄦ煆鎴愩��2016骞�6鏈�2鏃� 銆�<br/>
 *
 * @author zhou-baicheng
 * @email  so@sojson.com
 * @version 1.0,2016骞�6鏈�2鏃� <br/>
 * 
 */
@SuppressWarnings("unchecked")
public class VCache {


	/****
	 *
	 *
	 * 杩欓噷鑰佹湁鍚屽闂紝杩欎釜VCache鍜� {@link JedisManager} 鏈変粈涔堝尯鍒紝
	 *
	 * 娌″暐鍖哄埆锛屾垜鍙槸鎯虫妸shiro鐨勬搷浣滃拰 涓氬姟Cache鐨勬搷浣滃垎寮�
	 *
	 *
	 *
	 */




	final static JedisManager J = SpringContextUtil.getBean("jedisManager", JedisManager.class);
	private VCache() {}
	
	/**
	 * 绠�鍗曠殑Get
	 * @param <T>
	 * @param key
	 * @param requiredType
	 * @return
	 */
	public static <T> T get(String key , Class<T>...requiredType){
		Jedis jds = null;
        boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			byte[] skey = SerializeUtil.serialize(key);
			return SerializeUtil.deserialize(jds.get(skey),requiredType);
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jds, isBroken);
        }
		return null;
	}
	/**
	 * 绠�鍗曠殑set
	 * @param key
	 * @param value
	 */
	public static void set(Object key ,Object value){
		Jedis jds = null;
        boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			byte[] skey = SerializeUtil.serialize(key);
			byte[] svalue = SerializeUtil.serialize(value);
			jds.set(skey, svalue);
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jds, isBroken);
        }
	}
	/**
	 * 杩囨湡鏃堕棿鐨�
	 * @param key
	 * @param value
	 * @param timer 锛堢锛�
	 */
	public static void setex(Object key, Object value, int timer) {
		Jedis jds = null;
        boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			byte[] skey = SerializeUtil.serialize(key);
			byte[] svalue = SerializeUtil.serialize(value);
			jds.setex(skey, timer, svalue);
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jds, isBroken);
        }
		
	}
	/**
	 * 
	 * @param <T>
	 * @param mapkey map
	 * @param key	 map閲岀殑key
	 * @param requiredType value鐨勬硾鍨嬬被鍨�
	 * @return
	 */
	public static <T> T getVByMap(String mapkey,String key , Class<T> requiredType){
		Jedis jds = null;
		boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			byte[] mkey = SerializeUtil.serialize(mapkey);
			byte[] skey = SerializeUtil.serialize(key);
			List<byte[]> result = jds.hmget(mkey, skey);
			if(null != result && result.size() > 0 ){
				byte[] x = result.get(0);
				T resultObj = SerializeUtil.deserialize(x, requiredType);
				return resultObj;
			}
			
		} catch (Exception e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			returnResource(jds, isBroken);
		}
		return null;
	}
	/**
	 * 
	 * @param mapkey map
	 * @param key	 map閲岀殑key
	 * @param value   map閲岀殑value
	 */
	public static void setVByMap(String mapkey,String key ,Object value){
		Jedis jds = null;
        boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			byte[] mkey = SerializeUtil.serialize(mapkey);
			byte[] skey = SerializeUtil.serialize(key);
			byte[] svalue = SerializeUtil.serialize(value);
			jds.hset(mkey, skey,svalue);
        } catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jds, isBroken);
        }
		
	}
	/**
	 * 鍒犻櫎Map閲岀殑鍊�
	 * @param mapKey
	 * @param dkey
	 * @return
	 */
	public static Object delByMapKey(String mapKey ,String...dkey){
		Jedis jds = null;
		boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			byte[][] dx = new byte[dkey.length][];
			for (int i = 0; i < dkey.length; i++) {
				dx[i] = SerializeUtil.serialize(dkey[i]);
			}
			byte[] mkey = SerializeUtil.serialize(mapKey);
			Long result = jds.hdel(mkey, dx);
			return result;
		} catch (Exception e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			returnResource(jds, isBroken);
		}
		return new Long(0);
	}
	
	/**
	 * 寰�redis閲屽彇set鏁翠釜闆嗗悎
	 * 
	 * @param <T>
	 * @param setKey
	 * @param start
	 * @param end
	 * @param requiredType
	 * @return
	 */
	public static <T> Set<T> getVByList(String setKey,Class<T> requiredType){
		Jedis jds = null;
		boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			byte[] lkey = SerializeUtil.serialize(setKey);
			Set<T> set = new TreeSet<T>();
			Set<byte[]> xx = jds.smembers(lkey);
			for (byte[] bs : xx) {
				T t = SerializeUtil.deserialize(bs, requiredType);
				set.add(t);
			}
			return set;
		} catch (Exception e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			returnResource(jds, isBroken);
		}
		return null;
	}
	/**
	 * 鑾峰彇Set闀垮害
	 * @param setKey
	 * @return
	 */
	public static Long getLenBySet(String setKey){
		Jedis jds = null;
		boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			Long result = jds.scard(setKey);
			return result;
		} catch (Exception e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			returnResource(jds, isBroken);
		}
		return null;
	}
	/**
	 * 鍒犻櫎Set
	 * @param dkey
	 * @return
	 */
	public static Long delSetByKey(String key,String...dkey){
		Jedis jds = null;
		boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			Long result = 0L;
			if(null == dkey){
				result = jds.srem(key);
			}else{
				result = jds.del(key);
			}
			return result;
		} catch (Exception e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			returnResource(jds, isBroken);
		}
		return new Long(0);
	}
	/**
	 * 闅忔満 Set 涓殑涓�涓��
	 * @param key
	 * @return
	 */
	public static String srandmember(String key){
		Jedis jds = null;
		boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			String result = jds.srandmember(key);
			return result;
		} catch (Exception e){ 
			isBroken = true;
			e.printStackTrace();
		} finally {
			returnResource(jds, isBroken);
		}
		return null;
	}
	/**
	 * 寰�redis閲屽瓨Set
	 * @param setKey
	 * @param value
	 */
	public static void setVBySet(String setKey,String value){
		Jedis jds = null;
        boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			jds.sadd(setKey, value);
		} catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jds, isBroken);
        }
	}
	/**
	 * 鍙杝et 
	 * @param key
	 * @return
	 */
	public static Set<String> getSetByKey(String key){
		Jedis jds = null;
        boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			Set<String> result = jds.smembers(key);
			return result;
		} catch (Exception e) {
            isBroken = true;
            e.printStackTrace();
        } finally {
            returnResource(jds, isBroken);
        }
        return null;
		 
	}
	
	
	/**
	 * 寰�redis閲屽瓨List
	 * @param listKey
	 * @param value
	 */
	public static void setVByList(String listKey,Object value){
		Jedis jds = null;
		boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			byte[] lkey = SerializeUtil.serialize(listKey);
			byte[] svalue = SerializeUtil.serialize(value);
			jds.rpush(lkey, svalue);
		} catch (Exception e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			returnResource(jds, isBroken);
		}
	}
	/**
	 * 寰�redis閲屽彇list
	 * 
	 * @param <T>
	 * @param listKey
	 * @param start
	 * @param end
	 * @param requiredType
	 * @return
	 */
	public static <T> List<T> getVByList(String listKey,int start,int end,Class<T> requiredType){
		Jedis jds = null;
		boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			byte[] lkey = SerializeUtil.serialize(listKey);
			List<T> list = new ArrayList<T>();
			List<byte[]> xx = jds.lrange(lkey,start,end);
			for (byte[] bs : xx) {
				T t = SerializeUtil.deserialize(bs, requiredType);
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			returnResource(jds, isBroken);
		}
		return null;
	}
	/**
	 * 鑾峰彇list闀垮害
	 * @param listKey
	 * @return
	 */
	public static Long getLenByList(String listKey){
		Jedis jds = null;
		boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			byte[] lkey = SerializeUtil.serialize(listKey);
			Long result = jds.llen(lkey);
			return result;
		} catch (Exception e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			returnResource(jds, isBroken);
		}
		return null;
	}
	/**
	 * 鍒犻櫎
	 * @param dkey
	 * @return
	 */
	public static Long delByKey(String...dkey){
		Jedis jds = null;
		boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			byte[][] dx = new byte[dkey.length][];
			for (int i = 0; i < dkey.length; i++) {
				dx[i] = SerializeUtil.serialize(dkey[i]);
			}
			Long result = jds.del(dx);
			return result;
		} catch (Exception e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			returnResource(jds, isBroken);
		}
		return new Long(0);
	}
	/**
	 * 鍒ゆ柇鏄惁瀛樺湪
	 * @param existskey
	 * @return
	 */
	public static boolean exists(String existskey){
		Jedis jds = null;
		boolean isBroken = false;
		try {
			jds = J.getJedis();
			jds.select(0);
			byte[] lkey = SerializeUtil.serialize(existskey);
			return jds.exists(lkey);
		} catch (Exception e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			returnResource(jds, isBroken);
		}
		return false;
	}
	/**
	 * 閲婃斁
	 * @param jedis
	 * @param isBroken
	 */
	public static void returnResource(Jedis jedis, boolean isBroken) {
        if (jedis == null)
            return;
//        if (isBroken)
//            J.getJedisPool().returnBrokenResource(jedis);
//        else
//        	J.getJedisPool().returnResource(jedis);
//        鐗堟湰闂
        jedis.close();
	 }
}
