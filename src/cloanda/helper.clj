(set! *warn-on-reflection* true)

(ns cloanda.helper
    (:require [cloanda.core :as apicore]))




;; collection of helper functions
(defn get-history-of-instruments [ oa insts gran cnt]
  "oa -> oanda api ; insts -> instrumets ; gran -> granularity ; count-> history count"
  (loop [ inst insts r {}]
    (let [
          current-inst (first inst)
          pcs-resp (.get-instrument-history oa current-inst {"granularity" gran "count" (str cnt)})
          ]
      (if (nil? current-inst)
        r
        (recur (next inst) (assoc r current-inst pcs-resp  ))))
  ))




