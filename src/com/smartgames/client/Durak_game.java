package com.smartgames.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.AbsolutePanel;

import java.util.ArrayList;
import java.util.Random;

import com.smartgames.client.State;

public class Durak_game implements EntryPoint {

  private AbsolutePanel absolutePanel = new AbsolutePanel();
  private Button firstPlayerNextMoveButton = new Button("2");
  private Button secondPlayerNextMoveButton = new Button("2");
  private Button loginButton = new Button("log in");
  private Button saveGameStateButton = new Button("save game state");
  private Button loadGameStateButton = new Button("load game state");
  private ArrayList<Card> allCards = new ArrayList<Card>();
  private ArrayList<Card> tableCards = new ArrayList<Card>();
  private ArrayList<Card> cardPack = new ArrayList<Card>();
  private ArrayList<Card> trashCards = new ArrayList<Card>();
  private ArrayList<Card> firstPlayerCards = new ArrayList<Card>();
  private ArrayList<Card> secondPlayerCards = new ArrayList<Card>();
  private ArrayList<FocusPanel> secondPlayerFocusPanels = new ArrayList<FocusPanel>();
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
  private boolean isGameOver;
  
  private final StockServiceAsync stockService = GWT.create(StockService.class);

  public void onModuleLoad() {
	prepareGame();  
  }

  private void moveTableCardsToTrash() {
	// TODO Auto-generated method stub
	for(int i=0;i<tableCards.size();i++){
		tableCards.get(i).setState(State.played);
		tableCards.get(i).getImage().removeFromParent();
	}
	trashCards.addAll(tableCards);
	tableCards.clear();
  }

  private void prepareGame() {
	//absolutePanel = new AbsolutePanel();
	RootPanel rootPanel = RootPanel.get("rootItem");
	rootPanel.setSize("805", "660");
	rootPanel.add(loginButton,800,10);
	rootPanel.add(saveGameStateButton, 800, 150);
	rootPanel.add(loadGameStateButton, 800, 200);
	loginButton.addClickHandler(loginButtonClickHandler);
	saveGameStateButton.addClickHandler(showAllCardsStateClickHandler);
	loadGameStateButton.addClickHandler(loadGameStateButtonClickHandler);
	rootPanel.add(absolutePanel, 10, 10);
  	absolutePanel.setSize("800px", "650px");
  	
  	absolutePanel.add(firstPlayerNextMoveButton,600,260);
  	firstPlayerNextMoveButton.setText("Next move");
  	firstPlayerNextMoveButton.addClickHandler(nextMoveClickHandler);
  	
  	for (int i=0; i<36;i++){
  		int id   = (i/9 + 1) * 100 + i%9 + 6;
  		String src = "images/" + id + ".png";
  		cardPack.add(new Card(id, (i/9 + 1), src, new Image("images/0.png"), 0));
  		//Window.alert(""+i);
  	}
     
    allCards.addAll(cardPack);
//    Window.alert("all cards = " + allCards); 
     
  	//(new Randoms(new Random())).shuffle(cardPack);
  	for (int i=0; i<cardPack.size();i++){
  		//Window.alert(cardPack.get(i).getSrcImage());
  		absolutePanel.add(cardPack.get(i).getImage(), 0 + i*5 , 120  + i*5);
  	}
//  	allCards.addAll(cardPack);
  	
  	
  	servefirstPlayer();
  	serveSecondPlayer();
  	mainKind = cardPack.get(0).getKind();
  	moveCard(cardPack.get(0).getImage(), 0,250);
  	cardPack.get(0).getImage().setUrl(cardPack.get(0).getSrcImage());
  	
  	
 // Setup timer to refresh list automatically.
    Timer refreshTimer = new Timer() {
      @Override
      public void run() {
//        loadGameState();
    	  makeComputerNextMove();
      }
    };
    refreshTimer.scheduleRepeating(2000);
  }

  private void repaintTable(){
	//repaint card pack here
	repaintCardPack();  
	repaintPlayerCards(0);
	repaintPlayerCards(1);
	repaintTableCards();
	hideTrashCardsFromTable();
  }
  
