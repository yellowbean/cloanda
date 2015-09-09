(ns cloanda.analysis)

(defn avg [v] (/ (reduce + v) (count v)))


(defn ma [v w]
  (map avg (partition w 1 v))
  )

(defn ema [v w alpha]
  (loop [ s  (subvec v w)  r [ (nth v w)  ] ]
    (if (= (dec w) (count s))
      r
      (recur (next s) (conj r (+ (* alpha (first s) ) (* (- 1 alpha) (last r))))))))
