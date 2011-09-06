package org.basex.web.xquery;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.io.IOFile;
import org.basex.io.in.TextInput;
import org.basex.query.item.map.Map;
import org.basex.server.LocalSession;
import org.basex.server.Query;
import org.basex.server.Session;
import org.basex.web.servlet.util.ResultPage;

/**
 * Provides static methods to access BaseX.
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 */
public final class BaseXContext {
  /** Thread local resultpage. */
  private final static ThreadLocal<ResultPage> resultPage =
      new ThreadLocal<ResultPage>() {
    @Override
    protected ResultPage initialValue() {
      return ResultPage.getEmpty();
    }
  };
  
  /** Do not construct me. */
  private BaseXContext() { /* void */}
  
  /**
   * This Method reads and returns the result of a whole query.
   * @param f the filename
   * @param get GET map
   * @param post POST map
   * @param re response object
   * @param rq the request object
   * @return the query result
   * @throws IOException exception
   */
  public static ResultPage query(final File f, final Map get, final Map post,
      final HttpServletResponse re, final HttpServletRequest rq)
      throws IOException {
    return exec(TextInput.content(new IOFile(f)).toString(), get, post, re, rq);
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
  public static ResultPage exec(final String qu, final Map get, final Map post,
      final HttpServletResponse rp, final HttpServletRequest rq) {
    final Session sess = new LocalSession(new Context());
    try {
      final Query q = sess.query(qu);
      resultPage.get().setReq(rq);
      resultPage.get().setResp(rp);

      q.bind("GET", get, null);
      q.bind("POST", post, null);

      resultPage.get().setBody(q.execute());
      sess.close();
      return resultPage.get();
    } catch(BaseXException e) {
        return err(rp, rq, e);
    } catch(IOException e) {
        return err(rp, rq, e);
    }
  }

  /**
   * Returns a ResultPage containing an Error Message
   * @param rp response
   * @param rq request
   * @param e error
   * @return ResultPage with error message
   */
  private static ResultPage err(final HttpServletResponse rp,
      final HttpServletRequest rq, Exception e) {
    return new ResultPage("<div class=\"error\">" + e.getMessage() + "</div>",
        rp, rq);
  }


  /**
   * Returns the response.
   * @return response
   */
  static HttpServletResponse getResp() {
    return resultPage.get().getResp();
  }

  /**
   * Returns the request object.
   * @return request
   */
  static HttpServletRequest getReq() {
    return resultPage.get().getReq();
  }

}