package zorglux.inominax.client;

import java.util.List;

import zorglux.inominax.client.callback.BasicCallback;
import zorglux.inominax.shared.NameSet;

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

public class NameSetManagementDialogBox extends DialogBox {

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
   private final HorizontalPanel newNameSetPanel = new HorizontalPanel();
   private final HorizontalPanel removeNameSetPanel = new HorizontalPanel();
   private final HorizontalPanel renameListPanel = new HorizontalPanel();
   private final HorizontalPanel cloneListPanel = new HorizontalPanel();
   private final VerticalPanel exitPanel = new VerticalPanel();

   // widgets
   private final Label newNameSetLabel = new Label("Create list of names");
   private final TextBox newNameSetTextBox = new TextBox();
   private final Button createNewNameSetButton = new Button("New button");
   private final Button exitButton = new Button("New button");
   private final Label renameListLabel = new Label("Rename list of names ");
   private final Label cloneNameSetLabel = new Label("Clone list of names");
   private final Label removeNameSetLabel = new Label("Remove list of names ");
   private final ListBox renameNameSetComboBox = new ListBox();
   private final Label renameToLabel = new Label("to");
   private final TextBox renameNameSetTextBox = new TextBox();
   private final ListBox cloneNameSetComboBox = new ListBox();
   private final Label cloneNameSetAsLabel = new Label("as");
   private final TextBox cloneNameSetTextBox = new TextBox();
   private final ListBox removeNameSetComboBox = new ListBox();
   private final Button renameNameSetButton = new Button("New button");
   private final Button cloneNameSetButton = new Button("New button");
   private final Button removeNameSetButton = new Button("New button");
   private final InlineHTML horizontalRule1 = new InlineHTML("<hr>");
   private final InlineHTML horizontalRule2 = new InlineHTML("<hr>");
   private final InlineHTML horizontalRule3 = new InlineHTML("<hr>");
   private final InlineHTML horizontalRule4 = new InlineHTML("<hr>");
   private final Label emptyLabel = new Label("");
   private final Label label = new Label("");

