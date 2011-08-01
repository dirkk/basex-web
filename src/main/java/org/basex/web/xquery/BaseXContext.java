package org.basex.web.xquery;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.basex.core.Context;
import org.basex.core.Prop;
import org.basex.io.IOFile;
import org.basex.io.in.TextInput;
import org.basex.query.QueryException;
import org.basex.query.QueryProcessor;
import org.basex.query.item.map.Map;

/**
 * Provides static methods to access BaseX.
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 */
public final class BaseXContext {
  /** Do not construct me. */
  private BaseXContext() { /* void */}

  /** Query Context. */
  private static Context ctx = new Context();
  /** Current respone Object. */
  private static HttpServletResponse resp;
  /** Current request Object. */
  private static HttpServletRequest req;

  /**
   * This Method reads and returns the result of a whole query.
   * @param f the filename
   * @param get GET map
   * @param post POST map
   * @param r response object
   * @param rq the request object
   * @return the query result
   * @throws IOException exception
   */
  public static String query(final File f, final Map get, final Map post,
      final HttpServletResponse r, final HttpServletRequest rq)
      throws IOException {
    return exec(TextInput.content(new IOFile(f)).toString(), get, post, r, rq);
  }

  /**
   * Executes a query string.
   * @param qu query string
   * @param get GET map
   * @param post POST map
   * @param rp response object
   * @param rq request object
   * @return the query result.
   */
  public static String
      exec(final String qu, final Map get, final Map post,
          final HttpServletResponse rp, final HttpServletRequest rq) {
    try {
      ctx.close();
     // System.err.format("===\n%s\n=====", qu);
      QueryProcessor qp = new QueryProcessor(qu, ctx);
      ctx.prop.set(Prop.QUERYINFO, true);

      qp.bind("GET", get);
      setResp(rp);
      setReq(rq);
      qp.bind("POST", post);
      final String res = qp.execute().toString();
     // System.err.println(qp.info());
      return res;
    } catch(QueryException e) {
      return "<div class=\"error\">" + e.getMessage() + "</div>";
    }
  }


  /**
   * Sets the request object.
   * @param rq the request
   */
  private static void setReq(final HttpServletRequest rq) {
    BaseXContext.req = rq;
  }

  /**
   * Sets the response.
   * @param rsp response
   */
  private static void setResp(final HttpServletResponse rsp) {
    BaseXContext.resp = rsp;
  }

  /**
   * Returns the response.
   * @return response
   */
  static HttpServletResponse getResp() {
    return resp;
  }

  /**
   * Returns the request object.
   * @return request
   */
  static HttpServletRequest getReq() {
    return req;
  }

}
