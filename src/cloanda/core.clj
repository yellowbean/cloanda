(set! *warn-on-reflection* true)

(ns cloanda.core
    (:require [clj-http.client :as client]
              [clojure.string :as string]
              [clj-json.core :as json])

    (:import [java.io.FilterInputStream]))


(defmacro GET [ calling_url header]
  (list client/get calling_url {:as :json :headers header :content-type :json :throw-exceptions false}))

(defmacro POST
    ([ calling_url header]
     (list client/post calling_url {:as :json :headers header :content-type :json  :throw-exceptions false}))
    ([ calling_url form header]
     (list client/post calling_url {:body form :as :json :headers header :content-type :json  :throw-exceptions false})))

(defmacro DELETE [calling_url header]
    (list client/delete calling_url {:as :json :headers header :content-type :json :throw-exceptions false}))

(defmacro PUT [calling_url form header]
    (list client/put calling_url {:as :json :body form :headers header :content-type :json :throw-exceptions false}))

(defmacro PATCH
    ([calling_url header]
     (list client/patch calling_url {:as :json :headers header :content-type :json :throw-exceptions false}))
    ([calling_url form header]
     (list client/patch calling_url {:as :json :headers header :content-type :json :throw-exceptions false})))

;;;;;;;;;;;;;;global varialbes

(def server_env (hash-map
                        :practice ["https://api-fxpractice.oanda.com" "https://stream-fxpractice.oanda.com"]
                        :production ["https://api-fxtrade.oanda.com" "https://stream-fxtrade.oanda.com/"]))



;;;;;;;;;;;;;
(defn gen-headers [ ^String token]
    "To generate authorization token for headers"
    (let [ auth (str "Bearer " token)]
     {"Authorization" auth}))



(defn read-stream [^java.io.FilterInputStream x]
  (loop [ r (.read x)
         xs []]
    (if-not (= r 10)
      (recur (.read x) (conj xs r))
      (json/parse-string (string/join "" (map char xs))))))

;;;;;;;;;;;;; Utils
(defn params2query [ p ]
  (subs (apply str (for [i p] (str "&" (first i) "=" (second i)))) 1))



(defprotocol instrument_protocol
  (get-current-price [x cur])
  (get-instrument-history [x cur] [x cur params]))


(defprotocol account_protocol
  (get-accounts [x])
  (get-account-info [x id])
  (get-account-summary [ x id])
  (get-account-instruments [ x id])
  (patch-account [ x id])
  (get-account-changes [ x id ] [ x id params]))


(defprotocol order_protocol
  (create-order [x id inst unit side type params])
  (get-orders-by-account [x id] [x id params])
  (get-order-info [x id o_id])
  (get-pending-orders [x id])
  (replace-order [x id o_id params])
  (cancel-order [x id o_id]))


(defprotocol trade_protocol
  (get-open-trades [x id])
  (get-trades [x id])
  (get-trade-info [x id t_id])
  (update-trade [x id t_id params])
  (close-trade [x id t_id params]))


(defprotocol position_protocol
  (get-position [x id])
  (get-open-position [x id])
  (get-position-by-inst [x id inst])
  (close-position [x id inst params]))


(defprotocol transaction_protocol
  (get-txn-history [x id params])
  (get-txn-info [x id t_id])
  (get-txn-range [x id params])
  (get-txn-since [x id params])
  (get-txn-stream [x id params]))
  ;(get-account-history [x a_id]))
(defprotocol pricing_protocol
  (get-pricing-inst [x id params])
  (get-pricing-stream [x id params]))

