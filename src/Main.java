

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{

	Stage stage;
	String userShape = null;
	String computerShape = null;
	String currPlayer = "";

	ArrayList<Integer> playedByComputer = new ArrayList<Integer>();
	ArrayList<Integer> playedByUser = new ArrayList<Integer>();
	List<List<Integer>> winningCombinations = new ArrayList<List<Integer>>();

	List<Button> buttons;

	Label chooseLabel;
	HBox selectBox;
	Button playAgain;

	public static void main(String[] args){
		Application.launch(args);
	}


	@Override
	public void start(Stage stage){
		this.stage = stage;


		this.stage = stage;
		stage.setTitle("Tic Tac Toe");

		//WINNING COMBINATIONS
		List<Integer> item1 = Arrays.asList(0,1,2); winningCombinations.add(item1);
		List<Integer> item2 = Arrays.asList(3,4,5); winningCombinations.add(item2);
		List<Integer> item3 = Arrays.asList(6,7,8); winningCombinations.add(item3);
		List<Integer> item4 = Arrays.asList(0,3,6); winningCombinations.add(item4);
		List<Integer> item5 = Arrays.asList(1,4,7); winningCombinations.add(item5);
		List<Integer> item6 = Arrays.asList(2,5,8); winningCombinations.add(item6);
		List<Integer> item7 = Arrays.asList(0,4,8); winningCombinations.add(item7);
		List<Integer> item8 = Arrays.asList(2,4,6); winningCombinations.add(item8);


		buttons = new ArrayList<Button>();

		Button b1 = new Button();
		Button b2 = new Button();
		Button b3 = new Button();
		Button b4 = new Button();
		Button b5 = new Button();
		Button b6 = new Button();
		Button b7 = new Button();
		Button b8 = new Button();
		Button b9 = new Button();

		buttons = Arrays.asList(b1, b2, b3, b4, b5, b6, b7, b8, b9);

		GridPane buttonsGrid = new GridPane();


		int gridSize = 3;

		for(int i = 0; i < buttons.size(); i++){
			buttonsGrid.add(buttons.get(i), i % gridSize, (int)Math.floor(i / gridSize));
			final int j = i;
			buttons.get(i).setFont(Font.font("Arial", FontWeight.BOLD, 40));
			buttons.get(i).setPadding(new Insets(30, 40, 30, 40));
			buttons.get(i).setPrefSize(115, 115);
			//buttons.get(i).setText(""+i);


			buttons.get(i).setOnAction(e->{
				if(currPlayer.equals("user") && !playedByComputer.contains(j) && !playedByUser.contains(j)){
					buttons.get(j).setText(userShape);
					playedByUser.add(j);

					if(checkWin("user")){
						endGame("You win!!");
					}
		        	else if(playedByComputer.size() + playedByUser.size() >= 9){
		        		endGame("Tied Game!!");
		        	}
					else{
						chooseLabel.setText("Computer's turn");
						currPlayer = "computer";
						computerPlay();
					}


				}
			});

		}

		chooseLabel = new Label("Choose an alphabet between X and O");
		chooseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		Button oButton = new Button("O");
		oButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));



		Button xButton = new Button("X");
		xButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));

		selectBox = new HBox(oButton, xButton);
		selectBox.setAlignment(Pos.CENTER);
		selectBox.setSpacing(20);
		VBox topBox = new VBox(chooseLabel, selectBox);
		topBox.setAlignment(Pos.CENTER);
		topBox.setSpacing(10);

		xButton.setOnAction(e->{
			startGame("X", "O");
		});
		oButton.setOnAction(e->{
			startGame("O", "X");
		});

		buttonsGrid.setAlignment(Pos.CENTER);

		playAgain = new Button("Play Again");
		playAgain.setVisible(false);
		VBox bottomBox = new VBox(playAgain);
		bottomBox.setAlignment(Pos.CENTER);
		playAgain.setOnAction(e->{
			chooseLabel.setText("Choose an alphabet between X and O");
			selectBox.setVisible(true);
			playAgain.setVisible(false);
			for(Button b : buttons){
				b.setText("");
			}
		});

		VBox root = new VBox(topBox, buttonsGrid, bottomBox);
		root.setSpacing(20);root.setPadding(new Insets(20));
		Scene scene = new Scene(root, 500, 500);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public void computerPlay(){

		if(computerShape != null && currPlayer.equalsIgnoreCase("computer")){

			Timeline timeline = new Timeline(new KeyFrame(
			        Duration.millis(2000),
			        ae -> {
			        	int optimal = getOptimalNumber();
			        	//System.out.println(""+optimal);
			        	if(optimal == -1){
			        		System.exit(0);
			        		System.out.println("Unexpected error encountered");
			        	}
			        	else{
			        		buttons.get(optimal).setText(computerShape);


				        	playedByComputer.add(optimal);

				        	if(checkWin("computer")){
				        		endGame("Computer Wins!!");
				        	}
				        	else if(playedByComputer.size() + playedByUser.size() >= 9){
				        		endGame("Tied Game!!");
				        	}
				        	else{
				        		chooseLabel.setText("Your turn");
					    		currPlayer = "user";
				        	}



			        	}

			        }));
			timeline.play();

		}
	}

	public void startGame(String u, String c){
		userShape = u;computerShape = c;
		selectBox.setVisible(false);
		chooseLabel.setText("Computer's turn");

		currPlayer = "computer";
		computerPlay();
	}


	public int getOptimalNumber(){

		if(playedByUser.size() == 0){
			Random rand = new Random();
			return rand.nextInt(9);
		}
		else if(playedByUser.size() == 1){
			int ret = -1;
			int played = playedByComputer.get(0);
			for(int i = 0; i < winningCombinations.size(); i++){
				List<Integer> currComb = winningCombinations.get(i);
				if(currComb.contains(played) && !currComb.contains(playedByUser.get(0)))
				{
					if(currComb.get(0) == played){
						ret = currComb.get(1);
					}
					else{
						ret = winningCombinations.get(i).get(0);
					}
					break;
				}
			}
			return ret;
		}
		else{
			int ret = -1;

			for(int i = 0; i < winningCombinations.size(); i++){
				List<Integer> comb = winningCombinations.get(i);
				int notPresent = -1;
				int presentCount = 0;
				for(int j = 0; j < comb.size(); j++){
					if(playedByComputer.contains(comb.get(j))){
						presentCount++;
					}
					else if(!playedByUser.contains(comb.get(j))){
						notPresent = comb.get(j);
					}
				}
				if(presentCount == 2){
					ret = notPresent;
					break;
				}
			}


			if(ret > -1){
				return ret;
			}
			else{
				for(int i = 0; i < winningCombinations.size(); i++){
					List<Integer> comb = winningCombinations.get(i);
					int notPresent = -1;int presentCount = 0;
					for(int j = 0; j < comb.size(); j++){
						if(playedByUser.contains(comb.get(j))){
							presentCount++;
						}
						else  if(!playedByComputer.contains(comb.get(j))){
							notPresent = comb.get(j);
						}
					}
					if(presentCount == 2){
						ret = notPresent;
						break;
					}

				}
				if(ret > -1){
					return ret;
				}
				else{
					for(int j = 0; j < playedByComputer.size(); j++){
						int played = playedByComputer.get(j);
						for(int i = 0; i < winningCombinations.size(); i++){
							List<Integer> currComb = winningCombinations.get(i);
							if(currComb.contains(played))
							{
								if(!playedByComputer.contains(currComb.get(0)) && !playedByUser.contains(currComb.get(0))){
									ret = currComb.get(0);
								}
								else if(!playedByComputer.contains(currComb.get(1)) && !playedByUser.contains(currComb.get(1))){
									ret = currComb.get(1);
								}
								else if(!playedByComputer.contains(currComb.get(2)) && !playedByUser.contains(currComb.get(2))){
									ret = currComb.get(2);
								}
							}
						}
						if(ret > -1){
							break;
						}
						else{
							Random rand = new Random();
							ret = rand.nextInt(9);
							while(playedByComputer.contains(ret) || playedByUser.contains(ret)){
								ret = rand.nextInt(9);
							}
						}
					}
				}
				return ret;
			}
		}

	}



	public boolean checkWin(String player){

		List<Integer> list = null;
		if(player.equals("computer")){
			list = playedByComputer;
		}
		else{
			list = playedByUser;
		}


		for(int i = 0; i < winningCombinations.size(); i++){
			List<Integer> currComb = winningCombinations.get(i);
			if(list.containsAll(currComb)){
				return true;
			}

		}
		return false;
	}


	public void endGame(String text){
		chooseLabel.setText(text);

		userShape = null;
		computerShape = null;
		currPlayer = "";
		playedByComputer = new ArrayList<Integer>();
		playedByUser = new ArrayList<Integer>();

		playAgain.setVisible(true);



	}
}
