(ns net.bjoc.advent.year-2023.day-6
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines zip]]))

(defn extract-numbers [line]
  (->> (str/split line #"\s+")
       (map parse-long)
       (remove nil?)))

(defn make-distance-fn [time distance]
  (fn [charge-time]
    (* charge-time (- time charge-time))))

(defn file->races [filename]
  (->> filename
       file->lines
       (map extract-numbers)
       (apply zip)
       (map (fn [[a b]]
              {:time a :distance b :distance-fn (make-distance-fn a b)}))))

(defn race->num-winning-scenarios [{time :time distance :distance f :distance-fn}]
  (->> (range time)
       (map f)
       (remove #(<= % distance))
       count))

(defn file->num-winning-scenarios [filename]
  (->> filename
       file->races
       (map race->num-winning-scenarios)))

;;;

(defn file->race-2 [filename]
  (->> filename
       slurp
       (#(str/replace % #"[^\d\n]" ""))
       (#(str/split % #"\n"))
       (map parse-long)
       ((fn [[a b]]
          {:time a :distance b :distance-fn (make-distance-fn a b)}))))

(defn file->num-winning-scenarios-2 [filename]
  (->> filename
       file->race-2
       race->num-winning-scenarios))

;;;

(advent/defrunner 1 (comp #(reduce * %) file->num-winning-scenarios) "Product of num winning scenarios")
(advent/defrunner 2 file->num-winning-scenarios-2 "Num winning scenarios")
