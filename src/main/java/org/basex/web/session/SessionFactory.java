/**
 * 
 */
package org.basex.web.session;

import java.io.IOException;

import org.basex.BaseXServer;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.server.ClientSession;
import org.basex.util.Util;

/**
 * @author michael
 * 
 */
public class SessionFactory {

  private static final BaseXServer bsx;
  static {
    try {
      bsx = new BaseXServer();
    } catch(Exception e) {
      // TODO Auto-generated catch block
      throw Util.notexpected(e);
    }
        
  }
  /** My Session. */
  private static final ClientSession INSTANCE;
  static {
    try {
      INSTANCE = new ClientSession(new Context(), "admin", "admin");
    } catch(IOException e) {
      // TODO Auto-generated catch block
      throw Util.notexpected(e);
    }
  }
  /** My Session. */
  private static final ClientSession EVENTS;
  static {
    try {
      EVENTS = new ClientSession(new Context(), "admin", "admin");
    } catch(IOException e) {
      // TODO Auto-generated catch block
      throw Util.notexpected(e);
    }
    try {
      Notifications.setUp();
    } catch(BaseXException e) {
      // TODO Auto-generated catch block
      throw Util.notexpected(e);
    }
  }

  /**
   * The Session.
   * @return session singleton
   */
  public static ClientSession get() {
    // TODO Auto-generated method stub
    return INSTANCE;
  }
  /**
   * The Session.
   * @return session singleton
   */
  public static ClientSession events() {
    // TODO Auto-generated method stub
    return EVENTS;
  }

}
