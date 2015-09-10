# cloanda

The clojure wrapper for OANDA REST API

## Installation

* Clone the code and complie a jar
* download a jar package

## Usage


    $ java -jar cloanda-0.1.0-standalone.jar [args]

### Step 1 Initiate an api instance
    (def api (init-rest-api "http://api-sandbox.oanda.com" "https://api-fxpractice.oanda.com" )))

#### Authtencation

#### Options


### Step 2 Call functions bounded to api instance

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
    (create-order  account_id "EUR_USD" 100000 "buy" "market" )
    
    
#### Trade Management
#### Position Management
#### Transaction Management

## Reference

http://developer.oanda.com/rest-live/development-guide/

## Log

### Bugs
Please drop an eamil to always.zhang@gmail.com

## License

Copyright © 2015 Xiaoyu

Distributed under the Eclipse Public License either version 1.0 or any later version.


