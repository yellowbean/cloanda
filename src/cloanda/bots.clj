(ns cloanda.bots
  (:require [cloanda.core :as core]
            [org.clojure/tools.logging :as log]
            [cloanda.util :as util]
            )
  )

(defrecord quotes []
           
           )

(defrecord bot-rule [id ]
           
           
  )           


(defprotocol base-bot
  (start [x])
  (add-rule [x rule])
  (remove-rule [x rule])
  (end [x])
  (run-history [x history-quotes])
  )

(defrecord trading-bot [ id trading-instruments rules buy-threshold sell-threshold stop-loss-amount history-orders open-orders pips-earned]
  (start [x]
         (log/tracef "%s BOT Starting: %s" id (util/local-now))
         )
  (add-rule [x rule]
            (log/tracef "%s BOT Add Rule:$s %s" id rule (util/local-now))
            )
  (remove-rule [x rule]
            (log/tracef "%s BOT Remove Rule:$s %s" id rule (util/local-now))   
               )
  (end [x]
       (log/tracef "%s BOT Ending: %s" id (util/local-now))
       )
  (run-history [x history-quotes]
       (log/tracef "%s BOT Back Testing: %s" id (util/local-now))
       
       )               
  )
