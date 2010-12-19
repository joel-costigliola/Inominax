package zorglux.inominax.server;

import static com.google.appengine.repackaged.com.google.common.collect.Lists.newArrayList;
import static com.google.appengine.repackaged.com.google.common.collect.Lists.newArrayListWithCapacity;
import static com.google.appengine.repackaged.com.google.common.collect.Sets.newHashSet;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import zorglux.inominax.client.InominaxService;
import zorglux.inominax.shared.TokenSet;

@SuppressWarnings("serial")
public class InominaxServiceImpl extends RemoteServiceServlet implements InominaxService {

   private final List<TokenSet> tokenSets = newArrayList(new TokenSet("Elfe"), new TokenSet("Nain"), new TokenSet("Orc"));

   @Override
   public List<String> getTokenSetsNames() {
      List<TokenSet> allTokenSets = getAllTokenSets();
      List<String> tokenSetsNames = newArrayListWithCapacity(allTokenSets.size());
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
      return newHashSet();
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