(defrecord api [ rest_url stream_url header]
  instrument_protocol
  (get-instrument-history [ x inst ]
    (GET (str rest_url "/v3/instruments/" inst ) header))
  (get-instrument-history [ x inst params ]
    "get history candles of a instrument"
    (let [opt_str (params2query params) ]
      (GET (str rest_url "/v3/instruments/" inst "/candles?" opt_str ) header)))

  account_protocol
  (get-accounts [ x]
    (GET (str rest_url "/v3/accounts") header))
  (get-account-info [ x id]
    (GET (str rest_url "/v3/accounts/" id) header))
  (get-account-summary [ x id]
    (GET (str rest_url "/v3/accounts/" id "/summary") header))
  (get-account-instruments [ x id]
    (GET (str rest_url "/v3/accounts/" id "/instruments") header))
  (patch-account [ x id]
    (PATCH (str rest_url "/v3/accounts/" id "/configuration") header))
  (get-account-changes [ x id]
      (GET (str rest_url "/v3/accounts/" id "/changes") header))
  (get-account-changes [x id params]
      (GET (str rest_url "/v3/accounts/" id "/changes?" (params2query params)) header))

  order_protocol
  (create-order [x id inst units side type params]
    (let [base_cmd  {:instrument  inst :units units :side side :type type}
          exe_cmd (merge base_cmd params)
          order (json/generate-string {:order exe_cmd})]
      ;order
      (POST (str rest_url "/v3/accounts/" id "/orders") order header)

      ))
  (get-orders-by-account [x id ]
    (GET (str rest_url "/v3/accounts/" id "/orders" ) header))
  (get-orders-by-account [x id params]
    (GET (str rest_url "/v3/accounts/" id "/orders?" (params2query params) ) header))
  (get-order-info [x id o_id]
    (GET (str rest_url "/v3/accounts/" id "/orders/" o_id) header))
  (get-pending-orders [x id]
      (GET (str rest_url "/v3/accounts/" id "/pendingOrders" ) header))
  (replace-order [x id o_id params]
    (PUT (str rest_url "/v3/accounts/" id "/orders/" o_id) params header))
  (cancel-order [x id o_id]
    (PUT (str rest_url "/v1/accounts/" id "/orders/" o_id "/cancel") "" header))

  trade_protocol
  (get-open-trades [x id]
    (GET (str rest_url "/v3/accounts/" id "/openTrades/") header))
  (get-trades [x id]
    (GET (str rest_url "/v3/accounts/" id "/trades/") header))
  (get-trade-info [x id t_id]
    (GET (str rest_url "/v3/accounts/" id "/trades/" t_id) header))
  (update-trade [x id t_id params]
    (PUT (str rest_url "/v3/accounts/" id "/trades/" t_id "/orders") params header))
  (close-trade [x id t_id params]
    (PUT (str rest_url "/v3/accounts/" id "/trades/" t_id "/close") params header))

  position_protocol
  (get-position [x id]
    (GET (str rest_url "/v3/accounts/" id "/positions") header))
  (get-open-position [x id]
    (GET (str rest_url "/v3/accounts/" id "/openPositions") header))
  (get-position-by-inst [x id inst]
    (GET (str rest_url "/v3/accounts/" id "/positions/" inst) header))
  (close-position [x id inst params]
    (PUT (str rest_url "/v3/accounts/" id "/positions/" inst "/close") params header))

  transaction_protocol
  (get-txn-history [x id params]
    (GET (str rest_url "/v3/accounts/" id "/transactions" (params2query params) ) header))
  (get-txn-info [x id t_id]
    (GET (str rest_url "/v3/accounts/" id "/transactions/" t_id) header))
  (get-txn-range [x id params]
    (GET (str rest_url "/v3/accounts/" id "/transactions/idrange" (params2query params) ) header))
  (get-txn-since [x id params]
    (GET (str rest_url "/v3/accounts/" id "/transactions/sinceid" (params2query params) ) header))
  (get-txn-stream [x id params]
    (GET (str stream_url "/v3/accounts/" id "/transactions/stream" (params2query params) ) header)    )
  pricing_protocol
  (get-pricing-inst [x id params]
    (GET (str rest_url "/v3/accounts/" id "/pricing" (params2query params)) header))
  (get-pricing-stream [x id params]
    (GET (str stream_url "/v3/accounts/" id "/pricing/stream" (params2query params)) header)))

  ;(get-account-history [x a_id]
  ;  (GET (str rest_url "/v1/accounts/" a_id "/alltransactions/") header))

  ;forex_lab_protocol
  ;(get-calendar [x inst period]
  ;  (GET (str rest_url "/labs/v1/calendar?instrument=" inst "&period=" period) header))
  ;(hist-pos-ratios [x inst period]
  ;  (GET (str rest_url "/labs/v1/historical_position_ratios?instrument=" inst "&period=" period) header))
  ;(get-spreads [x inst period]
  ;  (GET (str rest_url "/labs/v1/spreads?instrument=" inst "&period=" period) header))
  ;(get-cot [x inst]
  ;  (GET (str rest_url "/labs/v1/commitments_of_traders?instrument=" inst) header))
  ;(get-order-book [x inst period]
  ;  (GET (str rest_url "/labs/v1/orderbook_data?instrument=" inst "&period=" period) header))

  ;streaming_protocol
  ;(rate-stream [x a_id inst]
  ;  (:body (client/get (str stream_url "/v1/prices?accountId=" a_id "&instruments=" (string/join "%2C" inst)) {:as :stream :headers header})))
;
  ;(event-stream [x]
  ;  (:body (client/get (str stream_url "/v1/events") {:as :stream  :headers header}))))


(defn init-api
  [server_env df] (api. (first server_env) (second server_env) df))
