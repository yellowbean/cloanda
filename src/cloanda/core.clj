(set! *warn-on-reflection* true)
(ns cloanda.core
    (:require [clj-http.client :as client]
              [clojure.string :as string]
              [clj-json.core :as json]
              )
    (:import [java.io.FilterInputStream])
    )

(def gen-headers [ token datetime-format ]
    (let [ auth (str "Bearer " token)] ; "UNIX" or "RFC3339"
    {"Authorization" auth  "X-Accept-Datetime-Format" datetime-format}
    )


(defn read-stream [^java.io.FilterInputStream x]
  (loop [ r (.read x)
         xs [] ]
    (if-not (= r 10)
      (recur (.read x) (conj xs r))
      (json/parse-string (string/join "" (map char xs))))))


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
  (create-order [x a_id inst unit side type opt ])
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

(defrecord api [ url stream_url opt ]
  rate_protocol
  (get-instrument-list [x]
    (:instruments (:body (client/get (str url "/v1/instruments" ) {:as :json :headers (:header opt)} ))))
  (get-current-price [ x cur ]
    (:body (client/get (str url "/v1/prices?instruments=" (string/join "%2C" cur)) {:as :json :headers (:header opt)} ))
    )
  (get-instrument-history [ x cur p]
    (let [opt_str (apply str (for [i p] (str "&" (first i) "=" (second i))))]
      (:body (client/get (str url "/v1/candles?instrument=" cur opt_str) {:as :json :headers (:header opt)} )
      )))

  account_protocol
  (get-accounts [x]
    (:body (client/post (str url "/v1/accounts") {:as :json :headers (:header opt)})))
  (get-account-info [x id]
    (:body (client/get (str url "/v1/accounts/" id) {:as :json :headers (:header opt)})))
  (create-account [x]
    (:body (client/post (str url "/v1/accounts") {:as :json :headers (:header opt)})))

  order_protocol
  (get-orders-by-account [x a_id]
    (:body (client/get (str url "/v1/accounts/" a_id "/orders/") {:as :json :headers (:header opt)})))
  (create-order [x a_id inst units side type opt]
    (let [base_cmd  {:instrument  inst :units units :side side :type type}
          exe_cmd (merge base_cmd opt)]
      (:body (client/post (str url "/v1/accounts/" a_id "/orders") {:form-params exe_cmd  :as :json})))
    )
  (get-order-info [x a_id o_id]
    (:body (client/get (str url "/v1/accounts/" a_id "/orders/" o_id) {:as :json :headers (:header opt)})))
  (update-order [x a_id o_id opt]
    (:body (client/patch (str url "/v1/accounts/" a_id "/orders/" o_id) {:form-params opt  :as :json})))
  (close-order [x a_id o_id]
    (:body (client/delete (str url "/v1/accounts/" a_id "/orders/" o_id) {:as :json :headers (:header opt)})))

  trade_protocol
  (get-open-trades [x id]
    (:body (client/get (str url "/v1/accounts/" id "/trades/") {:as :json :headers (:header opt)})))
  (get-trade-info [x a_id t_id]
    (:body  (client/delete (str url "/v1/accounts/" a_id "/trades/" t_id) {:as :json :headers (:header opt)})))
  (update-trade [x a_id t_id opt]

      (:body (client/patch (str url "/v1/accounts/" a_id "/trades/" t_id) {:form-params opt :as :json})))
  (close-trade [x a_id t_id]
    (:body (client/delete (str url "/v1/accounts/" a_id "/trades/" t_id) {:as :json :headers (:header opt)})))

  position_protocol
  (get-open-position [x a_id]
    (:body (client/get (str url "/v1/accounts/" a_id "/positions") {:as :json :headers (:header opt)})))
  (get-position-by-inst [x a_id inst]
    (:body (client/get (str url "/v1/accounts/" a_id "/positions/" inst) {:as :json :headers (:header opt)})))
  (close-position [x a_id inst]
    (:body (client/delete (str url "/v1/accounts/" a_id "/positions/" inst) {:as :json :headers (:header opt)})))

  transaction_protocol
  (get-txn-history [x a_id ]
    (:body (client/get (str url "/v1/accounts/" a_id "/transactions/") {:as :json :headers (:header opt)})))
  (get-txn-info [x a_id t_id ]
    (:body (client/get (str url "/v1/accounts/" a_id "/transactions/" t_id) {:as :json :headers (:header opt)})))
  (get-account-history [x a_id ]
    (:body (client/get (str url "/v1/accounts/" a_id "/alltransactions/") {:as :json :headers (:header opt)})))

  forex_lab_protocol
  (get-calendar [x inst period]
    (:body (client/get (str url "/labs/v1/calendar?instrument=" inst "&period=" period) {:as :json :headers (:header opt)})))
  (hist-pos-ratios [x inst period]
    (:body (client/get (str url "/labs/v1/historical_position_ratios?instrument=" inst "&period=" period) {:as :json :headers (:header opt)} )))
  (get-spreads [x inst period]
    (:body (client/get (str url "/labs/v1/spreads?instrument=" inst "&period=" period) {:as :json :headers (:header opt)})))
  (get-cot [x inst]
    (:body (client/get (str url "/labs/v1/commitments_of_traders?instrument=" inst) {:as :json :headers (:header opt)})))
  (get-order-book [x inst period]
    (:Body (client/get (str url "/labs/v1/orderbook_data?instrument=" inst "&period=" period) {:as :json :headers (:header opt)}))
    )
  streaming_protocol
  (rate-stream [x a_id inst]
    (:body (client/get (str stream_url "/v1/prices?accountId=" a_id "&instruments=" (string/join "%2C" inst)) {:as :stream :headers (:header opt)})))

  (event-stream [x]
    (:body (client/get (str stream_url "/v1/events") {:as :stream  :headers (:header opt) }))
    )
)

(defn init-api
  ([url stream_url df] (api. url stream_url df) )
  )
