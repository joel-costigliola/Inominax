package zorglux.inominax.server;

import static zorglux.inominax.exception.FunctionnalException.throwFunctionnalExceptionIfFalse;
import static zorglux.inominax.server.InominaxPersistenceManagerFactory.getPersistenceManager;

import java.util.*;

import javax.jdo.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import zorglux.inominax.client.InominaxService;
import zorglux.inominax.shared.*;

@SuppressWarnings("serial")
public class InominaxServiceImpl extends RemoteServiceServlet implements InominaxService {

	private static final String FIND_TOKEN_SET_BY_NAME_QUERY = "select from " + TokenSet.class.getName() + " where name == :name";
	private static final String ALL_TOKENS_SET_QUERY = "select from " + TokenSet.class.getName() + " order by name asc";
	private static final String FIND_NAME_SET_BY_NAME_QUERY = "select from " + NameSet.class.getName() + " where name == :name";
	private static final Object ALL_NAMES_SET_QUERY = "select from " + NameSet.class.getName() + " order by name asc";

	public InominaxServiceImpl() {
		super();
		populateDefaultTokenSetsOnce();
		populateDefaultNameSetsOnce();
	}

	// =============================================================================================================
	// Tokens section
	// =============================================================================================================

	private void populateDefaultTokenSetsOnce() {
		if (countTokenSets() == 0) {
			PersistenceManager persistenceManager = getPersistenceManager();
			try {
				TokenSet elvesTokenset = new TokenSet("Elfe");
				elvesTokenset.addToken("lae", "il", "mar", "sel", "fel", "fin", "iel", "gad", "del", "sin", "rin", "las", "gal",
						"ald", "ael", "din", "jad", "el", "ga", "la", "dri", "el", "ol");
				TokenSet dwarfTokenSet = new TokenSet("Nain");
				dwarfTokenSet.addToken("zak", "zok", "zek", "kar", "kor", "rok", "rak", "grim", "rek", "gra", "gru", "gre", "drak",
						"dak", "da", "du", "do", "gur", "hel", "ga", "gu", "go", "re", "ra", "ro", "bal", "bol", "ba", "bo", "bar",
						"bor", "bur", "son", "gir");
				TokenSet humanTokenSet = new TokenSet("Humain");
				humanTokenSet.addToken("bo", "ris", "ma", "ri", "drak", "jo", "kim", "joa", "mir", "ro", "a", "ra", "gorn", "sel",
						"rik", "drik", "jon", "gal", "bal", "bol", "ba", "bo", "del", "sin", "rin");
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
		if (tokenSetExists(name)) return findTokenSetByName(name).getTokens();
		// return empty set if we can't find a tokenset corresponding to the given
		// name
		return new HashSet<String>();
	}

	private TokenSet findTokenSetByName(String name) {
		PersistenceManager persistenceManager = getPersistenceManager();
		try {
			Query query = persistenceManager.newQuery(FIND_TOKEN_SET_BY_NAME_QUERY);
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
		throwFunctionnalExceptionIfFalse(tokenSetExists(nameOfTokenSetToClone), "no token list with name : "
				+ nameOfTokenSetToClone);
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

	// =============================================================================================================
	// Users names section
	// =============================================================================================================

	private void populateDefaultNameSetsOnce() {
		if (countNameSets() == 0) {
			PersistenceManager persistenceManager = getPersistenceManager();
			try {
				NameSet joeNameset = new NameSet("Joe");
				joeNameset.addName("Arkael", "Elwyn");
				NameSet nextGameNameSet = new NameSet("Next game");
				nextGameNameSet.addName("Joachim", "Sayal");
				persistenceManager.makePersistentAll(joeNameset, nextGameNameSet);
			} finally {
				persistenceManager.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private int countNameSets() {
		PersistenceManager persistenceManager = getPersistenceManager();
		try {
			Query query = persistenceManager.newQuery(ALL_NAMES_SET_QUERY);
			return ((List<NameSet>) query.execute()).size();
		} finally {
			persistenceManager.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getNameSetsNames() {
		PersistenceManager persistenceManager = getPersistenceManager();
		try {
			Query query = persistenceManager.newQuery(ALL_NAMES_SET_QUERY);
			List<NameSet> allNameSets = (List<NameSet>) query.execute();
			List<String> nameSetsNames = new ArrayList<String>(allNameSets.size());
			for (NameSet nameSet : allNameSets) {
				nameSetsNames.add(nameSet.getName());
			}
			return nameSetsNames;
		} finally {
			persistenceManager.close();
		}
	}

	@Override
	public NameSet createNameSet(String nameSetName) {
		throwFunctionnalExceptionIfFalse(checkNameSetNameIsAvailable(nameSetName), "name already used : " + nameSetName);
		NameSet newNameSet = new NameSet(nameSetName);
		PersistenceManager persistenceManager = getPersistenceManager();
		try {
			persistenceManager.makePersistent(newNameSet);
			GWT.log("create name set " + nameSetName);
			return newNameSet;
		} finally {
			persistenceManager.close();
		}
	}

	@Override
	public void removeNameSet(String name) {
		NameSet nameSet = findNameSetByName(name);
		if (nameSet != null) {
			PersistenceManager persistenceManager = getPersistenceManager();
			try {
				persistenceManager.deletePersistent(nameSet);
			} finally {
				persistenceManager.close();
			}
		}
	}

	// name management
	@Override
	public Set<String> getNamesOfSet(String name) {
		if (nameSetExists(name)) { return findNameSetByName(name).getNames(); }
		// return empty set if we can't find a nameset corresponding to the given
		// name
		return new HashSet<String>();
	}

	private NameSet findNameSetByName(String name) {
		PersistenceManager persistenceManager = getPersistenceManager();
		try {
			Query query = persistenceManager.newQuery(FIND_NAME_SET_BY_NAME_QUERY);
			query.setUnique(true);
			NameSet result = (NameSet) query.execute(name);
			return result == null ? null : persistenceManager.detachCopy(result);
		} finally {
			persistenceManager.close();
		}
	}

	@Override
	public void addToNameSet(String nameSetName, String... names) {
		if (nameSetExists(nameSetName)) {
			NameSet nameSet = findNameSetByName(nameSetName);
			PersistenceManager persistenceManager = getPersistenceManager();
			try {
				nameSet = persistenceManager.makePersistent(nameSet);
				nameSet.addName(names);
			} finally {
				persistenceManager.close();
			}
		}
	}

	@Override
	public void removeFromNameSet(String nameSetName, String... names) {
		if (nameSetExists(nameSetName)) {
			NameSet nameSet = findNameSetByName(nameSetName);
			PersistenceManager persistenceManager = getPersistenceManager();
			try {
				nameSet = persistenceManager.makePersistent(nameSet);
				nameSet.removeNames(names);
				GWT.log("removing names " + names + " from " + nameSetName);
			} finally {
				persistenceManager.close();
			}
		}
	}

	public boolean checkNameSetNameIsAvailable(String nameSetName) {
		return !nameSetExists(nameSetName);
	}

	private boolean nameSetExists(String nameSetName) {
		return getNameSetsNames().contains(nameSetName);
	}

	@Override
	public void renameNameSet(String oldName, String newName) {
		throwFunctionnalExceptionIfFalse(checkNameSetNameIsAvailable(newName), "name already used : " + newName);
		throwFunctionnalExceptionIfFalse(nameSetExists(oldName), "no name list with name : " + oldName);
		NameSet nameSet = findNameSetByName(oldName);
		PersistenceManager persistenceManager = getPersistenceManager();
		try {
			nameSet = persistenceManager.makePersistent(nameSet);
			nameSet.setName(newName);
		} finally {
			persistenceManager.close();
		}
	}

	@Override
	public void cloneNameSet(String nameOfNameSetToClone, String newNameSetName) {
		throwFunctionnalExceptionIfFalse(checkNameSetNameIsAvailable(newNameSetName), "name already used : " + newNameSetName);
		throwFunctionnalExceptionIfFalse(nameSetExists(nameOfNameSetToClone), "no name list with name : " + nameOfNameSetToClone);
		// everything should be ok to clone a name set
		NameSet nameSetToClone = findNameSetByName(nameOfNameSetToClone);
		PersistenceManager persistenceManager = getPersistenceManager();
		try {
			NameSet clone = createNameSet(newNameSetName);
			clone.getNames().addAll(nameSetToClone.getNames());
			persistenceManager.makePersistent(clone);
		} finally {
			persistenceManager.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getAllTokens() {
		PersistenceManager persistenceManager = getPersistenceManager();
		try {
			Query query = persistenceManager.newQuery(ALL_TOKENS_SET_QUERY);
			List<TokenSet> allTokenSets = (List<TokenSet>) query.execute();
			Set<String> tokens = new HashSet<String>();
			for (TokenSet tokenSet : allTokenSets) {
				tokens.addAll(tokenSet.getTokens());
			}
			return tokens;
		} finally {
			persistenceManager.close();
		}
	}

}
