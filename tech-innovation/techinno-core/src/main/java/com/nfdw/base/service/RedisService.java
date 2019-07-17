package com.nfdw.base.service;

import java.util.List;
import java.util.Set;

public interface RedisService {
	
	public boolean set(String key, Object value);
	
	public boolean set(String key, Object value, Long expireTime);
	
	public void remove(String... keys);
	
	public void removePattern(String pattern);
	
	public void remove(String key);
	
	public boolean exists(String key);
	
	public Object get(String key);
	
	public void hmSet(String key, Object hashKey, Object value);
	
	public Object hmGet(String key, Object hashKey);
	
	public Set<Object> hmKeys(String key);
	
	public void hmDel(String masterKey, Object... hashKey);
	
	public void lPush(String k, Object v);
	
	public List<Object> lRange(String k, long l, long l1);
	
	public void add(String key, Object value);
	
	public Set<Object> setMembers(String key);
	
	public void zAdd(String key, Object value, double scoure);
	
	public Set<Object> rangeByScore(String key, double scoure, double scoure1);
	
	public Object lPop(String k);
	
	public boolean expire(String key, long timeout);
	
	public long ttl(String key);
}
