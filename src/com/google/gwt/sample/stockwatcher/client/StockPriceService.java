package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("stockPrices")
public interface StockPriceService extends RemoteService {

   StockPrice[] getPrices(String[] symbols) throws DelistedException;

}
