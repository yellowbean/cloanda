(ns cloanda.util
    (:require [clojure.string :as string]
              )
    (:import [org.apache.commons.math3.stat.descriptive.moment Variance Mean]))




(defn get-instrument-names [ inst-resp ]
  (let [ inst-list  (:instruments (:body inst-resp)) ]
    (map :name inst-list)
  )
)


(defn get-instrument-prices [ history-resp price-type]
  (let [ candles (get-in history-resp [:body :candles])
          prices (pmap #(get-in % [:mid price-type]) candles)
          prices_in_double (pmap #(Double. %) prices)
          ]
    (into-array Double/TYPE prices_in_double))
)

(defn cal-variance [ d ]
  (let [ v (Variance. ) ]
    (.evaluate v d)))

(defn cal-mean [ d ]
  ( let [ m (Mean. )
          leng (alength d)]
    (.evaluate m  d 0 leng)))