  private void hideTrashCardsFromTable(){
	  //hiding trash cards from table
	  for(int i=0;i<trashCards.size();i++){
		  trashCards.get(i).getImage().removeFromParent();
	  }
  }
  
  private void repaintTableCards(){
	  //repainting table cards
	  for(int i=0;i<tableCards.size();i++){
		  Image image = tableCards.get(i).getImage();
		  int size = i+1;	
		  moveCard(image,120 - size* size%2 * 10 + (size + 1) /2 * 90, 200 - size%2*10);  
	  }
  }
  
  private void repaintCardPack(){
	  for(int i=0;i<cardPack.size();i++){
		  cardPack.get(i).getImage().removeFromParent();
	  }
	  for (int i=0; i<allCards.size();i++){
	  		//Window.alert(cardPack.get(i).getSrcImage());
	  		absolutePanel.add(allCards.get(i).getImage(), 0 + i*5 , 120  + i*5);
	  } 
	  if (cardPack.size()>0){
		  mainKind = cardPack.get(0).getKind();
	  	  moveCard(cardPack.get(0).getImage(), 0,250);
		  cardPack.get(0).getImage().setUrl(cardPack.get(0).getSrcImage());
	  }
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
		  	if(cardPack.size()>0){
		  		cardPack.get(cardPack.size()-1).setState(playerNo + 3);
		  		players[playerNo].add(cardPack.remove(cardPack.size()-1));
		  		players[playerNo].get(i).getImage().addMouseOverHandler(mouseOverHandler);
		  		players[playerNo].get(i).getImage().addMouseOutHandler(mouseOutHandler);
		  		players[playerNo].get(i).getImage().addClickHandler(clickHandler);
		  		//if(playerNo==1) {
		  			//players[playerNo].get(i).getImage().setUrl(players[playerNo].get(i).getSrcImage());
		  		//}
		  	} 	
		}
	  repaintPlayerCards(playerNo);
  }
  
  private void repaintPlayerCards(int playerNo){
	  //adding focus panel for images for second player
	  //it helps us arrows to navigate and play not only mouse
	  //it works for tv where we don't have mouse and clicks
	  
//	  if(playerNo==1){
//		  for(int i=0;i<secondPlayerFocusPanels.size();i++){
//			  secondPlayerFocusPanels.get(i).removeFromParent();
//		  }
//	  }
	  for(int i=0;i<players[playerNo].size();i++){
		    int maxCardInRow = 6;
//		    if(playerNo==1){
//	               //players[playerNo].get(i).getImage().removeFromParent();
//	               FocusPanel focusPanel = new FocusPanel();
//	               focusPanel.setSize("75px", "105px");
//	               //focusPanel.add(players[playerNo].get(i).getImage());
//	               focusPanel.addKeyDownHandler(clickFocusHandler);
//	               focusPanel.addClickHandler(clickFocusPanelHandler);
//	               absolutePanel.add(focusPanel,20 + i%6*80,417+i/6*40 );
//	               secondPlayerFocusPanels.add(focusPanel);
//	        }   
		    players[playerNo].get(i).getImage().setUrl(players[playerNo].get(i).getSrcImage());
			moveCard(players[playerNo].get(i).getImage(),20 + i%maxCardInRow*80, 
					(playerNo==0)?10+i/maxCardInRow*40:417+i/maxCardInRow*40);
	  }
  }
  
  private ClickHandler nextMoveClickHandler = new ClickHandler(){

	@Override
	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		handleNextMove();
	}
	  
  };
  

  private ClickHandler loginButtonClickHandler = new ClickHandler(){
	  @Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
		  
		//Check login status using login service.
	    LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	    	  Window.alert("login request failed");
	      }

	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	          //Window.alert("now we can start the game");
	          //prepareGame();
	          //startGame();
        	  signInLink.setVisible(true);
	          signOutLink.setVisible(true);
	        } else {
	          loadLogin();
	          signInLink.setVisible(true);
	          signOutLink.setVisible(true);
	          Window.alert("Please log in before game starts");
	        }
	      }
	    });
		}
  };

  
  private ClickHandler showAllCardsStateClickHandler = new ClickHandler(){
	  @Override
		public void onClick(ClickEvent event) {
		  	addStock(allCardsStateToString());
		  	Window.alert(allCardsStateToString());
	  }
  };
  
  
  
  private ClickHandler loadGameStateButtonClickHandler = new ClickHandler(){
	  @Override
		public void onClick(ClickEvent event) {
		  loadGameState();
		  	
//		  	Window.alert("restoring game state");
	  }
  };
  
  private void loadGameState(){
	  stockService.getStocks(new AsyncCallback<String>() {
	        public void onFailure(Throwable error) {
	    	  Window.alert("getStock request failed");
	        }
			  
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
//				Window.alert(result);
				stringToAllCardsState(result);
//				Window.alert("refilling card arrays");
				refillCardArrays();
//				Window.alert("repainting table");
				repaintTable();
//				Window.alert("finished loading game state");
			}
	    });
  }
  
  private void refillCardArrays(){
	  cardPack.clear();
	  tableCards.clear();
	  players[0].clear();
	  players[1].clear();
	  trashCards.clear();
	  for(int i=0;i<allCards.size();i++){
		 switch(allCards.get(i).getState()){
			 case 0:cardPack.add(allCards.get(i));break;
			 case 1:tableCards.add(allCards.get(i));break;
			 case 2:trashCards.add(allCards.get(i));break;
			 case 3:players[0].add(allCards.get(i));break;
			 case 4:players[1].add(allCards.get(i));break;
		 }
	  }
  }
  
  private void addStock(final String symbol) {
	    stockService.addStock(symbol, new AsyncCallback<Void>() {
	      public void onFailure(Throwable error) {
	    	Window.alert("addStock request failed");
	      }
	      public void onSuccess(Void ignore) {
	        //Window.alert(symbol + " saved");
	      }
	    });
	  }
  
  private String allCardsStateToString(){
	  String allCardsString = "";
	  	for(int i=0;i<allCards.size();i++){
	  		allCardsString += allCards.get(i).getId() +""+allCards.get(i).getState();
	  	}
	  	allCardsString += (currentPlayer+"");
	  return allCardsString;
  };
  
  private void stringToAllCardsState(String savedString){
	  for(int i=0;i<allCards.size();i++){
	  		allCards.get(i).setState(Integer.parseInt(savedString.charAt(4*i+3)+""));
	  	}
	  currentPlayer = Integer.parseInt(savedString.charAt(allCards.size()*4)+"");
	  
	  //Window.alert("new state is " + allCardsStateToString());
	  return;
  };
  
  private KeyDownHandler clickFocusHandler = new KeyDownHandler() {
      @Override               
      public void onKeyDown(KeyDownEvent event) {                     
    	  // TODO Auto-generated methodstub                    
    	  //Window.alert("Paff"+event.getNativeKeyCode());                        
    	  if(event.getNativeKeyCode()==13){ 
    		  FocusPanel focusPanel = (FocusPanel)event.getSource();
    		  playThisCard(players[1].get(calculateIndex(focusPanel)).getImage());
    	  } 
      }
  };
  
  private ClickHandler clickFocusPanelHandler = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			FocusPanel focusPanel = (FocusPanel)event.getSource();
  		  	playThisCard(players[1].get(calculateIndex(focusPanel)).getImage());
			
		}
	};
  
  private void moveTableCardsToCurrentPlayer() {
	  	// TODO Auto-generated method stub
	  	for(int i=0;i<tableCards.size();i++){
	  		tableCards.get(i).setState(currentPlayer+3);
	  	}
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
			playThisCard((Image)event.getSource());
			
		}
	};
	
  
