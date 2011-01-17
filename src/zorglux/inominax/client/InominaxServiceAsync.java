package zorglux.inominax.client;

import java.util.List;
import java.util.Set;

import zorglux.inominax.shared.NameSet;
import zorglux.inominax.shared.TokenSet;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InominaxServiceAsync {

   // token set management
   public void getTokenSetsNames(AsyncCallback<List<String>> callback);
   void createTokenSet(String name, AsyncCallback<TokenSet> callback);
   public void removeTokenSet(String name, AsyncCallback<Void> callback);
   void renameTokenSet(String oldName, String newName, AsyncCallback<Void> callback);
   void cloneTokenSet(String oldName, String newName, AsyncCallback<Void> callback);

   // token management
   public void getTokensOfSet(String name, AsyncCallback<Set<String>> callback);
   public void addToTokenSet(String tokenSetName, String[] tokens, AsyncCallback<Void> callback);
   public void removeFromTokenSet(String tokenSetName, String[] tokens, AsyncCallback<Void> callback);

   // name set management
   void getNameSetsNames(AsyncCallback<List<String>> callback);
   void createNameSet(String name, AsyncCallback<NameSet> callback);
   void cloneNameSet(String oldName, String newName, AsyncCallback<Void> callback);
   void renameNameSet(String oldName, String newName, AsyncCallback<Void> callback);
   void removeNameSet(String name, AsyncCallback<Void> callback);

   // name management
   void addToNameSet(String nameSetName, String[] names, AsyncCallback<Void> callback);
   void removeFromNameSet(String nameSetName, String[] names, AsyncCallback<Void> callback);
   void getNamesOfSet(String name, AsyncCallback<Set<String>> callback);

}
