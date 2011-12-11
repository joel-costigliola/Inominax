package zorglux.inominax.client;

import java.util.*;

import com.google.gwt.user.client.rpc.*;

import zorglux.inominax.shared.*;

@RemoteServiceRelativePath("tokenSets")
public interface InominaxService extends RemoteService {

	// token set management
	List<String> getTokenSetsNames();

	TokenSet createTokenSet(String name);

	void removeTokenSet(String name);

	void renameTokenSet(String oldName, String newName);

	void cloneTokenSet(String oldName, String newName);

	// token management
	Set<String> getTokensOfSet(String name);

	void addToTokenSet(String tokenSetName, String... tokens);

	void removeFromTokenSet(String tokenSetName, String... tokens);

	// name set management
	List<String> getNameSetsNames();

	NameSet createNameSet(String name);

	void removeNameSet(String name);

	void renameNameSet(String oldName, String newName);

	void cloneNameSet(String oldName, String newName);

	// name management
	Set<String> getNamesOfSet(String name);

	void addToNameSet(String nameSetName, String... names);

	void removeFromNameSet(String nameSetName, String... names);

	Set<String> getAllTokens();

}
