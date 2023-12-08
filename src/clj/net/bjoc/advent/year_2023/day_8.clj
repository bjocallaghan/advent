(ns net.bjoc.advent.year-2023.day-8
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(defn file->state [filename]
  (let [lines (file->lines filename)]
    {:directions (-> lines first cycle)
     :maps (->> lines
                (drop 2)
                (map (fn [line]
                       (let [[_ a b c] (re-find #"^(...)\s*=\s*\((...),\s*(...)\)\s*$" line)]
                         [a {\L b \R c}])))
                (into {}))}))

(defn file->num-steps [filename]
  (let [{:keys [directions maps]} (file->state filename)]
    (loop [dist 0
           loc "AAA"
           directions directions]
      (if (= loc "ZZZ")
        dist
        (recur (inc dist)
               (-> maps (get loc) (get (first directions)))
               (rest directions))))))

;;;

(defn a-loc? [[_ _ c]] (= \A c))
(defn z-loc? [[_ _ c]] (= \Z c))

(defn file->num-ghost-steps [filename]
  (let [{:keys [directions maps]} (file->state filename)]
    (loop [dist 0
           locs (->> maps keys (filter a-loc?))
           directions directions]
      (if (->> locs (remove z-loc?) empty?)
        dist
        (recur (inc dist)
               (map #(-> maps (get %) (get (first directions))) locs)
               (rest directions))))))

;;;

(advent/defrunner 1 file->num-steps "Num steps")
(advent/defrunner 2 file->num-ghost-steps "Num ghost steps")
