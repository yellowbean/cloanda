# cloanda (active development)

The clojure wrapper for OANDA REST API 

----
- [Installation](#installation)
- [Reference](#reference)



----


## Installation

* Clone the code and complie a jar
* download a jar package

## Usage

    $ java -jar cloanda-0.1.0-standalone.jar [args]

### Step 1 Creating a config header string
    ;; setup token in headers
    ;; set datetime type in response by passing either "UNIX" or "RFC3339"
    (gen-headers "Your tokens here" "UNIX")
    (gen-headers "Your tokens here" "RFC3339")
    
### Step 2 Initiate an api instance
    (def oanda_api (api. "http://api-sandbox.oanda.com" "https://api-fxpractice.oanda.com" )))
    
#### Authtencation

#### Options


### Reference
Call functions bounded to api instance

#### Get all avilable trading instruments
    (get-instrument-list api)

#### Get current quote for a given instrument
    (get-current-price api "EUR_USD")

#### Get history prices
    ;; get history prices of instrument "EUR_USD"
    ;; by default it will return 500 periods & 5 seconds as granularity
    (get-instrument-history api "EUR_USD" {})
    
    ;; getting 50 periods  by passing an option map
    (get-instrument-history api "EUR_USD" {"count" 50})
    
    ;; map can contains more than one parameter
    (get-instrument-history api "EUR_USD" {"count" 50 "granularity" "D"})

#### Account information
    ;; get all accounts associate with current login
    (get-accounts api)
    
    ;; get detail account information for given account id
    (get-account-info api account_id)
    
    ;; create an account
    (create-account api)
    
#### Order Management
    ;; list all orders under a account
    (get-orders-by-account api account_id)
    
    ;; place an buy market order
    (create-order api account_id "EUR_USD" 100000 "buy" "market" )
    
    ;; getting an order info under an account
    (get-order-info api account_id order_id)
    
    ;; change an order
    (update-order api account_id order_id opt)
    
    ;; close an order
    (close-order api account_id order_id)
    
#### Trade Management

    ;;
    (get-open-trades api account_id)
    
    ;; 
    (get-trade-info api account_id trade_id)
    
    ;;
    (update-trade api account_id trade_id {})
    
    ;; close a trade by trade id
    (close-trade api account_id trade_id)

#### Position Management

    ;;
    (get-open-position api account_id)
    
    ;;
    (get-position-by-inst api account_id "EUR_USD")
    
    ;;
    (close-position api account_id "EUR_USD")


#### Transaction Management

    ;;
    (get-txn-history api account_id)
    
    (get-txn-info api account_id txn_id)
    
    (get-account-history api account_id)




## Reference

http://developer.oanda.com/rest-live/development-guide/

## Log

### Bugs
Please drop an eamil to always.zhang@gmail.com

## License

Copyright © 2015 Xiaoyu

Distributed under the Eclipse Public License either version 1.0 or any later version.


