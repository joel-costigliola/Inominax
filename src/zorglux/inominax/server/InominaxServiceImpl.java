package zorglux.inominax.server;

import static zorglux.inominax.exception.FunctionnalException.throwFunctionnalExceptionIfFalse;
import static zorglux.inominax.server.InominaxPersistenceManagerFactory.getPersistenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import zorglux.inominax.client.InominaxService;
import zorglux.inominax.shared.TokenSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class InominaxServiceImpl extends RemoteServiceServlet implements InominaxService {

   private static final String FIND_TOKENSET_BY_NAME_QUERY = "select from " + TokenSet.class.getName() + " where name == :name";
   private static final String ALL_TOKENS_SET_QUERY = "select from " + TokenSet.class.getName() + " order by name asc";

   public InominaxServiceImpl() {
      super();
      populateDefaultTokenSetsOnce();
   }

   private void populateDefaultTokenSetsOnce() {
      if (countTokenSets() == 0) {
         PersistenceManager persistenceManager = getPersistenceManager();
         try {
            TokenSet elvesTokenset = new TokenSet("Elfe");
            elvesTokenset.addToken("lae", "il", "mar", "sel", "fel", "fin", "iel", "gad", "del", "sin", "rin", "las", "gal", "ald", "ael", "din", "jad", "el", "ga", "la", "dri", "el", "ol");
            TokenSet dwarfTokenSet = new TokenSet("Nain");
            dwarfTokenSet.addToken("zak", "zok", "zek", "kar", "kor", "rok", "rak", "grim", "rek", "gra", "gru", "gre", "drak", "dak", "da", "du", "do", "gur", "hel", "ga", "gu", "go", "re", "ra", "ro", "bal", "bol", "ba", "bo", "bar", "bor", "bur", "son", "gir");
            TokenSet humanTokenSet = new TokenSet("Humain");
            humanTokenSet.addToken("bo", "ris", "ma", "ri", "drak", "jo", "kim", "joa", "mir", "ro", "a", "ra", "gorn", "sel", "rik", "drik", "jon", "gal", "bal", "bol", "ba", "bo", "del", "sin", "rin");
            persistenceManager.makePersistentAll(elvesTokenset, dwarfTokenSet, humanTokenSet);
         } finally {
            persistenceManager.close();
         }
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<String> getTokenSetsNames() {
      PersistenceManager persistenceManager = getPersistenceManager();
      try {
         Query query = persistenceManager.newQuery(ALL_TOKENS_SET_QUERY);
         List<TokenSet> allTokenSets = (List<TokenSet>) query.execute();
         List<String> tokenSetsNames = new ArrayList<String>(allTokenSets.size());
         for (TokenSet tokenSet : allTokenSets) {
            tokenSetsNames.add(tokenSet.getName());
         }
         return tokenSetsNames;
      } finally {
         persistenceManager.close();
      }
   }

   @Override
   public TokenSet createTokenSet(String tokenSetName) {
      throwFunctionnalExceptionIfFalse(checkTokenSetNameIsAvailable(tokenSetName), "name already used : " + tokenSetName);
      TokenSet newTokenSet = new TokenSet(tokenSetName);
      PersistenceManager persistenceManager = getPersistenceManager();
      try {
         persistenceManager.makePersistent(newTokenSet);
         GWT.log("create token set " + tokenSetName);
         return newTokenSet;
      } finally {
         persistenceManager.close();
      }
   }

   @Override
   public void removeTokenSet(String name) {
      TokenSet tokenSet = findTokenSetByName(name);
      if (tokenSet != null) {
         PersistenceManager persistenceManager = getPersistenceManager();
         try {
            persistenceManager.deletePersistent(tokenSet);
         } finally {
            persistenceManager.close();
         }
      }
   }

   // token management
   @Override
   public Set<String> getTokensOfSet(String name) {
      if (tokenSetExists(name)) { return findTokenSetByName(name).getTokens(); }
      // return empty set if we can't find a tokenset corresponding to the given name
      return new HashSet<String>();
   }

   private TokenSet findTokenSetByName(String name) {
      PersistenceManager persistenceManager = getPersistenceManager();
      try {
         Query query = persistenceManager.newQuery(FIND_TOKENSET_BY_NAME_QUERY);
         query.setUnique(true);
         TokenSet result = (TokenSet) query.execute(name);
         return result == null ? null : persistenceManager.detachCopy(result);
      } finally {
         persistenceManager.close();
      }
   }

   @Override
   public void addToTokenSet(String tokenSetName, String... tokens) {
      if (tokenSetExists(tokenSetName)) {
         TokenSet tokenSet = findTokenSetByName(tokenSetName);
         PersistenceManager persistenceManager = getPersistenceManager();
         try {
            tokenSet = persistenceManager.makePersistent(tokenSet);
            tokenSet.addToken(tokens);
         } finally {
            persistenceManager.close();
         }
      }
   }

   @Override
   public void removeFromTokenSet(String tokenSetName, String... tokens) {
      if (tokenSetExists(tokenSetName)) {
         TokenSet tokenSet = findTokenSetByName(tokenSetName);
         PersistenceManager persistenceManager = getPersistenceManager();
         try {
            tokenSet = persistenceManager.makePersistent(tokenSet);
            tokenSet.removeTokens(tokens);
            GWT.log("removing tokens " + tokens + " from " + tokenSetName);
         } finally {
            persistenceManager.close();
         }
      }
   }

   @SuppressWarnings("unchecked")
   private int countTokenSets() {
      PersistenceManager persistenceManager = getPersistenceManager();
      try {
         Query query = persistenceManager.newQuery(ALL_TOKENS_SET_QUERY);
         return ((List<TokenSet>) query.execute()).size();
      } finally {
         persistenceManager.close();
      }
   }

   public boolean checkTokenSetNameIsAvailable(String tokenSetName) {
      return !tokenSetExists(tokenSetName);
   }

   private boolean tokenSetExists(String tokenSetName) {
      return getTokenSetsNames().contains(tokenSetName);
   }

   @Override
   public void renameTokenSet(String oldName, String newName) {
      throwFunctionnalExceptionIfFalse(checkTokenSetNameIsAvailable(newName), "name already used : " + newName);
      throwFunctionnalExceptionIfFalse(tokenSetExists(oldName), "no token list with name : " + oldName);
      TokenSet tokenSet = findTokenSetByName(oldName);
      PersistenceManager persistenceManager = getPersistenceManager();
      try {
         tokenSet = persistenceManager.makePersistent(tokenSet);
         tokenSet.setName(newName);
      } finally {
         persistenceManager.close();
      }
   }

   @Override
   public void cloneTokenSet(String nameOfTokenSetToClone, String newTokenSetName) {
      throwFunctionnalExceptionIfFalse(checkTokenSetNameIsAvailable(newTokenSetName), "name already used : " + newTokenSetName);
      throwFunctionnalExceptionIfFalse(tokenSetExists(nameOfTokenSetToClone), "no token list with name : " + nameOfTokenSetToClone);
      // everything should be ok to clone a token set
      TokenSet tokenSetToClone = findTokenSetByName(nameOfTokenSetToClone);
      PersistenceManager persistenceManager = getPersistenceManager();
      try {
         TokenSet clone = createTokenSet(newTokenSetName);
         clone.getTokens().addAll(tokenSetToClone.getTokens());
         persistenceManager.makePersistent(clone);
      } finally {
         persistenceManager.close();
      }
   }

}
