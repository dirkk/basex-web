package org.basex.web.servlet.impl;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.basex.query.item.map.Map;
import org.basex.web.parser.InlineXQuery;
import org.basex.web.servlet.PrepareParamsServlet;

/**
 * Parses HTML files with inline XQuery.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Michael Seiferle <ms@basex.org>
 */
public class Html extends PrepareParamsServlet {

  /** This is me, your version. */
  private static final long serialVersionUID = -7694236920689548933L;

  @Override
  public final void
      get(final HttpServletResponse response, final HttpServletRequest req,
          final File f, final Map get, final Map post) throws IOException {

    response.setContentType("text/html");
    // *TODO*
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpServletResponse.SC_OK);
    final String resp = new InlineXQuery(f).eval(get, post, response, req);
    response.getWriter().write(resp);
  }
}