   public NameSetManagementDialogBox(final InominaxServiceAsync inominaxServiceParam) {
      inominaxService = inominaxServiceParam;

      setWidth("426px");
      setHTML("Manage list of names");
      mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
      setWidget(mainPanel);

      // ===========================================================================================================================
      // new panel
      // ===========================================================================================================================

      newNameSetPanel.setSpacing(PANEL_SPACING);

      newNameSetPanel.add(newNameSetLabel);
      newNameSetPanel.setCellVerticalAlignment(newNameSetLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      newNameSetLabel.setWidth(LABEL_DEFAULT_SIZE);

      newNameSetPanel.add(newNameSetTextBox);
      newNameSetTextBox.setWidth(TEXT_BOX_DEFAULT_SIZE);
      createNewNameSetButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            createNewNamesSet(newNameSetTextBox.getValue());
         }
      });

      newNameSetPanel.add(emptyLabel);
      emptyLabel.setWidth("25px");
      createNewNameSetButton.setText("create");

      newNameSetPanel.add(createNewNameSetButton);
      createNewNameSetButton.setWidth(BUTTON_DEFAULT_SIZE);

      // ===========================================================================================================================
      // remove panel
      // ===========================================================================================================================

      removeNameSetPanel.setSpacing(PANEL_SPACING);

      removeNameSetPanel.add(removeNameSetLabel);
      removeNameSetPanel.setCellVerticalAlignment(removeNameSetLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      removeNameSetLabel.setWidth(LABEL_DEFAULT_SIZE);

      removeNameSetPanel.add(removeNameSetComboBox);
      removeNameSetComboBox.setWidth(COMBO_BOX_DEFAULT_SIZE);
      removeNameSetButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            final String selectedNameSetName = removeNameSetComboBox.getValue(removeNameSetComboBox.getSelectedIndex());
            if (Window.confirm("Are you sure you want to remove " + selectedNameSetName)) {
               removeNameSet(selectedNameSetName);
            }
         }
      });

      removeNameSetPanel.add(label);
      label.setWidth("25px");
      removeNameSetButton.setText("remove");

      removeNameSetPanel.add(removeNameSetButton);
      removeNameSetPanel.setCellVerticalAlignment(removeNameSetButton, HasVerticalAlignment.ALIGN_MIDDLE);
      removeNameSetButton.setWidth(BUTTON_DEFAULT_SIZE);

      // ===========================================================================================================================
      // rename panel
      // ===========================================================================================================================

      renameListPanel.setSpacing(PANEL_SPACING);
      renameListPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

      renameListPanel.add(renameListLabel);
      renameListLabel.setWidth(LABEL_DEFAULT_SIZE);

      renameListPanel.add(renameNameSetComboBox);
      renameNameSetComboBox.setWidth(COMBO_BOX_DEFAULT_SIZE);

      renameListPanel.add(renameToLabel);
      renameListPanel.setCellHorizontalAlignment(renameToLabel, HasHorizontalAlignment.ALIGN_CENTER);
      renameListPanel.setCellVerticalAlignment(renameToLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      renameToLabel.setWidth("25px");
      renameListPanel.add(renameNameSetTextBox);
      renameListPanel.setCellVerticalAlignment(renameNameSetTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      renameNameSetTextBox.setWidth(TEXT_BOX_DEFAULT_SIZE);
      renameNameSetButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            String selectedNameSetName = renameNameSetComboBox.getValue(renameNameSetComboBox.getSelectedIndex());
            renameNameSet(selectedNameSetName);
         }
      });
      renameNameSetButton.setText("rename");

      renameListPanel.add(renameNameSetButton);
      renameListPanel.setCellVerticalAlignment(renameNameSetButton, HasVerticalAlignment.ALIGN_MIDDLE);
      renameNameSetButton.setWidth(BUTTON_DEFAULT_SIZE);

      // ===========================================================================================================================
      // clone panel
      // ===========================================================================================================================

      cloneListPanel.setSpacing(PANEL_SPACING);

      cloneListPanel.add(cloneNameSetLabel);
      cloneListPanel.setCellVerticalAlignment(cloneNameSetLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      cloneNameSetLabel.setWidth(LABEL_DEFAULT_SIZE);

      cloneListPanel.add(cloneNameSetComboBox);
      cloneNameSetComboBox.setWidth(COMBO_BOX_DEFAULT_SIZE);

      cloneListPanel.add(cloneNameSetAsLabel);
      cloneListPanel.setCellHorizontalAlignment(cloneNameSetAsLabel, HasHorizontalAlignment.ALIGN_CENTER);
      cloneListPanel.setCellVerticalAlignment(cloneNameSetAsLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      cloneNameSetAsLabel.setWidth("25px");

      cloneListPanel.add(cloneNameSetTextBox);
      cloneListPanel.setCellVerticalAlignment(cloneNameSetTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      cloneNameSetTextBox.setWidth(TEXT_BOX_DEFAULT_SIZE);
      cloneNameSetButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            String selectedNameSetName = cloneNameSetComboBox.getValue(cloneNameSetComboBox.getSelectedIndex());
            String nameSetCloneName = cloneNameSetTextBox.getText();
            cloneNameSet(selectedNameSetName, nameSetCloneName);
         }
      });
      cloneNameSetButton.setText("clone");

      cloneListPanel.add(cloneNameSetButton);
      cloneListPanel.setCellVerticalAlignment(cloneNameSetButton, HasVerticalAlignment.ALIGN_MIDDLE);
      cloneNameSetButton.setWidth(BUTTON_DEFAULT_SIZE);

      // ===========================================================================================================================
      // exit panel
      // ===========================================================================================================================

      exitButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            Widget source = (Widget) event.getSource();
            while (!source.getParent().getClass().equals(NameSetManagementDialogBox.class)) {
               source = source.getParent();
            }
            NameSetManagementDialogBox newNameSetDialogBox = (NameSetManagementDialogBox) source.getParent();
            newNameSetDialogBox.hide();
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
      mainPanel.add(newNameSetPanel);
      mainPanel.setCellVerticalAlignment(newNameSetPanel, HasVerticalAlignment.ALIGN_MIDDLE);
      mainPanel.add(horizontalRule1);
      mainPanel.add(removeNameSetPanel);
      mainPanel.setCellVerticalAlignment(removeNameSetPanel, HasVerticalAlignment.ALIGN_MIDDLE);
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
      loadNamesSetNames();
      // disable main widows to make it clear dialog box is modal
      setGlassEnabled(true);
   }

   private void createNewNamesSet(String nameSetName) {
      AsyncCallback<NameSet> createNameSetCallback = new BasicCallback<NameSet>() {
         @Override
         public void onResult(NameSet newNameSet) {
            loadNamesSetNames();
         }
      };
      inominaxService.createNameSet(nameSetName, createNameSetCallback);
   }

   public void focusNewNameSetTextBox() {
      newNameSetTextBox.setFocus(true);
   }

   private void loadNamesSetNames() {
      AsyncCallback<List<String>> getNameSetsNamesCallback = new BasicCallback<List<String>>() {
         @Override
         public void onResult(List<String> nameSetsNames) {
            renameNameSetComboBox.clear();
            cloneNameSetComboBox.clear();
            removeNameSetComboBox.clear();
            for (String nameSetsName : nameSetsNames) {
               renameNameSetComboBox.addItem(nameSetsName);
               cloneNameSetComboBox.addItem(nameSetsName);
               removeNameSetComboBox.addItem(nameSetsName);
            }
         }
      };
      // call inominaxService
      inominaxService.getNameSetsNames(getNameSetsNamesCallback);
   }

   private void removeNameSet(String selectedNameSetName) {
      AsyncCallback<Void> removeNameSetCallback = new BasicCallback<Void>() {
         @Override
         public void onResult(Void nothing) {
            loadNamesSetNames();
         }
      };
      inominaxService.removeNameSet(selectedNameSetName, removeNameSetCallback);
   }

   private void renameNameSet(String selectedNameSetName) {
      AsyncCallback<Void> renameNameSetCallback = new BasicCallback<Void>() {
         @Override
         public void onResult(Void nothing) {
            loadNamesSetNames();
         }
      };
      inominaxService.renameNameSet(selectedNameSetName, renameNameSetTextBox.getText(), renameNameSetCallback);
   }

   private void cloneNameSet(String selectedNameSetName, String nameSetCloneName) {
      AsyncCallback<Void> cloneNameSetCallback = new BasicCallback<Void>() {
         @Override
         public void onResult(Void nothing) {
            loadNamesSetNames();
         }
      };
      inominaxService.cloneNameSet(selectedNameSetName, nameSetCloneName, cloneNameSetCallback);
   }

}
