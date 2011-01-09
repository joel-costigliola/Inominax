package zorglux.inominax.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public final class InominaxPersistenceManagerFactory {
   private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");

   private InominaxPersistenceManagerFactory() {}

   public static PersistenceManagerFactory get() {
      return pmfInstance;
   }

   public static PersistenceManager getPersistenceManager() {
      return pmfInstance.getPersistenceManager();
   }
}
