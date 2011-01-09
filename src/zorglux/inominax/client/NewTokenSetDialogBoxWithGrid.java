package zorglux.inominax.client;

import java.util.List;

import zorglux.inominax.client.callback.BasicCallback;
import zorglux.inominax.shared.TokenSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Grid;

public class NewTokenSetDialogBoxWithGrid extends DialogBox {

   private static final String SPACE_BETWEEN_WIDGETS = "10px";
   private static final String MINI_LABEL_LENGTH = "20px";
   // common attributes
   private static final int PANEL_SPACING = 2;
   private static final String BUTTON_DEFAULT_SIZE = "90px";
   private static final String LABEL_DEFAULT_SIZE = "160px";
   private static final String TEXT_BOX_DEFAULT_SIZE = "140px";
   private static final String COMBO_BOX_DEFAULT_SIZE = "150px";

   // services
   private InominaxServiceAsync inominaxService;

   // panels
   private final VerticalPanel mainPanel = new VerticalPanel();
   private final HorizontalPanel createTokenSetPanel = new HorizontalPanel();
   private final HorizontalPanel removeTokenSetPanel = new HorizontalPanel();
   private final HorizontalPanel renameTokenSetPanel = new HorizontalPanel();
   private final HorizontalPanel cloneTokenSetPanel = new HorizontalPanel();
   private final VerticalPanel exitPanel = new VerticalPanel();

   // widgets
   private final Label createTokenSetLabel = new Label("Create list of tokens");
   private final TextBox createTokenSetTextBox = new TextBox();
   private final Button createTokenSetButton = new Button("New button");
   private final Button exitButton = new Button("New button");
   private final Label renameListLabel = new Label("Rename list of tokens ");
   private final Label cloneTokenSetLabel = new Label("Clone list of tokens");
   private final Label removeTokenSetLabel = new Label("Remove list of tokens ");
   private final ListBox renameTokenSetComboBox = new ListBox();
   private final Label renameToLabel = new Label("to");
   private final TextBox renameTokenSetTextBox = new TextBox();
   private final ListBox cloneTokenSetComboBox = new ListBox();
   private final Label cloneTokenSetAsLabel = new Label("as");
   private final TextBox cloneTokenSetTextBox = new TextBox();
   private final ListBox removeTokenSetComboBox = new ListBox();
   private final Button renameTokenSetButton = new Button("New button");
   private final Button cloneTokenSetButton = new Button("New button");
   private final Button removeTokenSetButton = new Button("New button");
   private final InlineHTML horizontalRule1 = new InlineHTML("<hr>");
   private final InlineHTML horizontalRule2 = new InlineHTML("<hr>");
   private final InlineHTML horizontalRule3 = new InlineHTML("<hr>");
   private final InlineHTML horizontalRule4 = new InlineHTML("<hr>");
   private final Grid grid = new Grid(5, 3);
   private final Label renameSpaceLabel = new Label("");
   private final Label cloneSpaceLabel = new Label("");

