# cloanda (active development)

The clojure wrapper for OANDA REST API 

----
- [Installation](#installation)
- [A mini example](#A-mini-example)
- [Reference](#reference)
 - [Setup Header ](#setup-header)
 - [Initiate api Instance](#initiate-api-instance)
 - [Rate Management](#rate-management)
 - [Account Management](#account-management)
 - [Order Management](#order-management)
 - [Trade Management](#trade-management)
 - [Position Management](#position-management)
 - [Transaction Management](#transaction-management)
- [Development Reference](#development-reference)


----


## Installation

* Clone the code and complie a jar
* download a jar package

## Usage

    $ java -jar cloanda-0.1.0-standalone.jar [args]

## A mini example

    ;; setup a request header
    (def my-header (gen-header "your token" "UNIX"))
    
    ;; setup api instance
    (def oanda_api (api. "http://api-sandbox.oanda.com" "http://stream-sandbox.oanda.com/" my-header)))
    
    ;; get available trade instruments
    (get-instrument-list oanda_api)
    
    ;; other stuff

## Reference

### Setup Header 
    ;; setup token in headers
    ;; set datetime type in response by passing either "UNIX" or "RFC3339"
    (gen-header "Your tokens here" "UNIX")
    (gen-header "Your tokens here" "RFC3339")
    
### Initiate api instance
    (def oanda_api (api. "http://api-sandbox.oanda.com" "http://stream-sandbox.oanda.com/" ))

### Rate Management
    ;;Get all avilable trading instruments
    (get-instrument-list oanda_api)
    ;; response
    ;; {:instruments: {:instrument "USD_CZK", :displayName "USD/CZK", :pip "0.0001", :maxTradeUnits 10000000}...}


    ;; Get current quote for a given instrument
    (get-current-price api ["EUR_USD"])
    ;; response
    ;; {:prices [{:instrument "EUR_USD", :time "1442495217874957", :bid 1.24068, :ask 1.24082}]}

    ;; Get history prices
    ;; get history prices of instrument "EUR_USD"
    ;; by default it will return 500 periods & 5 seconds as granularity
    (get-instrument-history api "EUR_USD" {})
    ;; response
    ;; {:instrument "EUR_USD", :granularity "S5", :candles [{:highBid 1.24052, :time "1442495330000000", :lowBid 1.24052, :openAsk 1.24066, :closeAsk 1.24066, :openBid 1.24052, :volume 1, :highAsk 1.24066, :complete true, :closeBid 1.24052, :lowAsk 1.24066} {:highBid 1.24052, :time "1442495335000000", :lowBid 1.24044, :openAsk 1.24065, :closeAsk 1.24063, :openBid 1.24052, :volume 9, :highAsk 1.24066, :complete true, :closeBid 1.24044, :lowAsk 1.24063} {:highBid 1.24045, :time "1442495340000000", :lowBid 1.24044, :openAsk 1.24061, :closeAsk 1.24061, :openBid 1.24045, :volume 2, :highAsk 1.24061, :complete false, :closeBid 1.24044, :lowAsk 1.24061}]}
    
    ;; getting 50 periods  by passing an option map
    (get-instrument-history api "EUR_USD" {"count" 50})
    
    ;; map can contains more than one parameter, "D" stands for "Day"
    (get-instrument-history api "EUR_USD" {"count" 50 "granularity" "D"})

### Account Management
    ;; get all accounts associate with current login
    (get-accounts api)
    ;; response
    ;; {:username "jamesmon", :password "tehydJuch^", :accountId 8055333}
    
    ;; get detail account information for given account id
    (get-account-info api 8055333)
    ;; response
    ;; {:marginUsed 0, :accountCurrency "USD", :accountName "Primary", :realizedPl 0, :unrealizedPl 0, :balance 100000, :marginAvail 100000, :openTrades 0, :accountId 8055333, :marginRate 0.05, :openOrders 0}
    
    ;; create an account
    (create-account api)
    ;; response
    ;; {:username "grennett", :password "Ikfokded9", :accountId 7443292}
    
### Order Management
    ;; list all orders under a account
    (get-orders-by-account api account_id)
    
    ;; place an buy market order
    (create-order oanda_api 8055333 "EUR_USD" 100 "buy" "market" {})
    ;; response
    ;; {:instrument "EUR_USD", :time "2015-09-17T13:25:44.000000Z", :price 1.2405, :tradeOpened {:id 175584567, :units 100, :side "buy", :takeProfit 0, :stopLoss 0, :trailingStop 0}, :tradesClosed [], :tradeReduced {}}
    
    ;; getting an order info under an account
    (get-order-info api account_id order_id)
    
    ;; change an order by a map
    (update-order api account_id order_id params)
    
    ;; close an order
    (close-order api account_id order_id)
    
### Trade Management

    ;; list all trades given an account id
    (get-open-trades api account_id)
    
    ;; get detail trade info by trade id
    (get-trade-info api account_id trade_id)
    
    ;; update a trade by a map
    (update-trade api account_id trade_id {})
    
    ;; close a trade by trade id
    (close-trade api account_id trade_id)

### Position Management

    ;; list all position under a given account id
    (get-open-position api account_id)
    
    ;; get position of given instrument and account id
    (get-position-by-inst api account_id "EUR_USD")
    
    ;; close position of given instrument and account id
    (close-position api account_id "EUR_USD")


### Transaction Management

    ;; list all transaction history of a given account
    (get-txn-history api account_id)
    
    ;; get a detail information of a given transaction id
    (get-txn-info api account_id txn_id)
    
    ;; list account history by given account id
    (get-account-history api account_id)




## Development Reference

http://developer.oanda.com/rest-live/development-guide/



### Bugs
Please drop an eamil to always.zhang@gmail.com

## License

Copyright © 2015 Xiaoyu

Distributed under the Eclipse Public License either version 1.0 or any later version.


