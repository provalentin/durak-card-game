package com.smartgames.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.smartgames.client.NotLoggedInException;
import com.smartgames.client.StockService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class StockServiceImpl extends RemoteServiceServlet implements
StockService {

  /**
	 * 
	 */
	
private static final Logger LOG = Logger.getLogger(StockServiceImpl.class.getName());
  private static final PersistenceManagerFactory PMF =
      JDOHelper.getPersistenceManagerFactory("transactions-optional");
  private static MemcacheService keycache = MemcacheServiceFactory.getMemcacheService();

  public void addStock(String symbol) throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    LOG.log(Level.INFO, "starting adding stock record");
    try {
      pm.makePersistent(new Stock(getUser(), symbol));
    } finally {
      pm.close();
      Key key = KeyFactory.createKey("Customer", "Vasiliy");
      keycache.put("12345678", symbol);
    }
  }

  public void removeStock(String symbol) throws NotLoggedInException {
    //checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    try {
      LOG.log(Level.INFO, "starting removing stock record");
      long deleteCount = 0;
      Query q = pm.newQuery(Stock.class, "user == u");
      q.declareParameters("com.google.appengine.api.users.User u");
      List<Stock> stocks = (List<Stock>) q.execute(getUser());
      for (Stock stock : stocks) {
        if (symbol.equals(stock.getSymbol())) {
          deleteCount++;
          pm.deletePersistent(stock);
        }
      }
      if (deleteCount != 1) {
        LOG.log(Level.WARNING, "removeStock deleted "+deleteCount+" Stocks");
      }
    } finally {
      pm.close();
    }
  }

  public String getStocks() throws NotLoggedInException {
    //checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    List<String> symbols = new ArrayList<String>();
    LOG.log(Level.INFO, "starting getting stock records");
    List<Stock> stocks;
    String e = (String) keycache.get("12345678");
    if (e != null) {
    	LOG.log(Level.WARNING,"game state string from memcache : " + e);
    	return e;
    }
    
    try {
      Query q = pm.newQuery(Stock.class, "user == u");
      q.declareParameters("com.google.appengine.api.users.User u");
      q.setOrdering("createDate");
//      q.setRange(0,10);
      stocks = (List<Stock>) q.execute(getUser());
      for (Stock stock : stocks) {
        symbols.add(stock.getSymbol());
      }
    } finally {
      pm.close();
    }
    //getStockHistory((String[]) symbols.toArray(new String[0]));
    //return (String[]) symbols.toArray(new String[0]);
    return stocks.get(stocks.size()-1).getSymbol();
  }
  
//  public StockHistory[] getStockHistory(String[] stocks) throws NotLoggedInException {
//	  //String[] stocks = getStocks();
//	  Random rnd = new Random();
//	  ArrayList stockHistory = new ArrayList(stocks.length);
//	  for (int i=0; i<stocks.length;i++){
//		  PersistenceManager pm = getPersistenceManager();
//		    LOG.log(Level.INFO, "starting adding stock history record record");
//		    try {
//		      pm.makePersistent(new StockHistory(rnd.nextDouble(), stocks[i]));
//		    } finally {
//		      pm.close();
//		    }
//	  }
//	  
//	  return null;
//  }

  private void checkLoggedIn() throws NotLoggedInException {
    if (getUser() == null) {
      throw new NotLoggedInException("Not logged in.");
    }
  }

  private User getUser() {
    UserService userService = UserServiceFactory.getUserService();
    return userService.getCurrentUser();
  }

  private PersistenceManager getPersistenceManager() {
    return PMF.getPersistenceManager();
  }

//@Override
//public StockHistory[] getStockHistory() throws NotLoggedInException {
//	// TODO Auto-generated method stub
//	return null;
//}
}