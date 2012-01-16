package org.basex.web.session;

import org.basex.core.Context;
import org.basex.server.LocalSession;
import org.basex.server.Session;

/**
 * Session singleton.
 * @author Michael Seiferle, <ms@basex.org>
 * @author BaseX Team
 *
 */
public final class SessionFactory {
  /** My Session. */
  private static final Session INSTANCE = new LocalSession(new Context());
  /**
   * The Session.
   * @return session singleton
   */
  public static Session get() {
    return INSTANCE;
  }

    /** Private Constructor. */
    private SessionFactory() {
    }
}