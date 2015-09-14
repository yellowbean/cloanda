(ns cloanda.bots
  (:require [cloanda.core :as core]
            [org.clojure/tools.logging :as log]
            )
  )

(defprotocol base-bot
  (start [x])
  (add-rule [x rule])
  (remove-rule [x rule])
  (end [x])
  (run-history [x history-feed])
  )

(defrecord trading-bot [ trading-instruments rules stop-loss history-orders open-orders pips]
  (start [x]
         (log/trace "Yo")
         )
  (add-rule [x rule])
  (remove-rule [x rule])
  (end [x])
  (run-history [x history-feed])
  )
