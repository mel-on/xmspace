package com.ximelon.xmspace.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.ximelon.xmspace.dao.ICommonDao;

@Repository("commonDao")
public class CommonDao implements ICommonDao {
	//private static Logger logger = LoggerFactory.getLogger(CommonDao.class);

	@Autowired
	protected HibernateTemplate hibernateTemplate;


	public Integer executeHql(final String hql, final Map<String, Object> params) {
		return this.doExecute(hql, params, true);
	}
	
	public Integer executeSql(final String sql, final Map<String, Object> params) {
		return this.doExecute(sql, params, false);
	}
	
	private Integer doExecute(final String hqlOrSql, final Map<String, Object> params,final boolean isHql) {
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) {
				Query query ;
				if(isHql){
					query=session.createQuery(hqlOrSql);
				}else{
					query=session.createSQLQuery(hqlOrSql);
				}
				Set<String> pNames = params.keySet();
				for (String pName : pNames) {
					if (params.get(pName) instanceof Collection<?>) {
						query.setParameterList(pName, (Collection<?>) params
								.get(pName));
					} else {
						query.setParameter(pName, params.get(pName));
					}
				}
				return query.executeUpdate();
			}
		});
	}
	
	public Integer executeHql(final String hql,final Object... params){
		return this.doExecute(hql, params, true);
	}
	public Integer executeSql(final String sql,final Object... params){
		return this.doExecute(sql, params, false);
	}
	
	private Integer doExecute(final String hqlOrSql,final Object[] params,final boolean isHql){
		return hibernateTemplate.execute(new   HibernateCallback<Integer>(){
			public Integer doInHibernate(Session session){
				 Query query=null;
				 if(isHql){
					 query=session.createQuery(hqlOrSql);
				 }else{
					 query=session.createSQLQuery(hqlOrSql);
				 }
				 if(params!=null&&params.length>0){
					 for(int i=0;i<params.length;i++){
						  query.setParameter(i, params[i]);
					  } 
				 }

				 return query.executeUpdate();
			}
		});
	}
	

	
	

	

	@SuppressWarnings("unchecked")
	private <T> List<T> find(final String sqlOrHql,Class<T> clazz,final boolean isHql,final int pageNo,final int pageSize, final Map<String,Object> params){
		return (List<T>) hibernateTemplate.executeFind(new   HibernateCallback<List<T>>(){
			public List<T> doInHibernate(Session session){
					Query query=null;
					if(isHql){
						query=session.createQuery(sqlOrHql);
					}else{
						query=session.createSQLQuery(sqlOrHql);
					}
					if(pageSize>0){
						query.setFirstResult((pageNo-1)*pageSize);
						query.setMaxResults(pageSize);
					}
					
					if(params!=null){
						Set<String> pNames=params.keySet();
						  for(String pName:pNames){
							  Object param = params.get(pName);
							  if(param instanceof Collection<?>){
								  query.setParameterList(pName,(Collection<?>)param);
							  }else{
								  query.setParameter(pName, params.get(pName));
							  }
						  }
					}

				  
				  return (List<T>)query.list();
			}});
	}
	
	
	@SuppressWarnings("unchecked")
	private <T> List<T> find(final String sqlOrHql, Class<T> clazz, final boolean isHql,
			final int pageNo, final int pageSize, final Object... params) {
		return (List<T>) hibernateTemplate.executeFind(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session) {
				Query query = null;
				if (isHql) {
					query = session.createQuery(sqlOrHql);
				} else {
					query = session.createSQLQuery(sqlOrHql);
				}
				if (pageSize > 0) {
					query.setFirstResult((pageNo - 1) * pageSize);
					query.setMaxResults(pageSize);
				}

				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(i, params[i]);
					}
				}

				return query.list();
			}
		});
	}
	
	
	/**
	 * 根据SQL原生语句和查询参数获取数据持久化对象集合
	 * @param <T>
	 * @param sql
	 * @return
	 */
	public <T> List<T> findBySql(final String sql,Class<T> clazz,final Map<String,Object> params){
		return this.find(sql, clazz, false,0,0, params);
	}
	
	public <T> List<T> findByHql(final String hql,Class<T> clazz,final Map<String,Object> params){
		return this.find(hql, clazz, true,0,0, params);
	}
	
	public <T> List<T> findByHql(final String hql,Class<T> clazz){
		return this.find(hql, clazz, true,0,0, (Object[])null);
	}

	public <T> List<T> findBySql(final String hql,Class<T> clazz){
		return this.find(hql, clazz, false,0,0,(Object[])null);
	}
	
	@Override
	public <T> List<T> findBySql(final String sql,final Class<T> clazz,final Object... params){
		return this.find(sql, clazz, false, 0, 0, params);
	}
	
	@Override
	public <T> List<T> findByHql(final String hql, final Class<T> clazz, final Object... params) {
		return this.find(hql, clazz, true, 0, 0, params);
	}

	public <T> List<T> findBySqlPageable(final String sql,Class<T> clazz,final int pageNo,final int pageSize,final Map<String,Object> params){
		return this.find(sql, clazz, false, pageNo, pageSize, params);
	}

	public <T> List<T> findByHqlPageable(final String hql,Class<T> clazz, final int pageNo,final int pageSize,final Map<String,Object> params){
		return this.find(hql, clazz, true, pageNo, pageSize, params);
	}


	

	public <T> List<T> findBySqlPageable(final String sql,Class<T> clazz,final int pageNo,final int pageSize){
		return this.find(sql, clazz, false, pageNo, pageSize, (Object[])null);
	}


	public Integer executeHql(final String hql) {
		return this.doExecute(hql, true);
	}
	
	public Integer executeSql(final String sql) {
		return this.doExecute(sql, false);
	}

	private Integer doExecute(final String hqlOrSql,final boolean isHql){
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) {
				Query query=null;
				if(isHql){
					query = session.createQuery(hqlOrSql);
				}else{
					query = session.createSQLQuery(hqlOrSql);
				}
				return query.executeUpdate();
			}
		});
	}
	
	
	
	
	

	public void deleteObject(Object obj) {
		hibernateTemplate.delete(obj);
	}

	public Object mergeObject(Object obj) {
		return hibernateTemplate.merge(obj);
	}

	public Serializable saveObject(Object obj) {
		return  hibernateTemplate.save(obj);
	}

	public void saveOrUpdateObject(Object obj) {
		hibernateTemplate.saveOrUpdate(obj);
	}

	public void updateObject(Object obj) {
		hibernateTemplate.update(obj);
	}


	@Override
	public <T> List<T> findByHqlPageable(String hql, Class<T> clazz, int pageNo, int pageSize) {
		return this.find(hql, clazz, true, pageNo, pageSize, (Object[])null);
	}
	
	private<T> T findOne(String sqlOrHql, Class<T> clazz,boolean isHql, Map<String, Object> params){
		List<T> list=this.find(sqlOrHql, clazz, isHql, 0, 0, params);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		//logger.info("findOne but found null");
		return null;
	}
	
	private<T> T findOne(String sqlOrHql, Class<T> clazz,boolean isHql, Object... params){
		List<T> list=this.find(sqlOrHql, clazz, isHql, 0, 0, params);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}



	@Override
	public <T> T findOneByHql(String hql, Class<T> clazz, Map<String, Object> params) {
		return this.findOne(hql, clazz, true, params);
	}

	@Override
	public <T> T findOneBySql(String sql, Class<T> clazz, Map<String, Object> params) {
		return this.findOne(sql, clazz, false, params);
	}

	@Override
	public <T> T findOneByHql(String hql, Class<T> clazz) {
		return this.findOne(hql, clazz, true, (Object[])null);
	}

	@Override
	public <T> T findOneBySql(String sql, Class<T> clazz) {
		return this.findOne(sql, clazz, false, (Object[])null);
	}

	@Override
	public <T> T findOneByHql(String hql, Class<T> clazz, Object... params) {
		return this.findOne(hql, clazz, true, params);
	}

	@Override
	public <T> T findOneBySql(String sql, Class<T> clazz, Object... params) {
		return this.findOne(sql, clazz, false, params);
	}

	@Override
	public <T> T getObject(Class<T> clazz, Serializable id) {
		return hibernateTemplate.get(clazz, id);
	}

	@Override
	public <T> List<T> getAllObject(Class<T> clazz) {
		return hibernateTemplate.loadAll(clazz);
	}

}
