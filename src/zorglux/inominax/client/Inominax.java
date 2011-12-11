package zorglux.inominax.client;

import static com.google.gwt.user.client.Window.alert;

import java.util.*;

import com.google.gwt.core.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

import zorglux.inominax.client.callback.BasicCallback;
import zorglux.inominax.exception.FunctionnalException;

public class Inominax implements EntryPoint, CloseHandler<PopupPanel> {

	private static final String STARTS_ENDS_CONTAINS_SIZE = "50px";
	private static final int DEFAULT_NUMBER_OF_NAMES_TO_GENERATE = 40;
	private Inominax me;
	// UI
	private final HorizontalPanel addTokenPanel = new HorizontalPanel();
	private final TextBox newTokenTextBox = new TextBox();
	private final Button addTokenButton = new Button("Add");

	// services
	private final InominaxServiceAsync inominaxService = GWT.create(InominaxService.class);
	private final Set<String> allTokens = new HashSet<String>();

	private final ListBox tokensListBox = new ListBox(true);
	private final ListBox tokenSetDropBox = new ListBox(false);
	private final VerticalPanel tokensPanel = new VerticalPanel();
	private final Button removeSelectedTokensButton = new Button("remove selected");
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
	private final Grid mainPanel = new Grid(2, 3);
	private final HTML htmlMyTokens = new HTML("My Tokens", true);
	private final HTML htmlNameGenerator = new HTML("Name generator", true);
	private final HTML htmlMyNames = new HTML("My Names", true);
	private final ListBox generatedNamesListBox2 = new ListBox(true);
	private final HorizontalPanel generatedNamesPanel = new HorizontalPanel();
	private final HorizontalPanel generateNamesPanel = new HorizontalPanel();
	private final Label generateLabel = new Label("Generate");
	private final VerticalPanel nameGenerationParamsPanel = new VerticalPanel();
	private final CaptionPanel nameGenerationParamsCaptionPanel = new CaptionPanel("Name generation params");
	private final Label addSelectedNamesLabel = new Label("Add selected names to list ");
	private final HorizontalPanel addSelectedNamesPanel = new HorizontalPanel();
	private final ListBox addGeneratedNamesToUserNamesComboBox = new ListBox();
	private final Button addGeneratedNamesToUserListNamesButton = new Button("New button");
	private final VerticalPanel userNamesPanel = new VerticalPanel();
	private final Label chooseNameSetLabel = new Label("Choose a list of names");
	private final ListBox nameSetDropBox = new ListBox(false);
	private final HorizontalPanel addNamePanel = new HorizontalPanel();
	private final TextBox newNameTextBox = new TextBox();
	private final Button addNameButton = new Button("Add");
	private final ListBox namesListBox = new ListBox(true);
	private final Button removeSelectedNamesButton = new Button("remove selected");
	private final Button manageNameSetButton = new Button();
	private final HorizontalPanel horizontalPanel = new HorizontalPanel();
	private final Label useAllTokensLabel = new Label("use tokens of all list ?");
	private final SimpleCheckBox useAllTokens = new SimpleCheckBox();

