package com.smartgames.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.AbsolutePanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Durak_game implements EntryPoint {

  private AbsolutePanel absolutePanel = new AbsolutePanel();
  private Button firstPlayerNextMoveButton = new Button("2");
  private Button secondPlayerNextMoveButton = new Button("2");
  
  private ArrayList<Card> tableCards = new ArrayList<Card>();
  private ArrayList<Card> cardPack = new ArrayList<Card>();
  private ArrayList<Card> trashCards = new ArrayList<Card>();
  private ArrayList<Card> firstPlayerCards = new ArrayList<Card>();
  private ArrayList<Card> secondPlayerCards = new ArrayList<Card>();
  private ArrayList<Card>[] players =  (ArrayList<Card>[])new ArrayList[]{firstPlayerCards,secondPlayerCards};
  /**
   * Entry point method.
   */
  private LoginInfo loginInfo = null;
  private VerticalPanel loginPanel = new VerticalPanel();
  private Label loginLabel = new Label("Please sign in to your Google Account to access the StockWatcher application.");
  private Anchor signInLink = new Anchor("Sign In");
  private Anchor signOutLink = new Anchor("Sign Out");
  private int currentPlayer = 1;
  private int mainKind;

  public void onModuleLoad() {
  	firstPlayerNextMoveButton.setText("Next move");
	  //for nice visual
	  //prepareGame();
	//firstPlayerNextMoveButton.setText("next move");
  	//firstPlayerNextMoveButton.setHTML("1");
  	
  	absolutePanel.add(firstPlayerNextMoveButton,600,140);
  	secondPlayerNextMoveButton.setText("Next move");
  	//secondPlayerNextMoveButton.setText("next move");
  	//secondPlayerNextMoveButton.setHTML("2");
  	absolutePanel.add(secondPlayerNextMoveButton, 600, 380);
  	firstPlayerNextMoveButton.addClickHandler(nextMoveClickHandler1);
  	secondPlayerNextMoveButton.addClickHandler(nextMoveClickHandler2);
	  
	  
	  // Check login status using login service.
    LoginServiceAsync loginService = GWT.create(LoginService.class);
    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
      public void onFailure(Throwable error) {
      }

      public void onSuccess(LoginInfo result) {
        loginInfo = result;
        if(loginInfo.isLoggedIn()) {
          //Window.alert("now we can start the game");
          prepareGame();
          //startGame();
        } else {
          loadLogin();
          Window.alert("Please log in before game starts");
        }
      }
    });
  }

private void startGame() {
	//Window.alert("playing game");
	// TODO Auto-generated method stub
	while(gameInProgress()) {
		servefirstPlayer();
	  	serveSecondPlayer();
	  	moveTableCardsToTrash();
	  	moveToNextPlayer();
	}
}

private void moveTableCardsToTrash() {
	// TODO Auto-generated method stub
	for(int i=0;i<tableCards.size();i++){
		tableCards.get(i).getImage().removeFromParent();
	}
	trashCards.addAll(tableCards);
	tableCards.clear();
}

private void firstPlayerAttack() {
	// TODO Auto-generated method stub
	currentPlayer = 0;
	servefirstPlayer();
  	serveSecondPlayer();
}


private void secondPlayerAttack() {
	// TODO Auto-generated method stub
	currentPlayer = 1;
	servefirstPlayer();
  	serveSecondPlayer();
}

private boolean gameInProgress() {
	// TODO Auto-generated method stub
	return true;
}

private void prepareGame() {
	//Window.alert("preparing game");
	//absolutePanel = new AbsolutePanel();
	RootPanel.get("rootItem").add(absolutePanel, 10, 10);
  	absolutePanel.setSize("1200px", "650px");
  	
  	absolutePanel.add(firstPlayerNextMoveButton,600,140);
  	secondPlayerNextMoveButton.setText("Next move");
  	absolutePanel.add(secondPlayerNextMoveButton, 600, 380);
  	firstPlayerNextMoveButton.addClickHandler(nextMoveClickHandler1);
  	secondPlayerNextMoveButton.addClickHandler(nextMoveClickHandler2);
  	
  	for (int i=0; i<36;i++){
  		int id   = (i/9 + 1) * 100 + i%9 + 6;
  		String src = "images/" + id + ".png";
  		cardPack.add(new Card(id, (i/9 + 1), src, new Image(src), 0));
  		//Window.alert(""+i);
  	}
  	(new Randoms(new Random())).shuffle(cardPack);
  	for (int i=0; i<cardPack.size();i++){
  		//Window.alert(cardPack.get(i).getSrcImage());
  		absolutePanel.add(cardPack.get(i).getImage(), 0 + i*5 , 120  + i*5);
  	}
  	
  	servefirstPlayer();
  	serveSecondPlayer();
  	mainKind = cardPack.get(0).getKind();
  	moveCard(cardPack.get(0).getImage(), 0,250);
  	
}

  private void serveSecondPlayer() {
	// TODO Auto-generated method stub
	  servePlayer(1);
  }

  private void servefirstPlayer() {
	// TODO Auto-generated method stub
	  servePlayer(0);
  }
  
  private void servePlayer(int playerNo){
	  for(int i=players[playerNo].size();i<6;i++){
			players[playerNo].add(cardPack.remove(cardPack.size()-1));
			//moveCard(players[playerNo].get(i).getImage(),200 + i*80, (playerNo==0)?10:417);
			players[playerNo].get(i).getImage().addMouseOverHandler(mouseOverHandler);
			players[playerNo].get(i).getImage().addMouseOutHandler(mouseOutHandler);
			players[playerNo].get(i).getImage().addClickHandler(clickHandler);
		}
	  repaintPlayerCards(playerNo);
  }
  
  private void repaintPlayerCards(int playerNo){
	  for(int i=0;i<players[playerNo].size();i++){
			moveCard(players[playerNo].get(i).getImage(),20 + i*80, (playerNo==0)?10+i/12*40:417+i/12*40);
	  }
  }
  
  private ClickHandler nextMoveClickHandler1 = new ClickHandler(){

	@Override
	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		handleNextMove(0);
	}
	  
  };

  private ClickHandler nextMoveClickHandler2 = new ClickHandler(){

	@Override
	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		handleNextMove(1);
	}
	  
  };
	  
  private void handleNextMove(int playerNo) {
		// TODO Auto-generated method stub
	    if(playerNo==currentPlayer){
		    if(tableCards.size()%2==0){
		    	moveTableCardsToTrash();
		    	moveToNextPlayer();
		    }else{
		    	moveTableCardsToCurrentPlayer();
		    }
		    servefirstPlayer();
	    	serveSecondPlayer();
	    	
	    }
  }
  private void moveTableCardsToCurrentPlayer() {
	// TODO Auto-generated method stub
		players[currentPlayer].addAll(tableCards);
		tableCards.clear();
}

