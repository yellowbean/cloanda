(ns cloanda.analysis
  (:import
   (org.apache.commons.math3.stat.descriptive DescriptiveStatistics)
   (org.apache.commons.math3.stat.regression SimpleRegression))
  )


;;;;;; technical indicators

(defn avg [v] (/ (reduce + v) (count v)))


(defn ma [v w]
  (map avg (partition w 1 v))
  )

(defn ema [v w alpha]
  (loop [ s  (subvec v w)  r [ (nth v w)] ]
    (if (= (dec w) (count s))
      r
      (recur (next s) (conj r (+ (* alpha (first s) ) (* (- 1 alpha) (last r))))))))

;;;;;; signals
(defn cross-over [fast slow]
  (let [ pairs (map vector fast slow)]
    (map #(- %1 %2) pairs)

    )
  )


;;;;;; strategy
