package org.basex.web.session.watchers;

import org.basex.server.EventNotifier;

/**
 * Watches for Cookie Events
 * @author michael
 *
 */
public class CookieWatcher implements EventNotifier {
  
  @Override
  public void notify(String value) {
    System.out.println("Value" + value);
    
  }
}
