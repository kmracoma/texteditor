package editor;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


public class Editor extends Application{
    
    private Desktop desktop = Desktop.getDesktop();
    
    @Override
    public void start(Stage stage){
        
        Menu fileMenu = new Menu("File");
        MenuItem fileItem = new MenuItem("New File");
        MenuItem windowItem = new MenuItem("New Window");
        MenuItem saveItem = new MenuItem("Save File");
        MenuItem openItem = new MenuItem("Open");
        MenuItem exitItem = new MenuItem("Exit");
        SeparatorMenuItem separator = new SeparatorMenuItem();
        
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        
        fileMenu.getItems().addAll(fileItem, windowItem, saveItem, openItem, separator, exitItem);
        
        Menu editMenu = new Menu("Edit");
        MenuItem undoItem = new MenuItem("Undo");
        MenuItem redoItem = new MenuItem("Redo");
        MenuItem cutItem = new MenuItem("Cut");
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem pasteItem = new MenuItem("Paste");
        //editMenu.getItems().addAll(undoItem, separator);
        if(!clipboard.hasString())
        {
            pasteItem.setDisable(true);
        }
        
        editMenu.getItems().addAll(undoItem, redoItem, separator, cutItem, copyItem, pasteItem);
        
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(editMenu);
        
        TextArea textArea = new TextArea();
        
        VBox vbox = new VBox(menuBar, textArea);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        
        if(textArea.getSelectedText() == null){
            copyItem.setDisable(true);
        }
        
        //File Menu actions
        fileItem.setOnAction(e -> newFile());
        
        saveItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
            File selectedFile = fileChooser.showSaveDialog(stage);
            if(selectedFile != null)
            {
                saveToFile(textArea.getText(), selectedFile);
            }
        });
        
        openItem.setOnAction(e -> {
            try {
                openFile(stage);
            } catch (IOException ex) {
                Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        windowItem.setOnAction(e -> start(stage));
        exitItem.setOnAction(e -> System.exit(0));
        
        //Edit Menu actions
        undoItem.setOnAction(e -> {
            textArea.undo();
        });
        
        redoItem.setOnAction(e -> {
            textArea.redo();
        });
        
        pasteItem.setOnAction(e -> {
            textArea.paste();
        });
        
        copyItem.setOnAction(e -> {
            textArea.copy();
        });
        
        cutItem.setOnAction(e -> {
            textArea.cut();
        });
        
        Scene scene = new Scene(vbox, 500, 500);
        stage.setScene(scene);
        stage.setTitle("Text Editor");
        stage.show();
    }
    
    private void newFile(){
        
    }
    
    private void openFile(Stage stage) throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null)
        {
            desktop.open(selectedFile);
        }
    }
    
    private void saveToFile(String text, File file) 
    {
        try{
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(text);
            writer.close();
        }
        catch(IOException e){
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}