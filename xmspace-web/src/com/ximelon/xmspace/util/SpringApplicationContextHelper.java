/**
 * ==========================================================
 * Sequence     Author       Date          Description
 *    1          hsq 	    2008-2-19        ����
 * ========================================================== 
 */

package com.ximelon.xmspace.util;

import org.springframework.context.ApplicationContext;

/**
 * <p>Title:       
 * <p>Description: ������Ϊspring�İ����࣬��Ҫ�Ǳ����ʼ�����ApplicationContext���Ա���һЩ������Ҫ����������spring����
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eshore Technology Co.Ltd.</p>
 *
 * @author         hsq
 * @version        1.0
 */

public class SpringApplicationContextHelper
{
    private static ApplicationContext context;

    /**
     * @return  context
     */
    public static ApplicationContext getContext()
    {
        return context;
    }

    /**
     * @param context: ��Ҫ���õ�context 
     */
    public static void setContext(ApplicationContext context)
    {
        SpringApplicationContextHelper.context = context;
    }
}