private void loadLogin() {
    // Assemble login panel.
    signInLink.setHref(loginInfo.getLoginUrl());
    signOutLink.setHref(loginInfo.getLogoutUrl());
    loginPanel.add(loginLabel);
    loginPanel.add(signInLink);
    loginPanel.add(signOutLink);
    RootPanel.get("loginItem").add(loginPanel,800,50);
}

private boolean playThisCard(Image image) {
	String url = image.getUrl();
	url = url.substring(url.length()-14,url.length());
	Card card = findCard(url);
	if (isCardFound(url)) {
		int size = tableCards.size()+1;
		if(size<=12){
			if(size%2==0 ){
				if(isValidSecondCard(card)){
					moveThisCard(image, size, card);
					return true;
				}
			}else{
				if(isValidFirstCard(card)){
					moveThisCard(image, size, card);
					return true;
				}
			}
		}
		if(size==12){
			moveTableCardsToTrash();
			servefirstPlayer();
		  	serveSecondPlayer();
		  	moveToNextPlayer();
		}
	}
	return false;
}

private boolean isValidFirstCard(Card card) {
	// TODO Auto-generated method stub
	if (tableCards.size()==0){
		return true;
	}else{
		for(int i=0;i<tableCards.size();i++){
			if(tableCards.get(i).getPrice()==card.getPrice()){
				return true;
			}
		}
	}
	return false;
}

