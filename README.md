# cloanda

The clojure wrapper for OANDA REST API

## Installation

* Clone the code and complie a jar
* download a jar package

## Usage


    $ java -jar cloanda-0.1.0-standalone.jar [args]

### Step 1 Initiate a api instance
    (def api (init-rest-api "http://api-sandbox.oanda.com" "https://api-fxpractice.oanda.com" )))
### Step 2 Call functions adhere to api instance
    ;;get current price of instrument "EUR_USD"
    (get-current-price api "EUR_USD")

## Reference

http://developer.oanda.com/rest-live/development-guide/

## Options

FIXME: listing of options this app accepts.

## Log

### Bugs
Please drop an eamil to always.zhang@gmail.com

## License

Copyright © 2015 Xiaoyu

Distributed under the Eclipse Public License either version 1.0 or any later version.