	/**
	 * Entry point method.
	 */
	@Override
	public void onModuleLoad() {
		me = this;

		RootPanel rootPanel = RootPanel.get();
		rootPanel.setSize("800px", "700px");
		GWT.log(rootPanel.toString());

		// load token sets name
		loadTokensSetDropBox();
		loadNamesSetDropBox();

		htmlMyTokens.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		mainPanel.setWidget(0, 0, htmlMyTokens);
		htmlNameGenerator.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		mainPanel.setWidget(0, 1, htmlNameGenerator);

		mainPanel.setWidget(0, 2, htmlMyNames);
		mainPanel.setWidget(1, 0, tokensPanel);

		tokensPanel.setSpacing(4);
		tokensPanel.add(chooseTokenSetLabel);
		chooseTokenSetLabel.setWidth("");
		tokensPanel.add(tokenSetDropBox);
		tokenSetDropBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				loadTokensOfSelectedTokenSet();
			}
		});
		tokenSetDropBox.setWidth("11em");
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
		tokensPanel.add(removeSelectedTokensButton);
		removeSelectedTokensButton.setText("Remove selected");
		removeSelectedTokensButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeSelectedTokensFromCurrentTokenSet();
			}
		});
		removeSelectedTokensButton.setWidth("11em");
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
		mainPanel.setWidget(1, 1, nameGeneratorPanel);
		nameGeneratorPanel.setSize("100%", "");
		mainPanel.getCellFormatter().setHeight(1, 1, "");
		mainPanel.getCellFormatter().setWidth(1, 1, "");

		nameGeneratorPanel.add(nameGenerationParamsCaptionPanel);
		nameGenerationParamsCaptionPanel.setWidth("100pct");
		nameGeneratorPanel.setCellWidth(nameGenerationParamsCaptionPanel, "100pct");
		nameGenerationParamsPanel.setSpacing(4);
		nameGenerationParamsCaptionPanel.setContentWidget(nameGenerationParamsPanel);
		nameGenerationParamsPanel.setSize("100pct", "100pct");
		generateNamesPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		nameGenerationParamsPanel.add(generateNamesPanel);
		nameGenerationParamsPanel.setCellVerticalAlignment(generateNamesPanel, HasVerticalAlignment.ALIGN_MIDDLE);
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
		startsWithTokenPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		nameGenerationParamsPanel.add(startsWithTokenPanel);
		nameGenerationParamsPanel.setCellVerticalAlignment(startsWithTokenPanel, HasVerticalAlignment.ALIGN_MIDDLE);
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
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		nameGeneratorPanel.add(horizontalPanel);
		horizontalPanel.add(generateNamesButton);
		generateNamesButton.setHTML("Generate " + DEFAULT_NUMBER_OF_NAMES_TO_GENERATE + " names");
		generateNamesPanel.setCellVerticalAlignment(generateNamesButton, HasVerticalAlignment.ALIGN_MIDDLE);
		nameGeneratorPanel.setCellVerticalAlignment(generateNamesButton, HasVerticalAlignment.ALIGN_MIDDLE);
		nameGeneratorPanel.setCellHeight(generateNamesButton, "40");
		nameGeneratorPanel.setCellHorizontalAlignment(generateNamesButton, HasHorizontalAlignment.ALIGN_CENTER);
		useAllTokensLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		horizontalPanel.add(useAllTokensLabel);
		useAllTokensLabel.setSize("143px", "");

		horizontalPanel.add(useAllTokens);
		horizontalPanel.setCellHorizontalAlignment(useAllTokens, HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setCellVerticalAlignment(useAllTokens, HasVerticalAlignment.ALIGN_MIDDLE);
		useAllTokens.setSize("14px", "14px");
		generateNamesButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Set<String> generatedNames = generateNames(tokensForGeneratingNames());
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

			protected List<String> tokensForGeneratingNames() {
				List<String> tokens;
				if (useAllTokens.getValue()) {
					tokens = new ArrayList<String>(allTokens);
				} else {
					tokens = new ArrayList<String>(tokensListBox.getItemCount());
					// use only selected tokens list
					for (int i = 0; i < tokensListBox.getItemCount(); i++) {
						tokens.add(tokensListBox.getValue(i));
					}
				}
				return tokens;
			}

			private Set<String> generateNames(List<String> tokens) {
				try {
					NameGenerator nameGenerator = new NameGenerator(minTokensBox.getValue(), maxTokensBox.getValue(), tokens);
					if (!startsWithTextBox.getValue().isEmpty())
						nameGenerator.generatedNameMustStartsWith(startsWithTextBox.getValue());
					if (!endsWithTextBox.getValue().isEmpty()) nameGenerator.generatedNameMustEndWith(endsWithTextBox.getValue());
					if (!containsTextBox.getValue().isEmpty()) nameGenerator.generatedNameMustContain(containsTextBox.getValue());

					Set<String> generatedNames = nameGenerator.generateNames(numberOfNamesToGenerateBox.getValue());
					return generatedNames;
				} catch (FunctionnalException e) {
					alert(e.getMessage());
					return new HashSet<String>();
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
		addSelectedNamesPanel.setSpacing(4);

		nameGeneratorPanel.add(addSelectedNamesPanel);
		addSelectedNamesPanel.setWidth("100%");
		addSelectedNamesPanel.add(addSelectedNamesLabel);
		addSelectedNamesPanel.setCellHorizontalAlignment(addSelectedNamesLabel, HasHorizontalAlignment.ALIGN_RIGHT);
		addSelectedNamesPanel.setCellVerticalAlignment(addSelectedNamesLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		addSelectedNamesLabel.setWidth("100%");
		addGeneratedNamesToUserNamesComboBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				nameSetDropBox.setSelectedIndex(addGeneratedNamesToUserNamesComboBox.getSelectedIndex());
				loadNamesOfSelectedNameSet();
			}
		});

		addSelectedNamesPanel.add(addGeneratedNamesToUserNamesComboBox);
		addSelectedNamesPanel.setCellVerticalAlignment(addGeneratedNamesToUserNamesComboBox, HasVerticalAlignment.ALIGN_MIDDLE);
		addGeneratedNamesToUserNamesComboBox.setWidth("100%");
		addGeneratedNamesToUserListNamesButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addSelectedGeneratedNamesToNameList();
			}
		});
		addGeneratedNamesToUserListNamesButton.setText("Add");

		addSelectedNamesPanel.add(addGeneratedNamesToUserListNamesButton);
		addGeneratedNamesToUserListNamesButton.setWidth("100%");

		// users names panel
		mainPanel.setWidget(1, 2, userNamesPanel);

		// Associate the Main panel with the HTML host page.
		RootPanel.get("inominaX").add(mainPanel);
		mainPanel.setWidth("100pct");

		userNamesPanel.setSpacing(4);
		userNamesPanel.setWidth("13em");
		chooseNameSetLabel.setStyleName("h1");

		userNamesPanel.add(chooseNameSetLabel);
		chooseNameSetLabel.setWidth("");
		nameSetDropBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				loadNamesOfSelectedNameSet();
			}
		});

		userNamesPanel.add(nameSetDropBox);
		nameSetDropBox.setWidth("11em");

		userNamesPanel.add(addNamePanel);
		addNamePanel.setWidth("11em");
		newNameTextBox.setFocus(true);
		newNameTextBox.setAlignment(TextAlignment.LEFT);

		addNamePanel.add(newNameTextBox);
		newNameTextBox.setSize("8em", "");
		addNameButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addName();
			}
		});

		addNamePanel.add(addNameButton);
		addNameButton.setSize("3em", "25px");
		namesListBox.setVisibleItemCount(25);

		userNamesPanel.add(namesListBox);
		namesListBox.setWidth("11em");
		removeSelectedNamesButton.setText("Remove selected");
		removeSelectedNamesButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeSelectedNamesFromCurrentNameSet();
			}
		});

		userNamesPanel.add(removeSelectedNamesButton);
		removeSelectedNamesButton.setWidth("11em");
		manageNameSetButton.setText("Manage list of names");
		manageNameSetButton.addClickHandler(new ClickHandler() {
			private NameSetManagementDialogBox nameSetManagementDialogBox;

			@Override
			public void onClick(ClickEvent event) {
				nameSetManagementDialogBox = new NameSetManagementDialogBox(inominaxService);
				nameSetManagementDialogBox.addCloseHandler(me);
				nameSetManagementDialogBox.center();
				nameSetManagementDialogBox.show();
				nameSetManagementDialogBox.focusNewNameSetTextBox();
			}
		});

		userNamesPanel.add(manageNameSetButton);
		manageNameSetButton.setWidth("11em");
		mainPanel.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);

		hideLoadingIndicator();
	}

	private void hideLoadingIndicator() {
		Element loading = DOM.getElementById("loading");
		DOM.setInnerHTML(loading, "");
		RootPanel.getBodyElement().removeChild(loading);
	}

	private void addToken() {
		final String newToken = newTokenTextBox.getText().trim();

		AsyncCallback<Void> addToTokenSetCallback = new BasicCallback<Void>() {
			@Override
			public void onResult(Void result) {
				loadTokensOfSelectedTokenSet();
			}
		};
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

		AsyncCallback<Void> removeFromTokenSetCallback = new BasicCallback<Void>() {
			@Override
			public void onResult(Void result) {
				// refresh current token set
				loadTokensOf(selectedTokensSetName);
			}
		};
		inominaxService
				.removeFromTokenSet(selectedTokensSetName, selectedTokens.toArray(new String[0]), removeFromTokenSetCallback);
	}

	private String selectedTokenSet() {
		return tokenSetDropBox.getValue(tokenSetDropBox.getSelectedIndex());
	}

	private void loadTokensSetDropBox() {
		GWT.log("initializing getTokenSetsNamesCallback");
		AsyncCallback<List<String>> getTokenSetsNamesCallback = new BasicCallback<List<String>>() {
			@Override
			public void onResult(List<String> tokenSetsNames) {
				tokenSetDropBox.clear();
				for (String tokenSetName : tokenSetsNames) {
					tokenSetDropBox.addItem(tokenSetName);
				}
				loadTokensOfSelectedTokenSet();
			}
		};
		inominaxService.getTokenSetsNames(getTokenSetsNamesCallback);
		AsyncCallback<Set<String>> getAllTokensCallback = new BasicCallback<Set<String>>() {
			@Override
			public void onResult(Set<String> tokens) {
				allTokens.addAll(tokens);
			}
		};
		inominaxService.getAllTokens(getAllTokensCallback);
	}

	private void loadTokensOf(String tokenSetName) {
		// Set up the callback object.
		AsyncCallback<Set<String>> loadTokensOfCallback = new BasicCallback<Set<String>>() {
			@Override
			public void onResult(Set<String> tokens) {
				tokensListBox.clear();
				for (String token : tokens) {
					tokensListBox.addItem(token);
				}
			}
		};
		inominaxService.getTokensOfSet(tokenSetName, loadTokensOfCallback);
	}

	@Override
	public void onClose(CloseEvent<PopupPanel> event) {
		// identify from wich popup comes the event
		if (event.getTarget() instanceof TokenSetManagementDialogBox) {
			// we suppose some changes have been made on TokenSet => reload them
			loadTokensSetDropBox();
		} else if (event.getTarget() instanceof NameSetManagementDialogBox) {
			// we suppose some changes have been made on NameSet => reload them
			loadNamesSetDropBox();
		}
	}

	private void addName() {
		final String newName = newNameTextBox.getText().trim();

		AsyncCallback<Void> addToNameSetCallback = new BasicCallback<Void>() {
			@Override
			public void onResult(Void result) {
				loadNamesOfSelectedNameSet();
			}
		};
		inominaxService.addToNameSet(selectedNameSet(), new String[] { newName }, addToNameSetCallback);
		newNameTextBox.setFocus(true);
	}

	private void loadNamesOfSelectedNameSet() {
		loadNamesOf(selectedNameSet());
	}

	private void removeSelectedNamesFromCurrentNameSet() {
		final String selectedNamesSetName = selectedNameSet();
		List<Integer> selectedNamesIndexes = new ArrayList<Integer>();
		List<String> selectedNames = new ArrayList<String>();
		for (int i = 0; i < namesListBox.getItemCount(); i++) {
			if (namesListBox.isItemSelected(i)) {
				selectedNames.add(namesListBox.getValue(i));
				selectedNamesIndexes.add(i);
				// namesListBox.removeItem(i);
			}
		}
		GWT.log("selectedNames=" + selectedNames);

		// Set up the callback object.
		AsyncCallback<Void> removeFromNameSetCallback = new BasicCallback<Void>() {
			@Override
			public void onResult(Void result) {
				// refresh current name set
				loadNamesOf(selectedNamesSetName);
			}
		};
		// call inominaxService
		inominaxService.removeFromNameSet(selectedNamesSetName, selectedNames.toArray(new String[0]), removeFromNameSetCallback);
	}

	private String selectedNameSet() {
		return nameSetDropBox.getValue(nameSetDropBox.getSelectedIndex());
	}

	private void loadNamesSetDropBox() {
		GWT.log("initializing getNameSetsNamesCallback");
		// Set up the callback object.
		AsyncCallback<List<String>> getNameSetsNamesCallback = new BasicCallback<List<String>>() {
			@Override
			public void onResult(List<String> nameSetsNames) {
				nameSetDropBox.clear();
				addGeneratedNamesToUserNamesComboBox.clear();
				for (String nameSetName : nameSetsNames) {
					nameSetDropBox.addItem(nameSetName);
					addGeneratedNamesToUserNamesComboBox.addItem(nameSetName);
				}
				loadNamesOfSelectedNameSet();
			}
		};
		// call inominaxService
		inominaxService.getNameSetsNames(getNameSetsNamesCallback);
	}

	private void loadNamesOf(String nameSetName) {
		// Set up the callback object.
		AsyncCallback<Set<String>> loadNamesOfCallback = new BasicCallback<Set<String>>() {
			@Override
			public void onResult(Set<String> names) {
				namesListBox.clear();
				for (String name : names) {
					namesListBox.addItem(name);
				}
			}
		};
		// call inominaxService
		inominaxService.getNamesOfSet(nameSetName, loadNamesOfCallback);
	}

	private void addSelectedGeneratedNamesToNameList() {
		final String selectedNameSetForAddingGeneratedNames = selectedNameSetForAddingGeneratedNames();
		List<String> selectedGeneratedNames = new ArrayList<String>();
		// get generated names of first list box
		for (int i = 0; i < generatedNamesListBox1.getItemCount(); i++) {
			if (generatedNamesListBox1.isItemSelected(i)) {
				selectedGeneratedNames.add(generatedNamesListBox1.getValue(i));
			}
		}
		// get generated names of second list box
		for (int i = 0; i < generatedNamesListBox2.getItemCount(); i++) {
			if (generatedNamesListBox2.isItemSelected(i)) {
				selectedGeneratedNames.add(generatedNamesListBox2.getValue(i));
			}
		}
		GWT.log("selectedNames=" + selectedGeneratedNames);
		AsyncCallback<Void> addToNameSetCallback = new BasicCallback<Void>() {
			@Override
			public void onResult(Void result) {
				// refresh current name set
				loadNamesOf(selectedNameSetForAddingGeneratedNames);
			}
		};
		inominaxService.addToNameSet(selectedNameSetForAddingGeneratedNames, selectedGeneratedNames.toArray(new String[0]),
				addToNameSetCallback);
	}

	private String selectedNameSetForAddingGeneratedNames() {
		return addGeneratedNamesToUserNamesComboBox.getValue(addGeneratedNamesToUserNamesComboBox.getSelectedIndex());
	}

}