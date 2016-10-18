package com.ximelon.xmspace.dao;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.Oracle10gDialect;

public class CustomOracleDialect extends Oracle10gDialect {
	public CustomOracleDialect() {
		super();
//		registerHibernateType(Types.CHAR, Hibernate.STRING.getName());	
//		registerHibernateType(Types.CLOB, Hibernate.STRING.getName());
//		registerHibernateType(Types.NUMERIC,19,Hibernate.LONG.getName());
//		registerHibernateType(Types.NUMERIC,10,Hibernate.INTEGER.getName());
//		registerHibernateType(Types.NUMERIC,5,Hibernate.SHORT.getName());
//		registerHibernateType(Types.NUMERIC,4,Hibernate.FLOAT.getName());
//		registerHibernateType(Types.NUMERIC,3,Hibernate.BYTE.getName());
//		registerHibernateType(Types.NUMERIC,0,Hibernate.BIG_DECIMAL.getName());
		registerHibernateType(Types.DATE,Hibernate.TIMESTAMP.getName());
	}

}

