(defproject stravaclj "0.1.0-SNAPSHOT"
  :description "Clojure script for html report on strava activities"
  :url "https://github.com/oflisback/stravareport"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"],
                 [com.github.danshannon/javastrava-api "1.0.0-RC1"]
                ]
  :main ^:skip-aot stravaclj.stravareport
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
