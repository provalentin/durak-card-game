package com.smartgames.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StockPriceServiceAsync {
  public void getPrices(String[] symbols, AsyncCallback<StockPrice[]> callback);
}