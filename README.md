# cloanda

The clojuare wrapper for OANDA REST API

## Installation

Clone the code and complie a jar

## Usage

FIXME: explanation

    $ java -jar cloanda-0.1.0-standalone.jar [args]

### Initiate a api instance
    (def api (init-rest-api "http://api-sandbox.oanda.com" "https://api-fxpractice.oanda.com" )))
### Call functions adhere to api instance
    ;;get current price of instrument "EUR_USD"
    (get-current-price api "EUR_USD")

## Reference


## Options

FIXME: listing of options this app accepts.

## Log

### Bugs
Please drop an eamil to always.zhang@gmail.com

## License

Copyright © 2015 Xiaoyu

Distributed under the Eclipse Public License either version 1.0 or any later version.


