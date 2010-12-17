package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>StockPriceService</code>.
 */
public interface StockPriceServiceAsync {
   void getPrices(String[] symbols, AsyncCallback<StockPrice[]> callback);
}
