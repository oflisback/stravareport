;;;; The idea here is to get activities from Strava, transform them into a sequence of maps
;;;; with the keys activity-map-keys. Then they are filtered into a sequence where each item is
;;;; a set of activities matching an activity type and month. Distance is calculated for each
;;;; and a javascript array is written to a template html report page.

(ns stravaclj.stravareport
  (:gen-class)
  (:use [clojure.set :only [intersection]])
  (:import (javastrava.api.v3.auth.impl.retrofit AuthorisationServiceImpl))
  (:import (javastrava.api.v3.auth.ref AuthorisationScope))
  (:import (javastrava.api.v3.service Strava)))

(load-file "config/config.clj")

(defn fetch-activities-from-strava [year]
  (let [auth-service (AuthorisationServiceImpl.)
        scope (into-array AuthorisationScope [AuthorisationScope/VIEW_PRIVATE])
        after-date (java.time.LocalDateTime/of year 1 1 00 00)
        before-date (java.time.LocalDateTime/of year 12 31 23 59)
        token (.tokenExchange auth-service (int config/app-id) config/client-secret config/code scope)]
    (.listAllAuthenticatedAthleteActivities (Strava. token) before-date after-date)))

(defn filter-on-key-value [set-of-activity-maps key-value]
  (set (filter #(= (get % (first key-value)) (second key-value)) set-of-activity-maps)))

(defn get-activities-passing-filters [set-of-activity-maps set-of-key-value-filters]
  (apply intersection (map (partial filter-on-key-value set-of-activity-maps) (seq set-of-key-value-filters))))

(defn get-distance [set-of-activity-maps]
  (apply + (map #(get % :distance) set-of-activity-maps)))

(defn java-activity-to-seq [activity]
  (let [start-date (.getStartDate activity)]
    [(.getId (.getType activity)) (.getYear start-date) (.getMonthValue start-date) (.getDistance activity) (.getElapsedTime activity)]))

(defn fetch-data-to-js-vector [year]
  (let [activities-repeated (flatten (repeat 12 '("run" "ride" "rollerski" "UNKNOWN" "swim"))) ;; cross country skiing is UNKNOWN in the strava lib.
        nbr-activity-types 5
        months-repeated-for-each-type (flatten (map (partial repeat nbr-activity-types) (map #(inc %) (range 12))))
        seq-of-type-month-filter-vals (partition 2 (interleave activities-repeated months-repeated-for-each-type))
        seq-of-filter-maps (map (partial zipmap '(:type :month)) seq-of-type-month-filter-vals)
        seq-of-activity-values (map java-activity-to-seq (fetch-activities-from-strava year))
        activity-map-keys [:type :year :month :distance :time]
        set-of-activity-maps (set (map (partial zipmap activity-map-keys) seq-of-activity-values))
        seq-of-distances (map #(get-distance (get-activities-passing-filters set-of-activity-maps %)) seq-of-filter-maps)]
    (clojure.string/join "," (map #(str (Math/round (/ % 1000.0))) seq-of-distances))))


(defn replace-in-string [string search-replace-pairs]
  (let [search-replace-pair (first search-replace-pairs)]
    (if (empty? search-replace-pairs)
      string
      (replace-in-string (clojure.string/replace string (first search-replace-pair) (second search-replace-pair)) (rest search-replace-pairs)))))

(defn fetch-activities-and-produce-html [year output-file]
  (let [js-data-string (fetch-data-to-js-vector year)]
    (with-open [wrtr (clojure.java.io/writer output-file)]
      (.write wrtr (replace-in-string (slurp config/template) [["STRAVA_DATA" (str "[" js-data-string "]")] ["BASE_ADDRESS" config/address]])))))

(defn -main [& args]
  (if (= 2 (count args))
    (fetch-activities-and-produce-html (Integer/parseInt (first args)) (second args))
    (println "Usage: lein run <year> <output file>")))
