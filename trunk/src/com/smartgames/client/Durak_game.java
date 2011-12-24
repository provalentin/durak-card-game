package com.smartgames.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Durak_game implements EntryPoint {

  private static final int REFRESH_INTERVAL = 5000; // ms
  private VerticalPanel mainPanel = new VerticalPanel();
  private FlexTable stocksFlexTable = new FlexTable();
  private HorizontalPanel addPanel = new HorizontalPanel();
  private TextBox newSymbolTextBox = new TextBox();
  private Button addStockButton = new Button("Add");
  private Label lastUpdatedLabel = new Label();
  private HorizontalPanel horizontalPanel = new HorizontalPanel();
  private HorizontalPanel rootPanel = new HorizontalPanel();
  private AbsolutePanel absolutePanel = new AbsolutePanel();
  private VerticalPanel rightPanel = new VerticalPanel();
  private ArrayList<String> stocks = new ArrayList<String>();
  
  private Image[] firstPlayerCardsImages;
  private Image[] secondPlayerCardsImages;
  private Image[] cardPackImages;
  private ArrayList<Image> tableCardsImages = new ArrayList<Image>();
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

  public void onModuleLoad() {
	  //for nice visual
	  //prepareGame();
	  
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
		trashCards.add(tableCards.remove(i));
	}
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
  	absolutePanel.setSize("902px", "550px");
  	
  	for (int i=0; i<36;i++){
  		int id   = (i/9 + 1) * 100 + i%9 + 6;
  		String src = "images/" + id + ".png";
  		cardPack.add(new Card(id, (i/9 + 1), src, new Image(src), 0));
  		//Window.alert(""+i);
  	}
  	for (int i=0; i<cardPack.size();i++){
  		//Window.alert(cardPack.get(i).getSrcImage());
  		absolutePanel.add(cardPack.get(i).getImage(), 0 + i*5 , 120  + i*5);
  	}
  	
  	servefirstPlayer();
  	serveSecondPlayer();
  	
//  	firstPlayerCardsImages = new Image[]{image, image_1, image_2, image_3, 
//  			image_4, image_5, image_6, image_7, image_8, image_9};
//  	
//  	secondPlayerCardsImages = new Image[]{image_10, image_11, image_12, 
//  			image_13, image_14, image_15, image_16, image_17, image_18, image_19};
//  	for(int i=0; i< firstPlayerCardsImages.length;i++){
//  		//absolutePanel.add(firstPlayerCards[i], i * 70, 10);
//  		firstPlayerCardsImages[i].addMouseOverHandler(mouseOverHandler);
//  		firstPlayerCardsImages[i].addMouseOutHandler(mouseOutHandler);
//  		firstPlayerCardsImages[i].addClickHandler(clickHandler);
//  		//firstPlayerCards[i].setUrl("images/0.png");
//  		//absolutePanel.add(secondPlayerCards[i], i * 70, 417);
//  		secondPlayerCardsImages[i].addMouseOverHandler(mouseOverHandler);
//  		secondPlayerCardsImages[i].addMouseOutHandler(mouseOutHandler);
//  		secondPlayerCardsImages[i].addClickHandler(clickHandler);
//  	}
}

  private void serveSecondPlayer() {
	// TODO Auto-generated method stub
	  for(int i=players[1].size();i<6;i++){
		    players[1].add(cardPack.remove(cardPack.size()-1));
			moveCard(players[1].get(i).getImage(), 200 +i*80, 10);
			players[1].get(i).getImage().addMouseOverHandler(mouseOverHandler);
			players[1].get(i).getImage().addMouseOutHandler(mouseOutHandler);
			players[1].get(i).getImage().addClickHandler(clickHandler);
		}
  }

  private void servefirstPlayer() {
	// TODO Auto-generated method stub
	for(int i=players[0].size();i<6;i++){
		players[0].add(cardPack.remove(cardPack.size()-1));
		moveCard(players[0].get(i).getImage(),200 + i*80, 417);
		players[0].get(i).getImage().addMouseOverHandler(mouseOverHandler);
		players[0].get(i).getImage().addMouseOutHandler(mouseOutHandler);
		players[0].get(i).getImage().addClickHandler(clickHandler);
	}
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
			//Window.alert("image was moved to center");
			//((Image)event.getSource()).setVisibleRect(100, 15, 200, 200);
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
	Window.alert(url);
	if (isCardFound(url)) {
		if(tableCards.size()<=12){
			image.removeFromParent();
			int size = tableCards.size();
			absolutePanel.add(image,100 - size* size%2 * 10 + (size + 1) /2 * 90, 200 - size%2*10);
		}
		if(tableCards.size()==12){
			moveTableCardsToTrash();
			servefirstPlayer();
		  	serveSecondPlayer();
		}
		moveToNextPlayer();
	}
	
}

private void moveToNextPlayer() {
	currentPlayer = currentPlayer==0?1:0;
}

private boolean isCardFound(String url) {
	// TODO Auto-generated method stub
		for(int i=0;i<players[currentPlayer].size();i++){
			if (url.equals(players[currentPlayer].get(i).getSrcImage())){
				tableCards.add(players[currentPlayer].remove(i));
				//Window.alert(url);
				return true;
			}
		}
	return false;
}

private void moveCard(Image image, int newX, int newY){
	image.removeFromParent();
	//tableCardsImages.add(image);
	absolutePanel.add(image,newX, newY);

}
}