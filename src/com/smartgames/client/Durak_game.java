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
  
  private Image[] firstPlayerCards;
  private Image[] secondPlayerCards;
  private Image[] cardPack;
  private ArrayList<Image> tableCards = new ArrayList<Image>();
  
  /**
   * Entry point method.
   */
  private LoginInfo loginInfo = null;
  private VerticalPanel loginPanel = new VerticalPanel();
  private Label loginLabel = new Label("Please sign in to your Google Account to access the StockWatcher application.");
  private Anchor signInLink = new Anchor("Sign In");
  private Anchor signOutLink = new Anchor("Sign Out");

  public void onModuleLoad() {
	//absolutePanel = new AbsolutePanel();
  	rightPanel.add(absolutePanel);
  	absolutePanel.setSize("902px", "527px");
  	
  	Image image_2 = new Image("images/106.png");
  	absolutePanel.add(image_2, 10, 10);
  	
  	Image image_3 = new Image("images/107.png");
  	absolutePanel.add(image_3, 76, 10);
  	
  	Image image_4 = new Image("images/108.png");
  	absolutePanel.add(image_4, 142, 10);
  	
  	Image image_5 = new Image("images/109.png");
  	absolutePanel.add(image_5, 208, 10);
  	
  	Image image_1 = new Image("images/110.png");
  	absolutePanel.add(image_1, 274, 10);
  	
  	Image image_6 = new Image("images/111.png");
  	absolutePanel.add(image_6, 340, 10);
  	
  	Image image_7 = new Image("images/112.png");
  	absolutePanel.add(image_7, 406, 10);
  	
  	Image image_8 = new Image("images/113.png");
  	absolutePanel.add(image_8, 472, 10);
  	
  	Image image = new Image("images/114.png");
  	absolutePanel.add(image, 539, 10);
  	
  	Image image_9 = new Image("images/102.png");
  	absolutePanel.add(image_9, 605, 10);
  	
  	Image image_10 = new Image("images/202.png");
  	absolutePanel.add(image_10, 10, 417);
  	
  	Image image_11 = new Image("images/203.png");
  	absolutePanel.add(image_11, 76, 417);
  	
  	Image image_12 = new Image("images/204.png");
  	absolutePanel.add(image_12, 142, 417);
  	
  	Image image_13 = new Image("images/205.png");
  	absolutePanel.add(image_13, 208, 417);
  	
  	Image image_14 = new Image("images/206.png");
  	absolutePanel.add(image_14, 274, 417);
  	
  	Image image_15 = new Image("images/207.png");
  	absolutePanel.add(image_15, 340, 417);
  	
  	Image image_16 = new Image("images/208.png");
  	absolutePanel.add(image_16, 406, 417);
  	
  	Image image_17 = new Image("images/209.png");
  	absolutePanel.add(image_17, 472, 417);
  	
  	Image image_18 = new Image("images/210.png");
  	absolutePanel.add(image_18, 539, 417);
  	
  	Image image_19 = new Image("images/211.png");
  	absolutePanel.add(image_19, 605, 417);
  	
  	Image image_20 = new Image("images/0.png");
  	absolutePanel.add(image_20, 10, 190);
  	
  	Image image_21 = new Image("images/0.png");
  	absolutePanel.add(image_21, 15, 195);
  	
  	Image image_22 = new Image("images/0.png");
  	absolutePanel.add(image_22, 20, 200);
  	
  	Image image_23 = new Image("images/0.png");
  	absolutePanel.add(image_23, 25, 205);
  	
  	Image image_24 = new Image("images/0.png");
  	absolutePanel.add(image_24, 30, 210);
  	
  	Image image_25 = new Image("images/0.png");
  	absolutePanel.add(image_25, 35, 215);
  	
  	Image image_26 = new Image("images/0.png");
  	absolutePanel.add(image_26, 40, 220);
  	
  	Image image_27 = new Image("images/0.png");
  	absolutePanel.add(image_27, 45, 225);
  	
  	Image image_28 = new Image("images/0.png");
  	absolutePanel.add(image_28, 50, 230);
  	
  	Image image_29 = new Image("images/0.png");
  	absolutePanel.add(image_29, 55, 235);
  	
  	Image image_30 = new Image("images/0.png");
  	absolutePanel.add(image_30, 60, 240);
  	
  	Image image_31 = new Image("images/0.png");
  	absolutePanel.add(image_31, 65, 245);

  	image_2.addMouseOverHandler(mouseOverHandler);
  	image_2.addMouseOutHandler(mouseOutHandler);
  	image.addClickHandler(clickHandler);
  	cardPack = new Image[]{image_20, image_21, image_22, 
  			image_23, image_24, image_25, image_26, image_27, 
  			image_28, image_29, image_30, image_31};
  	for (int i=0;i<cardPack.length;i++){
//  		absolutePanel.add(cardPack[i], i * 5, 180 + i * 5);
  	}
  	firstPlayerCards = new Image[]{image, image_1, image_2, image_3, 
  			image_4, image_5, image_6, image_7, image_8, image_9};
  	secondPlayerCards = new Image[]{image_10, image_11, image_12, 
  			image_13, image_14, image_15, image_16, image_17, image_18, image_19};
  	for(int i=0; i< firstPlayerCards.length;i++){
  		//absolutePanel.add(firstPlayerCards[i], i * 70, 10);
  		firstPlayerCards[i].addMouseOverHandler(mouseOverHandler);
  		firstPlayerCards[i].addMouseOutHandler(mouseOutHandler);
  		firstPlayerCards[i].addClickHandler(clickHandler);
  		firstPlayerCards[i].setUrl("images/0.png");
  		//absolutePanel.add(secondPlayerCards[i], i * 70, 417);
  		secondPlayerCards[i].addMouseOverHandler(mouseOverHandler);
  		secondPlayerCards[i].addMouseOutHandler(mouseOutHandler);
  		secondPlayerCards[i].addClickHandler(clickHandler);
  	}
  	
  	
    // Check login status using login service.
    LoginServiceAsync loginService = GWT.create(LoginService.class);
    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
      public void onFailure(Throwable error) {
      }

      public void onSuccess(LoginInfo result) {
        loginInfo = result;
        if(loginInfo.isLoggedIn()) {
          Window.alert("now we can start the game");
        } else {
          loadLogin();
        }
      }
    });
  }

  private MouseOverHandler mouseOverHandler = new MouseOverHandler(){

		@Override
		public void onMouseOver(MouseOverEvent event) {
			// TODO Auto-generated method stub
			((Image)event.getSource()).setSize("70px", "100px");
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
    RootPanel.get("stockList").add(loginPanel);
}

private void playThisCard(ClickEvent event) {
	((Image)event.getSource()).removeFromParent();
	tableCards.add((Image)event.getSource());
	absolutePanel.add((Image)event.getSource(),200+tableCards.size()*10, 200 +tableCards.size()*10);
}
}