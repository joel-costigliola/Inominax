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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;

public class Inominax implements EntryPoint, CloseHandler<PopupPanel> {

   private static final String STARTS_ENDS_CONTAINS_SIZE = "50px";
   private static final int DEFAULT_NUMBER_OF_NAMES_TO_GENERATE = 40;
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
   private final Label minTokensLabel = new Label("tokens min and ");
   private final Button generateNamesButton = new Button("Generate names");
   private final IntegerBox minTokensBox = new IntegerBox();
   private final HorizontalPanel startsWithTokenPanel = new HorizontalPanel();
   private final Label maxTokensLabel = new Label("tokens max");
   private final IntegerBox maxTokensBox = new IntegerBox();
   private final ListBox generatedNamesListBox1 = new ListBox(true);
   private final Label numberOfNamesToGenerateLabel = new Label("names");
   private final IntegerBox numberOfNamesToGenerateBox = new IntegerBox();
   private final Label numberOfTokensLabel = new Label("with");
   private final Label startsWithLabel = new Label("starting with");
   private final TextBox startsWithTextBox = new TextBox();
   private final Label endsWithLabel = new Label("ending with");
   private final TextBox endsWithTextBox = new TextBox();
   private final Label containsLabel = new Label("containing");
   private final TextBox containsTextBox = new TextBox();
   private final Button manageTokenSetButton = new Button();
   private final Grid grid = new Grid(2, 3);
   private final HTML htmlMyTokens = new HTML("My Tokens", true);
   private final HTML htmlNameGenerator = new HTML("Name generator", true);
   private final HTML htmlMyNames = new HTML("My Names", true);
   private final ListBox generatedNamesListBox2 = new ListBox(true);
   private final HorizontalPanel generatedNamesPanel = new HorizontalPanel();
   private final HorizontalPanel generateNamesPanel = new HorizontalPanel();
   private final Label generateLabel = new Label("Generate");
   private final VerticalPanel verticalPanel = new VerticalPanel();
   private final CaptionPanel nameGenerationParamsCaptionPanel = new CaptionPanel("Name generation params");
   private final Label lblAddSelectedNames = new Label("Add selected names to ");
   private final HorizontalPanel horizontalPanel = new HorizontalPanel();
   private final ListBox userNamesComboBox = new ListBox();
   private final Button addGeneratedNamesToUserListNamesButton = new Button("New button");

