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
  private static ThreadLocal<ResultPage> r = new ThreadLocal<ResultPage>() {
    @Override
    protected ResultPage initialValue() {
      return ResultPage.getEmpty();
    }
  };
  
  /** Do not construct me. */
  private BaseXContext() { /* void */}
  

  //  /** Memcached Client. */
//  private static final boolean USE_MEMCACHED = false;

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
  public static ResultPage
      exec(final String qu, final Map get, final Map post,
          final HttpServletResponse rp, final HttpServletRequest rq) {
    final ResultPage result = r.get();
    final Session sess = new LocalSession(new Context());
    try {
      final Query q = sess.query(qu);
      result.setReq(rq);
      result.setResp(rp);
      q.bind("GET", get, null);
      q.bind("POST", post, null);
      
//    final String hash = DigestUtils.sha512Hex(String.format("%s%s%s", qu, get,
//          post));

//      if(USE_MEMCACHED) {
//        final String cached = (String) MyCache.getInstance().get(hash);
//        if(cached != null) return cached;
//      }
      result.setBody(q.execute());
//      if(USE_MEMCACHED) {
//        if(qp.updates() == 0) {
//          MyCache.getInstance().set(hash, 360, res);
//        } else {
//          MyCache.getInstance().flushAll();
//        }
//      }
//      ctx.get().close();
      sess.close();
      return result;
    } catch(BaseXException e) {
      return new ResultPage("<div class=\"error\">" + e.getMessage() + "</div>",
          rp, rq); 
//    } catch(IOException e) {
//      return new ResultPage("<div class=\"error\">" + e.getMessage() + "</div>",
//          rp, rq); 
    } catch(IOException e) {
      return new ResultPage("<div class=\"error\">" + e.getMessage() + "</div>",
          rp, rq); 
    }
  }


  /**
   * Returns the response.
   * @return response
   */
  static HttpServletResponse getResp() {
    return r.get().getResp();
  }

  /**
   * Returns the request object.
   * @return request
   */
  static HttpServletRequest getReq() {
    return r.get().getReq();
  }

}
