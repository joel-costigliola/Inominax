package zorglux.inominax.client;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("tokenSets")
public interface InominaxService extends RemoteService {

   // token set management
   List<String> getTokenSetsNames();
   void createTokenSet(String name);
   void removeTokenSet(String name);
   void renameTokenSet(String oldName, String newName);

   // token management
   Set<String> getTokensOfSet(String name);
   void addToTokenSet(String tokenSetName, String... tokens);
   void removeFromTokenSet(String tokenSetName, String... tokens);

}
