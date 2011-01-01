package zorglux.inominax.client;

import java.util.List;

import zorglux.inominax.client.callback.BasicCallback;
import zorglux.inominax.client.callback.DoNothingCallback;
import zorglux.inominax.exception.FunctionnalException;
import zorglux.inominax.shared.TokenSet;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class NewTokenSetDialogBox extends DialogBox {

   private static final String LABEL_DEFAULT_SIZE = "170px";
   private final HorizontalPanel newTokenSetPanel = new HorizontalPanel();
   private final Label newTokenSetLabel = new Label("Enter new list name ");
   private final TextBox newTokenSetTextBox = new TextBox();
   private final Button createNewTokenSetButton = new Button("New button");
   private final VerticalPanel mainPanel = new VerticalPanel();
   private final VerticalPanel verticalPanel = new VerticalPanel();
   private final Button exitButton = new Button("New button");
   private InominaxServiceAsync inominaxService;
   private final HorizontalPanel renameListPanel = new HorizontalPanel();
   private final HorizontalPanel cloneListPanel = new HorizontalPanel();
   private final Label renameListLabel = new Label("rename list of tokens ");
   private final Label cloneListLabel = new Label("clone list and name as");
   private final HorizontalPanel removeListPanel = new HorizontalPanel();
   private final Label removeListLabel = new Label("remove list of tokens ");
   private final InlineHTML inlineHTML = new InlineHTML("<hr>");
   private final InlineHTML inlineHTML_1 = new InlineHTML("<hr>");
   private final ListBox listBox = new ListBox();
   private final CellList<String> tokenSetCellList = new CellList<String>(new TextInputCell());
   private String selectedTokenSetForRename;
   private SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();

   public NewTokenSetDialogBox(final InominaxServiceAsync inominaxService) {
      listBox.setVisibleItemCount(5);
      this.inominaxService = inominaxService;
      // set
      setWidth("426px");
      setHTML("New list of tokens");
      mainPanel.setSpacing(4);

      mainPanel.setSize("368px", "25px");

      // assemble main panel
      setWidget(mainPanel);

      newTokenSetPanel.add(newTokenSetLabel);
      newTokenSetPanel.setCellVerticalAlignment(newTokenSetLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      newTokenSetLabel.setWidth(LABEL_DEFAULT_SIZE);

      newTokenSetPanel.add(newTokenSetTextBox);
      newTokenSetTextBox.setWidth("150px");
      createNewTokenSetButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            createNewTokensSet(newTokenSetTextBox.getValue());
         }
      });
      createNewTokenSetButton.setText("create");

      newTokenSetPanel.add(createNewTokenSetButton);
      createNewTokenSetButton.setWidth("100px");
      mainPanel.add(newTokenSetPanel);

      mainPanel.add(inlineHTML);

      selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
         @Override
         public void onSelectionChange(SelectionChangeEvent event) {
            String selected = selectionModel.getSelectedObject();
            if (selected != null) {
               selectedTokenSetForRename = selected;
            }
         }
      });

      tokenSetCellList.setSelectionModel(selectionModel);
      mainPanel.add(tokenSetCellList);

      // Create a value updater that will be called when the value in a cell changes.
      ValueUpdater<String> valueUpdater = new ValueUpdater<String>() {
         @Override
         public void update(String newName) {
            Window.alert("renaming " + selectedTokenSetForRename + " to " + newName);
            inominaxService.renameTokenSet(selectedTokenSetForRename, newName, new DoNothingCallback<Void>());
         }
      };

      // Add the value updater to the cellList.
      tokenSetCellList.setValueUpdater(valueUpdater);

      mainPanel.add(renameListPanel);

      renameListPanel.add(renameListLabel);
      renameListLabel.setWidth(LABEL_DEFAULT_SIZE);
      mainPanel.add(cloneListPanel);

      cloneListPanel.add(cloneListLabel);
      cloneListLabel.setWidth(LABEL_DEFAULT_SIZE);
      mainPanel.add(removeListPanel);

      removeListPanel.add(removeListLabel);
      removeListLabel.setWidth(LABEL_DEFAULT_SIZE);
      mainPanel.add(inlineHTML_1);
      verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
      mainPanel.add(verticalPanel);
      mainPanel.setCellHorizontalAlignment(verticalPanel, HasHorizontalAlignment.ALIGN_RIGHT);
      exitButton.addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            Widget source = (Widget) event.getSource();
            while (!source.getParent().getClass().equals(NewTokenSetDialogBox.class)) {
               source = source.getParent();
            }
            NewTokenSetDialogBox newTokenSetDialogBox = (NewTokenSetDialogBox) source.getParent();
            newTokenSetDialogBox.hide();
         }
      });
      exitButton.setText("Exit");

      verticalPanel.add(exitButton);
      exitButton.setWidth("50px");

      // fill cell list
      loadTokensSetCellList();

      newTokenSetTextBox.setFocus(true);
      newTokenSetTextBox.setTabIndex(0);
      setGlassEnabled(true);

   }

   private void createNewTokensSet(String tokenSetName) {
      AsyncCallback<Void> createTokenSetCallback = new AsyncCallback<Void>() {
         @Override
         public void onFailure(Throwable caught) {
            if (caught instanceof FunctionnalException) {
               Window.alert(caught.getMessage());
            }
         }
         @Override
         public void onSuccess(Void aVoid) {
            // nothing to do
         }
      };
      // call inominaxService
      inominaxService.createTokenSet(tokenSetName, createTokenSetCallback);
   }

   public void focusNewTokenSetTextBox() {
      newTokenSetTextBox.setFocus(true);
   }

   public void loadTokensSetCellList() {
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
            tokenSetCellList.setRowCount(tokenSetsNames.size());
            tokenSetCellList.setRowData(tokenSetsNames);
            selectedTokenSetForRename = tokenSetCellList.getVisibleItems().get(0);
         }
      };
      // call inominaxService
      inominaxService.getTokenSetsNames(getTokenSetsNamesCallback);
   }

}
