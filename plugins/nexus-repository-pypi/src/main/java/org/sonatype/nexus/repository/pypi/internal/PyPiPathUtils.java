/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.repository.pypi.internal;

import java.util.Locale;

import org.sonatype.nexus.repository.http.HttpMethods;
import org.sonatype.nexus.repository.view.Context;
import org.sonatype.nexus.repository.view.Request;
import org.sonatype.nexus.repository.view.matchers.token.TokenMatcher;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility methods for working with PyPI routes and paths.
 *
 * @since 3.1
 */
public final class PyPiPathUtils
{
  /**
   * Returns whether or not the request is a PyPI search request.
   */
  static boolean isSearchRequest(final Request request) {
    return HttpMethods.POST.equals(request.getAction()) && request.getPath().endsWith("/pypi");
  }

  /**
   * Returns the {@link TokenMatcher.State} for the content.
   */
  static TokenMatcher.State matcherState(final Context context) {
    return context.getAttributes().require(TokenMatcher.State.class);
  }

  /**
   * Returns the name from a {@link TokenMatcher.State}.
   */
  static String name(final TokenMatcher.State state) {
    return match(state, "name");
  }

  /**
   * Returns the name from a {@link TokenMatcher.State}.
   */
  static String path(final TokenMatcher.State state) {
    return match(state, "path");
  }

  /**
   * Utility method encapsulating getting a particular token by name from a matcher, including preconditions.
   */
  private static String match(final TokenMatcher.State state, final String name) {
    checkNotNull(state);
    String result = state.getTokens().get(name);
    checkNotNull(result);
    return result;
  }

  /**
   * Builds a path to the simple index for a particular name.
   */
  static String indexPath(final String name) {
    return "simple/" + name + (name.endsWith("/") ? "" : "/");
  }

  /**
   * Builds a path to a package for a particular path.
   */
  static String packagesPath(final String... parts) {
    return "packages/" + String.join("/", parts);
  }

  /**
   * Normalizes a name following the algorithm outlined in PEP 503.
   */
  static String normalizeName(final String name) {
    return name.replaceAll("[\\-\\_\\.]+", "-").toLowerCase(Locale.ENGLISH);
  }

  private PyPiPathUtils() {
    // empty
  }
}
