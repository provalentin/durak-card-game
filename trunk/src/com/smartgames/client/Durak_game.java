package com.smartgames.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.smartgames.client.StockPriceServiceAsync;
import com.smartgames.client.StockService;
import com.smartgames.client.StockServiceAsync;
import com.smartgames.client.StockPrice;
import com.smartgames.client.StockPriceService;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.AbsolutePanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

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
  //private AbsolutePanel absolutePanel = new AbsolutePanel();
  private VerticalPanel rightPanel = new VerticalPanel();
  private ArrayList<String> stocks = new ArrayList<String>();
  private StockPriceServiceAsync stockPriceSvc;
  private final StockServiceAsync stockService = GWT.create(StockService.class);
	
	
	
  private AbsolutePanel absolutePanel = new AbsolutePanel();
  private Button firstPlayerNextMoveButton = new Button("2");
  private Button secondPlayerNextMoveButton = new Button("2");
  
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
  private HorizontalPanel loginPanel = new HorizontalPanel();
  private Button loginButton = new Button("log in");
  
  private VerticalPanel tablesPanel = new VerticalPanel();
  private Label tablesLabel = new Label("Tables");
  private FlexTable tablesFlexTable = new FlexTable();
  private Button createNewTableButton = new Button("Create new table");
  private TextBox tableTextBox = new TextBox();
  
  private Label loginLabel = new Label("      your Google Account to access the Durak card game multiplayer mode  ");
  private Anchor signInLink = new Anchor("Sign in to");
  private Anchor signOutLink = new Anchor("Sign out");
  private int currentPlayer = 1;
  private int mainKind;
  private boolean isGameOver;

  public void onModuleLoad() {
	  prepareGame();  
//	  RootPanel.get("loginItem").add(loginButton);
	  loginButton.addClickHandler(loginButtonClickHandler); 
	  loginPanel.add(loginButton);
	  RootPanel.get("loginItem").add(loginPanel);
	  loadStockWatcher();
//	  addTablesPanel();
	  
  }

private void addTablesPanel() {
	// TODO Auto-generated method stub
	tablesPanel.add(tablesLabel);
	tablesPanel.add(tablesFlexTable);
	tablesPanel.add(tableTextBox);
	tablesPanel.add(createNewTableButton);
	RootPanel.get("loginItem").add(tablesPanel);
}

private void moveTableCardsToTrash() {
	// TODO Auto-generated method stub
	for(int i=0;i<tableCards.size();i++){
		tableCards.get(i).getImage().removeFromParent();
	}
	trashCards.addAll(tableCards);
	tableCards.clear();
}

private void prepareGame() {
	//absolutePanel = new AbsolutePanel();
	RootPanel rootPanel = RootPanel.get("rootItem");
	rootPanel.setSize("805", "660");
	rootPanel.add(absolutePanel, 10, 30);
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
    Window.alert("all cards = " + allCards); 
     
  	(new Randoms(new Random())).shuffle(cardPack);
  	for (int i=0; i<cardPack.size();i++){
  		//Window.alert(cardPack.get(i).getSrcImage());
  		absolutePanel.add(cardPack.get(i).getImage(), 0 + i*5 , 120  + i*5);
  	}
  	
  	servefirstPlayer();
  	serveSecondPlayer();
  	mainKind = cardPack.get(0).getKind();
  	moveCard(cardPack.get(0).getImage(), 0,250);
  	cardPack.get(0).getImage().setUrl(cardPack.get(0).getSrcImage());
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
		  		players[playerNo].add(cardPack.remove(cardPack.size()-1));
		  		players[playerNo].get(i).getImage().addMouseOverHandler(mouseOverHandler);
		  		players[playerNo].get(i).getImage().addMouseOutHandler(mouseOutHandler);
		  		players[playerNo].get(i).getImage().addClickHandler(clickHandler);
		  		//if(playerNo==1) {
		  			players[playerNo].get(i).getImage().setUrl(players[playerNo].get(i).getSrcImage());
		  		//}
		  	} 	
		}
	  repaintPlayerCards(playerNo);
  }
  
  private void repaintPlayerCards(int playerNo){
	  if(playerNo==1){
		  for(int i=0;i<secondPlayerFocusPanels.size();i++){
			  secondPlayerFocusPanels.get(i).removeFromParent();
		  }
	  }
	  for(int i=0;i<players[playerNo].size();i++){
		    int maxCardInRow = 6;
		    if(playerNo==1){
	               //players[playerNo].get(i).getImage().removeFromParent();
	               FocusPanel focusPanel = new FocusPanel();
	               focusPanel.setSize("75px", "105px");
	               //focusPanel.add(players[playerNo].get(i).getImage());
	               focusPanel.addKeyDownHandler(clickFocusHandler);
	               focusPanel.addClickHandler(clickFocusPanelHandler);
	               absolutePanel.add(focusPanel,20 + i%6*80,417+i/6*40 );
	               secondPlayerFocusPanels.add(focusPanel);
	        }   
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
	
private ClickHandler loginButtonClickHandler = new ClickHandler() {
	@Override
	public void onClick(ClickEvent event) {
		// Check login status using login service.
	    LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	      }

	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        loadLogin();
	        if(loginInfo.isLoggedIn()) {
	        	//Window.alert("now we can start the game");
	        	//prepareGame();
	        	//startGame();
	        	//Window.alert("already loged in");
	        	signOutLink.setVisible(true);
	        	signInLink.setVisible(false);
	        	loginLabel.setVisible(false);
	        } else {
	        	loginLabel.setVisible(true);
	        	signOutLink.setVisible(false);
	        	signInLink.setVisible(true);
	        	//Window.alert("Please log in before game starts");
	        }
	      }
	    });
	}
};
  
