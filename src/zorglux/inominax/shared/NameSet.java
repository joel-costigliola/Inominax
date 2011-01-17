package zorglux.inominax.shared;


import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class NameSet implements Serializable {

   private static final long serialVersionUID = -160520685358307201L;

   @SuppressWarnings("unused")
   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
   private String id;

   @Persistent
   private String name;

   @Persistent(defaultFetchGroup = "true")
   private SortedSet<String> names;

   public NameSet() {
      this("");
   }

   public NameSet(String name) {
      super();
      this.name = name;
      names = new TreeSet<String>();
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Set<String> getNames() {
      return names;
   }

   public void setNames(SortedSet<String> tokens) {
      names = tokens;
   }

   public void addName(String... newNames) {
      for (String token : newNames) {
         names.add(token);
      }
   }

   public void removeNames(String... tokensToRemove) {
      for (String token : tokensToRemove) {
         names.remove(token);
      }
   }

   @Override
   public String toString() {
      return name;
   }

}