   public NewTokenSetDialogBoxWithGrid(final InominaxServiceAsync inominaxServiceParam) {
      inominaxService = inominaxServiceParam;
      setWidth("350px");
      setHTML("Manage list of tokens");
      mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
      setWidget(mainPanel);

      // ===========================================================================================================================
      // new panel
      // ===========================================================================================================================

      createTokenSetPanel.setSpacing(PANEL_SPACING);

      // createTokenSetPanel.add(createTokenSetLabel);
      createTokenSetPanel.setCellVerticalAlignment(createTokenSetLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      createTokenSetLabel.setWidth(LABEL_DEFAULT_SIZE);

      // createTokenSetPanel.add(createTokenSetTextBox);
      createTokenSetTextBox.setWidth(TEXT_BOX_DEFAULT_SIZE);
      createTokenSetButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            createNewTokensSet(createTokenSetTextBox.getValue());
         }
      });
      createTokenSetButton.setText("create");

      // createTokenSetPanel.add(createTokenSetButton);
      createTokenSetButton.setWidth(BUTTON_DEFAULT_SIZE);

      // ===========================================================================================================================
      // remove panel
      // ===========================================================================================================================

      removeTokenSetPanel.setSpacing(PANEL_SPACING);

      // removeTokenSetPanel.add(removeTokenSetLabel);
      removeTokenSetPanel.setCellVerticalAlignment(removeTokenSetLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      removeTokenSetLabel.setWidth(LABEL_DEFAULT_SIZE);

      // removeTokenSetPanel.add(removeTokenSetComboBox);
      removeTokenSetComboBox.setWidth(COMBO_BOX_DEFAULT_SIZE);
      removeTokenSetButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            String selectedTokenSetName = removeTokenSetComboBox.getValue(removeTokenSetComboBox.getSelectedIndex());
            removeTokenSet(selectedTokenSetName);
         }
      });
      removeTokenSetButton.setText("remove");

      // removeTokenSetPanel.add(removeTokenSetButton);
      removeTokenSetPanel.setCellVerticalAlignment(removeTokenSetButton, HasVerticalAlignment.ALIGN_MIDDLE);
      removeTokenSetButton.setWidth(BUTTON_DEFAULT_SIZE);
      renameTokenSetPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

      // renameListPanel.add(renameListLabel);
      renameListLabel.setWidth(LABEL_DEFAULT_SIZE);

      // renameListPanel.add(renameTokenSetComboBox);
      renameTokenSetComboBox.setWidth(COMBO_BOX_DEFAULT_SIZE);
      renameToLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

      renameTokenSetPanel.add(renameToLabel);
      renameTokenSetPanel.setCellHorizontalAlignment(renameToLabel, HasHorizontalAlignment.ALIGN_CENTER);
      renameTokenSetPanel.setCellVerticalAlignment(renameToLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      renameToLabel.setWidth(MINI_LABEL_LENGTH);
      renameTokenSetPanel.add(renameTokenSetTextBox);
      renameTokenSetPanel.setCellVerticalAlignment(renameTokenSetTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      renameTokenSetTextBox.setWidth(TEXT_BOX_DEFAULT_SIZE);
      renameTokenSetButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            String selectedTokenSetName = renameTokenSetComboBox.getValue(renameTokenSetComboBox.getSelectedIndex());
            renameTokenSet(selectedTokenSetName);
         }
      });
      renameSpaceLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

      renameTokenSetPanel.add(renameSpaceLabel);
      renameSpaceLabel.setWidth(SPACE_BETWEEN_WIDGETS);
      renameTokenSetButton.setText("rename");

      renameTokenSetPanel.add(renameTokenSetButton);
      renameTokenSetPanel.setCellVerticalAlignment(renameTokenSetButton, HasVerticalAlignment.ALIGN_MIDDLE);
      renameTokenSetButton.setWidth(BUTTON_DEFAULT_SIZE);

