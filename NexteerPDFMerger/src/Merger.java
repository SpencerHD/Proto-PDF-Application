
// Program Name: Nexteer PDF Merger
// Program Description: This program was created for Nexteer Prototype Center
// to merge any PDF documents into one single PDF document. In addition, it
// allows added functionality for creating combined data packages.
// Program Creator: Spencer Huebler-Davis
// Program Creation Date: 12/13/2019
// Java Development Kit Version: 1.8.0_211

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.prefs.BackingStoreException;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Merger extends Application {

	// Main method for launching
	public static void main(String[] args) {
		launch(args);
	}

	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
	static List<String> lines = new ArrayList<String>();
	static List<File> listOfFiles = new ArrayList<File>();
	String cf = "";
	String m = "";

	@SuppressWarnings("unchecked")
	public void start(final Stage primaryStage) throws BackingStoreException {

		String font = "Courier";

		ArrayList<File> files = new ArrayList<File>();
		Stage askOpen = new Stage();
		FileChooser fileChooser = new FileChooser();

		askOpen.setTitle("Select Files");

		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

		InputStream inputStream = getClass().getResourceAsStream("/Transparent Logo.png");

		Image logo = new Image(inputStream);
		ImageView imageView = new ImageView(logo);
		Line line = new Line();
		Line separate = new Line();
		Line separate2 = new Line();
		Line separate3 = new Line();

		Region region = new Region();
		HBox header = new HBox(0);
		HBox left = new HBox(10);
		HBox right = new HBox(0);
		HBox topHBox = new HBox(0);
		HBox.setHgrow(region, Priority.ALWAYS);
		HBox botHBox = new HBox(0);
		VBox mainVBox = new VBox(20);

		final Scene scene = new Scene(mainVBox, 1400, 600);

		BackgroundFill background_fill = new BackgroundFill(Color.WHITE, null, null);
		BackgroundFill no_fill = new BackgroundFill(Color.TRANSPARENT, null, null);
		Background background = new Background(background_fill);
		Background transparent = new Background(no_fill);
		Background buttonAdd = new Background(new BackgroundFill(Color.MEDIUMSEAGREEN, null, null));
		Background buttonMerge = new Background(new BackgroundFill(Color.ROYALBLUE, null, null));
		Background buttonDelete = new Background(new BackgroundFill(Color.LIGHTCORAL, null, null));
		Background buttonClear = new Background(new BackgroundFill(Color.LIGHTSLATEGRAY, null, null));
		Background buttonSplit = new Background(new BackgroundFill(Color.SANDYBROWN, null, null));

		@SuppressWarnings("rawtypes")
		TableView table = new TableView();
		JFXListView<String> group = new JFXListView<String>();
		JFXTextField usageText = new JFXTextField();
		JFXTextField groupName = new JFXTextField();
		JFXTextField groupEmail = new JFXTextField();
		TextFlow information = new TextFlow();
		Text partInfo = new Text();
		Text promptText = new Text("Would you like to send a shipping request for ");
		Text punctuation = new Text("?");
		Label popupHeader = new Label("Popup header goes here.");
		Label popupText = new Label("Popup text goes here.");
		Label placeholder = new Label("Click 'Add New Files' to add PDF files to the list.");
		Label currentProg = new Label("Program: ");
		Label program = new Label("");
		Label currentSerial = new Label(" | Serial Number: ");
		Label serialNum = new Label("XXXX-XXX");
		Label currentPart = new Label(" | Part Number: ");
		Label currentRev = new Label(" | Rev: ");
		Label revNum = new Label("XXXXX");
		Label partNum = new Label("XXXXXXXX");
		Label dest = new Label("Destination File: ");
		Label destName = new Label();
		Label prompt1 = new Label("");
		Label selectGroup = new Label("Select Shipping Group");
		Label intendedUsage = new Label("Intended Usage");
		Label confirmText = new Label();
		Label enterGroup = new Label("Enter Group");
		Label groupText = new Label("Group Name");
		Label emailText = new Label("Group Email");
		JFXButton popupButton = new JFXButton("Okay");
		JFXButton add = new JFXButton("Add New Files");
		JFXButton merge = new JFXButton("Merge Files");
		JFXButton split = new JFXButton("Split File");
		JFXButton delete = new JFXButton("Delete File");
		JFXButton clear = new JFXButton("Clear List");
		JFXButton yes = new JFXButton("Yes");
		JFXButton yesDelete = new JFXButton("Yes");
		JFXButton no = new JFXButton("No");
		JFXButton noDelete = new JFXButton("No");
		JFXButton addProgram = new JFXButton("Add New Group");
		JFXButton removeProgram = new JFXButton("Remove Group");
		JFXButton send = new JFXButton("Prepare Email");
		JFXButton addGroup = new JFXButton("Add Group");
		JFXCheckBox template = new JFXCheckBox("Template Only (Opens Browser)");

		line.setStartX(0);
		line.setEndX(260);
		popupHeader.setFont(Font.font(font, 16));
		popupText.setFont(Font.font(font, 14));
		popupButton.setFont(Font.font(font, 14));
		popupButton.setOnMouseEntered(e -> popupButton.setOpacity(0.7));
		popupButton.setOnMouseExited(e -> popupButton.setOpacity(1));

		separate.setStartY(0);
		separate.setEndY(30);
		separate2.setStartY(0);
		separate2.setEndY(30);
		separate3.setStartY(0);
		separate3.setEndY(30);

		placeholder.setFont(Font.font(font, 16));
		table.setPlaceholder(placeholder);
		table.prefHeightProperty().bind(primaryStage.heightProperty());
		table.setBackground(transparent);
		currentProg.setFont(Font.font(font, 16));
		currentProg.setVisible(false);
		currentSerial.setFont(Font.font(font, 16));
		currentSerial.setVisible(false);
		currentPart.setFont(Font.font(font, 16));
		currentPart.setVisible(false);
		currentRev.setFont(Font.font(font, 16));
		currentRev.setVisible(false);
		program.setFont(Font.font(font, FontWeight.BOLD, 16));
		program.setVisible(false);
		serialNum.setFont(Font.font(font, FontWeight.BOLD, 16));
		serialNum.setVisible(false);
		partNum.setFont(Font.font(font, FontWeight.BOLD, 16));
		partNum.setVisible(false);
		revNum.setFont(Font.font(font, FontWeight.BOLD, 16));
		revNum.setVisible(false);
		dest.setFont(Font.font(font, 16));
		dest.setVisible(false);
		destName.setFont(Font.font(font, 16));
		destName.setUnderline(true);
		destName.setTextFill(Color.DEEPSKYBLUE);
		destName.setTooltip(new Tooltip("Click to open file destination folder."));
		partInfo.setFont(Font.font(font, FontWeight.BOLD, 16));
		promptText.setFont(Font.font(font, 16));
		punctuation.setFont(Font.font(font, 16));
		confirmText.setFont(Font.font(font, 16));
		confirmText.setWrapText(true);
		confirmText.setTextAlignment(TextAlignment.CENTER);
		information.getChildren().addAll(promptText, partInfo, punctuation);
		information.setTextAlignment(TextAlignment.CENTER);
		selectGroup.setFont(Font.font(font, 16));
		selectGroup.setTextAlignment(TextAlignment.CENTER);
		selectGroup.setUnderline(true);
		enterGroup.setFont(Font.font(font, 16));
		enterGroup.setTextAlignment(TextAlignment.CENTER);
		enterGroup.setUnderline(true);
		add.setFont(Font.font(font, 14));
		add.setButtonType(ButtonType.FLAT);
		add.setBackground(buttonAdd);
		add.setTextFill(Color.WHITE);
		add.setOnMouseEntered(e -> add.setButtonType(ButtonType.RAISED));
		add.setOnMouseExited(e -> add.setButtonType(ButtonType.FLAT));
		add.setTooltip(new Tooltip("Add new files to the list from file explorer."));
		merge.setFont(Font.font(font, 14));
		merge.setButtonType(ButtonType.FLAT);
		merge.setBackground(buttonMerge);
		merge.setTextFill(Color.WHITE);
		merge.setOnMouseEntered(e -> merge.setButtonType(ButtonType.RAISED));
		merge.setOnMouseExited(e -> merge.setButtonType(ButtonType.FLAT));
		merge.setVisible(false);
		merge.setTooltip(new Tooltip("Merge all files in the order they appear on the list."));
		split.setFont(Font.font(font, 14));
		split.setButtonType(ButtonType.FLAT);
		split.setBackground(buttonSplit);
		split.setTextFill(Color.WHITE);
		split.setOnMouseEntered(e -> split.setButtonType(ButtonType.RAISED));
		split.setOnMouseExited(e -> split.setButtonType(ButtonType.FLAT));
		split.setVisible(false);
		split.setTooltip(new Tooltip("Split selected file into individual pages."));
		delete.setFont(Font.font(font, 14));
		delete.setBackground(buttonDelete);
		delete.setTextFill(Color.WHITE);
		delete.setOnMouseEntered(e -> delete.setButtonType(ButtonType.RAISED));
		delete.setOnMouseExited(e -> delete.setButtonType(ButtonType.FLAT));
		delete.setButtonType(ButtonType.FLAT);
		delete.setVisible(false);
		delete.setTooltip(new Tooltip("Delete selected file from the list."));
		clear.setFont(Font.font(font, 14));
		clear.setBackground(buttonClear);
		clear.setTextFill(Color.WHITE);
		clear.setOnMouseEntered(e -> clear.setButtonType(ButtonType.RAISED));
		clear.setOnMouseExited(e -> clear.setButtonType(ButtonType.FLAT));
		clear.setButtonType(ButtonType.FLAT);
		clear.setVisible(false);
		clear.setTooltip(new Tooltip("Clear all files from the current list."));
		yes.setFont(Font.font(font, 16));
		yes.setBackground(transparent);
		yes.setOnMouseEntered(e -> yes.setOpacity(0.7));
		yes.setOnMouseExited(e -> yes.setOpacity(1));
		no.setFont(Font.font(font, 16));
		no.setBackground(transparent);
		no.setOnMouseEntered(e -> no.setOpacity(0.7));
		no.setOnMouseExited(e -> no.setOpacity(1));
		yesDelete.setFont(Font.font(font, 16));
		yesDelete.setOnMouseEntered(e -> yesDelete.setOpacity(0.7));
		yesDelete.setOnMouseExited(e -> yesDelete.setOpacity(1));
		noDelete.setFont(Font.font(font, 16));
		noDelete.setOnMouseEntered(e -> noDelete.setOpacity(0.7));
		noDelete.setOnMouseExited(e -> noDelete.setOpacity(1));
		addProgram.setFont(Font.font(font, 16));
		addProgram.setOnMouseEntered(e -> addProgram.setOpacity(0.7));
		addProgram.setOnMouseExited(e -> addProgram.setOpacity(1));
		removeProgram.setFont(Font.font(font, 16));
		removeProgram.setOnMouseEntered(e -> removeProgram.setOpacity(0.7));
		removeProgram.setOnMouseExited(e -> removeProgram.setOpacity(1));
		usageText.setFont(Font.font(font, 14));
		usageText.setAlignment(Pos.CENTER);
		groupName.setFont(Font.font(font, 14));
		groupName.setAlignment(Pos.CENTER);
		groupEmail.setFont(Font.font(font, 14));
		groupEmail.setAlignment(Pos.CENTER);
		addGroup.setFont(Font.font(font, 14));
		addGroup.setFont(Font.font(font, 16));
		addGroup.setOnMouseEntered(e -> addGroup.setOpacity(0.7));
		addGroup.setOnMouseExited(e -> addGroup.setOpacity(1));
		send.setFont(Font.font(font, 16));
		send.setOnMouseEntered(e -> send.setOpacity(0.7));
		send.setOnMouseExited(e -> send.setOpacity(1));
		template.setFont(Font.font(font, FontPosture.ITALIC, 14));

		MemoryUsageSetting setupMainMemoryOnly = MemoryUsageSetting.setupMainMemoryOnly();

		TableColumn<String, itemsList> column1 = new TableColumn<>("File Name");
		column1.setCellValueFactory(new PropertyValueFactory<>("name"));
		column1.prefWidthProperty().bind(primaryStage.widthProperty().divide(1.35));
		column1.setStyle("-fx-font: 14px \"Courier\";");
		column1.setSortable(false);
		TableColumn<String, itemsList> column2 = new TableColumn<>("Size");
		column2.setCellValueFactory(new PropertyValueFactory<>("size"));
		column2.prefWidthProperty().bind(primaryStage.widthProperty().divide(14));
		column2.setStyle("-fx-font: 14px \"Courier\";-fx-alignment: CENTER;");
		column2.setSortable(false);
		TableColumn<Integer, itemsList> column3 = new TableColumn<>("Pages");
		column3.setCellValueFactory(new PropertyValueFactory<>("pages"));
		column3.prefWidthProperty().bind(primaryStage.widthProperty().divide(14));
		column3.setStyle("-fx-font: 14px \"Courier\";-fx-alignment: CENTER;");
		column3.setSortable(false);
		TableColumn<String, itemsList> column4 = new TableColumn<>("Date Modified");
		column4.setCellValueFactory(new PropertyValueFactory<>("date"));
		column4.prefWidthProperty().bind(primaryStage.widthProperty().divide(14));
		column4.setStyle("-fx-font: 14px \"Courier\";-fx-alignment: CENTER;");
		column4.setSortable(false);

		List<String> progs = new ArrayList<String>();
		File fileOpen = new File("progs.txt");
		try (Scanner scanner = new Scanner(fileOpen)) {
			while (scanner.hasNextLine()) {
				progs.add(scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e2) {
		}

		Collections.sort(progs);
		for (int i = 0; i < progs.size(); i++) {
			String[] progsSplit = progs.get(i).split(":");
			group.getItems().add(progsSplit[0]);
		}

		// Popup for alerts and info
		final Stage popup = new Stage();
		popup.setTitle("Popup");
		popup.setResizable(false);
		popup.setAlwaysOnTop(true);
		VBox contents = new VBox(10);
		contents.setPadding(new Insets(5, 5, 5, 5));
		contents.setBackground(background);
		contents.setAlignment(Pos.CENTER);
		contents.getChildren().addAll(popupHeader, line, popupText, popupButton);
		Scene popupScene = new Scene(contents, 300, 150);
		popup.setScene(popupScene);
		popup.initOwner(primaryStage);
		popup.initModality(Modality.WINDOW_MODAL);

		// Stage setup for asking a user for shipping approval
		final Stage askUser = new Stage();
		askUser.setTitle("Option Select");
		askUser.setResizable(false);
		VBox askVBox = new VBox(20);
		askVBox.setPadding(new Insets(10, 10, 10, 10));
		askVBox.setBackground(background);
		HBox buttons = new HBox(5, yes, separate, no);
		buttons.setAlignment(Pos.CENTER);
		askVBox.setAlignment(Pos.CENTER);
		askVBox.getChildren().addAll(information, buttons);
		Scene askScene = new Scene(askVBox, 300, 150);
		askUser.setScene(askScene);
		askUser.initOwner(primaryStage);
		askUser.initModality(Modality.WINDOW_MODAL);

		// Stage setup for selecting a program to send to
		final Stage askGroup = new Stage();
		askGroup.setTitle("Select Group");
		askGroup.setResizable(false);
		VBox groupVBox = new VBox(5);
		groupVBox.setBackground(background);
		HBox addRemove = new HBox(5);
		addRemove.getChildren().addAll(addProgram, separate2, removeProgram);
		addRemove.setAlignment(Pos.CENTER);
		groupVBox.setPadding(new Insets(10, 10, 10, 10));
		groupVBox.setAlignment(Pos.CENTER);
		groupVBox.getChildren().addAll(selectGroup, group, addRemove, template, usageText, intendedUsage, send);
		Scene groupScene = new Scene(groupVBox, 300, 400);
		askGroup.setScene(groupScene);
		askGroup.initOwner(primaryStage);
		askGroup.initModality(Modality.WINDOW_MODAL);

		// Stage setup for creating a new group
		final Stage newGroup = new Stage();
		newGroup.setTitle("Enter Group");
		newGroup.setResizable(false);
		VBox newGroupVBox = new VBox(5);
		newGroupVBox.setBackground(background);
		Region space = new Region();
		VBox.setVgrow(space, Priority.ALWAYS);
		HBox create = new HBox(10);
		newGroupVBox.setPadding(new Insets(10, 10, 10, 10));
		newGroupVBox.setAlignment(Pos.CENTER);
		create.setAlignment(Pos.CENTER);
		create.getChildren().addAll(addGroup);
		newGroupVBox.getChildren().addAll(enterGroup, groupName, groupText, groupEmail, emailText, space, create);
		Scene newGroupScene = new Scene(newGroupVBox, 300, 200);
		newGroup.setScene(newGroupScene);
		newGroup.initOwner(primaryStage);
		newGroup.initOwner(askGroup);
		newGroup.initModality(Modality.WINDOW_MODAL);

		// Stage setup for group delete confirmation
		final Stage confirmDelete = new Stage();
		confirmDelete.setTitle("Option Select");
		confirmDelete.setResizable(false);
		VBox confirmVBox = new VBox(20);
		confirmVBox.setBackground(background);
		confirmVBox.setPadding(new Insets(10, 10, 10, 10));
		HBox buttons2 = new HBox(5, yesDelete, separate3, noDelete);
		buttons2.setAlignment(Pos.CENTER);
		confirmVBox.setAlignment(Pos.CENTER);
		confirmVBox.getChildren().addAll(confirmText, buttons2);
		Scene confirmScene = new Scene(confirmVBox, 300, 150);
		confirmDelete.setScene(confirmScene);
		confirmDelete.initOwner(primaryStage);
		confirmDelete.initOwner(askGroup);
		confirmDelete.initModality(Modality.WINDOW_MODAL);

		popupButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				popup.close();
			}
		});

		// Allow user to select multiple files to add to list and add them to
		// table and arraylist
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Create the list of lists for sorting
				ArrayList<ArrayList<File>> allLists = new ArrayList<ArrayList<File>>();

				// Create lists to add each type of file by name in customer
				// sort
				ArrayList<File> backdrive = new ArrayList<File>();
				ArrayList<File> turningFive = new ArrayList<File>();
				ArrayList<File> turningTen = new ArrayList<File>();
				ArrayList<File> dynamic = new ArrayList<File>();
				ArrayList<File> effort = new ArrayList<File>();

				// Create lists to add each type of file by name in report setup
				// sort
				ArrayList<File> wearIn = new ArrayList<File>();
				ArrayList<File> centerRack = new ArrayList<File>();
				ArrayList<File> digitalPos = new ArrayList<File>();
				ArrayList<File> digitalTorque = new ArrayList<File>();
				ArrayList<File> preFlex = new ArrayList<File>();
				ArrayList<File> epsInfo = new ArrayList<File>();
				ArrayList<File> noise = new ArrayList<File>();

				// Show prompt to select files and begin get info from them and
				// add to lists
				listOfFiles = fileChooser.showOpenMultipleDialog(askOpen);
				if (listOfFiles != null) {
					popupHeader.setText("Loading Files");
					popup.setTitle("PDF Progress");
					popupText.setText("Sorting and adding PDFs to list...");
					contents.getChildren().remove(popupButton);
					popup.show();
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							String[] split2 = null;
							String date = "";
							int count = 0;
							boolean noSkip = true;
							boolean cfSkip = true;
							cf = "";
							// Add customer PDFs on top
							for (File fileList : listOfFiles) {
								try {
									// Load in files as a PDF using PDFBox
									PDDocument document = PDDocument.load(new File(fileList.toString()));

									// Get document information
									PDDocumentInformation docInfo = document.getDocumentInformation();
									if (docInfo.getCreationDate() != null) {
										split2 = docInfo.getCreationDate().toInstant().toString().split("T");
										if (split2[0] != null) {
											date = split2[0];
										} else {
											date = "None";
										}
									} else {
										date = "----/--/--";
									}
									String[] nameSplit = fileList.getName().toString().split("#");
									String[] firstBlock = nameSplit[0].split(" - ");
									String[] firstBlockSpace = nameSplit[0].split(" ");
									long kilobytes = fileList.length() / 1024;
									// Set the part number from the file name
									try {
										if (count == 0) {
											partNum.setText(nameSplit[3]);
											serialNum.setText(nameSplit[4]);
											PDFTextStripper pdfStripper = new PDFTextStripper();
											pdfStripper.setStartPage(1);
											pdfStripper.setEndPage(1);
											partInfo.setText(nameSplit[4]);

											// load all lines into a string
											String pages = pdfStripper.getText(document);

											// split by detecting newline
											String[] lines = pages.split("\r\n|\r|\n");

											for (int i = 0; i < lines.length; i++) {
												if (lines[i].equals(nameSplit[4])) {
													revNum.setText(lines[i - 1]);
												}
											}
										}
										count = 1;
									} catch (ArrayIndexOutOfBoundsException e2) {
										partNum.setText("Unknown");
										serialNum.setText("Unknown");
										revNum.setText("Unknown");
									}
									if (noSkip) {
										for (int i = 0; i < firstBlock.length; i++) {
											if (cfSkip) {
												for (int j = 0; j < firstBlockSpace.length; j++) {
													if (firstBlockSpace[j].contains("CF")
															|| firstBlockSpace[j].contains("Cf")) {
														cf = firstBlockSpace[j];
														cfSkip = false;
													}
												}
											}
											for (int j = 0; j < progs.size(); j++) {
												String[] progsSplit = progs.get(j).split(":");
												if (firstBlock[i].contains(progsSplit[0])) {
													program.setText(progsSplit[0]);
													group.getSelectionModel().select(progsSplit[0]);
													noSkip = false;
												}
												if (firstBlock[i].contains("M")) {
													String[] splitM = firstBlock[i].split(" ");
													m = splitM[0];
													break;
												}
											}
										}
									}
									// Add files into the correct lists for
									// sorting
									if (!serialNum.getText().equals("Unknown")) {
										for (int j = 0; j < nameSplit.length; j++) {
											if (nameSplit[j].equals("Report Customer.pdf")) {
												if (nameSplit[0].contains("Backdrive")) {
													backdrive.add(fileList);
												} else if (nameSplit[0].contains("Turning")) {
													if (nameSplit[0].contains("5 RPM")
															|| nameSplit[0].contains("5RPM")) {
														turningFive.add(fileList);
													} else {
														turningTen.add(fileList);
													}
												} else if (nameSplit[0].contains("Dynamic")) {
													dynamic.add(fileList);
												} else {
													effort.add(fileList);
												}
											}
										}
									} else {
										table.getItems().add(new itemsList(fileList.getName(), kilobytes + " KB",
												document.getNumberOfPages(), date));
										files.add(fileList);
									}
									// Only show part info if it is a Nexteer
									// report
									if (serialNum.getText().equals("Unknown")) {
										currentProg.setVisible(false);
										program.setVisible(false);
										currentPart.setVisible(false);
										partNum.setVisible(false);
										currentSerial.setVisible(false);
										serialNum.setVisible(false);
										currentRev.setVisible(false);
										revNum.setVisible(false);
										dest.setVisible(true);
										destName.setText(fileList.getParent() + "\\");
									} else {
										currentProg.setVisible(true);
										program.setVisible(true);
										currentPart.setVisible(true);
										partNum.setVisible(true);
										currentSerial.setVisible(true);
										serialNum.setVisible(true);
										currentRev.setVisible(true);
										revNum.setVisible(true);
										dest.setVisible(true);
										destName.setText(fileList.getParent() + "\\" + serialNum.getText() + " "
												+ program.getText() + " FINAL FUNCTION.pdf");
										prompt1.setText("Would you like to send a shipping approval request for "
												+ serialNum.getText() + "?");
									}
									document.close();
								} catch (IOException e2) {
								}
							}
							// Add all items to the list of lists
							Collections.sort(backdrive);
							allLists.add(backdrive);
							Collections.sort(turningFive);
							allLists.add(turningFive);
							Collections.sort(turningTen);
							allLists.add(turningTen);
							Collections.sort(dynamic);
							allLists.add(dynamic);
							allLists.add(effort);
							// Add all items in required sorting order
							for (int i = 0; i < allLists.size(); i++) {
								for (int j = 0; j < allLists.get(i).size(); j++) {
									String[] nameSplit = allLists.get(i).get(j).toString().split(Pattern.quote("\\"));
									String fileName = "";
									String nextFileName = "";
									for (int m = 0; m < nameSplit.length; m++) {
										fileName = nameSplit[nameSplit.length - 1];
									}
									long kilobytes = allLists.get(i).get(j).length() / 1024;
									PDDocument document = null;
									try {
										document = PDDocument.load(new File(allLists.get(i).get(j).toString()));
									} catch (IOException e) {
									}
									try {
										String[] nextSplit = allLists.get(i).get(j + 1).toString()
												.split(Pattern.quote("\\"));
										for (int m = 0; m < nextSplit.length; m++) {
											nextFileName = nextSplit[nextSplit.length - 1];
										}
									} catch (IndexOutOfBoundsException e) {

									}
									String[] firstBlock = fileName.split("#");
									String[] secondBlock = nextFileName.split("#");
									if (!firstBlock[0].equals(secondBlock[0])) {
										files.add(allLists.get(i).get(j));
										table.getItems().add(new itemsList(fileName, kilobytes + " KB",
												document.getNumberOfPages(), date));
									}
									try {
										document.close();
									} catch (IOException e) {
									}
								}
								allLists.get(i).clear();
							}
							allLists.clear();
							if (!serialNum.getText().equals("Unknown")) {
								// Then add the setup reports after
								for (File fileList : listOfFiles) {
									try {
										// Load in files as a PDF using PDFBox
										PDDocument document = PDDocument.load(new File(fileList.toString()));
										// Get document information
										PDDocumentInformation docInfo = document.getDocumentInformation();
										if (docInfo.getCreationDate() != null) {
											split2 = docInfo.getCreationDate().toInstant().toString().split("T");
											if (split2[0] != null) {
												date = split2[0];
											} else {
												date = "None";
											}
										}
										String[] nameSplit = fileList.getName().toString().split("#");
										for (int j = 0; j < nameSplit.length; j++) {
											if (nameSplit[j].equals("Report Setup.pdf")) {
												if (nameSplit[0].contains("Backdrive")) {
													backdrive.add(fileList);
												} else if (nameSplit[0].contains("Turning")) {
													if (nameSplit[0].contains("5 RPM")
															|| nameSplit[0].contains("5RPM")) {
														turningFive.add(fileList);
													} else {
														turningTen.add(fileList);
													}
												} else if (nameSplit[0].contains("Dynamic")) {
													dynamic.add(fileList);
												} else if (nameSplit[0].contains("Static")) {
													effort.add(fileList);
												} else if (nameSplit[0].contains("Wear")
														|| nameSplit[0].contains("Loaded")) {
													wearIn.add(fileList);
												} else if (nameSplit[0].contains("Center")) {
													centerRack.add(fileList);
												} else if (nameSplit[0].contains("Digital")
														|| nameSplit[0].contains("Sensor")) {
													if (nameSplit[0].contains("Position")) {
														digitalPos.add(fileList);
													} else {
														digitalTorque.add(fileList);
													}
												} else if (nameSplit[0].contains("Pre")) {
													preFlex.add(fileList);
												} else if (nameSplit[0].contains("EPS")) {
													epsInfo.add(fileList);
												} else {
													noise.add(fileList);
												}
											}
										}
										document.close();
									} catch (IOException e2) {
									}
								}
								allLists.add(wearIn);
								Collections.sort(backdrive);
								allLists.add(backdrive);
								Collections.sort(turningFive);
								allLists.add(turningFive);
								Collections.sort(turningTen);
								allLists.add(turningTen);
								allLists.add(centerRack);
								allLists.add(digitalPos);
								allLists.add(digitalTorque);
								allLists.add(preFlex);
								Collections.sort(dynamic);
								allLists.add(dynamic);
								allLists.add(effort);
								allLists.add(epsInfo);
								allLists.add(noise);
								// Add all items in required sorting order
								for (int i = 0; i < allLists.size(); i++) {
									for (int j = 0; j < allLists.get(i).size(); j++) {
										String[] nameSplit = allLists.get(i).get(j).toString()
												.split(Pattern.quote("\\"));
										String fileName = "";
										String nextFileName = "";
										for (int m = 0; m < nameSplit.length; m++) {
											fileName = nameSplit[nameSplit.length - 1];
										}
										long kilobytes = allLists.get(i).get(j).length() / 1024;
										PDDocument document = null;
										try {
											document = PDDocument.load(new File(allLists.get(i).get(j).toString()));
										} catch (IOException e) {
										}
										try {
											String[] nextSplit = allLists.get(i).get(j + 1).toString()
													.split(Pattern.quote("\\"));
											for (int m = 0; m < nextSplit.length; m++) {
												nextFileName = nextSplit[nextSplit.length - 1];
											}
										} catch (IndexOutOfBoundsException e) {

										}
										String[] firstBlock = fileName.split("#");
										String[] secondBlock = nextFileName.split("#");
										if (!firstBlock[0].equals(secondBlock[0])) {
											files.add(allLists.get(i).get(j));
											table.getItems().add(new itemsList(fileName, kilobytes + " KB",
													document.getNumberOfPages(), date));
										}
										try {
											document.close();
										} catch (IOException e) {
										}
									}
									allLists.get(i).clear();
								}
								allLists.clear();
							}
							popup.close();
							contents.getChildren().add(popupButton);
							merge.setVisible(true);
							clear.setVisible(true);
						}
					});
				}
			}
		});

		// Merge all files in list sequentially
		merge.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				PDFMergerUtility merger = new PDFMergerUtility();
				if (!table.getItems().isEmpty() || !files.isEmpty()) {
					File tempFile = new File(destName.getText());
					if (!tempFile.exists()) {
						popupHeader.setText("Merging Files");
						popup.setTitle("PDF Progress");
						popupText.setText("Merging PDF files in list...");
						contents.getChildren().remove(popupButton);
						popup.show();
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								for (int i = 0; i < files.size(); i++) {
									try {
										merger.addSource(files.get(i));
									} catch (FileNotFoundException e) {
										e.printStackTrace();
									}
								}
								merger.setDestinationFileName(destName.getText());
								try {
									merger.mergeDocuments(setupMainMemoryOnly);
									popup.close();
									contents.getChildren().add(popupButton);
									popupHeader.setText("PDF Merge Status");
									popup.setTitle("PDF Progress");
									popupText.setText("PDFs merged successfully!");
									popup.showAndWait();
									if (!serialNum.getText().equals("Unknown")) {
										askUser.show();
									}
								} catch (IOException e) {
								}
							}
						});
					} else {
						Stage saveFile = new Stage();
						saveFile.setTitle("Save New File Name");
						fileChooser.setInitialDirectory(new File(files.get(0).getParent()));
						if (!serialNum.getText().equals("Unknown")) {
							fileChooser.setInitialFileName(
									serialNum.getText() + " " + program.getText() + " FINAL FUNCTION");
						} else {
							fileChooser.setInitialFileName("");
						}
						File file = fileChooser.showSaveDialog(saveFile);
						popupHeader.setText("Merging Files");
						popup.setTitle("PDF Progress");
						popupText.setText("Merging PDF files in list...");
						contents.getChildren().remove(popupButton);
						popup.show();
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (file != null) {
									for (int i = 0; i < files.size(); i++) {
										try {
											merger.addSource(files.get(i));
										} catch (FileNotFoundException e) {
											e.printStackTrace();
										}
									}
									merger.setDestinationFileName(file.toString());
									try {
										merger.mergeDocuments(setupMainMemoryOnly);
										popup.close();
										contents.getChildren().add(popupButton);
										popupHeader.setText("PDF Merge Status");
										popup.setTitle("PDF Progress");
										popupText.setText("PDFs merged successfully!");
										popup.showAndWait();
										if (!serialNum.getText().equals("Unknown")) {
											askUser.show();
										}
									} catch (IOException e) {
									}
								}
							}
						});
					}
				} else {
					popupHeader.setText("No PDF Found");
					popup.setTitle("List Empty");
					popupText.setText("No PDF files to merge in list.");
					popup.showAndWait();
				}
			}
		});

		// Delete a single item from the list after selected
		split.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				itemsList toSplit = (itemsList) table.getSelectionModel().getSelectedItem();
				Object selectedItem = table.getSelectionModel().getSelectedItem();
				Splitter splitter = new Splitter();
				String date = "";
				if (selectedItem != null) {
					for (int i = 0; i < files.size(); i++) {
						String[] nameSplit = files.get(i).toString().split(Pattern.quote(File.separator));
						if (nameSplit[nameSplit.length - 1].equals(toSplit.getName())) {
							try {
								PDDocument document = PDDocument.load(files.get(i));
								List<PDDocument> Pages = splitter.split(document);
								Iterator<PDDocument> iterator = Pages.listIterator();
								String[] nameSplit2 = toSplit.getName().toString().split(".pdf");
								int j = 1;
								while (iterator.hasNext()) {
									PDDocument pd = iterator.next();
									PDDocumentInformation docInfo = pd.getDocumentInformation();
									if (docInfo.getCreationDate() != null) {
										nameSplit = docInfo.getCreationDate().toInstant().toString().split("T");
										if (nameSplit[0] != null) {
											date = nameSplit[0];
										} else {
											date = "None";
										}
									} else {
										date = "----/--/--";
									}
									pd.save(files.get(i).getParent() + "\\" + nameSplit2[0] + " (" + j + ").pdf");
									File fileName = new File(
											files.get(i).getParent() + "\\" + nameSplit2[0] + " (" + j + ").pdf");
									table.getItems().add(new itemsList(nameSplit2[0] + " (" + j + ").pdf",
											(fileName.length() / 1024) + " KB", pd.getNumberOfPages(), date));
									files.add(fileName);
									pd.close();
									j++;
								}
								table.getItems().remove(selectedItem);
								files.remove(i);
								document.close();
								split.setVisible(false);
								popupHeader.setText("PDF Split Success");
								popup.setTitle("PDF Status");
								popupText.setText("PDF files split successfully!");
								popup.show();
							} catch (IOException e) {
							}
						}
					}
				}
			}
		});

		// Delete a single item from the list after selected
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				itemsList toRemove = (itemsList) table.getSelectionModel().getSelectedItem();
				Object selectedItem = table.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					table.getItems().remove(selectedItem);
					for (int i = 0; i < files.size(); i++) {
						String[] split = files.get(i).toString().split(Pattern.quote(File.separator));
						if (split[split.length - 1].equals(toRemove.getName())) {
							files.remove(i);
						}
					}
				}
				if (table.getItems().isEmpty()) {
					currentProg.setVisible(false);
					program.setVisible(false);
					program.setText("");
					currentPart.setVisible(false);
					partNum.setVisible(false);
					partNum.setText("XXXXXXXX");
					currentPart.setVisible(false);
					partNum.setVisible(false);
					partNum.setText("XXXXXXXX");
					currentSerial.setVisible(false);
					serialNum.setVisible(false);
					serialNum.setText("XXXX-XXX");
					currentRev.setVisible(false);
					revNum.setText("XXXXX");
					revNum.setVisible(false);
					dest.setVisible(false);
					destName.setText("");
					merge.setVisible(false);
					clear.setVisible(false);
					delete.setVisible(false);
					split.setVisible(false);
				}
			}
		});

		// CLear all items from the list and reset program values
		clear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				files.clear();
				table.getItems().clear();
				currentProg.setVisible(false);
				program.setVisible(false);
				program.setText("");
				currentPart.setVisible(false);
				partNum.setVisible(false);
				partNum.setText("XXXXXXXX");
				currentPart.setVisible(false);
				partNum.setVisible(false);
				partNum.setText("XXXXXXXX");
				currentSerial.setVisible(false);
				serialNum.setVisible(false);
				serialNum.setText("XXXX-XXX");
				currentRev.setVisible(false);
				revNum.setText("XXXXX");
				revNum.setVisible(false);
				dest.setVisible(false);
				destName.setText("");
				merge.setVisible(false);
				clear.setVisible(false);
				delete.setVisible(false);
				split.setVisible(false);
			}
		});

		// Allow clicking on destination name to open system explorer location
		destName.setOnMouseClicked(act -> {
			try {
				Runtime.getRuntime().exec("explorer.exe /select," + files.get(0).getParent());
			} catch (Exception e) {
			}
		});

		table.setRowFactory(tv -> {
			TableRow<itemsList> row = new TableRow<itemsList>();

			// DIsplay file info on double click
			row.setOnMouseClicked(event -> {
				delete.setVisible(true);
				final Stage view = new Stage();
				view.setTitle("View PDF");
				VBox viewBox = new VBox(20);
				viewBox.setAlignment(Pos.CENTER);
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					// Integer index = row.getIndex();
					// itemsList value = (itemsList)
					// table.getSelectionModel().getSelectedItem();
				} else if (event.getClickCount() == 1 && (!row.isEmpty())) {
					itemsList value = (itemsList) table.getSelectionModel().getSelectedItem();
					if (value.getPages() == 1) {
						split.setVisible(false);
					} else {
						split.setVisible(true);
					}
				}
			});

			// The next three events trigger a drag event to allow moving of
			// file placement
			row.setOnDragDetected(event -> {
				if (!row.isEmpty()) {
					Integer index = row.getIndex();
					Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
					db.setDragView(row.snapshot(null, null));
					ClipboardContent cc = new ClipboardContent();
					cc.put(SERIALIZED_MIME_TYPE, index);
					db.setContent(cc);
					event.consume();
				}
			});

			row.setOnDragOver(event -> {
				Dragboard db = event.getDragboard();
				if (db.hasContent(SERIALIZED_MIME_TYPE)) {
					if (row.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
						event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
						event.consume();
					}
				}
			});

			row.setOnDragEntered(e -> row.setOpacity(0.5));

			row.setOnDragExited(e -> row.setOpacity(1));

			row.setOnDragDropped(event -> {
				Dragboard db = event.getDragboard();
				if (db.hasContent(SERIALIZED_MIME_TYPE)) {
					int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
					Object draggedItem = table.getItems().remove(draggedIndex);

					int dropIndex;

					if (row.isEmpty()) {
						dropIndex = table.getItems().size();
					} else {
						dropIndex = row.getIndex();
					}
					for (int i = 0; i < files.size(); i++) {
						if (draggedIndex == i) {
							File tmp = files.get(i);
							files.remove(i);
							files.add(dropIndex, tmp);
						}
					}

					table.getItems().add(dropIndex, draggedItem);

					event.setDropCompleted(true);
					table.getSelectionModel().select(dropIndex);
					event.consume();
				}
			});
			return row;
		});

		yes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String receiver = group.getSelectionModel().getSelectedItem();
				askUser.close();
				popup.close();
				if (program.getText().equals("")) {
					group.getSelectionModel().select("Other");
				} else {
					usageText.setText(receiver + " " + cf);
				}
				askGroup.show();
			}
		});

		no.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				askUser.close();
				popup.close();
			}
		});

		group.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String receiver = group.getSelectionModel().getSelectedItem();
				try {
					if (receiver.equals("Other")) {
						usageText.setText(">Enter Program< " + cf);
					} else {
						usageText.setText(receiver + " " + cf);
					}
				} catch (NullPointerException e) {

				}
			}
		});

		addProgram.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				newGroup.show();
			}
		});

		addGroup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (!groupName.getText().equals("") && !groupEmail.getText().equals("")) {
					progs.add(groupName.getText() + ":" + groupEmail.getText());
					newGroup.close();
					groupName.setText("");
					groupEmail.setText("");
					File programs = new File("progs.txt");
					PrintWriter output;
					Collections.sort(progs);
					group.getItems().clear();
					try {
						output = new PrintWriter(programs);
						for (int i = 0; i < progs.size(); i++) {
							String[] progsSplit = progs.get(i).split(":");
							output.println(progs.get(i));
							group.getItems().add(progsSplit[0]);
						}
						output.close();
					} catch (FileNotFoundException e1) {
						popupHeader.setText("File Not Found");
						popup.setTitle("No File");
						popupText.setText("progs.txt was not found. Please see administrator.");
						popup.show();
					}
				} else {
					popupHeader.setText("Invalid Entry");
					popup.setTitle("Empty Fields");
					popupText.setText("Please check that all fields have been entered.");
					popup.showAndWait();
				}
			}
		});

		removeProgram.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String receiver = group.getSelectionModel().getSelectedItem();
				if (!receiver.equals("Other")) {
					confirmText
							.setText("Are you sure you want to delete the " + receiver + " shipping approval group?");
					confirmDelete.show();
				} else {
					popupHeader.setText("Cannot Delete");
					popup.setTitle("Static Item");
					popupText.setText("Other cannot be removed from items.");
					popup.showAndWait();
				}
			}
		});

		yesDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String receiver = group.getSelectionModel().getSelectedItem();
				group.getItems().remove(receiver);
				for (int i = 0; i < progs.size(); i++) {
					String[] progsSplit = progs.get(i).split(":");
					if (receiver.equals(progsSplit[0])) {
						progs.remove(i);
					}
				}
				File programs = new File("progs.txt");
				PrintWriter output;
				try {
					output = new PrintWriter(programs);
					for (int i = 0; i < progs.size(); i++) {
						output.println(progs.get(i));
					}
					output.close();
				} catch (FileNotFoundException e1) {
					popupHeader.setText("File Not Found");
					popup.setTitle("No File");
					popupText.setText("progs.txt was not found. Please see administrator.");
					popup.show();
				}
				confirmDelete.close();
				popupHeader.setText("Group Deleted");
				popup.setTitle("Deletion Success");
				popupText.setText(receiver + " has been successfully deleted.");
				popup.showAndWait();
			}
		});

		noDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				confirmDelete.close();
			}
		});

		send.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				popup.close();
				popupHeader.setText("Opening Outlook");
				popup.setTitle("Send Email");
				popupText.setText("Please wait while the new email is prepared...");
				contents.getChildren().remove(popupButton);
				popup.show();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						String rev = revNum.getText();
						String usage = usageText.getText();
						String receiver = group.getSelectionModel().getSelectedItem();
						String program = "";
						String receiverEmail = "";
						if (receiver.equals("Other")) {
							program = ">Enter Program< ";
						} else {
							program = receiver + " " + m;
						}
						for (int i = 0; i < progs.size(); i++) {
							String[] progsSplit = progs.get(i).split(":");
							if (receiver.equals(progsSplit[0])) {
								receiverEmail = progsSplit[1];
							}
						}
						askGroup.close();
						// Create the message part
						BodyPart messageBodyPart = new MimeBodyPart();
						// String with body Text
						String bodyText = "<FONT SIZE='4'><FONT COLOR = 'red'><b>- REVIEW ATTACHED PDF</b></FONT><br>";
						bodyText += "<FONT COLOR = 'red'><b>- 'REPLY ALL'</b></FONT><br>";
						bodyText += "<FONT COLOR = 'red'><b>- COPY/PASTE THE DETAILS & QUESTIONS</b> <small>(P247 REQUIREMENT)</small></FONT><br>";
						bodyText += "<FONT COLOR = 'red'><b>- EDIT <u>ALL</u> BLUE TEXT</b> <small>(PLEASE DO NOT LEAVE ANY UNEDITED)</small></FONT><br>";
						bodyText += "------------------------------------------------------------------------------------------------------<br>";
						bodyText += "<b>" + serialNum.getText() + " " + program + "</b><br>";
						bodyText += "Part Number: " + partNum.getText() + "<br>";
						bodyText += "Change Level: " + rev + "<br>";
						bodyText += "Intended Usage: " + usage + "<br><br>";
						bodyText += "Is this part usable for its intended usage? "
								+ "<FONT COLOR = 'blue'><b>YES/NO</b></FONT><br>";
						bodyText += "Comments: " + "<FONT COLOR = 'blue'><b>TEXT/NA</b></FONT><br>";
						bodyText += "Have all non-conformance's in the build package been addressed? "
								+ "<FONT COLOR = 'blue'><b>YES/NO</b></FONT><br>";
						bodyText += "Vehicle Systems Evaluation? "
								+ "<FONT COLOR = 'blue'><b>NOT RUN/PASS/FAIL</b></FONT><br>";
						bodyText += "NVH: Field Data Replicator? "
								+ "<FONT COLOR = 'blue'><b>NOT RUN/PASS/FAIL</b></FONT><br>";
						bodyText += "NVH: Rotational Noise? "
								+ "<FONT COLOR = 'blue'><b>NOT RUN/PASS/FAIL</b></FONT><br>";
						bodyText += "NVH: In-Vehicle Evaluation? "
								+ "<FONT COLOR = 'blue'><b>NOT RUN/PASS/FAIL</b></FONT><br>";
						bodyText += "Functional Testing: " + "<FONT COLOR = 'blue'><b>NOT RUN/PASS/FAIL</b></FONT><br>";
						bodyText += "Other Comments: " + "<FONT COLOR = 'blue'><b>TEXT/NA</b></FONT></FONT><br>";

						try {
							messageBodyPart.setContent(bodyText, "text/html");
							MimeMessage message = new MimeMessage((Session) null);
							message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
							message.setSubject(serialNum.getText() + " " + program + " (Shipping Approval Request)");
							message.setHeader("X-Unsent", "1");

							// Create a multipar message
							Multipart multipart = new MimeMultipart();

							// Part two is attachment
							MimeBodyPart attachPart = new MimeBodyPart();
							String fileName = destName.getText();
							DataSource source = new FileDataSource(fileName);
							attachPart.setDataHandler(new DataHandler(source));
							attachPart.setFileName(new File(fileName).getName());
							multipart.addBodyPart(messageBodyPart);
							multipart.addBodyPart(attachPart);

							// Send the complete message parts
							message.setContent(multipart);

							message.saveChanges();

							File resultEmail = File.createTempFile("ShippingTemplate", ".eml");
							try (FileOutputStream fs = new FileOutputStream(resultEmail)) {
								message.writeTo(fs);
								fs.flush();
								fs.getFD().sync();
							}

							ProcessBuilder pb = new ProcessBuilder();
							if (template.isSelected() == true) {
								pb.command("cmd.exe", "/C", "start chrome", resultEmail.getCanonicalPath());
							} else {
								pb.command("cmd.exe", "/C", "start", "outlook.exe", "/eml",
										resultEmail.getCanonicalPath());
							}
							Process p = pb.start();
							try {
								p.waitFor();
							} catch (InterruptedException e) {

							} finally {
								p.getErrorStream().close();
								p.getInputStream().close();
								p.destroy();
							}
							popup.close();
							contents.getChildren().add(popupButton);
						} catch (Exception e) {
						}
					}
				});
			}
		});

		table.getColumns().addAll(column1, column2, column3, column4);
		left.setAlignment(Pos.BOTTOM_LEFT);
		right.setAlignment(Pos.BOTTOM_RIGHT);

		mainVBox.setSpacing(10);
		mainVBox.setPadding(new Insets(20, 20, 20, 20));
		mainVBox.setAlignment(Pos.CENTER);
		mainVBox.setBackground(background);
		header.setAlignment(Pos.CENTER);
		header.getChildren().addAll(imageView);
		left.getChildren().addAll(add, merge, clear, delete, split);
		right.getChildren().addAll(currentProg, program, currentPart, partNum, currentSerial, serialNum, currentRev,
				revNum);
		topHBox.getChildren().addAll(left, region, right);
		botHBox.getChildren().addAll(dest, destName);
		mainVBox.getChildren().addAll(header, topHBox, table, botHBox);

		primaryStage.setTitle("Nexteer PDF Application");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
