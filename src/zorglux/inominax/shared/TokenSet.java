package zorglux.inominax.shared;


import java.util.Set;
import java.util.TreeSet;

public class TokenSet {

   private String name;
   private Set<String> tokens;

   public TokenSet() {
      super();
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

   public void setTokens(Set<String> tokens) {
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

}