private void loadLogin() {
    // Assemble login panel.
    signInLink.setHref(loginInfo.getLoginUrl());
    signOutLink.setHref(loginInfo.getLogoutUrl());
    loginPanel.add(signInLink);
    loginPanel.add(signOutLink);
    loginPanel.add(loginLabel);
    signInLink.setVisible(false);
    signOutLink.setVisible(false);
//    RootPanel.get("loginItem").add(loginPanel);
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
		if(currentPlayer == 0) {
			makeComputerNextMove();
		}
		repaintPlayerCards(1);
		repaintPlayerCards(0);
	}
	
}

private void makeComputerNextMove() {
	// TODO Auto-generated method stub
	Random random = new Random();
	for(int i=0;i<players[0].size()*2 ;i++){
		int number = random.nextInt(players[0].size());
		//Window.alert("#"+number);
		if(playThisCard(players[0].get(number).getImage())) return;
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

private void loadStockWatcher() {
	// Set up sign out hyperlink.
    //signOutLink.setHref(loginInfo.getLogoutUrl());
    
	// Create table for stock data.
    stocksFlexTable.setText(0, 0, "Symbol");
    stocksFlexTable.setText(0, 1, "Price");
    stocksFlexTable.setText(0, 2, "Change");
    stocksFlexTable.setText(0, 3, "Remove");
    stocksFlexTable.setText(0, 4, "History");

    // Add styles to elements in the stock list table.
    stocksFlexTable.setCellPadding(6);
    stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
    stocksFlexTable.addStyleName("watchList");
    stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");

    loadStocks();

    // Assemble Add Stock panel.
    addPanel.add(newSymbolTextBox);
    addPanel.add(addStockButton);
    addPanel.addStyleName("addPanel");

    // Assemble Main panel.
    mainPanel.add(signOutLink);
    mainPanel.add(stocksFlexTable);
    stocksFlexTable.setWidth("385px");
    
    Button btnNewButton = new Button("New button");
    mainPanel.add(btnNewButton);
    mainPanel.add(addPanel);
    mainPanel.add(lastUpdatedLabel);
    
    //rightPanel.add(horizontalPanel);
    //rightPanel.add(absolutePanel);
    
    rootPanel.add(mainPanel);
    rootPanel.add(rightPanel);
   
    // Associate the Main panel with the HTML host page.
    RootPanel.get("rootItem").add(rootPanel);

    // Move cursor focus to the input box.
    newSymbolTextBox.setFocus(true);

    // Setup timer to refresh list automatically.
    Timer refreshTimer = new Timer() {
      @Override
      public void run() {
        refreshWatchList();
      }
    };
    refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

    // Listen for mouse events on the Add button.
    addStockButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        addStock();
      }
    });

    // Listen for keyboard events in the input box.
    newSymbolTextBox.addKeyPressHandler(new KeyPressHandler() {
      public void onKeyPress(KeyPressEvent event) {
        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
          addStock();
        }
      }
    });
}

  private void loadStocks() {
    stockService.getStocks(new AsyncCallback<String[]>() {
      public void onFailure(Throwable error) {
      }
      public void onSuccess(String[] symbols) {
        displayStocks(symbols);
      }
    });
  }

  private void displayStocks(String[] symbols) {
    for (String symbol : symbols) {
      displayStock(symbol);
    }
  }

  /**
   * Add stock to FlexTable. Executed when the user clicks the addStockButton or
   * presses enter in the newSymbolTextBox.
   */
  private void addStock() {
	    final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
	    newSymbolTextBox.setFocus(true);

	    // Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
	    if (!symbol.matches("^[0-9a-zA-Z\\.]{1,10}$")) {
	      Window.alert("'" + symbol + "' is not a valid symbol.");
	      newSymbolTextBox.selectAll();
	      return;
	    }

	    newSymbolTextBox.setText("");

	    // Don't add the stock if it's already in the table.
	    if (stocks.contains(symbol))
	      return;

	    addStock(symbol);
  }

  private void addStock(final String symbol) {
    stockService.addStock(symbol, new AsyncCallback<Void>() {
      public void onFailure(Throwable error) {
      }
      public void onSuccess(Void ignore) {
        displayStock(symbol);
      }
    });
  }
  private void displayStock(final String symbol) {
	    // Add the stock to the table.
	    int row = stocksFlexTable.getRowCount();
	    stocks.add(symbol);
	    stocksFlexTable.setText(row, 0, symbol);
	    stocksFlexTable.setWidget(row, 2, new Label());
	    stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");

	 // Add a button to remove this stock from the table.
	    Button removeStock = new Button("x");
	    removeStock.addStyleDependentName("remove");

	    removeStock.addClickHandler(new ClickHandler(){
	      public void onClick(ClickEvent event) {
	        removeStock(symbol);
	      }
	    });
	    stocksFlexTable.setWidget(row, 3, removeStock);

	    // Get the stock price.
	    refreshWatchList();

	  }

	  private void removeStock(final String symbol) {
	    stockService.removeStock(symbol, new AsyncCallback<Void>() {
	      public void onFailure(Throwable error) {
	      }
	      public void onSuccess(Void ignore) {
	        undisplayStock(symbol);
	      }
	    });
	  }

	  private void undisplayStock(String symbol) {
	    int removedIndex = stocks.indexOf(symbol);
	    stocks.remove(removedIndex);
	    stocksFlexTable.removeRow(removedIndex+1);
	  }
  protected void getStockHistory() {
	// TODO Auto-generated method stub
	
}

  /**
   * Generate random stock prices.
   */
  private void refreshWatchList() {
//    final double MAX_PRICE = 100.0; // $100.00
//    final double MAX_PRICE_CHANGE = 0.02; // +/- 2%
//
//    StockPrice[] prices = new StockPrice[stocks.size()];
//    for (int i = 0; i < stocks.size(); i++) {
//      double price = Random.nextDouble() * MAX_PRICE;
//      double change = price * MAX_PRICE_CHANGE
//          * (Random.nextDouble() * 2.0 - 1.0);
//
//      prices[i] = new StockPrice(stocks.get(i), price, change);
//    }
//
//    updateTable(prices);
	// Initialize the service proxy.
    if (stockPriceSvc == null) {
      stockPriceSvc = GWT.create(StockPriceService.class);
    }

    // Set up the callback object.
    AsyncCallback<StockPrice[]> callback = new AsyncCallback<StockPrice[]>() {
      public void onFailure(Throwable caught) {
        // TODO: Do something with errors.
      }

      public void onSuccess(StockPrice[] result) {
        updateTable(result);
      }
    };

    // Make the call to the stock price service.
    stockPriceSvc.getPrices(stocks.toArray(new String[0]), callback);
	  
  }

  /**
   * Update the Price and Change fields all the rows in the stock table.
   *
   * @param prices Stock data for all rows.
   */
  private void updateTable(StockPrice[] prices) {
    for (int i = 0; i < prices.length; i++) {
      updateTable(prices[i]);
    }

    // Display timestamp showing last refresh.
    lastUpdatedLabel.setText("Last update : "
        + DateTimeFormat.getMediumDateTimeFormat().format(new Date()) + 
        "\nPrices: " + Arrays.toString(prices));

  }

  /**
   * Update a single row in the stock table.
   *
   * @param price Stock data for a single row.
   */
  private void updateTable(StockPrice price) {
    // Make sure the stock is still in the stock table.
    if (!stocks.contains(price.getSymbol())) {
      return;
    }

    int row = stocks.indexOf(price.getSymbol()) + 1;

    // Format the data in the Price and Change fields.
    String priceText = NumberFormat.getFormat("#,##0.00").format(
        price.getPrice());
    NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
    String changeText = changeFormat.format(price.getChange());
    String changePercentText = changeFormat.format(price.getChangePercent());

    // Populate the Price and Change fields with new data.
    stocksFlexTable.setText(row, 1, priceText);
    Label changeWidget = (Label)stocksFlexTable.getWidget(row, 2);
    changeWidget.setText(changeText + " (" + changePercentText + "%)");

    // Change the color of text in the Change field based on its value.
    String changeStyleName = "noChange";
    if (price.getChangePercent() < -0.1f) {
      changeStyleName = "negativeChange";
    }
    else if (price.getChangePercent() > 0.1f) {
      changeStyleName = "positiveChange";
    }

    changeWidget.setStyleName(changeStyleName);
  }
}