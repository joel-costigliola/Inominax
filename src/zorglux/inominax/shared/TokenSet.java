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
public class TokenSet implements Serializable {

   private static final long serialVersionUID = 8401658275213004116L;

   @SuppressWarnings("unused")
   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
   private String id;

   @Persistent
   private String name;

   @Persistent
   private SortedSet<String> tokens;

   public TokenSet() {
      this("");
   }

   public TokenSet(String name) {
      super();
      this.name = name;
      tokens = new TreeSet<String>();
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Set<String> getTokens() {
      return tokens;
   }

   public void setTokens(SortedSet<String> tokens) {
      this.tokens = tokens;
   }

   public void addToken(String... newTokens) {
      for (String token : newTokens) {
         tokens.add(token);
      }
   }

   public void removeTokens(String... tokensToRemove) {
      for (String token : tokensToRemove) {
         tokens.remove(token);
      }
   }

   @Override
   public String toString() {
      return name;
   }

}
