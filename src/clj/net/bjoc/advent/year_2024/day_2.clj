(ns net.bjoc.advent.year-2024.day-2
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines zip]]))

(defn line->report [line]
  (map parse-long
       (str/split line #"\s+")))

(defn file->reports [filename]
  (->> (file->lines filename)
       (map line->report)))

(defn safe? [report]
  (let [diffs (->> (zip report (drop 1 report))
                   (map (fn [[a b]] (- b a))))]
    (and (empty? (filter zero? diffs))
         (or (every? pos? diffs)
             (every? neg? diffs)
             )
         (every? (fn [x] (<= -3 x 3)) diffs))))

(defn file->safe-count [filename]
  (->> (file->reports filename)
       (filter safe?)
       count))

;;;


(defn expand-via-drop [report]
  (let [v (vec report)]
    (map (fn [loc]
           (concat (subvec v 0 loc) (subvec v (inc loc))))
         (range (count v)))))

(defn safe?* [report]
  (->> (concat [report] (expand-via-drop report))
       (filter safe?)
       not-empty))

(defn file->safe*-count [filename]
  (->> (file->reports filename)
       (filter safe?*)
       count))

;;;

(advent/defrunner 1 file->safe-count "Count of safe reports")
(advent/defrunner 2 file->safe*-count "Count of safe* reports")
