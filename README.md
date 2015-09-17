# cloanda (active development)

The clojure wrapper for OANDA REST API 

----
- [Installation](#installation)
- [A mini example](#A-mini-example)
- [Reference](#reference)
 - [Header Setup](header-setup)
 - [Initiate Api Instance](initiate-api-instance)
 - [Rate Management](rate-management)
 - [Account Management](account-management)
 - [Order Management](order-management)
 - [Trade Management](trade-management)
 - [Position Management](position-management)
 - [Transaction Management](transaction-management)
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

### header setup
    ;; setup token in headers
    ;; set datetime type in response by passing either "UNIX" or "RFC3339"
    (gen-headers "Your tokens here" "UNIX")
    (gen-headers "Your tokens here" "RFC3339")
    
### Initiate an api instance
    (def oanda_api (api. "http://api-sandbox.oanda.com" "http://stream-sandbox.oanda.com/" )))

### Rate Management
    ;;Get all avilable trading instruments
    (get-instrument-list api)

    ;; Get current quote for a given instrument
    (get-current-price api "EUR_USD")

    ;; Get history prices
    ;; get history prices of instrument "EUR_USD"
    ;; by default it will return 500 periods & 5 seconds as granularity
    (get-instrument-history api "EUR_USD" {})
    
    ;; getting 50 periods  by passing an option map
    (get-instrument-history api "EUR_USD" {"count" 50})
    
    ;; map can contains more than one parameter
    (get-instrument-history api "EUR_USD" {"count" 50 "granularity" "D"})

### Account Management
    ;; get all accounts associate with current login
    (get-accounts api)
    
    ;; get detail account information for given account id
    (get-account-info api account_id)
    
    ;; create an account
    (create-account api)
    
### Order Management
    ;; list all orders under a account
    (get-orders-by-account api account_id)
    
    ;; place an buy market order
    (create-order api account_id "EUR_USD" 100000 "buy" "market" )
    
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


