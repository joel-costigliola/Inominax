package zorglux.inominax.client;

import java.util.List;

import zorglux.inominax.client.callback.BasicCallback;
import zorglux.inominax.shared.TokenSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
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

public class TokenSetManagementDialogBox extends DialogBox {

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
   private final HorizontalPanel newTokenSetPanel = new HorizontalPanel();
   private final HorizontalPanel removeTokenSetPanel = new HorizontalPanel();
   private final HorizontalPanel renameListPanel = new HorizontalPanel();
   private final HorizontalPanel cloneListPanel = new HorizontalPanel();
   private final VerticalPanel exitPanel = new VerticalPanel();

   // widgets
   private final Label newTokenSetLabel = new Label("Create list of tokens");
   private final TextBox newTokenSetTextBox = new TextBox();
   private final Button createNewTokenSetButton = new Button("New button");
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
   private final Label emptyLabel = new Label("");
   private final Label label = new Label("");

   public TokenSetManagementDialogBox(final InominaxServiceAsync inominaxServiceParam) {
      inominaxService = inominaxServiceParam;

      setWidth("426px");
      setHTML("Manage list of tokens");
      mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
      setWidget(mainPanel);

      // ===========================================================================================================================
      // new panel
      // ===========================================================================================================================

      newTokenSetPanel.setSpacing(PANEL_SPACING);

      newTokenSetPanel.add(newTokenSetLabel);
      newTokenSetPanel.setCellVerticalAlignment(newTokenSetLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      newTokenSetLabel.setWidth(LABEL_DEFAULT_SIZE);

      newTokenSetPanel.add(newTokenSetTextBox);
      newTokenSetTextBox.setWidth(TEXT_BOX_DEFAULT_SIZE);
      createNewTokenSetButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            createNewTokensSet(newTokenSetTextBox.getValue());
         }
      });

      newTokenSetPanel.add(emptyLabel);
      emptyLabel.setWidth("25px");
      createNewTokenSetButton.setText("create");

      newTokenSetPanel.add(createNewTokenSetButton);
      createNewTokenSetButton.setWidth(BUTTON_DEFAULT_SIZE);

      // ===========================================================================================================================
      // remove panel
      // ===========================================================================================================================

      removeTokenSetPanel.setSpacing(PANEL_SPACING);

      removeTokenSetPanel.add(removeTokenSetLabel);
      removeTokenSetPanel.setCellVerticalAlignment(removeTokenSetLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      removeTokenSetLabel.setWidth(LABEL_DEFAULT_SIZE);

      removeTokenSetPanel.add(removeTokenSetComboBox);
      removeTokenSetComboBox.setWidth(COMBO_BOX_DEFAULT_SIZE);
      removeTokenSetButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            final String selectedTokenSetName = removeTokenSetComboBox.getValue(removeTokenSetComboBox.getSelectedIndex());
            if (Window.confirm("Are you sure you want to remove " + selectedTokenSetName)) {
               removeTokenSet(selectedTokenSetName);
            }
         }
      });

      removeTokenSetPanel.add(label);
      label.setWidth("25px");
      removeTokenSetButton.setText("remove");

      removeTokenSetPanel.add(removeTokenSetButton);
      removeTokenSetPanel.setCellVerticalAlignment(removeTokenSetButton, HasVerticalAlignment.ALIGN_MIDDLE);
      removeTokenSetButton.setWidth(BUTTON_DEFAULT_SIZE);

      // ===========================================================================================================================
      // rename panel
      // ===========================================================================================================================

      renameListPanel.setSpacing(PANEL_SPACING);
      renameListPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

      renameListPanel.add(renameListLabel);
      renameListLabel.setWidth(LABEL_DEFAULT_SIZE);

      renameListPanel.add(renameTokenSetComboBox);
      renameTokenSetComboBox.setWidth(COMBO_BOX_DEFAULT_SIZE);

      renameListPanel.add(renameToLabel);
      renameListPanel.setCellHorizontalAlignment(renameToLabel, HasHorizontalAlignment.ALIGN_CENTER);
      renameListPanel.setCellVerticalAlignment(renameToLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      renameToLabel.setWidth("25px");
      renameListPanel.add(renameTokenSetTextBox);
      renameListPanel.setCellVerticalAlignment(renameTokenSetTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      renameTokenSetTextBox.setWidth(TEXT_BOX_DEFAULT_SIZE);
      renameTokenSetButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            String selectedTokenSetName = renameTokenSetComboBox.getValue(renameTokenSetComboBox.getSelectedIndex());
            renameTokenSet(selectedTokenSetName);
         }
      });
      renameTokenSetButton.setText("rename");

      renameListPanel.add(renameTokenSetButton);
      renameListPanel.setCellVerticalAlignment(renameTokenSetButton, HasVerticalAlignment.ALIGN_MIDDLE);
      renameTokenSetButton.setWidth(BUTTON_DEFAULT_SIZE);

      // ===========================================================================================================================
      // clone panel
      // ===========================================================================================================================

      cloneListPanel.setSpacing(PANEL_SPACING);

      cloneListPanel.add(cloneTokenSetLabel);
      cloneListPanel.setCellVerticalAlignment(cloneTokenSetLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      cloneTokenSetLabel.setWidth(LABEL_DEFAULT_SIZE);

      cloneListPanel.add(cloneTokenSetComboBox);
      cloneTokenSetComboBox.setWidth(COMBO_BOX_DEFAULT_SIZE);

      cloneListPanel.add(cloneTokenSetAsLabel);
      cloneListPanel.setCellHorizontalAlignment(cloneTokenSetAsLabel, HasHorizontalAlignment.ALIGN_CENTER);
      cloneListPanel.setCellVerticalAlignment(cloneTokenSetAsLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      cloneTokenSetAsLabel.setWidth("25px");

      cloneListPanel.add(cloneTokenSetTextBox);
      cloneListPanel.setCellVerticalAlignment(cloneTokenSetTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      cloneTokenSetTextBox.setWidth(TEXT_BOX_DEFAULT_SIZE);
      cloneTokenSetButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            String selectedTokenSetName = cloneTokenSetComboBox.getValue(cloneTokenSetComboBox.getSelectedIndex());
            String tokenSetCloneName = cloneTokenSetTextBox.getText();
            cloneTokenSet(selectedTokenSetName, tokenSetCloneName);
         }
      });
      cloneTokenSetButton.setText("clone");

      cloneListPanel.add(cloneTokenSetButton);
      cloneListPanel.setCellVerticalAlignment(cloneTokenSetButton, HasVerticalAlignment.ALIGN_MIDDLE);
      cloneTokenSetButton.setWidth(BUTTON_DEFAULT_SIZE);

      // ===========================================================================================================================
      // exit panel
      // ===========================================================================================================================

      exitButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            Widget source = (Widget) event.getSource();
            while (!source.getParent().getClass().equals(TokenSetManagementDialogBox.class)) {
               source = source.getParent();
            }
            TokenSetManagementDialogBox tokenSetMaangementDialogBox = (TokenSetManagementDialogBox) source.getParent();
            tokenSetMaangementDialogBox.hide();
         }
      });
      exitButton.setText("Exit");
      exitButton.setWidth(BUTTON_DEFAULT_SIZE);

      exitPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
      exitPanel.add(exitButton);

      // ===========================================================================================================================
      // main panel
      // ===========================================================================================================================

      mainPanel.setSpacing(4);
      mainPanel.setSize("368px", "25px");
      mainPanel.add(newTokenSetPanel);
      mainPanel.setCellVerticalAlignment(newTokenSetPanel, HasVerticalAlignment.ALIGN_MIDDLE);
      mainPanel.add(horizontalRule1);
      mainPanel.add(removeTokenSetPanel);
      mainPanel.setCellVerticalAlignment(removeTokenSetPanel, HasVerticalAlignment.ALIGN_MIDDLE);
      mainPanel.add(horizontalRule3);
      mainPanel.add(renameListPanel);
      mainPanel.setCellVerticalAlignment(renameListPanel, HasVerticalAlignment.ALIGN_MIDDLE);
      mainPanel.add(horizontalRule4);
      mainPanel.add(cloneListPanel);
      mainPanel.setCellVerticalAlignment(cloneListPanel, HasVerticalAlignment.ALIGN_MIDDLE);
      mainPanel.add(horizontalRule2);
      mainPanel.add(exitPanel);
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
      newTokenSetTextBox.setFocus(true);
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
