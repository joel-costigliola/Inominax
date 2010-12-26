package zorglux.inominax.server;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import zorglux.inominax.client.InominaxService;
import zorglux.inominax.shared.TokenSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class InominaxServiceImpl extends RemoteServiceServlet implements InominaxService {

   private final List<TokenSet> tokenSets = initDefaultTokenSets();

   private List<TokenSet> initDefaultTokenSets() {
      TokenSet elvesTokenset = new TokenSet("Elfe");
      elvesTokenset.addToken("lae", "il", "mar", "sel", "fel", "fin", "iel", "gad", "del", "sin", "rin", "las", "gal", "ald", "ael", "din", "jad", "el", "ga", "la", "dri", "el", "ol");
      TokenSet dwarfTokenSet = new TokenSet("Nain");
      dwarfTokenSet.addToken("zak", "zok", "zek", "kar", "kor", "rok", "rak", "grim", "rek", "gra", "gru", "gre", "drak", "dak", "da", "du", "do", "gur", "hel", "ga", "gu", "go", "re", "ra", "ro", "bal", "bol", "ba", "bo", "bar", "bor", "bur", "son", "gir");
      TokenSet humanTokenSet = new TokenSet("Humain");
      humanTokenSet.addToken("bo", "ris", "ma", "ri", "drak", "jo", "kim", "joa", "mir", "ro", "a", "ra", "gorn", "sel", "rik", "drik", "jon", "gal", "bal", "bol", "ba", "bo", "del", "sin", "rin");
      return asList(elvesTokenset, dwarfTokenSet, humanTokenSet);
   }

   @Override
   public List<String> getTokenSetsNames() {
      List<TokenSet> allTokenSets = getAllTokenSets();
      List<String> tokenSetsNames = new ArrayList<String>(allTokenSets.size());
      for (TokenSet tokenSet : allTokenSets) {
         tokenSetsNames.add(tokenSet.getName());
      }
      return tokenSetsNames;
   }

   @Override
   public void createTokenSet(String tokenSetName) {
      if (checkTokenSetNameIsAvailable(tokenSetName)) {
         tokenSets.add(new TokenSet(tokenSetName));
      }
   }

   @Override
   public void removeTokenSet(String name) {
      tokenSets.remove(findTokenSetByName(name));
   }

   // token management
   @Override
   public Set<String> getTokensOfSet(String name) {
      if (tokenSetExists(name)) {
         return findTokenSetByName(name).getTokens();
      }
      // return empty set if we can't find a tokenset corresponding to the given name
      return new HashSet<String>();
   }

   private TokenSet findTokenSetByName(String name) {
      for (TokenSet tokenSet : tokenSets) {
         if (tokenSet.getName().equals(name)) {
            return tokenSet;
         }
      }
      // return null if we can't find a tokenset corresponding to the given name
      return null;
   }

   @Override
   public void addToTokenSet(String tokenSetName, String... tokens) {
      if (tokenSetExists(tokenSetName)) {
         findTokenSetByName(tokenSetName).addToken(tokens);
      }
   }

   @Override
   public void removeFromTokenSet(String tokenSetName, String... tokens) {
      if (tokenSetExists(tokenSetName)) {
         GWT.log("removing tokens " + tokens + " from " + tokenSetName);
         findTokenSetByName(tokenSetName).removeTokens(tokens);
      }
   }

   private List<TokenSet> getAllTokenSets() {
      return tokenSets;
   }

   public boolean checkTokenSetNameIsAvailable(String tokenSetName) {
      return !tokenSetExists(tokenSetName);
   }

   private boolean tokenSetExists(String tokenSetName) {
      return getTokenSetsNames().contains(tokenSetName);
   }

}
