import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Math;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Driver extends Application{
	private int col = -1;
	private int row = -1;
	private boolean knowstheirstuff = false;
	private boolean [][] visited = new boolean[4][4];
	private static ArrayList<String> words = new ArrayList<String>();
	private int width = 0;
	private int height = 0;
	private Button [][] defaults = new Button[4][4];
	private String theRealOG = "";
	private ArrayList<String[]> from = new ArrayList<String[]>(Arrays.asList(new String[]{"A","A","E","E","G","N"}, 
																			 new String[]{"A","B","B","J","O","O"},
																			 new String[]{"A","C","H","O","P","S"},
																			 new String[]{"A","F","F","K","P","S"},
																			 new String[]{"A","O","O","T","T","W"},
																			 new String[]{"C","I","M","O","T","U"},
																			 new String[]{"D","E","I","L","R","X"},
																			 new String[]{"D","E","L","R","V","Y"},
																			 new String[]{"D","I","S","T","T","Y"},
																			 new String[]{"E","E","G","H","N","W"},
																			 new String[]{"E","E","I","N","S","U"},
																			 new String[]{"E","H","R","T","V","W"},
																			 new String[]{"E","I","O","S","S","T"},
																			 new String[]{"E","L","R","T","T","Y"},
																			 new String[]{"H","I","M","N","U","QU"},
																			 new String[]{"H","L","N","N","R","Z"}));
	private static ArrayList<String> insults = new ArrayList<String>(Arrays.asList("Get the fuck outta my sight before I demolish you.", 
																			"What are you, a fucking a cappela group? Now play the God damn kit!",
																			"Start practicing harder Neiman.",
																			"Not quite my tempo.",
																			"Oh my fucking God. Are you one of those single tear people? Do I look like a double fucking rainbow to you?",
																			"For the record, Metz wasn't out of tune, you were Ericcson. But Metz didn't know the difference, and that's bad enough.",
																			"I wasn't there to conduct. Any fuckin' moron can wave his arms and keep people in tempo. I was there to push people beyond what's expected of them.",
																			"Were you rushing or were you dragging? ",
																			"So you DO know the difference! If you deliberately sabotage my band, I will fuck you like a pig. Now are you a \"rusher\", or are you a \"dragger\", or are you going to be ON MY FUCKING TIME?!? "));
	private boolean mustbe = true;
	private DropShadow shadow = new DropShadow();
	private Glow glow = new Glow(10);
	private SepiaTone sepiatone = new SepiaTone(0.4);
	private int temp = 0;
	
	//String from an ArrayList of strings
	public static String getString(ArrayList<String> a){
		StringBuilder thingy = new StringBuilder();
		for(int i = 0; i < a.size(); i++){
			thingy.append(a.get(i));
		}
		String temp = thingy.toString();
		return temp;
	}
	
	public static void lookUp(String s, Text f, FadeTransition a, Text m) throws IOException{
		boolean works = true;
		
		URL url = new URL("http://services.aonaware.com/DictService/Default.aspx?action=define&dict=*&query=" + s);
	
		URLConnection con = url.openConnection();
        InputStream ipman = con.getInputStream();
        BufferedReader wow = new BufferedReader(new InputStreamReader(ipman));
        String line = null;        
        while ((line = wow.readLine()) != null) {
            System.out.println(line);
            if(line.contains("No definitions found")){
            	m.setText(insults.get((int)(Math.random()*insults.size())));
            	m.setVisible(true);
            	works = false;
            	a.play();
            	new Timer().schedule(
            			new TimerTask(){
            				@Override
            				public void run(){
            					m.setVisible(false);
            				}
            			}, 
            			5000
            	);
            	break;
            }
        }
        
        if(works){
        	words.add(s);
        	if(s.length() >= 8){
        		f.setText(String.valueOf(Integer.parseInt(f.getText()) + 11));
        	}
        	else if(s.length() == 7){
        		f.setText(String.valueOf(Integer.parseInt(f.getText()) + 5));
        	}
        	else if(s.length() == 6){
        		f.setText(String.valueOf(Integer.parseInt(f.getText()) + 3));
        	}
        	else if(s.length() == 5){
        		f.setText(String.valueOf(Integer.parseInt(f.getText()) + 2));
        	}
        	else{
        		f.setText(String.valueOf(Integer.parseInt(f.getText()) + 1));
        	}
        }
	}
	
	public Node getNode(int col, int row, GridPane gp){
		ObservableList<Node> c = gp.getChildren();
		for(Node n : c){
			if(GridPane.getRowIndex(n) == row && GridPane.getColumnIndex(n) == col){
				return n;
			}
		}
		return null;
	}
	
	public ArrayList<Node> getWorkingNodesByText(String text, GridPane gp){
		ObservableList<Node> c = gp.getChildren();
		ArrayList<Node> deliverable = new ArrayList<Node>();
		for(Node n : c){
			if((((Button)n).getText().toUpperCase().equals(text.toUpperCase())) && ((isNeighbor((Button)n)) || (row == -1 && col == -1)) && !visited[GridPane.getRowIndex(n)][GridPane.getColumnIndex(n)]){
				deliverable.add(n);
			}
		}
		return deliverable;
	}
	
	public void colorSurrounds(int row, int col, GridPane gp){
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				if((i == col - 1 || i == col || i == col + 1) && (j == row - 1 || j == row || j == row + 1) && !visited[j][i]){
					getNode(i, j, gp).setEffect(shadow);
				}
				else if(visited[j][i]){
					getNode(i, j, gp).setEffect(glow);
				}
				else{
					getNode(i, j, gp).setEffect(null);
				}
			}
		}
	}
	
	public void clean(GridPane gp){
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				getNode(i, j, gp).setEffect(null);
			}
		}
	}
	
	public boolean isAlpha(String s){
		char[] thing = s.toCharArray();
		
		for(char c : thing){
			if(!Character.isLetter(c)){
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isNeighbor(Button button){
		/**if(((GridPane.getRowIndex(a) == GridPane.getRowIndex(b) + 1) 
				&& ((GridPane.getColumnIndex(a) == GridPane.getColumnIndex(b) + 1) || (GridPane.getColumnIndex(a) == GridPane.getColumnIndex(b)) || (GridPane.getColumnIndex(a) == GridPane.getColumnIndex(b) - 1))) 
				|| ((GridPane.getRowIndex(a) == GridPane.getRowIndex(b)) 
				&& ((GridPane.getColumnIndex(a) == GridPane.getColumnIndex(b) + 1) || (GridPane.getColumnIndex(a) == GridPane.getColumnIndex(b) -1)))
				||  ((GridPane.getRowIndex(a) == GridPane.getRowIndex(b) - 1) 
				&& ((GridPane.getColumnIndex(a) == GridPane.getColumnIndex(b) + 1) || (GridPane.getColumnIndex(a) == GridPane.getColumnIndex(b)) || (GridPane.getColumnIndex(a) == GridPane.getColumnIndex(b) - 1)))){
			return true;
		}**/
		if((((GridPane.getRowIndex(button) == row + 1) 
			&& ((GridPane.getColumnIndex(button) == col + 1) || (GridPane.getColumnIndex(button) == col) || (GridPane.getColumnIndex(button) == col - 1))) 
			|| ((GridPane.getRowIndex(button) == row) 
			&& ((GridPane.getColumnIndex(button) == col + 1) || (GridPane.getColumnIndex(button) == col -1)))
			||  ((GridPane.getRowIndex(button) == row - 1) 
			&& ((GridPane.getColumnIndex(button) == col + 1) || (GridPane.getColumnIndex(button) == col) || (GridPane.getColumnIndex(button) == col - 1))))){
			return true;
		}
		return false;
	}
	
	public void resetTheNumberedOnes(GridPane gp){
		ObservableList<Node> stuffy = gp.getChildren();
		for(Node n : stuffy){
			try{
				Integer number = Integer.parseInt(((Button)n).getText());
				((Button)n).setText(theRealOG);
			}
			catch(NumberFormatException e){
				continue;
			}
		}
		
	}
	
	@Override
	public void start(Stage thingy){
		//thingy.getIcons().add(new Image("C:\\Users\\panda\\Desktop\\Eclipse\\Boggle with Josh\\Icon.png"));
		//why can't this be a part of the executable jar?
		
		ArrayList<String> word = new ArrayList<String>();
		
		GridPane root = new GridPane();		
		
		//Side Panel
		VBox tasks = new VBox(5);
		tasks.setSpacing(10);
		tasks.setPadding(new Insets(10,10,10,10));
		
		Text scoret = new Text();
		scoret.setText("Score:");
		tasks.getChildren().add(scoret);
		
		Text score = new Text();
		score.setText("0");
		tasks.getChildren().add(score);
		
		Text message = new Text();
		message.setFont(Font.font("Verdana", 12));
		message.setX(10);
		message.setY(90);
		message.setWrappingWidth(160);
		message.setTextAlignment(TextAlignment.CENTER);
		message.setVisible(false);
		
		FadeTransition ft = new FadeTransition(Duration.millis(2000));
		ft.setAutoReverse(true);
		ft.setNode(message);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.setCycleCount(1);
		//ft.play();
		
	
		/**HBox cw = new HBox();
		cw.setSpacing (10);**/
		
		Text currwordt = new Text();
		currwordt.setText("Current Word:");
		//cw.getChildren().add(currwordt);
		tasks.getChildren().add(currwordt);
		
		Text currword = new Text();
		currword.setText("[Nothing]");
		//cw.getChildren().add(currword);
		tasks.getChildren().add(currword);
		
		//tasks.getChildren().add(cw);
		
		
		//Game Board
		GridPane pboard = new GridPane();
		pboard.setHgap(0);
		pboard.setVgap(0);
		pboard.setPadding(new Insets(10,10,10,10));
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				int stuffy = (int)(Math.random()*(double)from.size());
				Button button = new Button(from.get(stuffy)[(int)(Math.random()*6)]);
				button.setMinSize(40, 40);
				button.setMaxSize(40, 40);
				from.remove(stuffy);
				button.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event){
						if(isNeighbor(button) && !visited[GridPane.getRowIndex(button)][GridPane.getColumnIndex(button)]){
							word.add(button.getText());
							currword.setText(getString(word));
							col = GridPane.getColumnIndex(button);
							row = GridPane.getRowIndex(button);
							visited[row][col] = true;
							colorSurrounds(row, col, pboard);
						}
						else if(col == -1 && row == -1){
							word.add(button.getText());
							currword.setText(getString(word));
							col = GridPane.getColumnIndex(button);
							row = GridPane.getRowIndex(button);			
							visited[row][col] = true;
							colorSurrounds(row, col, pboard);							
						}
					}
				});
				width += 1;
				pboard.add(button, i, j);
				defaults[i][j] = button;
				System.out.println(button.getText());
			}
			height += 1;
		}		
		width /= height;
		
		//change to handle two consecutive letters
		//change to handle two of the same letters on board
		//handle letters that don't appear
		
		
		HBox buttons = new HBox();
		buttons.setSpacing(15);
		
		Button sButton = new Button("Submit");
		sButton.setDefaultButton(true);
		sButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){		
				try {
					if(getString(word).length() >= 3){
						if(!words.contains(getString(word))){
							lookUp(getString(word), score, ft, message);
						}
					}
					else if(knowstheirstuff){
					}
					else{
						final Stage dialog = new Stage();
						dialog.initModality(Modality.APPLICATION_MODAL);
		                dialog.initOwner(thingy);
		                
		                VBox ugh = new VBox(10);
		                ugh.getChildren().add(new Text("Points are only recorded for words longer than 3 characters."));
		                
		                Button okay = new Button("Okay");
		                okay.setOnAction(new EventHandler<ActionEvent>(){
		                	@Override
		                	public void handle(ActionEvent event){
		                		knowstheirstuff = true;
		                		dialog.close();
		                	}
		                });
		                okay.setDefaultButton(true);
		                ugh.getChildren().add(okay);
		                
		                Scene scene = new Scene(ugh, 350, 60);
		                dialog.setScene(scene);
		                dialog.show();
					}
					word.clear();
					currword.setText("[Nothing]");
					for(int i = 0; i < visited.length; i++){
						for(int j = 0; j < visited[0].length; j++){
							visited[i][j] = false;
						}
					}
					col = -1;
					row = -1;
					clean(pboard);
					resetTheNumberedOnes(pboard);
					mustbe = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});		
		buttons.getChildren().add(sButton);
		
		Button cButton = new Button("Clear");//check esc works
		cButton.setCancelButton(true);
		cButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				word.clear();
				currword.setText("[Nothing]");
				score.setText("0");
				
				for(int i = 0; i < visited.length; i++){
					for(int j = 0; j < visited[0].length; j++){
						visited[i][j] = false;
					}
				}
				col = -1;
				row = -1;
				clean(pboard);
				resetTheNumberedOnes(pboard);
				mustbe = true;
			}
		});
		
		buttons.getChildren().add(cButton);
		
		tasks.getChildren().add(buttons);
		
		
		Pane pane = new Pane();
		pane.getChildren().addAll(pboard, message);
		
		root.add(pane, 0, 0);
		root.add(tasks, 1, 0);
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			System.out.println(ev.getCode());
			if(ev.getCode().toString().equals("BACK_SPACE")){
				cButton.fire();
			}
			else if(isAlpha(ev.getText()) && mustbe){
				String a = ev.getText();
				if(ev.getText().equals("q")){
					a = "qu";
				}
				ArrayList<Node> stuff = getWorkingNodesByText(a, pboard);
				temp = stuff.size();
				if(stuff.size() == 1){
					((Button)stuff.get(0)).fire();
				}
				else if (stuff.size() > 1){
					//String theRealOG = ((Button)stuff.get(0)).getText();
					theRealOG = ((Button)stuff.get(0)).getText();
					word.add(((Button)stuff.get(0)).getText());//fix so that this appears before the numbers do2
					currword.setText(getString(word));
					mustbe = false;
					for(int i = 0; i < stuff.size(); i++){
						((Button)stuff.get(i)).setText(Integer.toString(i+1));
						((Button)stuff.get(i)).setEffect(sepiatone);
					}
				}
			}
			else if(!mustbe){//take care of exception where it doesn't work because it is a number
				try{
					if(Integer.parseInt(ev.getText()) > 0 && Integer.parseInt(ev.getText()) <= temp){
						ArrayList<Node> stuff = getWorkingNodesByText(ev.getText(), pboard);
						//word.add(theRealOG);
						currword.setText(getString(word));
						col = GridPane.getColumnIndex((Button)stuff.get(0));
						row = GridPane.getRowIndex((Button)stuff.get(0));
						visited[row][col] = true;
						colorSurrounds(row, col, pboard);
						resetTheNumberedOnes(pboard);
						mustbe = true;
						temp = 0;
						//reset all
					}
				}
				catch(NumberFormatException e){
					System.out.println("stupid");
				}
			}
		}
		);		
		
		/**Group blended = new Group(message, root);
		blended.setBlendMode(BlendMode.OVERLAY);
		root.getChildren().add(blended);**/ //need to add sprite and fix
		
		//root.getChildren().add(newscene)
		
		Scene scene = new Scene(root, 320, 180);
		
		thingy.setTitle("Boggle");
		thingy.setScene(scene);
		thingy.show();
		
		Media music = new Media(new File("ReggieWatts2.mp3").toURI().toString());
		MediaPlayer mediaplayer = new MediaPlayer(music);
		mediaplayer.setOnEndOfMedia(new Runnable(){
			public void run(){
				mediaplayer.seek(Duration.ZERO);
			}
		});
		mediaplayer.play(); //--> why can this be a part of the jar?
		
		//ask why this only works with an internet connection
	}	

	public static void main(String[] args) {
		launch(args);
	}

}
