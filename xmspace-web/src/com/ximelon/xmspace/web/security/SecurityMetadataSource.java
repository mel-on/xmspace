package com.ximelon.xmspace.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntUrlPathMatcher;
import org.springframework.security.web.util.UrlMatcher;

import com.ximelon.xmspace.dao.IMenuDao;
import com.ximelon.xmspace.web.WebConst;

/**
 * 修改自org.springframework.security.web.access.intercept.
 * DefaultFilterInvocationSecurityMetadataSource 从数据库读取待过滤的url列表
 * 
 * @author xujianhui
 *
 */
public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	protected final Logger logger = LoggerFactory.getLogger(SecurityMetadataSource.class);
	private UrlMatcher urlMatcher = new AntUrlPathMatcher();
	private boolean stripQueryStringFromUrls;
	// private LinkedHashMap<String, Collection<ConfigAttribute>> requestMap=new
	// LinkedHashMap<String, Collection<ConfigAttribute>>();
	private Map<Object, Collection<ConfigAttribute>> mapToUse = new LinkedHashMap<Object, Collection<ConfigAttribute>>();

	private IMenuDao menuDao;

	public SecurityMetadataSource(IMenuDao menudao) {
		this.menuDao = menudao;
		loadMenu();

	}

	public void asyncLoadMenu() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				loadMenu();
			}

		}).start();

	}

	private void loadMenu() {
		try {
			LinkedHashMap<String, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<String, Collection<ConfigAttribute>>();
			List<Object[]> opList = menuDao.findBySql(
					"select m.url,rm.role_id from t_menu m left join t_role_menu rm on m.id=rm.menu_id order by m.url",
					Object[].class);
			if (opList != null && opList.size() > 0) {
				Map<String, Set<Long>> urlRoleMap = new HashMap<String, Set<Long>>();
				for (Object[] objects : opList) {
					String url = (String) objects[0];
					if (StringUtils.isNotEmpty(url)) {
						if (!urlRoleMap.containsKey(url)) {
							urlRoleMap.put(url, new HashSet<Long>());
						}
						if (objects[1] != null) {
							urlRoleMap.get(url).add(((Number) objects[1]).longValue());
						}
					}
				}
				for (String url : urlRoleMap.keySet()) {
					if (StringUtils.isNotEmpty(url)) {

						Set<Long> roleAppSet = urlRoleMap.get(url);
						List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>(roleAppSet.size());
						for (Long roleId : roleAppSet) {
							attributes.add(new MenuOperConfigAttribute(String.valueOf(roleId), null));
						}
						requestMap.put(url, attributes);
					} else {
						// 二级菜单没有URL，只用于菜单的分级展示
					}
				}
			}
			List<ConfigAttribute> defaultAttrList = new ArrayList<ConfigAttribute>();
			defaultAttrList.add(new MenuOperConfigAttribute(WebConst.USER_DEFAULT_AUTHORITY, null));
			requestMap.put("/**", defaultAttrList);
			Map<Object, Collection<ConfigAttribute>> tempMapToUse = new LinkedHashMap<Object, Collection<ConfigAttribute>>();
			for (Map.Entry<String, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
				tempMapToUse.put(urlMatcher.compile(entry.getKey()), entry.getValue());
				// addSecureUrl(entry.getKey(), entry.getValue());
			}
			mapToUse = tempMapToUse;
		} catch (Exception e) {
			logger.error("加载菜单失败", e);
		}
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
		for (Collection<ConfigAttribute> attrs : mapToUse.values()) {
			allAttributes.addAll(attrs);
		}
		return allAttributes;
	}

	public Collection<ConfigAttribute> getAttributes(Object object) {
		if ((object == null) || !this.supports(object.getClass())) {
			throw new IllegalArgumentException("Object must be a FilterInvocation");
		}
		String url = ((FilterInvocation) object).getRequestUrl();
		return lookupAttributes(url);
	}

	public final Collection<ConfigAttribute> lookupAttributes(String url) {
		if (stripQueryStringFromUrls) {
			int firstQuestionMarkIndex = url.indexOf("?");
			if (firstQuestionMarkIndex != -1) {
				url = url.substring(0, firstQuestionMarkIndex);
			}
		}
		if (urlMatcher.requiresLowerCaseUrl()) {
			url = url.toLowerCase();
		}
		Collection<ConfigAttribute> attributes = extractMatchingAttributes(url, mapToUse);
		return attributes;
	}

	private Collection<ConfigAttribute> extractMatchingAttributes(String url,
			Map<Object, Collection<ConfigAttribute>> map) {
		if (map == null) {
			return null;
		}
		for (Map.Entry<Object, Collection<ConfigAttribute>> entry : map.entrySet()) {
			boolean matched = urlMatcher.pathMatchesUrl(entry.getKey(), url);
			if (matched) {
				return entry.getValue();
			}
		}
		return null;
	}

	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	protected UrlMatcher getUrlMatcher() {
		return urlMatcher;
	}

	public boolean isConvertUrlToLowercaseBeforeComparison() {
		return urlMatcher.requiresLowerCaseUrl();
	}

	public void setStripQueryStringFromUrls(boolean stripQueryStringFromUrls) {
		this.stripQueryStringFromUrls = stripQueryStringFromUrls;
	}

}
