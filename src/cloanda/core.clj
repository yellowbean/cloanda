(ns cloanda.core
    (:require [clj-http.client :as client]
              [clojure.string :as string]
            ))

(defmacro json-in-body [x]
  (:body (x {:as :json}) )
  )


;;util functions
(defn opt_to_str [opt]
  (string/join "&" (map #(str (first %) "=" (second %)) (seq opt)))
  )


(defprotocol rate_protocol
  (get-instrument-list [x])
  (get-current-price [x cur])
  (get-instrument-history [x cur opt])
  )

(defprotocol account_protocol
  (get-accounts [x])
  (get-account-info [x id])
  (create-account [x])
  )

(defprotocol order_protocol
  (get-orders-by-account [x a_id])
  (create-order [x a_id inst units side type opt ])
  (get-order-info [x a_id o_id])
  (update-order [x a_id o_id opt])
  (close-order [x a_id o_id])
  )

(defprotocol trade_protocol
  (get-open-trades [x id])
  (get-trade-info [x a_id t_id])
  (update-trade [x a_id t_id opt])
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



(defrecord api [ url stream_url username password ]
  rate_protocol
  (get-instrument-list [x]
    (:instruments (:body (client/get (str url "/v1/instruments" ) {:as :json}))))
  (get-current-price [ x cur ]
    (:body (client/get (str url "/v1/prices?instruments=" (string/join "%2C" cur)) {:as :json} ))
    )
  (get-instrument-history [ x cur opt]
    (let [opt_str (apply str (for [i opt] (str "&" (first i) "=" (second i))))]
      (:body (client/get (str url "/v1/candles?instrument=" cur opt_str) {:as :json} ))))

  account_protocol
  (get-accounts [x]
    (:body (client/post (str url "/v1/accounts") {:as :json})))
  (get-account-info [x id]
    (:body (client/get (str url "/v1/accounts/" id) {:as :json})))
  (create-account [x]
    (:body (client/post (str url "/v1/accounts") {:as :json}))
    )

  order_protocol
  (get-orders-by-account [x a_id]
    (:body (client/get (str url "/v1/accounts/" a_id "/orders/") {:as :json})))
  (create-order [x a_id inst units side type opt]
    (let [base_cmd  {:instrument  inst :units units :side side :type type}
          exe_cmd (merge base_cmd opt)]
      (:body (client/post (str url "/v1/accounts/" a_id "/orders") {:form-params exe_cmd  :as :json})))
    )
  (get-order-info [x a_id o_id]
    (:body (client/get (str url "/v1/accounts/" a_id "/orders/" o_id) {:as :json})))
  (update-order [x a_id o_id opt]
    (:body (client/patch (str url "/v1/accounts/" a_id "/orders/" o_id) {:form-params opt  :as :json})))
  (close-order [x a_id o_id]
    (:body (client/delete (str url "/v1/accounts/" a_id "/orders/" o_id) {:as :json})))

  trade_protocol
  (get-open-trades [x id]
    (:body (client/get (str url "/v1/accounts/" id "/trades/") {:as :json})))
  (get-trade-info [x a_id t_id]
    (:body  (client/delete (str url "/v1/accounts/" a_id "/trades/" t_id) {:as :json})))
  (update-trade [x a_id t_id opt]
    (let [exe_cmd (opt_to_str opt)]
      (:body (client/patch (str url "/v1/accounts/" a_id "/trades/" t_id) {:form-params opt :as :json}))))
  (close-trade [x a_id t_id]
    (:body (client/delete (str url "/v1/accounts/" a_id "/trades/" t_id) {:as :json})))

  position_protocol
  (get-open-position [x a_id]
    (:body (client/get (str url "/v1/accounts/" a_id "/positions") {:as :json})))
  (get-position-by-inst [x a_id inst]
    (:body (client/get (str url "/v1/accounts/" a_id "/positions/" inst) {:as :json})))
  (close-position [x a_id inst]
    (:body (client/delete (str url "/v1/accounts/" a_id "/positions/" inst) {:as :json})))

  transaction_protocol
  (get-txn-history [x a_id ]
    (:body (client/get (str url "/v1/accounts/" a_id "/transactions/") {:as :json})))
  (get-txn-info [x a_id t_id ]
    (:body (client/get (str url "/v1/accounts/" a_id "/transactions/" t_id) {:as :json})))
  (get-account-history [x a_id ]
    (:body (client/get (str url "/v1/accounts/" a_id "/alltransactions/") {:as :json})))

  forex_lab_protocol
  (get-calendar [x inst period]
    (:body (client/get (str url "/labs/v1/calendar?instrument=" inst "&period=" period) {:as :json})))
  (hist-pos-ratios [x inst period]
    (:body (client/get (str url "/labs/v1/historical_position_ratios?instrument=" inst "&period=" period) {:as :json} )))
  (get-spreads [x inst period]
    (:body (client/get (str url "/labs/v1/spreads?instrument=" inst "&period=" period) {:as :json})))
  (get-cot [x inst]
    (:body (client/get (str url "/labs/v1/commitments_of_traders?instrument=" inst) {:as :json})))
  (get-order-book [x inst period]
    (:body (client/get (str url "/labs/v1/orderbook_data?instrument=" inst "&period=" period)  {:as :json}))
    )

  streaming_protocol
  (rate-stream [x a_id inst]
    (:body (client/get (str stream_url "/v1/prices?accountId=" a_id "&instruments=" (string/join "%2C" inst))))
    )

  (event-stream [x]
    (:body (client/get (str stream_url "/v1/events")))
    )
 )

(defn init-rest-api
  ([url stream_url] (api. url stream_url "" "") )
  ([url stream_url user password] (api. url stream_url user password))
  )


(defn -main [ &args]
  (init-rest-api "http://api-sandbox.oanda.com" "http://stream-sandbox.oanda.com/")
  )
