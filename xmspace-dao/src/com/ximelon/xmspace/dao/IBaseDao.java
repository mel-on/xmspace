package com.ximelon.xmspace.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * 基础DAO接口
 * @author wangqin
 *
 * @param <T>
 * @param <PK>
 */
public interface IBaseDao<T,PK extends Serializable> extends  ICommonDao {
	/**
	 * 得到某数据对象的所有记录数
	 * @return
	 */
	public int getAllSize();
	
	public HibernateTemplate getHibernateTemplate();

	/**
	 * 根据主键得到数据对象
	 * @param key
	 * @return
	 */
	public T get(PK key);
	
	/**
	 * 得到某数据对象的所有记录
	 * @return
	 */
	public List<T> getAll();
	
	/**
	 * 更新一个对象
	 * @param object
	 */
	public void update(T obj);
	
	/**
	 * 新增一个对象
	 * @param obj
	 */
	public PK save(T obj);
	
	/**
	 * 删除一个对象
	 * @param obj
	 */
	public void delete(T obj); 
	
	public void saveOrUpdate(T obj);
	
	/**
	 * 保存或更新,不更改传入的参数obj,持久化结果以方法的返回值T返回
	 * @param obj
	 * @return T 保存或更新之后的持久化结果
	 */
	public T merge(T obj);
	
	/**
	 * 根据HQL语句得到持久化对象的集合
	 * @param hql
	 * @return
	 */
	public List<T> find(String hql);
	
	
	/**
	 * 根据HQL语句得到持久化对象的集合
	 * @param hql
	 * @return
	 */
	public List<T> find(String hql,Object...params );
	
	/**
	 * 根据HQL语句和分页参数获取参数获取指定位置的指定条数的数据持久化对象集合
	 * @param query
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<T> findPageable(String hql,int pageNo,int pageSize);
	
	
	/**
	 * 根据HQL语句，分页参数和查询参数获取参数获取指定位置的指定条数的数据持久化对象集合
	 * @param hql
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public List<T> findPageable(String hql,int pageNo,int pageSize,Map<String,Object> params);
	
	/**
	 * 根据HQL语句和查询参数得到持久化对象的集合
	 * @param hql
	 * @return
	 */
	public List<T> find(String hql,Map<String,Object> params);
	
	
}