   /**
    * Entry point method.
    */
   @Override
   public void onModuleLoad() {
      me = this;

      RootPanel rootPanel = RootPanel.get();
      rootPanel.setSize("500px", "700px");
      GWT.log(rootPanel.toString(), null);
      // load token sets name
      loadTokensSetDropBox();

      htmlMyTokens.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

      grid.setWidget(0, 0, htmlMyTokens);
      htmlNameGenerator.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

      grid.setWidget(0, 1, htmlNameGenerator);

      grid.setWidget(0, 2, htmlMyNames);
      grid.setWidget(1, 0, tokensPanel);

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
      tokensPanel.setWidth("13em");
      manageTokenSetButton.addClickHandler(new ClickHandler() {
         private TokenSetManagementDialogBox tokenSetManagementDialogBox;

         @Override
         public void onClick(ClickEvent event) {
            tokenSetManagementDialogBox = new TokenSetManagementDialogBox(inominaxService);
            tokenSetManagementDialogBox.addCloseHandler(me);
            tokenSetManagementDialogBox.center();
            tokenSetManagementDialogBox.show();
            tokenSetManagementDialogBox.focusNewTokenSetTextBox();
         }
      });
      manageTokenSetButton.setText("Manage list of tokens");

      tokensPanel.add(manageTokenSetButton);
      manageTokenSetButton.setWidth("11em");
      nameGeneratorPanel.setSpacing(4);
      grid.setWidget(1, 1, nameGeneratorPanel);
      nameGeneratorPanel.setSize("", "");
      grid.getCellFormatter().setHeight(1, 1, "");
      grid.getCellFormatter().setWidth(1, 1, "");

      nameGeneratorPanel.add(nameGenerationParamsCaptionPanel);
      nameGenerationParamsCaptionPanel.setWidth("100pct");
      nameGeneratorPanel.setCellWidth(nameGenerationParamsCaptionPanel, "100pct");
      verticalPanel.setSpacing(4);
      nameGenerationParamsCaptionPanel.setContentWidget(verticalPanel);
      verticalPanel.setSize("100pct", "100pct");
      generateNamesPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
      verticalPanel.add(generateNamesPanel);
      verticalPanel.setCellVerticalAlignment(generateNamesPanel, HasVerticalAlignment.ALIGN_MIDDLE);
      generateNamesPanel.setWidth("");
      generateNamesPanel.setSpacing(4);

      generateNamesPanel.add(generateLabel);
      generateNamesPanel.setCellVerticalAlignment(generateLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      generateLabel.setWidth("");
      numberOfNamesToGenerateBox.addValueChangeHandler(new ValueChangeHandler<Integer>() {
         @Override
         public void onValueChange(ValueChangeEvent<Integer> event) {
            Integer numberOfNamesToGenerate = event.getValue();
            generateNamesButton.setHTML("Generate " + numberOfNamesToGenerate + " names");
         }
      });
      numberOfNamesToGenerateBox.setAlignment(TextAlignment.CENTER);
      generateNamesPanel.add(numberOfNamesToGenerateBox);
      generateNamesPanel.setCellVerticalAlignment(numberOfNamesToGenerateBox, HasVerticalAlignment.ALIGN_MIDDLE);
      numberOfNamesToGenerateBox.setText(String.valueOf(DEFAULT_NUMBER_OF_NAMES_TO_GENERATE));
      generateNamesButton.setHTML("Generate " + DEFAULT_NUMBER_OF_NAMES_TO_GENERATE + " names");
      numberOfNamesToGenerateBox.setWidth("40px");
      generateNamesPanel.add(numberOfNamesToGenerateLabel);
      generateNamesPanel.setCellVerticalAlignment(numberOfNamesToGenerateLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      numberOfNamesToGenerateLabel.setWidth("");
      generateNamesPanel.add(numberOfTokensLabel);
      generateNamesPanel.setCellVerticalAlignment(numberOfTokensLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      generateNamesPanel.add(minTokensBox);
      generateNamesPanel.setCellVerticalAlignment(minTokensBox, HasVerticalAlignment.ALIGN_MIDDLE);
      minTokensBox.setAlignment(TextAlignment.CENTER);
      minTokensBox.setText("2");
      minTokensBox.setWidth("25px");
      generateNamesPanel.add(minTokensLabel);
      generateNamesPanel.setCellVerticalAlignment(minTokensLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      minTokensLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      minTokensLabel.setDirectionEstimator(true);
      minTokensLabel.setWidth("");
      generateNamesPanel.add(maxTokensBox);
      generateNamesPanel.setCellVerticalAlignment(maxTokensBox, HasVerticalAlignment.ALIGN_MIDDLE);
      maxTokensBox.setAlignment(TextAlignment.CENTER);
      maxTokensBox.setText("4");
      maxTokensBox.setWidth("25px");
      generateNamesPanel.add(maxTokensLabel);
      generateNamesPanel.setCellVerticalAlignment(maxTokensLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      maxTokensLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      maxTokensLabel.setDirectionEstimator(true);
      maxTokensLabel.setWidth("");
      generateNamesPanel.setCellVerticalAlignment(generateNamesButton, HasVerticalAlignment.ALIGN_MIDDLE);
      startsWithTokenPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
      verticalPanel.add(startsWithTokenPanel);
      verticalPanel.setCellVerticalAlignment(startsWithTokenPanel, HasVerticalAlignment.ALIGN_MIDDLE);
      startsWithTokenPanel.setSpacing(4);
      startsWithLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
      startsWithTokenPanel.setSize("", "");
      startsWithTokenPanel.add(startsWithLabel);
      startsWithLabel.setWidth("");
      startsWithTokenPanel.setCellVerticalAlignment(startsWithLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      startsWithTextBox.setAlignment(TextAlignment.LEFT);

      startsWithTokenPanel.add(startsWithTextBox);
      startsWithTokenPanel.setCellVerticalAlignment(startsWithTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      startsWithTextBox.setWidth(STARTS_ENDS_CONTAINS_SIZE);
      startsWithTokenPanel.add(containsLabel);
      startsWithTokenPanel.setCellVerticalAlignment(containsLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      containsLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
      containsLabel.setWidth("");
      startsWithTokenPanel.add(containsTextBox);
      containsTextBox.setWidth(STARTS_ENDS_CONTAINS_SIZE);
      startsWithTokenPanel.add(endsWithLabel);
      startsWithTokenPanel.setCellVerticalAlignment(endsWithLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      endsWithLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      endsWithLabel.setWidth("");
      startsWithTokenPanel.add(endsWithTextBox);
      endsWithTextBox.setWidth(STARTS_ENDS_CONTAINS_SIZE);
      nameGeneratorPanel.add(generateNamesButton);
      nameGeneratorPanel.setCellVerticalAlignment(generateNamesButton, HasVerticalAlignment.ALIGN_MIDDLE);
      nameGeneratorPanel.setCellHeight(generateNamesButton, "40");
      nameGeneratorPanel.setCellHorizontalAlignment(generateNamesButton, HasHorizontalAlignment.ALIGN_CENTER);
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
            generatedNamesListBox1.clear();
            generatedNamesListBox2.clear();
            int i = 0;
            for (String generatedName : generatedNames) {
               if (i % 2 == 0) {
                  generatedNamesListBox1.addItem(generatedName, generatedName);
               } else {
                  generatedNamesListBox2.addItem(generatedName, generatedName);
               }
               i++;
            }
         }
      });
      generatedNamesPanel.setSpacing(2);
      generatedNamesPanel.setSize("100pct", "");

      nameGeneratorPanel.add(generatedNamesPanel);
      nameGeneratorPanel.setCellHeight(generatedNamesPanel, "100pct");
      nameGeneratorPanel.setCellWidth(generatedNamesPanel, "100pct");
      generatedNamesPanel.add(generatedNamesListBox1);
      generatedNamesListBox1.setVisibleItemCount(20);
      generatedNamesListBox1.setWidth("17em");
      generatedNamesPanel.add(generatedNamesListBox2);
      generatedNamesListBox2.setVisibleItemCount(20);
      generatedNamesListBox2.setWidth("17em");
      horizontalPanel.setSpacing(4);

      nameGeneratorPanel.add(horizontalPanel);
      horizontalPanel.setWidth("100%");
      horizontalPanel.add(lblAddSelectedNames);
      horizontalPanel.setCellHorizontalAlignment(lblAddSelectedNames, HasHorizontalAlignment.ALIGN_RIGHT);
      horizontalPanel.setCellVerticalAlignment(lblAddSelectedNames, HasVerticalAlignment.ALIGN_MIDDLE);
      lblAddSelectedNames.setWidth("100%");

      horizontalPanel.add(userNamesComboBox);
      horizontalPanel.setCellVerticalAlignment(userNamesComboBox, HasVerticalAlignment.ALIGN_MIDDLE);
      userNamesComboBox.setWidth("100%");
      addGeneratedNamesToUserListNamesButton.setText("Add");

      horizontalPanel.add(addGeneratedNamesToUserListNamesButton);
      addGeneratedNamesToUserListNamesButton.setWidth("100%");

      // Associate the Main panel with the HTML host page.
      RootPanel.get("inominaX").add(grid);
      grid.setWidth("100pct");
      grid.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);
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