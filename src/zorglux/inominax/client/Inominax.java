package zorglux.inominax.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import zorglux.inominax.exception.FunctionnalException;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Inominax implements EntryPoint, CloseHandler<PopupPanel> {

   private static final String STARTS_ENDS_CONTAINS_LABEL_SIZE = "90px";
   private Inominax me;
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
   private final Label minTokensLabel = new Label("min ");
   private final Button generateNamesButton = new Button("Generate names");
   private final HorizontalPanel minMaxTokensPanel = new HorizontalPanel();
   private final IntegerBox minTokensBox = new IntegerBox();
   private final HorizontalPanel startsWithTokenPanel = new HorizontalPanel();
   private final Label maxTokensLabel = new Label("max");
   private final IntegerBox maxTokensBox = new IntegerBox();
   private final ListBox generatedNamesListBox = new ListBox(true);
   private final HorizontalPanel numberOfNamesToGeneratePanel = new HorizontalPanel();
   private final Label numberOfNamesToGenerateLabel = new Label("nb names");
   private final IntegerBox numberOfNamesToGenerateBox = new IntegerBox();
   private final Label nameGenerationLabel = new Label("Name generation params");
   private final Label numberOfTokensLabel = new Label("number of tokens in name");
   private final Label startsWithLabel = new Label("- starts with ");
   private final TextBox startsWithTextBox = new TextBox();
   private final HorizontalPanel endsWithPanel = new HorizontalPanel();
   private final Label endsWithLabel = new Label("- ends with ");
   private final TextBox endsWithTextBox = new TextBox();
   private final HorizontalPanel containsPanel = new HorizontalPanel();
   private final Label containsLabel = new Label("- contains");
   private final TextBox containsTextBox = new TextBox();
   private final Label nameConstraintsLabel = new Label("generated names must ...");
   private final Button manageTokenSetButton = new Button();

   /**
    * Entry point method.
    */
   @Override
   public void onModuleLoad() {
      me = this;

      RootPanel rootPanel = RootPanel.get();
      rootPanel.setSize("500px", "700px");
      GWT.log(rootPanel.toString(), null);


      tokensPanel.setSpacing(4);
      tokensPanel.add(chooseTokenSetLabel);
      chooseTokenSetLabel.setWidth("");
      tokensPanel.add(tokensSetDropBox);
      tokensSetDropBox.addChangeHandler(new ChangeHandler() {
         @Override
         public void onChange(ChangeEvent event) {
            loadTokensOfSelectedTokenSet();
         }
      });
      tokensSetDropBox.setWidth("11em");
      newTokenTextBox.setAlignment(TextAlignment.LEFT);
      tokensPanel.add(addTokenPanel);
      addTokenPanel.setWidth("11em");
      // load token sets name
      loadTokensSetDropBox();

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

      tokensPanel.add(tokensListBox);

      // Assemble Tokens listbox panel.

      tokensListBox.setWidth("11em");
      tokensListBox.setVisibleItemCount(25);
      tokensPanel.add(btnRemoveSelected);
      btnRemoveSelected.setText("Remove selected");
      btnRemoveSelected.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            removeSelectedTokensFromCurrentTokenSet();
         }
      });
      btnRemoveSelected.setWidth("11em");
      mainPanel.add(tokensPanel);
      tokensPanel.setWidth("13em");
      manageTokenSetButton.addClickHandler(new ClickHandler() {
         private NewTokenSetDialogBox newTokenSetDialogBox;

         @Override
         public void onClick(ClickEvent event) {
            newTokenSetDialogBox = new NewTokenSetDialogBox(inominaxService);
            newTokenSetDialogBox.addCloseHandler(me);
            newTokenSetDialogBox.center();
            newTokenSetDialogBox.show();
            newTokenSetDialogBox.focusNewTokenSetTextBox();
         }
      });
      manageTokenSetButton.setText("Manage list of tokens");

      tokensPanel.add(manageTokenSetButton);
      manageTokenSetButton.setWidth("11em");
      nameGeneratorPanel.setSpacing(4);
      mainPanel.add(nameGeneratorPanel);

      nameGeneratorPanel.add(nameGenerationLabel);
      nameGenerationLabel.setWidth("");

      nameGeneratorPanel.add(numberOfNamesToGeneratePanel);

      numberOfNamesToGeneratePanel.add(numberOfNamesToGenerateLabel);
      numberOfNamesToGeneratePanel.setCellVerticalAlignment(numberOfNamesToGenerateLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      numberOfNamesToGenerateLabel.setWidth("90px");
      numberOfNamesToGenerateBox.setText("20");

      numberOfNamesToGeneratePanel.add(numberOfNamesToGenerateBox);
      numberOfNamesToGenerateBox.setWidth("50px");

      nameGeneratorPanel.add(numberOfTokensLabel);

      nameGeneratorPanel.add(minMaxTokensPanel);
      nameGeneratorPanel.setCellVerticalAlignment(minMaxTokensPanel, HasVerticalAlignment.ALIGN_MIDDLE);
      minTokensLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
      minTokensLabel.setDirectionEstimator(true);
      minMaxTokensPanel.add(minTokensLabel);
      minMaxTokensPanel.setCellVerticalAlignment(minTokensLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      minTokensLabel.setWidth("40px");
      minTokensBox.setText("2");

      minMaxTokensPanel.add(minTokensBox);
      minMaxTokensPanel.setCellVerticalAlignment(minTokensBox, HasVerticalAlignment.ALIGN_MIDDLE);
      minTokensBox.setWidth("30px");
      maxTokensLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
      maxTokensLabel.setDirectionEstimator(true);
      minMaxTokensPanel.add(maxTokensLabel);
      minMaxTokensPanel.setCellVerticalAlignment(maxTokensLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      maxTokensLabel.setWidth("40px");
      minMaxTokensPanel.add(maxTokensBox);
      minMaxTokensPanel.setCellVerticalAlignment(maxTokensBox, HasVerticalAlignment.ALIGN_MIDDLE);
      maxTokensBox.setText("4");
      maxTokensBox.setWidth("30px");
      nameConstraintsLabel.setWordWrap(false);

      nameGeneratorPanel.add(nameConstraintsLabel);

      nameGeneratorPanel.add(startsWithTokenPanel);
      startsWithLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
      startsWithTokenPanel.add(startsWithLabel);
      startsWithLabel.setWidth(STARTS_ENDS_CONTAINS_LABEL_SIZE);
      startsWithTokenPanel.setCellVerticalAlignment(startsWithLabel, HasVerticalAlignment.ALIGN_MIDDLE);

      startsWithTokenPanel.add(startsWithTextBox);
      startsWithTokenPanel.setCellVerticalAlignment(startsWithTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      startsWithTextBox.setWidth("50px");

      nameGeneratorPanel.add(endsWithPanel);
      endsWithPanel.setWidth("");
      endsWithLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

      endsWithPanel.add(endsWithLabel);
      endsWithPanel.setCellVerticalAlignment(endsWithLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      endsWithLabel.setWidth(STARTS_ENDS_CONTAINS_LABEL_SIZE);

      endsWithPanel.add(endsWithTextBox);
      endsWithPanel.setCellVerticalAlignment(endsWithTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      endsWithTextBox.setWidth("50px");

      nameGeneratorPanel.add(containsPanel);
      containsLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

      containsPanel.add(containsLabel);
      containsLabel.setWidth(STARTS_ENDS_CONTAINS_LABEL_SIZE);
      containsPanel.setCellVerticalAlignment(containsLabel, HasVerticalAlignment.ALIGN_MIDDLE);

      containsPanel.add(containsTextBox);
      containsPanel.setCellVerticalAlignment(containsTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      containsTextBox.setWidth("50px");
      generateNamesButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            List<String> tokens = new ArrayList<String>(tokensListBox.getItemCount());
            for (int i = 0; i < tokensListBox.getItemCount(); i++) {
               tokens.add(tokensListBox.getValue(i));
            }
            NameGenerator nameGenerator = new NameGenerator(minTokensBox.getValue(), maxTokensBox.getValue(), tokens);
            if (!startsWithTextBox.getValue().isEmpty()) {
               nameGenerator.generatedNameMustStartsWith(startsWithTextBox.getValue());
            }
            if (!endsWithTextBox.getValue().isEmpty()) {
               nameGenerator.generatedNameMustEndWith(endsWithTextBox.getValue());
            }
            if (!containsTextBox.getValue().isEmpty()) {
               nameGenerator.generatedNameMustContain(containsTextBox.getValue());
            }
            Set<String> generatedNames = nameGenerator.generateNames(numberOfNamesToGenerateBox.getValue());
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
      generatedNamesListBox.setVisibleItemCount(20);

      nameGeneratorPanel.add(generatedNamesListBox);
      generatedNamesListBox.setWidth("15em");

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

   public void loadTokensSetDropBox() {
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
            tokensSetDropBox.clear();
            for (String tokenSetName : tokenSetsNames) {
               tokensSetDropBox.addItem(tokenSetName);
            }
            loadTokensOfSelectedTokenSet();
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

   @Override
   public void onClose(CloseEvent<PopupPanel> event) {
      // only one popup : newTokenSetDialogBox, thus we know what to do.
      loadTokensSetDropBox();
   }
}