package com.ximelon.xmspace.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.ximelon.xmspace.dao.IBaseDao;
import com.ximelon.xmspace.dao.ICommonDao;

public class HibernateDao<T, PK extends Serializable> extends CommonDao implements IBaseDao<T, PK>, ICommonDao {
	
	// private static final Logger logger = LoggerFactory.getLogger(HibernateDao.class);
	
	protected Class<T> persistentClass;

	/**
	 * 获取指定类的某个泛型类
	 * 
	 * @param clazz
	 *            指定的类
	 * @param index
	 *            泛型序号
	 * @return 泛型类
	 */
	public static Class<?> getSuperClassGenricType(Class<?> clazz, int index){
		if(clazz == null)return null;
		Type genType = clazz.getGenericSuperclass();
		if(genType == null)return null;
		if (!(genType instanceof ParameterizedType)) {
			System.err.println(clazz.getSimpleName()+ "'s superclass not ParameterizedType");
			return null;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			System.err.println("Index: " + index + ", Size of " + clazz.getSimpleName()
					+ "'s Parameterized Type: " + params.length);
			return null;
		}
		if(params.length <= index)return null;
		if (!(params[index] instanceof Class<?>)) {
			System.err.println(clazz.getSimpleName()
							+ " not set the actual class on superclass generic parameter");
			return null;
		}
		return (Class<?>) params[index];
	}
	
	@SuppressWarnings("unchecked")
	public HibernateDao(){
		Class<?> beanClass = getSuperClassGenricType(getClass(), 0);
		persistentClass = (Class<T>)beanClass;
	}
	
	public HibernateTemplate getHibernateTemplate(){
		return hibernateTemplate;
	}
	
	/**
	 * 得到某数据对象的所有记录数
	 * @return
	 */
	public int getAllSize(){
		final String hql="select count(*) from "+persistentClass.getSimpleName();

		Long size = hibernateTemplate.execute(new   HibernateCallback<Long>(){
			public Long doInHibernate(Session session){
				 Query query=session.createQuery(hql);
				 return (Long)query.uniqueResult();
			}});
		return size.intValue();
	}
	
	/**
	 * 得到某数据对象的所有记录
	 * @return
	 */
	public List<T> getAll(){
		return hibernateTemplate.loadAll(persistentClass);
	}
	
	/**
	 * 更新一个对象
	 * @param object
	 */
	public void update(T obj){
		hibernateTemplate.update(obj);
	}
	
//	/**
//	 * 新增一个列表对象
//	 * @param obj
//	 */
//	public void saveAll(List<T> obj){
//		 hibernateTemplate.saveOrUpdateAll(obj);
//	}
	
	/**
	 * 新增一个对象
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	public PK save(T obj){
		return (PK)hibernateTemplate.save(obj);
	}
	
	/**
	 * 删除一个对象
	 * @param obj
	 */
	public void delete(T obj){
		hibernateTemplate.delete(obj);
	}
	
	/**
	 * 根据HQL语句得到持久化对象的集合
	 * @param hql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(String hql){
		return (List<T>) hibernateTemplate.find(hql);
	}
	
	/**
	 * 保存或更新
	 * @param obj
	 */
	public void saveOrUpdate(T obj){
		hibernateTemplate.saveOrUpdate(obj);
	}
	
	/**
	 * 保存或更新,不更改传入的参数obj,持久化结果以方法的返回值T返回
	 * @param obj
	 * @return T 保存或更新之后的持久化结果
	 */
	public T merge(T obj){
		return hibernateTemplate.merge(obj);
	}
	
	/**
	 * 根据HQL语句和分页参数获取参数获取指定位置的指定条数的数据持久化对象集合
	 * @param query
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findPageable(final String hql,final int pageNo,final int pageSize){
		return (List<T>) hibernateTemplate.executeFind(new   HibernateCallback<List<T>>(){
			public List<T> doInHibernate(Session session){
				  Query query=session.createQuery(hql);
				  query.setFirstResult((pageNo-1)*pageSize);
				  query.setMaxResults(pageSize);
				  return  query.list();
			}});
	}
	
	/**
	 * 根据主键得到数据对象
	 * @param key
	 * @return
	 */
	public T get(PK key){
		return hibernateTemplate.get(persistentClass, key);
	}
	
	/**
	 * 根据HQL语句，分页参数和查询参数获取参数获取指定位置的指定条数的数据持久化对象集合
	 * @param hql
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findPageable(final String hql,final int pageNo,final int pageSize,final Map<String,Object> params){
		return (List<T>) hibernateTemplate.executeFind(new   HibernateCallback<List<T>>(){
			public List<T> doInHibernate(Session session){
				  Query query=session.createQuery(hql);
				  query.setFirstResult((pageNo-1)*pageSize);
				  query.setMaxResults(pageSize);
				  Set<String> pNames=params.keySet();
				  for(String pName:pNames){
					  query.setParameter(pName, params.get(pName));
				  }
				  return  query.list();
			}});
	}
	
	/**
	 * 根据HQL语句和查询参数得到持久化对象的集合
	 * @param hql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(final String hql,final Map<String,Object> params){
		return (List<T>) hibernateTemplate.executeFind(new   HibernateCallback<List<T>>(){
			public List<T> doInHibernate(Session session){
				  Query query=session.createQuery(hql);
				  Set<String> pNames=params.keySet();
				  for(String pName:pNames){
					  Object param = params.get(pName);
					  if(param instanceof Collection){
						  query.setParameterList(pName, (Collection<?>)param);
					  }else{
						  query.setParameter(pName, params.get(pName));
					  }
				  }
				  return  query.list();
			}});
	}
	
	@SuppressWarnings("unchecked")
	public List<T> find(final String hql,final Object... params){
		return (List<T>) hibernateTemplate.executeFind(new   HibernateCallback<List<T>>(){
			public List<T> doInHibernate(Session session){
				  Query query=session.createQuery(hql);
				  if(params!=null){
					  for (int i=0;i<params.length;i++) {
						  query.setParameter(i, params[i]);
					  }
				  }
				  return  query.list();
			}});
	}

}
