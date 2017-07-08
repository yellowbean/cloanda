(ns cloanda.util
    (:require [clojure.string :as string]
              [cheshire.core :as json]
              [clj-time.format :as fmt]
              ))

(defn get-resp [x]
 (json/parse-string (:body x)))


(defn extract-account-numbers [ accs-resp]
  (map :id (get-in accs-resp [:body :accounts] ))
  )

(defn extract-instrument-names [ inst-resp ]
  "Get a list of instrument names"
  (let [ inst-list  (get-in inst-resp [:body :instruments] ) ]
    (map :name inst-list)
  )
)

(defn extract-instrument-prices
  "Get instrument prices given option of open/close/high/low, return a double array"
  ([ history-resp price-type]
    (extract-instrument-prices history-resp price-type :array))
  ([ history-resp price-type t]
    (let [ candles (get-in history-resp [:body :candles])
           prices (map #(get-in % [:mid price-type]) candles)
           prices_in_double (map #(Double. %) prices)
          ]
         (cond
               ;(= t :array) (into-array Double/TYPE prices_in_double)
               (= t :array) JsonIterator.deserialize(prices, Double[].class);
               (= t :list) prices_in_double
               )
         ))
)


(def custom-formatter (fmt/formatter :date-time))
(defn t2mfe [x] (fmt/parse custom-formatter x))

(defn extract-times
      ([ history-resp ]
        (let [ cdls (get-in history-resp [:body :candles])

              ]
             (map #(t2mfe (:time %)) cdls))
        ))


(defn init-ts
      ([ history-resp ]
        (let [ ps (extract-instrument-prices history-resp)
               times (extract-times history-resp)
              ])
        (ts. ps times)
        ))


(defrecord ts [ time value ])
