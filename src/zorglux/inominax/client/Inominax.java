package zorglux.inominax.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import zorglux.inominax.exception.FunctionnalException;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Inominax implements EntryPoint {

   // UI
   private final HorizontalPanel mainPanel = new HorizontalPanel();
   private final HorizontalPanel addTokenPanel = new HorizontalPanel();
   private final TextBox newTokenTextBox = new TextBox();
   private final Button addTokenButton = new Button("Add");
   private final List<String> stocks = new ArrayList<String>();
   private final Label errorMessageLabel = new Label();

   // services
   private final InominaxServiceAsync inominaxService = GWT.create(InominaxService.class);
   private ListBox tokensListBox;
   private ListBox tokensSetDropBox;
   private VerticalPanel tokensPanel;

   /**
    * Entry point method.
    */
   @Override
   public void onModuleLoad() {

      GWT.log(RootPanel.get().toString(), null);

      // Assemble Token panel.
      addTokenPanel.add(newTokenTextBox);
      addTokenPanel.add(addTokenButton);
      addTokenPanel.addStyleName("addTokenPanel");

      tokensSetDropBox = new ListBox(false);
      initTokensSetNamesDropBox();
      tokensPanel = new VerticalPanel();
      tokensPanel.setSpacing(4);

      // Assemble Tokens listbox panel.
      tokensListBox = new ListBox(true);
      tokensListBox.setWidth("11em");
      tokensListBox.setVisibleItemCount(10);
      VerticalPanel tokensListBoxPanel = new VerticalPanel();
      tokensListBoxPanel.setSpacing(4);
      tokensListBoxPanel.add(tokensListBox);

      // Assemble Main panel.
      errorMessageLabel.setStyleName("errorMessage");
      errorMessageLabel.setVisible(false);

      tokensPanel.add(tokensSetDropBox);
      tokensPanel.add(addTokenPanel);
      tokensPanel.add(tokensListBoxPanel);
      mainPanel.add(errorMessageLabel);
      mainPanel.add(tokensPanel);

      // Associate the Main panel with the HTML host page.
      RootPanel.get("inominaX").add(mainPanel);

      // Move cursor focus to the input box.
      newTokenTextBox.setFocus(true);

      // Listen for mouse events on the Add button.
      addTokenButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            addToken();
         }
      });

      // Listen for keyboard events in the input box.
      newTokenTextBox.addKeyPressHandler(new KeyPressHandler() {
         @Override
         public void onKeyPress(KeyPressEvent event) {
            if (event.getCharCode() == KeyCodes.KEY_ENTER) {
               addToken();
            }
         }
      });

   }

   private void addToken() {
      final String token = newTokenTextBox.getText().trim();

      // Set up the callback object.
      AsyncCallback<Void> addToTokenSetCallback = new AsyncCallback<Void>() {
         @Override
         public void onFailure(Throwable caught) {
            if (caught instanceof FunctionnalException) {
               Window.alert(caught.getMessage());
            }

         }
         @Override
         public void onSuccess(Void result) {
            int selectedIndex = tokensSetDropBox.getSelectedIndex();
            String tokenSetName = tokensSetDropBox.getItemText(selectedIndex);
            loadTokensOf(tokenSetName);
         }
      };
      // call inominaxService
      inominaxService.addToTokenSet("Nain", new String[] { token }, addToTokenSetCallback);
      newTokenTextBox.setFocus(true);
   }

   private void initTokensSetNamesDropBox() {
      // Set up the callback object.
      AsyncCallback<List<String>> getTokenSetsNamesCallback = new AsyncCallback<List<String>>() {
         @Override
         public void onFailure(Throwable caught) {
            if (caught instanceof FunctionnalException) {
               Window.alert(caught.getMessage());
            }
         }
         @Override
         public void onSuccess(List<String> tokenSetsNames) {
            for (String tokenSetName : tokenSetsNames) {
               tokensSetDropBox.addItem(tokenSetName);
            }
         }
      };
      // call inominaxService
      inominaxService.getTokenSetsNames(getTokenSetsNamesCallback);
   }

   private void loadTokensOf(String tokenSetName) {
      // Set up the callback object.
      AsyncCallback<Set<String>> loadTokensOfCallback = new AsyncCallback<Set<String>>() {
         @Override
         public void onFailure(Throwable caught) {
            if (caught instanceof FunctionnalException) {
               Window.alert(caught.getMessage());
            }
         }
         @Override
         public void onSuccess(Set<String> tokens) {
            tokensListBox.clear();
            for (String token : tokens) {
               tokensListBox.addItem(token);
            }
         }
      };
      // call inominaxService
      inominaxService.getTokensOfSet(tokenSetName, loadTokensOfCallback);
   }

}