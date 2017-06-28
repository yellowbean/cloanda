(ns cloanda.util
    (:require [clojure.string :as string]
              [cheshire.core :as json]
              )
    (:import [org.apache.commons.math3.stat.descriptive.moment Variance Mean]
             [org.apache.commons.math3.stat.regression SimpleRegression]))

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
          prices (pmap #(get-in % [:mid price-type]) candles)
          prices_in_double (pmap #(Double. %) prices)
          ]
         (cond
               (= t :array) (into-array Double/TYPE prices_in_double)
               (= t :list) prices_in_double
               )
         ))
)

(defn cal-variance [ d ]
  "Caculate variance of a given double array"
  (let [ v (Variance. ) ]
    (.evaluate v d)))

(defn cal-mean [ d ]
  "Caculate mean of a given double array"
  ( let [ m (Mean. )
          leng (alength d)]
    (.evaluate m  d 0 leng)))

(defn get-simple-regression [d]
  "Get a Simple Regression"
  (let [r (SimpleRegression. )
        leng (alength d)]
        (doseq [ i (range leng)]
          (.addData r i (aget d i)))
        r
        )
  )
