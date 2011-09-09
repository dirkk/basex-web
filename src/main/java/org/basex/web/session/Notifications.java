package org.basex.web.session;

import java.util.ArrayList;
import java.util.List;

import org.basex.core.BaseXException;
import org.basex.server.EventNotifier;
import org.basex.web.session.watchers.CookieWatcher;

/**
 * Sets up the notifiers.
 * @author michael
 */
public class Notifications {
  /** List of enabled notifiers. */
  private final static List<EventNotifier> classes =
      new ArrayList<EventNotifier>();
  static {
    classes.add(new CookieWatcher());
  }
  /**
   * Initializes.
   * @throws BaseXException on err
   */
  static void setUp() throws BaseXException{
    for(final EventNotifier ee : classes){
      final String name = eventName(ee.getClass().getSimpleName());
      try {
      SessionFactory.get().execute(String.format("DROP EVENT %s", name));
      }catch(BaseXException e){
        //this is allowed to fail, iff the event did not exist previously
      }
      SessionFactory.get().execute(String.format("CREATE EVENT %s", name));
      SessionFactory.events().watch(name, ee);
      System.out.println("Registered " + name);
      
    }
  }
  /**
   * Returns a friendly event name.
   * @param string string.
   * @return name
   */
  private static String eventName(final String string) {
    return String.format("web%s",string);
  }
}
