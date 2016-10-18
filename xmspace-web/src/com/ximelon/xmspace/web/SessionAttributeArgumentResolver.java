package com.ximelon.xmspace.web;

import org.impalaframework.extension.mvc.annotation.SessionAttribute;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


public class SessionAttributeArgumentResolver  implements HandlerMethodArgumentResolver {
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(SessionAttribute.class)!=null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		SessionAttribute sessionAttribute=parameter.getParameterAnnotation(SessionAttribute.class);
		String attributeName=sessionAttribute.value();
		if (attributeName == null) {
			return null;
		}

		return webRequest.getAttribute(attributeName, 1);

	}


}
