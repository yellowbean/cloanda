(set! *warn-on-reflection* true)

(ns cloanda.core
    (:require [clj-http.client :as client]
              [clojure.string :as string]
              [clj-json.core :as json]
              )
    (:import [java.io.FilterInputStream])
    )

(defmacro GET [ calling_url header ]
  (list client/get calling_url {:as :json :headers header })
)

(defmacro POST
    ([ calling_url header]
        (list client/post calling_url {:as :json :headers header}))
    ([ calling_url form header]
        (list client/post calling_url {:form-params form :as :json :headers header}))
    )

(defmacro DELETE [calling_url header]
    (list client/delete calling_url {:as :json :headers header})
    )

(defmacro PATCH
    ([calling_url header]
        (list client/patch calling_url {:as :json :headers header}))
    ([calling_url form header]
        (list client/patch calling_url {:as :json :headers header}))
    )


;;;;;;;;;;;;;;global varialbes

(def server_env (hash-map
                        :practice ["https://api-fxpractice.oanda.com" "https://stream-fxpractice.oanda.com"]
                        :production ["https://api-fxtrade.oanda.com" "https://stream-fxtrade.oanda.com/"]
                     ))


;;;;;;;;;;;;;
(defn gen-headers [ ^String token ^String datetime-format ]
    (let [ auth (str "Bearer " token)] ; "UNIX" or "RFC3339"
    {"Authorization" auth  "X-Accept-Datetime-Format" datetime-format}
    ))


(defn read-stream [^java.io.FilterInputStream x]
  (loop [ r (.read x)
         xs [] ]
    (if-not (= r 10)
      (recur (.read x) (conj xs r))
      (json/parse-string (string/join "" (map char xs))))))


(defprotocol rate_protocol
  (get-instrument-list [x])
  (get-current-price [x cur])
  (get-instrument-history [x cur params])
  )

(defprotocol account_protocol
  (get-accounts [x])
  (get-account-info [x id])
  (create-account [x])
  )

(defprotocol order_protocol
  (get-orders-by-account [x a_id])
  (create-order [x a_id inst unit side type params ])
  (get-order-info [x a_id o_id])
  (update-order [x a_id o_id params])
  (close-order [x a_id o_id])
  )

(defprotocol trade_protocol
  (get-open-trades [x id])
  (get-trade-info [x a_id t_id])
  (update-trade [x a_id t_id params])
  (close-trade [x a_id t_id])
  )

(defprotocol position_protocol
  (get-open-position [x a_id])
  (get-position-by-inst [x a_id inst])
  (close-position [x a_id inst])
  )

(defprotocol transaction_protocol
  (get-txn-history [x a_id ])
  (get-txn-info [x a_id t_id ])
  (get-account-history [x a_id ])
  )

(defprotocol forex_lab_protocol
  (get-calendar [x inst period])
  (hist-pos-ratios [x inst period])
  (get-spreads [x inst period])
  (get-cot [x inst])
  (get-order-book [x inst period])
  )

(defprotocol streaming_protocol
  (rate-stream [x a_id inst])
  (event-stream [x])
  )

(defrecord api [ rest_url stream_url header]
  rate_protocol
  (get-instrument-list [x]
    (GET (str rest_url "/v1/instruments" ) header ))

  (get-current-price [ x cur ]
    (GET (str rest_url "/v1/prices?instruments=" (string/join "%2C" cur)) header))

  (get-instrument-history [ x cur params]
    (let [opt_str (apply str (for [i params] (str "&" (first i) "=" (second i))))]
      (GET (str rest_url "/v1/candles?instrument=" cur opt_str) header)))


  account_protocol
  (get-accounts [ x ]
    (GET (str rest_url "/v1/accounts") header ))
  (get-account-info [ x id ]
    (GET (str rest_url "/v1/accounts/" id) header))
  (create-account [ x ]
    (POST (str rest_url "/v1/accounts") header ))

  order_protocol
  (get-orders-by-account [x a_id]
    (GET (str rest_url "/v1/accounts/" a_id "/orders/") header))
  (create-order [x a_id inst units side type params]
    (let [base_cmd  {:instrument  inst :units units :side side :type type}
          exe_cmd (merge base_cmd params)]
      (POST (str rest_url "/v1/accounts/" a_id "/orders") exe_cmd ))
    )
  (get-order-info [x a_id o_id]
    (GET (str rest_url "/v1/accounts/" a_id "/orders/" o_id) header))
  (update-order [x a_id o_id params]
    (PATCH (str rest_url "/v1/accounts/" a_id "/orders/" o_id) params header))
  (close-order [x a_id o_id]
    (DELETE (str rest_url "/v1/accounts/" a_id "/orders/" o_id) header ))

  trade_protocol
  (get-open-trades [x id]
    (GET (str rest_url "/v1/accounts/" id "/trades/") header))
  (get-trade-info [x a_id t_id]
    (DELETE (str rest_url "/v1/accounts/" a_id "/trades/" t_id) header))
  (update-trade [x a_id t_id params]
    (PATCH (str rest_url "/v1/accounts/" a_id "/trades/" t_id) params header))
  (close-trade [x a_id t_id]
    (DELETE (str rest_url "/v1/accounts/" a_id "/trades/" t_id) header ))

  position_protocol
  (get-open-position [x a_id]
    (GET (str rest_url "/v1/accounts/" a_id "/positions") header))
  (get-position-by-inst [x a_id inst]
    (GET (str rest_url "/v1/accounts/" a_id "/positions/" inst) header))
  (close-position [x a_id inst]
    (DELETE (str rest_url "/v1/accounts/" a_id "/positions/" inst) header ))

  transaction_protocol
  (get-txn-history [x a_id ]
    (GET (str rest_url "/v1/accounts/" a_id "/transactions/") header))
  (get-txn-info [x a_id t_id ]
    (GET (str rest_url "/v1/accounts/" a_id "/transactions/" t_id) header))
  (get-account-history [x a_id ]
    (GET (str rest_url "/v1/accounts/" a_id "/alltransactions/") header))

  forex_lab_protocol
  (get-calendar [x inst period]
    (GET (str rest_url "/labs/v1/calendar?instrument=" inst "&period=" period) header ))
  (hist-pos-ratios [x inst period]
    (GET (str rest_url "/labs/v1/historical_position_ratios?instrument=" inst "&period=" period) header ))
  (get-spreads [x inst period]
    (GET (str rest_url "/labs/v1/spreads?instrument=" inst "&period=" period) header))
  (get-cot [x inst]
    (GET (str rest_url "/labs/v1/commitments_of_traders?instrument=" inst) header))
  (get-order-book [x inst period]
    (GET (str rest_url "/labs/v1/orderbook_data?instrument=" inst "&period=" period) header))

  streaming_protocol
  (rate-stream [x a_id inst]
    (:body (client/get (str stream_url "/v1/prices?accountId=" a_id "&instruments=" (string/join "%2C" inst)) {:as :stream :headers header})))

  (event-stream [x]
    (:body (client/get (str stream_url "/v1/events") {:as :stream  :headers header })))
)

(defn init-api
  ([server_env df] (api. (first server_env) (second server_env) df) )
  )
