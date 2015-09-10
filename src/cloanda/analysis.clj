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

(defn get-sr [v]
  (let [horizon (->(range 1 (inc (count v))) (double-array))
        ys (double-array v)
        sr (SimpleRegression.)]
    (doseq [[x y] (map list horizon ys)]
      (.addData sr x y))
    sr)
  )

(defn get-ds [v]
  (let [ ds (DescriptiveStatistics. (double-array v))]
    ds)
  )


;;;;;; signals
(defn cross-over [fast slow]
  (let [ pairs (map list fast slow)]
    (map #(- %1 %2) pairs)
    )
  )



;;;;;; strategy
