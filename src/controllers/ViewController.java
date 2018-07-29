package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import crawler.SearchEngineType;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import crawler.ImageClawer;


public class ViewController {
	
	ArrayList<String>result = new ArrayList<String>();
	 
	HashMap<String, Image>	currentPageItems = new HashMap<String, Image>();
	
	HashMap<String, Image> selectedItems =new HashMap<String, Image>();
	
	@FXML
	ImageView resultPreviewImageView;
	
	@FXML
	ImageView selectedPreviewImageView;
	
	@FXML
	ListView<String> resultListView;
	
	@FXML
	ListView<String> selectedListView;
	
	@FXML
	TextField searchTextField;
	
	@FXML
	ChoiceBox<String> pageChoiceBox;
	
	@FXML
	ProgressBar	progressBar;
	
	@FXML
	ProgressIndicator progressIndicator;
	
	@FXML
	TextField savePathTextField;
	
	@FXML
    private void initialize() {
		resultListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				resultPreviewImageView.setImage(currentPageItems.get(newValue));
			}
		});
		
		selectedListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				selectedPreviewImageView.setImage(selectedItems.get(newValue));
			}
		});
		
		pageChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				new Thread() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						currentPageItems.clear();
						int start = (Integer.parseInt(newValue)-1)*100;
			
						for(int index = start, progress =0; index < start+100; index++,progress++) {
							System.out.println("INDEX==>"+index);
							progressBar.setProgress((float)(progress+1)/100);
							progressIndicator.setProgress((float)(progress+1)/100);
							currentPageItems.put(result.get(index), new Image(result.get(index)));//)new URL(result.get(index)).openStream()));
						}
						updateListView(resultListView, currentPageItems);
					}
					
				}.start();
				
				
			}
		});
    }
	
	
	
	
	
	
	
	
	void updateListView(ListView<String> listView,HashMap<String, Image> items) {
		String[] itemsKey = items.keySet().toArray(new String[ items.keySet().size()]);


		ObservableList<String> itemsObservableList = FXCollections.unmodifiableObservableList(
				
	            FXCollections.observableArrayList(itemsKey)
	    );
		
		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listView.setCellFactory(parma -> new ImageCell(items));
		listView.setItems(itemsObservableList);
		
	}
	
	

	@FXML
    private void addButtonAction() {
		for(String selectedItemKey : resultListView.getSelectionModel().getSelectedItems()) {
			
			
			selectedItems.put(selectedItemKey, currentPageItems.get(selectedItemKey));
		}
		
		updateListView(selectedListView, selectedItems);
		
		System.out.println("添加！！！");
	}
	
	@FXML
    private void removeButtonAction() {
		for(String selectedItemKey : selectedListView.getSelectionModel().getSelectedItems()) {
			
			selectedItems.remove(selectedItemKey);
		}
		
		updateListView(selectedListView, selectedItems);
		
		System.out.println("移除！！！");
	}
	
	@FXML
	private void searchButtonAction() {
		currentPageItems.clear();
		result.clear();
		
		
		
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				for(int index=1; index <= 8; index++) {
					progressBar.setProgress((float)index/8);
					progressIndicator.setProgress((float)index/8);
					try {
						result.addAll( ImageClawer.fetch(SearchEngineType.GOOGLE,  URLEncoder.encode(searchTextField.getText(), "UTF-8"),index,index*100));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				int total = result.size();
				
				int pages = total/100;
				
				String[] pageIndex =new String[pages];
				
				for(int index =0; index < pageIndex.length; index++)
					pageIndex[index] = String.valueOf(index+1);


				ObservableList<String> pageIndexList = FXCollections.unmodifiableObservableList(
						
			            FXCollections.observableArrayList(pageIndex)
			    );
				pageChoiceBox.setItems(pageIndexList);
			}
			
		}.start();;
		
	}
	
	@FXML
    private void browseButtonAction() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("儲存路徑");
		File directory = directoryChooser.showDialog(null);
		savePathTextField.setText(directory.toString());
		System.out.println("瀏覽！！！");
	}
	
	@FXML
    private void saveButtonAction() {
		int index =0;
		for(String key : selectedItems.keySet()) {
			
		
				
			    try {
			    	File outputFile = new File(savePathTextField.getText()+File.separator+String.format("%d.png", index));
			    	System.out.println(outputFile.toString());
				    BufferedImage bImage = SwingFXUtils.fromFXImage(selectedItems.get(key), null);
				    ImageIO.write(bImage, "png", outputFile);
				    index++;
			    } catch (IOException e) {
			      throw new RuntimeException(e);
			    }
			
			
		}
		System.out.println("儲存！！！");
		
	}
	
	

		class ImageCell extends ListCell<String> {
			
			HashMap<String, Image> images = null;
			public ImageCell(HashMap<String, Image> images) {
				super();
				this.images = images;
			}
			ImageView imageView = null;
			String url = null;
			@Override
			protected void updateItem(String item, boolean empty) {
				// TODO Auto-generated method stub
				
				
				super.updateItem(item, empty);
				if(!(empty || item == null)) {
					url = item;
					Image image =images.get(item);
					imageView= new ImageView();
					imageView.setFitHeight(100);
					imageView.setFitWidth(100);
					imageView.setPreserveRatio(true);
					imageView.setImage(image);
				}
				
				setText(url);
				setGraphic(imageView);
				
			}
			
		}
		
	
}
