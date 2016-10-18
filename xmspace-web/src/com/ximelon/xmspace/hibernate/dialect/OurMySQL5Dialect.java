package com.ximelon.xmspace.hibernate.dialect;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

/**
 * 自定义hibernate方言
 * 支持中文排序convert_gbk(name);
 * @author chenzexin
 *
 */
public class OurMySQL5Dialect extends MySQL5Dialect{

	public OurMySQL5Dialect(){  
        super();  
        registerFunction("convert_gbk",new SQLFunctionTemplate(StandardBasicTypes.STRING, "convert(?1 using gbk)") );
    }  
}
