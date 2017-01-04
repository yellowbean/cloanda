(defproject cloanda "0.1.3-SNAPSHOT"
  :description "A clojure wrapper for Oanda REST API"
  :url "https://github.com/yellowbean/cloanda"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "2.3.0"]
                 [clj-json "0.5.3"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.apache.commons/commons-math3 "3.5"]
                 ]

  :main ^:skip-aot cloanda.core
  :target-path "target/%s"
  :plugins [[lein-codox "0.10.2"]
            [lein-autoreload "0.1.1"]]
  :profiles {:uberjar {:aot :all}})
