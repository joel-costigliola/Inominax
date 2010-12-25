package zorglux.inominax.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import zorglux.inominax.exception.FunctionnalException;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.util.collect.Lists;
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
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.IntegerBox;

public class Inominax implements EntryPoint {

   // UI
   private final HorizontalPanel mainPanel = new HorizontalPanel();
   private final HorizontalPanel addTokenPanel = new HorizontalPanel();
   private final TextBox newTokenTextBox = new TextBox();
   private final Button addTokenButton = new Button("Add");

   // services
   private final InominaxServiceAsync inominaxService = GWT.create(InominaxService.class);
   private final ListBox tokensListBox = new ListBox(true);
   private final ListBox tokensSetDropBox = new ListBox(false);
   private final VerticalPanel tokensPanel = new VerticalPanel();
   private final Button btnRemoveSelected = new Button("remove selected");
   private final Label chooseTokenSetLabel = new Label("Choose a list of tokens");
   private final VerticalPanel nameGeneratorPanel = new VerticalPanel();
   private final Label minTokensLabel = new Label("min tokens");
   private final Button generateNamesButton = new Button("Generate names");
   private final HorizontalPanel minTokensPanel = new HorizontalPanel();
   private final IntegerBox minTokensBox = new IntegerBox();
   private final HorizontalPanel maxTokensPanel = new HorizontalPanel();
   private final Label maxTokensLabel = new Label("max tokens");
   private final IntegerBox maxTokensBox = new IntegerBox();
   private final ListBox generatedNamesListBox = new ListBox(true);

   /**
    * Entry point method.
    */
   @Override
   public void onModuleLoad() {

      RootPanel rootPanel = RootPanel.get();
      GWT.log(rootPanel.toString(), null);


      // load token sets name
      initTokensSetDropBox();

      tokensPanel.setSpacing(4);

      // Assemble Tokens listbox panel.

      tokensListBox.setWidth("11em");
      tokensListBox.setVisibleItemCount(20);
      VerticalPanel tokensListBoxPanel = new VerticalPanel();
      tokensListBoxPanel.setSpacing(4);
      tokensListBoxPanel.add(chooseTokenSetLabel);
      chooseTokenSetLabel.setWidth("");
      tokensListBoxPanel.add(tokensSetDropBox);
      tokensSetDropBox.addChangeHandler(new ChangeHandler() {
         @Override
         public void onChange(ChangeEvent event) {
            loadTokensOfSelectedTokenSet();
         }
      });
      tokensSetDropBox.setWidth("11em");
      newTokenTextBox.setAlignment(TextAlignment.LEFT);
      tokensListBoxPanel.add(addTokenPanel);
      addTokenPanel.setWidth("11em");

      // Assemble Token panel.
      addTokenPanel.add(newTokenTextBox);
      addTokenPanel.setCellVerticalAlignment(newTokenTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      addTokenPanel.setCellWidth(newTokenTextBox, "11em");
      newTokenTextBox.setSize("8em", "");
      addTokenButton.setSize("3em", "25px");
      addTokenPanel.add(addTokenButton);
      addTokenPanel.setCellVerticalAlignment(addTokenButton, HasVerticalAlignment.ALIGN_MIDDLE);
      addTokenPanel.addStyleName("addTokenPanel");

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
      tokensListBoxPanel.add(tokensListBox);
      tokensPanel.add(tokensListBoxPanel);
      btnRemoveSelected.setText("Remove selected");
      btnRemoveSelected.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            removeSelectedTokensFromCurrentTokenSet();
         }
      });

      tokensListBoxPanel.add(btnRemoveSelected);
      btnRemoveSelected.setWidth("11em");
      mainPanel.add(tokensPanel);
      mainPanel.add(nameGeneratorPanel);
      generateNamesButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            List<String> tokens = new ArrayList<String>(tokensListBox.getItemCount());
            for (int i = 0; i < tokensListBox.getItemCount(); i++) {
               tokens.add(tokensListBox.getValue(i));
            }
            NameGenerator nameGenerator = new NameGenerator(minTokensBox.getValue(), maxTokensBox.getValue(), tokens);
            Set<String> generatedNames = nameGenerator.generateNames(10);
            generatedNamesListBox.clear();
            int i = 0;
            for (String generatedName : generatedNames) {
               generatedNamesListBox.addItem(generatedName, generatedName);
               i++;
            }
         }
      });
      generateNamesButton.setText("Generate names");

      nameGeneratorPanel.add(generateNamesButton);

      nameGeneratorPanel.add(minTokensPanel);
      minTokensPanel.add(minTokensLabel);
      minTokensLabel.setWidth("90px");
      minTokensBox.setText("2");

      minTokensPanel.add(minTokensBox);
      minTokensBox.setWidth("50px");

      nameGeneratorPanel.add(maxTokensPanel);

      maxTokensPanel.add(maxTokensLabel);
      maxTokensLabel.setWidth("90px");
      maxTokensBox.setText("5");

      maxTokensPanel.add(maxTokensBox);
      maxTokensBox.setWidth("50px");
      generatedNamesListBox.setVisibleItemCount(20);

      nameGeneratorPanel.add(generatedNamesListBox);
      generatedNamesListBox.setWidth("11em");

      // Associate the Main panel with the HTML host page.
      RootPanel.get("inominaX").add(mainPanel);
   }

   private void addToken() {
      final String newToken = newTokenTextBox.getText().trim();

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
            loadTokensOfSelectedTokenSet();
         }
      };
      // call inominaxService
      inominaxService.addToTokenSet(selectedTokenSet(), new String[] { newToken }, addToTokenSetCallback);
      newTokenTextBox.setFocus(true);
   }

   private void loadTokensOfSelectedTokenSet() {
      loadTokensOf(selectedTokenSet());
   }

   private void removeSelectedTokensFromCurrentTokenSet() {
      final String selectedTokensSetName = selectedTokenSet();
      List<Integer> selectedTokensIndexes = new ArrayList<Integer>();
      List<String> selectedTokens = new ArrayList<String>();
      for (int i = 0; i < tokensListBox.getItemCount(); i++) {
         if (tokensListBox.isItemSelected(i)) {
            selectedTokens.add(tokensListBox.getValue(i));
            selectedTokensIndexes.add(i);
            // tokensListBox.removeItem(i);
         }
      }
      GWT.log("selectedTokens=" + selectedTokens);

      // Set up the callback object.
      AsyncCallback<Void> removeFromTokenSetCallback = new AsyncCallback<Void>() {
         @Override
         public void onFailure(Throwable caught) {
            if (caught instanceof FunctionnalException) {
               Window.alert(caught.getMessage());
            }

         }
         @Override
         public void onSuccess(Void result) {
            // refresh current token set
            loadTokensOf(selectedTokensSetName);
         }
      };
      // call inominaxService
      inominaxService.removeFromTokenSet(selectedTokensSetName, selectedTokens.toArray(new String[0]), removeFromTokenSetCallback);
   }

   private String selectedTokenSet() {
      return tokensSetDropBox.getValue(tokensSetDropBox.getSelectedIndex());
   }

   private void initTokensSetDropBox() {
      GWT.log("initializing getTokenSetsNamesCallback");
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