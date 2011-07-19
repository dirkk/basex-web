package org.basex.web.xquery;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.basex.web.annotations.XQext;

/**
 * Sloppy Interface for communicating with Jetty from XQuery.
 * Will need a rewrite sometime soon.
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 */
public final class XQueryExternal {

  /**
   * Sets the content type.
   * @param ct content type
   */
  @XQext(name = "content-type", params = { "type"},
      paramhelp = { "the content type"}, help = "")
  public static void contentType(final String ct) {
    BaseXContext.getResp().setContentType(ct);
  }
  /**
   * Sets a cookie with the specified parameters.
   * @param name name
   * @param value value
   * @param expires expires in seconds
   * @param path path
   */
  @XQext(name = "set-cookie", params = { "name", "value", "expires", "path"},
      paramhelp = { "the cookie name", "the cookie value",
          "expires in seconds", "the cookie path"},
          help = "Sets a cookie with the specified parameters.")
  public static void setCookie(final String name, final String value,
      final int expires, final String path) {
    final Cookie c = new Cookie(name, value);
    c.setPath(path);
    c.setMaxAge(expires);
    BaseXContext.getResp().addCookie(c);
  }
  /**
   * Returns the cookie value.
   * @param name cookie name
   * @return value for Cookie 'name'
   */
  @XQext(name = "get-cookie", paramhelp = { "name of the cookie"},
      params = { "name"}, help = "")
  public static String getCookieValue(final String name) {
    HttpServletRequest req = BaseXContext.getReq();
    if(req != null) {
      Cookie[] cookies = req.getCookies();
      if(cookies == null) return "";
      for(Cookie cookie : cookies) {
        if(name.equals(cookie.getName())) return cookie.getValue();
      }
    }
    return "";
  }

  /** Ninja constructor. */
  private XQueryExternal() { }
}
