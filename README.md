# cloanda

The clojuare wrapper for OANDA REST API

## Installation

Clone the code and complie a jar

## Milestone
### cover all OANDA's REST API
### cover OANDA's Lab API
### performance 


## Usage

FIXME: explanation

    $ java -jar cloanda-0.1.0-standalone.jar [args]

### Initiate a api instance
    (def api (init-rest-api "http://api-sandbox.oanda.com" "https://api-fxpractice.oanda.com" )))
### Call functions adhere to api instance
    ;;get current price of instrument "EUR_USD"
    (get-current-price api "EUR_USD")



## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

## License

Copyright © 2015 Xiaoyu

Distributed under the Eclipse Public License either version 1.0 or any later version.


