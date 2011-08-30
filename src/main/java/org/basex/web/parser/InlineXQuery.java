package org.basex.web.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.basex.query.item.map.Map;
import org.basex.web.xquery.BaseXContextImpl;

/**
 * Parses and substitutes inline XQuery with a query result.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 */
public final class InlineXQuery {
  /** GET/Post Params. */
  private final File file;

  /**
   * Parses and executes inline xquery.
   * @param f file to query
   */
  public InlineXQuery(final File f) {
    this.file = f;
  }

  /** Denotes start of query. */
  static final char[] START = "<?xq".toCharArray();
  /** Denotes end of query. */
  static final char[] END = "?>".toCharArray();

  /**
   * Parses HTML for <?xq ... ?>. *TODO* parse queries that contain ?>
   * intermediate.
   * @param get GET map
   * @param post POST map
   * @param resp HTTP response object
   * @param req HTTP request object
   * @return the result of the parse
   * @throws IOException on error.
   */
  public String eval(final Map get, final Map post,
      final HttpServletResponse resp, final HttpServletRequest req)
      throws IOException {
    final BufferedReader fis = new BufferedReader(new InputStreamReader(
        new FileInputStream(file)));
    final StringBuilder sb = new StringBuilder(128);
    while(fis.ready()) {
      consumeXQ(fis, sb, get, post, resp, req);
      sb.append((char) fis.read());
    }
    return sb.toString();
  }

  /**
   * Tries to consume <?xq starting tag.
   * @param fis file input stream
   * @param sb resultbuffer to fill.
   * @param get GET map
   * @param post POST map
   * @param resp HTTP response
   * @param req HTTP request
   * @throws IOException on error.
   */
  private void consumeXQ(final BufferedReader fis, final StringBuilder sb,
      final Map get, final Map post, final HttpServletResponse resp,
      final HttpServletRequest req)
      throws IOException {
    int apos = -1;
    fis.mark(START.length);
    final StringBuilder curQuery = new StringBuilder();
    boolean inqry = false;
    while(fis.ready() && ((char) fis.read()) == START[++apos]) {
      if(apos == START.length - 1) {
        inqry = true;
        break;
      }
    }
    if(inqry) {
      while(!consumeEnd(fis) && fis.ready()) {
        curQuery.append((char) fis.read());
      }

      sb.append(BaseXContextImpl.exec(curQuery.toString(), get, post, resp, req));
    } else {
      fis.reset();
    }
  }

  /**
   * Tries to consume an ?> end of a xquery block.
   * @param fis file input stream
   * @return true if end detected
   * @throws IOException on err
   */
  private static boolean consumeEnd(final BufferedReader fis)
      throws IOException {
    int epos = -1;
    fis.mark(END.length);
    while(fis.ready() && ((char) fis.read()) == END[++epos]) {
      if(epos == END.length - 1) {
        return true;
      }
    }
    fis.reset();
    return false;
  }
}
