# cloanda (upgrading to rest-live-v20!)

## Status update: 2017-06-07

I'm still reviewing breaking sample code during the migration to v20 REST API, these sample codes are subject to be changed soon
----

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
    (def my-header (gen-headers "your token" ))

    ;; pick a trading env by a hash-map
    ;; production
    (:production server_env)
    ;; practice
    (:practice server_env)

    ;; setup api instance
    (def oanda_api (init-api (:practice server_env) my-header))

    ;; get available accounts
    (get-accounts oanda_api)

    ;; other stuff

## Reference

### Setup Header
    ;; setup token in headers
    (def my-header (gen-header "Your tokens here") )

### Initiate api instance
    (def oanda_api (init-api (:practice server_env) my-header))

### Instrument Management
    ;;Get all available trading instruments
    (get-account-instruments oanda_api "account id")
    ;; response
    ;; {:instruments: {:instrument "USD_CZK", :displayName "USD/CZK", :pip "0.0001", :maxTradeUnits 10000000}...}


    ;; Get current quote for a given instrument
    (get-current-price api ["EUR_USD"])
    ;; response
    ;; {:prices [{:instrument "EUR_USD", :time "1442495217874957", :bid 1.24068, :ask 1.24082}]}

    (get-instrument-history api "EUR_USD" )
    ;; Get history prices
    ;; get history prices of instrument "EUR_USD"
    ;; by default it will return 500 periods & 5 seconds as granularity

    ;; getting EUR/USD last 15 ticks with granularity = 5 minutes
    (get-instrument-history api "EUR_USD" {"count" "5" "granularity" "M5"})
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
    ;; .... :body {:accounts [{:id "ACCOUNT ID", :tags []}]} ....

    ;; get all tradable instruments under a given account
    (get-account-instruments api "ACCOUNT ID")

    ;; get detail account information for given account id
    (get-account-info api "ACCOUNT ID")
    ;; response
    ;; {:marginUsed 0, :accountCurrency "USD", :accountName "Primary", :realizedPl 0, :unrealizedPl 0, :balance 100000, :marginAvail 100000, :openTrades 0, :accountId 8055333, :marginRate 0.05, :openOrders 0}

    ;; get summary account information for given account id
    (get-account-summary api "ACCOUNT ID")

    ;; get current account state and changes since a transaction id
    (get-account-changes api "ACCOUNT ID")
    (get-account-changes api "ACCOUNT ID" {"sinceTransactionID" "1523"})


### Order Management
    ;; list all orders under a account
    (get-orders-by-account api  "ACCOUNT ID")

    ;; place an buy market order
    (create-order api "ACCOUNT ID" "EUR_USD" "1000" "buy" "MARKET" {})

    ;; getting an order info under an account
    (get-order-info api "ACCOUNT ID" "order_id")

    ;; get all pending orders in a given account
    (get-pending-orders api "ACCOUNT ID")

    ;; change an order via a map
    (replace-order api account_id order_id params)

    ;; cancel an order
    (cancel-order api account_id order_id)

### Trade Management

    ;; list all trades given an account id
    (get-open-trades api account_id)

    ;; list all trades associate with a given account
    (get-trades api account_id)

    ;; get detail trade info by trade id
    (get-trade-info api account_id trade_id)

    ;; update a trade by a map
    (update-trade api account_id trade_id {})

    ;; close a trade by trade id
    (close-trade api account_id trade_id)

### Position Management

    ;; list all position under a given account id
    (get-open-position api account_id)

    ;; list all position of an account
    (get-position api account_id)

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

### Pricing


## Development Reference

http://developer.oanda.com/rest-live-v20/development-guide/



### Bugs
Please drop an email to always.zhang@gmail.com

## License & Legal

* No quality guarantee for any functionality or effectiveness or correct mapping of OANDA API.
* Any user shall use this library by his/her own discretion and be responsible for any losses caused by this library.
* Copyright © 2015/2016 Xiaoyu
* Distributed under the Eclipse Public License either version 1.0 or any later version.
