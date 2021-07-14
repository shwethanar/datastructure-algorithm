# ds-algo
Datastructure Projects

https://stackoverflow.com/questions/3093112/certificateexception-no-name-matching-ssl-someurl-de-found


http://java.globinch.com/enterprise-java/security/fix-java-security-certificate-exception-no-matching-localhost-found/

https://stackoverflow.com/questions/8744607/how-to-add-subject-alernative-name-to-ssl-certs




Skip to content
Sign in
Sign up
Instantly share code, notes, and snippets.

sleroy/HttpServletRequestDebug.java
Created 4 years ago
 Code
 Revisions 2
 Stars 5
 Forks 1
How to debug an HttpServletRequest ,Servlet API 3.0
HttpServletRequestDebug.java
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ch.novartis.otdc.test;

import java.io.ObjectInputStream;
import java.security.Principal;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class HttpServletRequestDebug is an Utility class to print all the
 * informations of a {@link HttpServletRequest} and its session and cookies. The
 * functionality is wrapped with a trycatch to avoid any side-effect.
 */
public class HttpServletRequestDebug {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpServletRequestDebug.class);

	/**
	 * Debug the servle request
	 *
	 * @param httpServletRequest            the http servlet request
	 */
	public static void debugRequest(final HttpServletRequest httpServletRequest) {
		try {
			printRequest(httpServletRequest);
		} catch (final Throwable e) {
			LOGGER.error("Could not dump the servlet", e);
		}
	}

	/**
	 * Prints the request.
	 *
	 * @param httpServletRequest the http servlet request
	 */
	private static void printRequest(final HttpServletRequest httpServletRequest) {
		if (httpServletRequest == null) {
			return;
		}
		LOGGER.info("----------------------------------------");
		LOGGER.info("W4 HttpServletRequest");
		LOGGER.info("\tRequestURL : {}", httpServletRequest.getRequestURL());
		LOGGER.info("\tRequestURI : {}", httpServletRequest.getRequestURI());
		LOGGER.info("\tScheme : {}", httpServletRequest.getScheme());
		LOGGER.info("\tAuthType : {}", httpServletRequest.getAuthType());
		LOGGER.info("\tEncoding : {}", httpServletRequest.getCharacterEncoding());
		LOGGER.info("\tContentLength : {}", httpServletRequest.getContentLength());
		LOGGER.info("\tContentType : {}", httpServletRequest.getContentType());
		LOGGER.info("\tContextPath : {}", httpServletRequest.getContextPath());
		LOGGER.info("\tMethod : {}", httpServletRequest.getMethod());
		LOGGER.info("\tPathInfo : {}", httpServletRequest.getPathInfo());
		LOGGER.info("\tProtocol : {}", httpServletRequest.getProtocol());
		LOGGER.info("\tQuery : {}", httpServletRequest.getQueryString());
		LOGGER.info("\tRemoteAddr : {}", httpServletRequest.getRemoteAddr());
		LOGGER.info("\tRemoteHost : {}", httpServletRequest.getRemoteHost());
		LOGGER.info("\tRemotePort : {}", httpServletRequest.getRemotePort());
		LOGGER.info("\tRemoteUser : {}", httpServletRequest.getRemoteUser());
		LOGGER.info("\tSessionID : {}", httpServletRequest.getRequestedSessionId());
		LOGGER.info("\tServerName : {}", httpServletRequest.getServerName());
		LOGGER.info("\tServerPort : {}", httpServletRequest.getServerPort());
		LOGGER.info("\tServletPath : {}", httpServletRequest.getServletPath());

		LOGGER.info("");
		LOGGER.info("\tCookies");
		int i = 0;
		for (final Cookie cookie : httpServletRequest.getCookies()) {
			LOGGER.info("\tCookie[{}].name={}", i, cookie.getName());
			LOGGER.info("\tCookie[{}].comment={}", i, cookie.getComment());
			LOGGER.info("\tCookie[{}].domain={}", i, cookie.getDomain());
			LOGGER.info("\tCookie[{}].maxAge={}", i, cookie.getMaxAge());
			LOGGER.info("\tCookie[{}].path={}", i, cookie.getPath());
			LOGGER.info("\tCookie[{}].secured={}", i, cookie.getSecure());
			LOGGER.info("\tCookie[{}].value={}", i, cookie.getValue());
			LOGGER.info("\tCookie[{}].version={}", i, cookie.getVersion());
			i++;
		}
		LOGGER.info("\tDispatcherType : {}", httpServletRequest.getDispatcherType());
		LOGGER.info("");

		LOGGER.info("\tHeaders");
		int j = 0;
		final Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			final String headerName = headerNames.nextElement();
			final String header = httpServletRequest.getHeader(headerName);
			LOGGER.info("\tHeader[{}].name={}", j, headerName);
			LOGGER.info("\tHeader[{}].value={}", j, header);
			j++;
		}

		LOGGER.info("\tLocalAddr : {}", httpServletRequest.getLocalAddr());
		LOGGER.info("\tLocale : {}", httpServletRequest.getLocale());
		LOGGER.info("\tLocalPort : {}", httpServletRequest.getLocalPort());

		LOGGER.info("");
		LOGGER.info("\tParameters");
		int k = 0;
		final Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			final String paramName = parameterNames.nextElement();
			final String paramValue = httpServletRequest.getParameter(paramName);
			LOGGER.info("\tParam[{}].name={}", k, paramName);
			LOGGER.info("\tParam[{}].value={}", k, paramValue);
			k++;
		}

		LOGGER.info("");
		LOGGER.info("\tParts");
		int l = 0;
		try {
			for (final Object part : httpServletRequest.getParts()) {
				LOGGER.info("\tParts[{}].class={}", l, part != null ? part.getClass() : "");
				LOGGER.info("\tParts[{}].value={}", l, part != null ? part.toString() : "");
				l++;
			}
		} catch (final Exception e) {
			LOGGER.error("Exception e", e);
		}
		printSession(httpServletRequest.getSession());
		printUser(httpServletRequest.getUserPrincipal());

		try {
			LOGGER.info("Request Body : {}",
					IOUtils.toString(httpServletRequest.getInputStream(), httpServletRequest.getCharacterEncoding()));
			LOGGER.info("Request Object : {}", new ObjectInputStream(httpServletRequest.getInputStream()).readObject());
		} catch (final Exception e) {
			LOGGER.debug("Exception e", e);
		}
		LOGGER.info("----------------------------------------");
	}

	/**
	 * Prints the session.
	 *
	 * @param session the session
	 */
	private static void printSession(final HttpSession session) {
		LOGGER.info("-");
		if (session == null) {
			LOGGER.error("No session");
			return;
		}
		LOGGER.info("\tSession Attributes");
		LOGGER.info("\tSession.id:  {}", session.getId());
		LOGGER.info("\tSession.creationTime:  {}", session.getCreationTime());
		LOGGER.info("\tSession.lastAccessTime:  {}", session.getLastAccessedTime());
		LOGGER.info("\tSession.maxInactiveInterval:  {}", session.getMaxInactiveInterval());

		int k = 0;
		final Enumeration<String> attributeNames = session.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			final String paramName = attributeNames.nextElement();
			final Object paramValue = session.getAttribute(paramName);
			LOGGER.info("\tSession Attribute[{}].name={}", k, paramName);
			if (paramValue.getClass() != null) {
				LOGGER.info("\tSession Attribute[{}].class={}", k, paramValue.getClass());
			}
			LOGGER.info("\tSession Attribute[{}].value={}", k, paramValue);
			k++;
		}

	}

	/**
	 * Prints the user.
	 *
	 * @param userPrincipal the user principal
	 */
	private static void printUser(final Principal userPrincipal) {
		LOGGER.info("-");
		if (userPrincipal == null) {
			LOGGER.info("User Authentication : none");
			return;
		} else {
			LOGGER.info("User Authentication.name :  {}", userPrincipal.getName());
			LOGGER.info("User Authentication.class :  {}", userPrincipal.getClass());
			LOGGER.info("User Authentication.value :  {}", userPrincipal);
		}

	}

}
 to join this conversation on GitHub. Already have an account? Sign in to comment
© 2021 GitHub, Inc.
Terms
Privacy
Security
Status
Docs
Contact GitHub
Pricing
API
Training
Blog
About