private MouseOverHandler mouseOverHandler = new MouseOverHandler(){

		@Override
		public void onMouseOver(MouseOverEvent event) {
			// TODO Auto-generated method stub
			((Image)event.getSource()).setSize("80px", "120px");
		}
		
  };  
  
  private MouseOutHandler mouseOutHandler = new MouseOutHandler(){

		@Override
		public void onMouseOut(MouseOutEvent event) {
			// TODO Auto-generated method stub
			((Image)event.getSource()).setSize("", "");
		}
		
  };
  
  private ClickHandler clickHandler = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			playThisCard(event);
			
		}
	};

  
private void loadLogin() {
    // Assemble login panel.
    signInLink.setHref(loginInfo.getLoginUrl());
    loginPanel.add(loginLabel);
    loginPanel.add(signInLink);
    RootPanel.get("loginItem").add(loginPanel);
}



private void playThisCard(ClickEvent event) {
	Image image = (Image)event.getSource();
	
	//Window.alert(image.getUrl());
	String url = image.getUrl();
	url = url.substring(url.length()-14,url.length());
	//Window.alert("-"+url);
	//Window.alert(url);
	Card card = findCard(url);
	if (isCardFound(url)) {
		int size = tableCards.size()+1;
		if(size<=12){
			if(size%2==0 ){
				if(isValidSecondCard(card)){
					//Window.alert(card.getId()+"-"+size);
					playThisCard(image, size, card);
				}
			}else{
				if(isValidFirstCard(card)){
					//Window.alert(card.getId()+"-"+size);
					playThisCard(image, size, card);
				}
			}
			//Window.alert(card.getId()+"-"+size);
		}
		if(size==12){
			moveTableCardsToTrash();
			servefirstPlayer();
		  	serveSecondPlayer();
		  	moveToNextPlayer();
		}
		
		//Window.alert(card.getId()+"-"+size);
	}
	
}

private boolean isValidFirstCard(Card card) {
	// TODO Auto-generated method stub
	if (tableCards.size()==0){
		return true;
	}else{
		for(int i=0;i<tableCards.size();i++){
			if(tableCards.get(i).getPrice()==card.price){
				return true;
			}
		}
	}
	return false;
}

private void playThisCard(Image image, int size, Card card) {
	players[currentPlayer].remove(card);
	tableCards.add(card);
	moveCard(image,120 - size* size%2 * 10 + (size + 1) /2 * 90, 200 - size%2*10);
	moveToNextPlayer();
}

private boolean isValidSecondCard(Card card) {
	// TODO Auto-generated method stub
	//Window.alert(tableCards.get(tableCards.size()-1).price + "-" + card.price);
	if(card.price>tableCards.get(tableCards.size()-1).price){
		if(card.kind==tableCards.get(tableCards.size()-1).kind){
			return true;
		}
	}
	if((card.kind==mainKind) && (tableCards.get(tableCards.size()-1).kind!=mainKind)){
			return true;
	}
	return false;
}

private void moveToNextPlayer() {
	currentPlayer = currentPlayer==0?1:0;
}

private boolean isCardFound(String url) {
	// TODO Auto-generated method stub
	return players[currentPlayer].contains(findCard(url));	
}

private Card findCard(String url){
	for(int i=0;i<players[currentPlayer].size();i++){
		//Window.alert(players[currentPlayer].get(i).getSrcImage()+"-"+url);
		if (url.equals(players[currentPlayer].get(i).getSrcImage())){
			return players[currentPlayer].get(i);
		}
	}
 return null;
}

private void moveCard(Image image, int newX, int newY){
	//image.removeFromParent();
	//absolutePanel.add(image,newX, newY);
	CustomAnimation animation = new CustomAnimation(image.getElement());
    animation.scrollTo(newX, newY, 2000);

}
}