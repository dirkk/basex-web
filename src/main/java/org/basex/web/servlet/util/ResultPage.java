package org.basex.web.servlet.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class represents a complete result.
 * Consisting of the HTTP-headers and the body
 * @author Michael Seiferle, ms@basex.org
 *
 */
public final class ResultPage {
  /** The body. */
  private String body;
  /** Servlet Response. */
  private HttpServletResponse resp;
  /** Servlet Request. */
  private HttpServletRequest req;
  /**
   * Constructs the result.
   * @param bd the body
   * @param rsp the response
   * @param rq request
   */
  public ResultPage(final String bd, final HttpServletResponse rsp,
      final HttpServletRequest rq) {
    body = bd;
    resp = rsp;
    req = rq;
  }

  /**
   * Returns the body.
   * @return the body
   */
  public String getBody() {
    return body;
  }

  /**
   * Returns the response.
   * @return the resp
   */
  public HttpServletResponse getResp() {
    return resp;
  }

  /**
   * Returns an empty ResultPage Object.
   * @return empty ResultPage
   */
  public static ResultPage getEmpty() {
    return new ResultPage(null, null, null);
  }

  /**
   * Returns the request.
   * @return the req
   */
  public HttpServletRequest getReq() {
    return req;
  }

  /**
   * Sets the request.
   * @param rq the req to set
   */
  public void setReq(final HttpServletRequest rq) {
    this.req = rq;
  }

  /**
   * Sets the body.
   * @param b the body to set
   */
  public void setBody(final String b) {
    this.body = b;
  }

  /**
   * Sets the response.
   * @param rp the resp to set
   */
  public void setResp(final HttpServletResponse rp) {
    this.resp = rp;
  }
}
