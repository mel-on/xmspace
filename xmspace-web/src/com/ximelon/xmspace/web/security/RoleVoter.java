package com.ximelon.xmspace.web.security;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class RoleVoter implements AccessDecisionVoter {

	//@Override
	public boolean supports(ConfigAttribute attribute) {
		if(attribute!=null&&attribute instanceof MenuOperConfigAttribute){
			return true;
		}
		return false;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
		int result = ACCESS_ABSTAIN;
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		boolean checked = false;
		for (ConfigAttribute attribute : attributes) {
			if (this.supports(attribute)) {
				checked = true;
				MenuOperConfigAttribute moAttr = (MenuOperConfigAttribute) attribute;
				for (GrantedAuthority authority : authorities) {
					if (moAttr.getRole().equals(authority.getAuthority())) {
						result = ACCESS_GRANTED;
					}
				}
			}
		}
		if (result == ACCESS_ABSTAIN && checked) {
			result = ACCESS_DENIED;
		}
		return result;
	}

}