private void moveThisCard(Image image, int size, Card card) {
	players[currentPlayer].remove(card);
	tableCards.add(card);
	card.setState(State.onTable);
	moveCard(image,120 - size* size%2 * 10 + (size + 1) /2 * 90, 200 - size%2*10);
	moveToNextPlayer();
}

private boolean isValidSecondCard(Card card) {
	// TODO Auto-generated method stub
	//Window.alert(tableCards.get(tableCards.size()-1).price + "-" + card.price);
	if(card.getPrice()>tableCards.get(tableCards.size()-1).getPrice()){
		if(card.getKind()==tableCards.get(tableCards.size()-1).getKind()){
			return true;
		}
	}
	if((card.getKind()==mainKind) && (tableCards.get(tableCards.size()-1).getKind()!=mainKind)){
			return true;
	}
	return false;
}

private void moveToNextPlayer() {
	//Window.alert("#moveToNextPlayer-current player" + currentPlayer);
	if(cardPack.size()==0) {
		if(firstPlayerCards.size()==0 && secondPlayerCards.size()==0){
			Window.alert("Equal");
			isGameOver = true;
		}
		if(firstPlayerCards.size()==0){
			Window.alert("You lose :-(");
			isGameOver = true;
		}
		if(secondPlayerCards.size()==0){
			Window.alert("You win :-)");
			isGameOver = true;
		}
	}
	if(!isGameOver){
		
		currentPlayer = currentPlayer==0?1:0;
		addStock(allCardsStateToString());
//		if(currentPlayer == 0) {
//			makeComputerNextMove();
//		}
		repaintPlayerCards(1);
		repaintPlayerCards(0);
	}
	
}

private void makeComputerNextMove() {
	// TODO Auto-generated method stub
	Random random = new Random();
	for(int i=0;i<players[currentPlayer].size()*2 ;i++){
		int number = random.nextInt(players[currentPlayer].size());
		//Window.alert("#"+number);
		if(playThisCard(players[currentPlayer].get(number).getImage())) return;
	}
	handleNextMove();
	
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
    animation.scrollTo(newX, newY, 500);
}

private void handleNextMove() {
	//Window.alert("#handeNextMove-current player" + currentPlayer);
	if(tableCards.size()%2==0){
		moveTableCardsToTrash();
	}else{
		moveTableCardsToCurrentPlayer();
	}
	servefirstPlayer();
	serveSecondPlayer();
	moveToNextPlayer();
}

private int calculateIndex(FocusPanel focusPanel) {
	int leftIndex = focusPanel.getAbsoluteLeft() / 80;
	int topIndex = (focusPanel.getAbsoluteTop() - 427) / 40;
	return leftIndex + 6 * topIndex;
}
}