// cloneListPanel.add(cloneTokenSetLabel);
      cloneTokenSetPanel.setCellVerticalAlignment(cloneTokenSetLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      cloneTokenSetLabel.setWidth(LABEL_DEFAULT_SIZE);

      // cloneListPanel.add(cloneTokenSetComboBox);
      cloneTokenSetComboBox.setWidth(COMBO_BOX_DEFAULT_SIZE);
      cloneTokenSetAsLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

      cloneTokenSetPanel.add(cloneTokenSetAsLabel);
      cloneTokenSetPanel.setCellHorizontalAlignment(cloneTokenSetAsLabel, HasHorizontalAlignment.ALIGN_CENTER);
      cloneTokenSetPanel.setCellVerticalAlignment(cloneTokenSetAsLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      cloneTokenSetAsLabel.setWidth(MINI_LABEL_LENGTH);

      cloneTokenSetPanel.add(cloneTokenSetTextBox);
      cloneTokenSetPanel.setCellVerticalAlignment(cloneTokenSetTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      cloneTokenSetTextBox.setWidth(TEXT_BOX_DEFAULT_SIZE);
      cloneTokenSetButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            String selectedTokenSetName = cloneTokenSetComboBox.getValue(cloneTokenSetComboBox.getSelectedIndex());
            String tokenSetCloneName = cloneTokenSetTextBox.getText();
            cloneTokenSet(selectedTokenSetName, tokenSetCloneName);
         }
      });
      cloneSpaceLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

      cloneTokenSetPanel.add(cloneSpaceLabel);
      cloneSpaceLabel.setWidth(SPACE_BETWEEN_WIDGETS);
      cloneTokenSetButton.setText("clone");

      cloneTokenSetPanel.add(cloneTokenSetButton);
      cloneTokenSetPanel.setCellVerticalAlignment(cloneTokenSetButton, HasVerticalAlignment.ALIGN_MIDDLE);
      cloneTokenSetButton.setWidth(BUTTON_DEFAULT_SIZE);

      // ===========================================================================================================================
      // exit panel
      // ===========================================================================================================================

      exitButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            Widget source = (Widget) event.getSource();
            while (!source.getParent().getClass().equals(NewTokenSetDialogBoxWithGrid.class)) {
               source = source.getParent();
            }
            NewTokenSetDialogBoxWithGrid newTokenSetDialogBox = (NewTokenSetDialogBoxWithGrid) source.getParent();
            newTokenSetDialogBox.hide();
         }
      });
      exitButton.setText("Exit");
      exitButton.setWidth(BUTTON_DEFAULT_SIZE);

      exitPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
      // exitPanel.add(exitButton);

      // ===========================================================================================================================
      // main panel
      // ===========================================================================================================================

      mainPanel.setSpacing(4);
      mainPanel.setSize("368px", MINI_LABEL_LENGTH);
      grid.setCellSpacing(10);
      grid.setBorderWidth(0);

      grid.setWidget(0, 0, createTokenSetLabel);
      grid.setWidget(0, 1, createTokenSetTextBox);
      grid.setWidget(0, 2, createTokenSetButton);

      grid.setWidget(1, 0, removeTokenSetLabel);
      grid.setWidget(1, 1, removeTokenSetComboBox);
      grid.setWidget(1, 2, removeTokenSetButton);

      grid.setWidget(2, 0, renameListLabel);
      grid.setWidget(2, 1, renameTokenSetComboBox);
      grid.setWidget(2, 2, renameTokenSetPanel);

      grid.setWidget(3, 0, cloneTokenSetLabel);
      grid.setWidget(3, 1, cloneTokenSetComboBox);
      grid.setWidget(3, 2, cloneTokenSetPanel);

      grid.setWidget(4, 2, exitButton);

      mainPanel.add(grid);
      mainPanel.setCellVerticalAlignment(grid, HasVerticalAlignment.ALIGN_MIDDLE);
      grid.getCellFormatter().setHorizontalAlignment(4, 2, HasHorizontalAlignment.ALIGN_RIGHT);
      grid.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
      grid.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
      grid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
      grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
      // mainPanel.add(newTokenSetPanel);
      // mainPanel.setCellVerticalAlignment(newTokenSetPanel, HasVerticalAlignment.ALIGN_MIDDLE);
      // mainPanel.add(horizontalRule1);
      // mainPanel.add(removeTokenSetPanel);
      // mainPanel.setCellVerticalAlignment(removeTokenSetPanel, HasVerticalAlignment.ALIGN_MIDDLE);
      // mainPanel.add(horizontalRule3);
      // mainPanel.add(renameListPanel);
      // mainPanel.setCellVerticalAlignment(renameListPanel, HasVerticalAlignment.ALIGN_MIDDLE);
      // mainPanel.add(horizontalRule4);
      // mainPanel.add(cloneListPanel);
      // mainPanel.setCellVerticalAlignment(cloneListPanel, HasVerticalAlignment.ALIGN_MIDDLE);
      // mainPanel.add(horizontalRule2);
      // mainPanel.add(exitPanel);
      mainPanel.setCellVerticalAlignment(exitPanel, HasVerticalAlignment.ALIGN_MIDDLE);
      mainPanel.setCellHorizontalAlignment(exitPanel, HasHorizontalAlignment.ALIGN_RIGHT);

      // fill cell list
      loadTokensSetNames();
      // disable main widows to make it clear dialog box is modal
      setGlassEnabled(true);
   }

   private void createNewTokensSet(String tokenSetName) {
      AsyncCallback<TokenSet> createTokenSetCallback = new BasicCallback<TokenSet>() {
         @Override
         public void onResult(TokenSet newTokenSet) {
            loadTokensSetNames();
         }
      };
      inominaxService.createTokenSet(tokenSetName, createTokenSetCallback);
   }

   public void focusNewTokenSetTextBox() {
      createTokenSetTextBox.setFocus(true);
   }

   private void loadTokensSetNames() {
      AsyncCallback<List<String>> getTokenSetsNamesCallback = new BasicCallback<List<String>>() {
         @Override
         public void onResult(List<String> tokenSetsNames) {
            renameTokenSetComboBox.clear();
            cloneTokenSetComboBox.clear();
            removeTokenSetComboBox.clear();
            for (String tokenSetsName : tokenSetsNames) {
               renameTokenSetComboBox.addItem(tokenSetsName);
               cloneTokenSetComboBox.addItem(tokenSetsName);
               removeTokenSetComboBox.addItem(tokenSetsName);
            }
         }
      };
      // call inominaxService
      inominaxService.getTokenSetsNames(getTokenSetsNamesCallback);
   }

   private void removeTokenSet(String selectedTokenSetName) {
      AsyncCallback<Void> removeTokenSetCallback = new BasicCallback<Void>() {
         @Override
         public void onResult(Void nothing) {
            loadTokensSetNames();
         }
      };
      inominaxService.removeTokenSet(selectedTokenSetName, removeTokenSetCallback);
   }

   private void renameTokenSet(String selectedTokenSetName) {
      AsyncCallback<Void> renameTokenSetCallback = new BasicCallback<Void>() {
         @Override
         public void onResult(Void nothing) {
            loadTokensSetNames();
         }
      };
      inominaxService.renameTokenSet(selectedTokenSetName, renameTokenSetTextBox.getText(), renameTokenSetCallback);
   }

   private void cloneTokenSet(String selectedTokenSetName, String tokenSetCloneName) {
      AsyncCallback<Void> cloneTokenSetCallback = new BasicCallback<Void>() {
         @Override
         public void onResult(Void nothing) {
            loadTokensSetNames();
         }
      };
      inominaxService.cloneTokenSet(selectedTokenSetName, tokenSetCloneName, cloneTokenSetCallback);
   }

}
