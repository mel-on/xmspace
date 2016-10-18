package com.ximelon.xmspace.dao;


/**
 * 对于一般的查询，不需使用与实体bean对应的DAO，可视同CommonDao的通用方法
 * 由于其他实体的Dao类是CommonDao的子类，导致在service中注入ICommonDao需要使用@Qualifier来指定
 * 因而创建IGenericDao 以及实现类GenericDao，使注入更方便
 * 
 * @author xujianhui
 *
 */
public interface IGenericDao extends ICommonDao {


}
