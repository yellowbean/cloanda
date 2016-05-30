(defproject cloanda "0.1.1-SNAPSHOT"
  :description "A clojure wrapper for Oanda REST API"
  :url "https://github.com/yellowbean/cloanda"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "1.0.1"]
                 [clj-json "0.5.3"]
                 [org.clojure/tools.logging "0.3.1"]
		]
  :main ^:skip-aot cloanda.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
