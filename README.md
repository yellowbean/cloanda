# cloanda

The clojure wrapper for OANDA REST API

## Installation

* Clone the code and complie a jar
* download a jar package

## Usage


    $ java -jar cloanda-0.1.0-standalone.jar [args]

### Step 1 Initiate an api instance
    (def api (init-rest-api "http://api-sandbox.oanda.com" "https://api-fxpractice.oanda.com" )))

#### Options


### Step 2 Call functions bounded to api instance

#### Get all avilable trading instruments
    (get-instrument-list api)

#### Get current quote for a given instrument
    (get-current-price api "EUR_USD")

#### Get history prices
    ;;get history prices of instrument "EUR_USD"
    (get-instrument-history api "EUR_USD")


    
#### Account information
#### Order Management
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


