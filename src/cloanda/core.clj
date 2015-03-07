(ns cloanda.core
    (:require [clj-http.client :as client]
              [clojure.string :as string]
            ))

(defmacro json-in-body [x]
  (:body (x {:as :json}) )
  )


;;util functions
(def opt_to_str [opt]
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
  )

(defprotocol order_protocol
  (get-orders-by-account [x id])
  (create-order [x inst units side type expiry price opt ])
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


(defrecord api [ url username password ]
  rate_protocol
  (get-instrument-list [x]
    (:instruments (:body (client/get (str url "/v1/instruments" ) {:as :json}))))
  (get-current-price [ x cur ]
    (:body (client/get (str url "/v1/prices?instruments=" (string/join "%2C" cur)) {:as :json} )))
  (get-instrument-history [ x cur opt]
    (let [opt_str (apply str (for [i opt] (str "&" (first i) "=" (second i))))]
      (:body (client/get (str url "/v1/candles?instrument=" cur opt_str) {:as :json} ))))

  account_protocol
  (get-accounts [x]
    (:body (client/post (str url "/v1/accounts") {:as :json})))
  (get-account-info [x id]
    (:body (client/get (str url "/v1/accounts/" id) {:as :json})))

  order_protocol
  (get-orders-by-account [x id]
    (client/get (str url "/v1/accounts/" id "/orders/") {:as :json}))
  (create-order [x inst units side type opt]
    (let [base_cmd (str "instrument=" inst "&units=" units "&side=" side "&type=" type)
          exe_cmd (str base_cmd (opt_to_str opt))]
      (client/post (str url "/v1/accounts" id "/orders") {:body exe_cmd  :as :json}))
    )
  (get-order-info [x a_id o_id]
    (client/get (str url "/v1/accounts/" a_id "/orders/" o_id) {:as :json}))
  (update-order [x a_id o_id opt]
    (let [exe_cmd (opt_to_str opt)]
      (client/patch (str url "/v1/accounts/" a_id "/orders/" o_id) {:body exe_cmd  :as :json})))
  (close-order [x a_id o_id]
    (client/delete (str url "/v1/accounts/" a_id "/orders/" o_id) {:as :json}))

  trade_protocol
  (get-open-trades [x id]
    (client/get (str url "/v1/accounts/" id "/trades/") {:as :json}))
  (get-trade-info [x a_id t_id]
    (client/delete (str url "/v1/accounts/" a_id "/trades/" t_id) {:as :json}))
  (update-trade [x a_id t_id opt]
    (let [exe_cmd (opt_to_str opt)]
      (client/patch (str url "/v1/accounts/" a_id "/trades/" t_id) {:body exe_cmd :as :json})))
  (close-trade [x a_id t_id]
    (client/delete (str url "/v1/accounts/" a_id "/trades/" t_id) {:as :json}))

  position_protocol
  (get-open-position [x a_id]
    (client/get (str url "/v1/accounts/" a_id "/positions/") {:as :json}))
  (get-position-by-inst [x a_id inst]
    (client/get (str url "/v1/accounts/" a_id "/positions/" inst) {:as :json}))
  (close-position [x a_id inst]
    (client/delete (str url "/v1/accounts/" a_id "/positions/" inst) {:as :json}))

  transaction_protocol
  (get-txn-history [x a_id ]
   (client/get (str url "/v1/accounts/" a_id "/transactions/") {:as :json}))
  (get-txn-info [x a_id t_id ]
   (client/get (str url "/v1/accounts/" a_id "/transactions/" t_id) {:as :json}))
  (get-account-history [x a_id ]
    (client/get (str url "/v1/accounts/" a_id "/alltransactions/") {:as :json}))

  forex_lab_protocol
  (get-calendar [x inst period]
    (client/get (str url "/labs/v1/calendar?instrument=" inst "&period=" period) {:as :json}))
  (hist-pos-ratios [x inst period]
    (client/get (str url "/labs/v1/historical_position_ratios?instrument=" inst "&period=" period) {:as :json} ))
  (get-spreads [x inst period]
    (client/get (str url "/labs/v1/spreads?instrument=" inst "&period=" period) {:as :json}))
  (get-cot [x inst]
    (client/get (str url "/labs/v1/commitments_of_traders?instrument=" inst) {:as :json}))
  (get-order-book [x inst period]
    (client/get (str url "/labs/v1/orderbook_data?instrument=" inst "&period=" period)  {:as :json})
    )

 )

(defn init-rest-api
  ([url] (api. url "" "") )
  ([url user password] (api. url user password))
  )


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (init-rest-api "http://api-sandbox.oanda.com"))
