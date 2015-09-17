(ns cloanda.core-test
  (:require [clojure.test :refer :all]
            [cloanda.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(with-test
  (def test-api (api. rest_url stream_url df))
  
  (is (= 4 (my-function 2 2)))
  (is (= 7 (my-function 3 4)))
  
  
  )