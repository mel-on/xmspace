package com.ximelon.xmspace.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ICommonDao {
	
	public Integer executeHql(final String hql, final Map<String, Object> params) ;
	public Integer executeSql(final String sql, final Map<String, Object> params);
	public Integer executeHql(final String hql, final Object... params);
	public Integer executeSql(final String sql, final Object... params);
	public Integer executeHql(final String hql);
	public Integer executeSql(final String sql);
	

	public <T> List<T> findByHql(final String hql,Class<T> clazz,final Map<String,Object> params);
	public <T> List<T> findBySql(final String sql,Class<T> clazz,final Map<String,Object> params);
	public <T> List<T> findByHql(final String hql,Class<T> clazz);
	public <T> List<T> findBySql(final String sql,Class<T> clazz);
	public <T> List<T> findByHql(final String hql,final Class<T> clazz,final Object... params);
	public <T> List<T> findBySql(final String sql,final Class<T> clazz,final Object... params);
	
	public <T> T findOneByHql(final String hql,Class<T> clazz,final Map<String,Object> params);
	public <T> T findOneBySql(final String sql,Class<T> clazz,final Map<String,Object> params);
	public <T> T findOneByHql(final String hql,Class<T> clazz);
	public <T> T findOneBySql(final String sql,Class<T> clazz);
	public <T> T findOneByHql(final String hql,final Class<T> clazz,final Object... params);
	public <T> T findOneBySql(final String sql,final Class<T> clazz,final Object... params);
	

	public <T> List<T> findBySqlPageable(final String sql,Class<T> clazz, final int pageNo,final int pageSize);
	public <T> List<T> findByHqlPageable(final String hql,Class<T> clazz, final int pageNo,final int pageSize);
	public <T> List<T> findBySqlPageable(final String sql,Class<T> clazz, final int pageNo,final int pageSize, final Map<String,Object> params);
	public <T> List<T> findByHqlPageable(final String hql,Class<T> clazz, final int pageNo,final int pageSize, final Map<String,Object> params);


	public void updateObject(Object obj);
	
	public <T> T getObject(Class<T> clazz,Serializable id);
	
	public <T> List<T> getAllObject(Class<T> clazz);

	public Serializable saveObject(Object obj);

	public void deleteObject(Object obj); 
	
	public void saveOrUpdateObject(Object obj);

	public Object mergeObject(Object obj);
	
}
