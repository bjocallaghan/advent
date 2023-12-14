(ns net.bjoc.advent.year-2023.day-8
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.numeric :refer [lcm]]
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

(defn make-num-steps-fn [maps directions termination-pred]
  (fn [loc]
    (loop [dist 0
           loc loc
           directions directions]
      (if (termination-pred loc)
        dist
        (recur (inc dist)
               (-> maps (get loc) (get (first directions)))
               (rest directions))))))

(def zzz-loc? #(= % "ZZZ"))

(defn file->num-steps [filename]
  (let [{:keys [maps directions]} (file->state filename)]
    ((make-num-steps-fn maps directions zzz-loc?) "AAA")))

;;;

(defn a-loc? [[_ _ c]] (= \A c))
(defn z-loc? [[_ _ c]] (= \Z c))

(defn file->num-ghost-steps [filename]
  (let [{:keys [directions maps]} (file->state filename)]
    (->> maps
         keys
         (filter a-loc?)
         (map (make-num-steps-fn maps directions z-loc?))
         (apply lcm))))

;;;

(advent/defrunner 1 file->num-steps "Num steps")
(advent/defrunner 2 file->num-ghost-steps "Num ghost steps")
